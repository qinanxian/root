package com.vekai.workflow.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.JSONKit;
import com.vekai.workflow.exception.WorkflowException;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.handler.ProcHandler;
import com.vekai.workflow.handler.ProcHandlerResolve;
import com.vekai.workflow.model.HistoricInstance;
import com.vekai.workflow.model.ProcComment;
import com.vekai.workflow.model.ProcInstance;
import com.vekai.workflow.model.enums.ProcStatusEnum;
import com.vekai.workflow.nopub.service.WorkflowBomParser;
import com.vekai.workflow.nopub.service.WorkflowEntityService;
import com.vekai.workflow.service.WorkflowProcService;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.explorer.util.XmlUtil;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 流程的启动,中断,激活操作
 *
 * @author 左晓敏 <xmzuo@amarsoft.com>
 * @author 陈文楷 <wkchen@amarsoft.com>
 * @date 2017-12-26
 */
@Component
public class WorkflowProcServiceImpl implements WorkflowProcService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected IdentityService identityService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected HistoryService historyService;
    @Autowired
    protected WorkflowEntityService workflowEntityService;
    @Autowired
    private ProcHandlerResolve procHandlerResolve;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private WorkflowBomParser workflowBomParser;

    @Transactional
    @Override
    public ProcInstance start(String key, Map<String, Object> variables, String userId) {
        Validate.notBlank(key, "流程启动传入参数key为空");
        String objectId = (String) variables.get("ObjectId");
        String objectType = (String) variables.get("ObjectType");
        String summary = (String) variables.get("Summary");
        Map<String, Object> pageParam = (Map<String, Object>) variables.get("PageParam");
        Validate.notBlank(objectId, "流程启动传入参数ObjectId为空");
        Validate.notBlank(objectType, "流程启动传入参数ObjectType为空");
        Validate.notBlank(summary, "流程启动传入参数Summary为空");

        String pageParamJSON = "";
        if (null != pageParam) {
            pageParamJSON = JSONKit.toJsonString(pageParam);
            // 将PageParam移除,使其不存在到Activiti的表中,因数据后期有可能发生改变,存Activiti是没有意义的
            variables.remove("PageParam");
        }

        List<ProcHandler> procHandlers = procHandlerResolve.findHandlers(key);

        // 流程参数解析
        Map<String, Object> vars = workflowBomParser.parse(key, procHandlers, null, null);
        if (null != vars)
            variables.putAll(vars);

        logger.info("流程实例启动,KEY:" + key + " 参数:" + variables.toString() + "启动用户:" + userId);

        //判断该流程对应的modal 是否已经部署
        long isDeploy = repositoryService.createDeploymentQuery().processDefinitionKey(key).count();
        if (isDeploy == 0) {
            //判断该modal 是否存在
            long isHaveProcess = repositoryService.createModelQuery().modelKey(key).count();
            if (isHaveProcess == 0) {
                logger.warn(key + "流程定义模型不存在!");
                throw new WorkflowException("{0}流程模型不存在!", key);
            } else {
                logger.warn(key + "流程尚未部署!");
                throw new WorkflowException("{0}流程尚未部署!", key);
            }
        }
        Validate.notBlank(userId, "流程启动用户为空!");
        identityService.setAuthenticatedUserId(userId);
        ProcessInstance processInstance = null;

        //activiti 启动
        if (variables.size() > 0) {
            //带参数
            processInstance = runtimeService.startProcessInstanceByKey(key, variables);
        } else {
            //不带参数
            processInstance = runtimeService.startProcessInstanceByKey(key);
        }

        String procInstId = processInstance.getId();

        logger.info("流程实例启动成功,流程实例id:{}", procInstId);


        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();


        //首个任务的受理人为启动流程的用户
        if (tasks != null && tasks.size() == 1) {
            taskService.claim(tasks.get(0).getId(), userId);

            tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();

            List<WorkflowTask> workflowTasks = new ArrayList<WorkflowTask>();
            tasks.forEach(task -> {
                WorkflowTask workflowTask = new WorkflowTask();
                workflowTask.setProcId(task.getProcessInstanceId());
                workflowTask.setTaskId(task.getId());
                workflowTask.setTaskInstId(task.getId());
                workflowTask.setTaskDefKey(task.getTaskDefinitionKey());
                workflowTask.setTaskName(task.getName());
                workflowTask.setAssignee(task.getAssignee());
                workflowTask.setOwner(task.getAssignee());
                workflowTask.setArrivalTime(task.getCreateTime());
                workflowTasks.add(workflowTask);
            });

            //对外挂表 wkfl_proc 进行操作
            for (ProcHandler procHandler : procHandlers) {
                WorkflowProc workflowProc = new WorkflowProc();
                workflowProc.setObjectType(objectType);
                workflowProc.setObjectId(objectId);
                workflowProc.setProcId(procInstId);
                workflowProc.setProcInstId(procInstId);
                workflowProc.setProcDefKey(key);

                //页面参数
                if (null != pageParamJSON && !"".equals(pageParamJSON)) {
                    workflowProc.setPageParam(pageParamJSON);
                }
                workflowProc.setProcDefId(processInstance.getProcessDefinitionId());

                //取流程定义名称,ACT_HI_PROCINST中的NAME_启动流程后为空
                //应该是Activiti预留的字段表示业务意义上的流程名称
                workflowProc
                    .setProcName(
                        ((ExecutionEntity) processInstance).getProcessDefinition().getName());
                workflowProc.setSummary(summary);
                workflowProc.setSponsor(userId);
                workflowProc.setFlowType("full");
                workflowProc.setStartTime(DateKit.now());
                workflowProc.setStatus(ProcStatusEnum.PRESUBMIT.getName());

                //流程启动后对于外挂表的一些操作
                //该方法可被重写,用于业务方面在流程启动(节点)后对于业务的一些操作
                procHandler.afterProcStart(workflowProc, workflowTasks);
            }

        }

        return new ProcInstance(processInstance);
    }

    @Transactional
    @Override
    public void suspend(String procInstId) {
        Validate.notBlank(procInstId, "流程中断传入参数procInstId为空");
        logger.info("流程实例中断,ProcInstId:" + procInstId);
        runtimeService.suspendProcessInstanceById(procInstId);
    }

    @Transactional
    @Override
    public void activate(String procInstId) {
        Validate.notBlank(procInstId, "流程实例中断激活传入参数procInstId为空");
        logger.info("流程实例中断激活,ProcInstId:" + procInstId);
        runtimeService.activateProcessInstanceById(procInstId);
    }

    @Override
    public boolean isSuspended(String procInstId) {
        Validate.notBlank(procInstId, "传入参数procInstId为空");
        ExecutionEntity exec =
            (ExecutionEntity) runtimeService.createExecutionQuery().processInstanceId(procInstId)
                .singleResult();
        return exec.isSuspended();
    }

    @Override
    public ProcInstance getProcInst(String procInstId) {
        Validate.notBlank(procInstId, "传入参数procInstId为空");
        ProcessInstance processInstance =
            runtimeService.createProcessInstanceQuery().processInstanceId(procInstId)
                .singleResult();
        return new ProcInstance(processInstance);
    }


    @Override
    public Map<String, Object> getProcVariables(String procInstId) {
        Validate.notBlank(procInstId, "传入参数procInstId为空");
        List<HistoricVariableInstance> histVariables =
            historyService.createHistoricVariableInstanceQuery().processInstanceId(procInstId)
                .list();
        Map<String, Object> variables = new HashMap<String, Object>();
        for (HistoricVariableInstance histVariable : histVariables) {
            //            System.out.println(histVariable.getTaskId()+"-"+histVariable
            // .getVariableName()+"-"+histVariable.getValue()+"-"+histVariable
            // .getVariableTypeName());
            variables.put(histVariable.getVariableName(), histVariable.getValue());
        }
        return variables;
    }

    @Override
    public HistoricInstance getHistInst(String procInstId) {
        Validate.notBlank(procInstId, "传入参数procInstId为空");
        HistoricProcessInstance histProcInst = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(procInstId).singleResult();
        if (histProcInst == null)
            throw new WorkflowException("流程实例:{0}不存在", procInstId);
        HistoricInstance historicInstance = new HistoricInstance(histProcInst);
        String startUserId = histProcInst.getStartUserId();
        if (startUserId != null && !"".equals(startUserId)) {
            User startUser = identityService.createUserQuery().userId(histProcInst.getStartUserId())
                .singleResult();
            historicInstance.setStartUserName(startUser.getFirstName());
        }
        return historicInstance;
    }

    @Override
    public boolean isFinished(String procInstId) {
        Validate.notBlank(procInstId, "传入参数procInstId为空");
        boolean isHave =
            historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId)
                .count() > 0;
        if (isHave) {

            return runtimeService.createExecutionQuery().processInstanceId(procInstId).count() <= 0;
        } else {
            throw new WorkflowException("不存在此流程:{0}", procInstId);
        }
    }

    @Override
    public List<ProcComment> getProcComments(String procInstId) {
        Validate.notBlank(procInstId, "传入参数procInstId为空");
        String sql =
            "SELECT ID_,TYPE_,TIME_,USER_ID_,TASK_ID_,PROC_INST_ID_,ACTION_,MESSAGE_,FULL_MSG_  " +
                "FROM ACT_HI_COMMENT WHERE ACTION_ = 'AddComment' AND PROC_INST_ID_ = ? ORDER BY "
                + "TIME_ ASC";
        List<Comment> comments =
            jdbcTemplate.query(sql, new Object[] {procInstId}, new CommentMapper());
        List<ProcComment> procComments = new ArrayList<ProcComment>();
        for (Comment comment : comments) {
            procComments.add(new ProcComment(comment));
        }
        if (procComments.size() == 0) {
            throw new WorkflowException("该流程暂无意见");
        }
        return procComments;
    }

    @Override
    public boolean inParallelBranch(String procInstId) {
        Validate.notBlank(procInstId, "传入参数procInstId为空");
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(procInstId).list();
        if (0 == taskList.size())
            throw new WorkflowException("该流程不存在任务");
        for (Task task : taskList) {
            String executionId = task.getExecutionId();
            ExecutionEntity srcExec =
                (ExecutionEntity) runtimeService.createExecutionQuery().executionId(executionId)
                    .singleResult();
            if (srcExec.isConcurrent()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<TaskDefinition> getTaskDefinitions(String procInstId) {
        Validate.notBlank(procInstId, "传入参数procInstId为空");
        HistoricProcessInstance hisProcIns = this.getHistInst(procInstId);
        ProcessDefinitionEntity processDefinitionEntity =
            (ProcessDefinitionEntity) repositoryService
                .getProcessDefinition(hisProcIns.getProcessDefinitionId());
        List<ActivityImpl> activityImpls = processDefinitionEntity.getActivities();

        List<TaskDefinition> taskDefs = new ArrayList<TaskDefinition>();
        for (PvmActivity pvmAct : activityImpls) {
            ActivityImpl activityImpl = (ActivityImpl) pvmAct;

            if ("userTask".equals(activityImpl.getProperty("type"))) {
                TaskDefinition taskDefinition =
                    ((UserTaskActivityBehavior) activityImpl.getActivityBehavior())
                        .getTaskDefinition();
                taskDefs.add(taskDefinition);
            }
        }
        return taskDefs;
    }

    @Override
    public List<HistoricActivityInstance> getHisActInsts(String procInstId, boolean isFinished) {
        Validate.notBlank(procInstId, "传入参数procInstId为空");
        if (isFinished) {
            // 没有时间排序字段
            return historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInstId).orderByHistoricActivityInstanceStartTime().asc().finished()
                .list();
        } else {
            List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(procInstId).orderByHistoricActivityInstanceStartTime().asc()
                    .list();
            return list;
        }
    }

    private class CommentMapper implements RowMapper<Comment> {
        final LobHandler lobHandler = new DefaultLobHandler();

        @Override
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            CommentEntity comment = new CommentEntity();
            comment.setId(rs.getString("ID_"));
            comment.setType(rs.getString("TYPE_"));
            try {
                Timestamp time = rs.getTimestamp("TIME_");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                comment.setTime(formatter.parse(time.toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            comment.setUserId(rs.getString("USER_ID_"));
            comment.setTaskId(rs.getString("TASK_ID_"));
            comment.setProcessInstanceId(rs.getString("PROC_INST_ID_"));
            comment.setAction(rs.getString("ACTION_"));
            comment.setMessage(rs.getString("MESSAGE_"));
            byte[] inp = lobHandler.getBlobAsBytes(rs, "FULL_MSG_");
            if (null != inp)
                comment.setFullMessage(new String(inp));
            return comment;
        }
    }

    public void importUpdateDefinitionModel(String modelId, InputStream inputStream) {
        try {
            XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
            InputStreamReader in = new InputStreamReader(inputStream);
            XMLStreamReader xtr = xif.createXMLStreamReader(in);
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
            Model model = repositoryService.getModel(modelId);
            this.operateForModel(model, bpmnModel);
        } catch (XMLStreamException e) {
            logger.error("导入流程模型出错，XML流输入异常", e);
            throw new WorkflowException(e);
        }
    }

    @Override
    public Model importAddModel(InputStream inputStream) {
        try {
            XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
            InputStreamReader in = new InputStreamReader(inputStream);
            XMLStreamReader xtr = xif.createXMLStreamReader(in);
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
            Model model = repositoryService.newModel();
            this.operateForModel(model, bpmnModel);
            return model;
        } catch (XMLStreamException e) {
            logger.error("导入流程模型出错，XML流输入异常", e);
            throw new WorkflowException(e);
        }
    }

    private void operateForModel(Model model, BpmnModel bpmnModel) {
        if (bpmnModel.getMainProcess() == null || bpmnModel.getMainProcess().getId() == null) {
            logger.error("BPMN中MainProcess读取失败");
            throw new WorkflowException("BPMN中MainProcess读取失败");
        } else {
            if (bpmnModel.getLocationMap().isEmpty()) {
                logger.error("BPMN中LocationMap为空");
                throw new WorkflowException("BPMN中MainProcess读取失败");
            } else {
                String processName = bpmnModel.getMainProcess().getName();
                String key = bpmnModel.getMainProcess().getId();

                ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
                modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processName);
                modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);

                model.setMetaInfo(modelObjectNode.toString());
                model.setName(processName);
                model.setKey(key);
                repositoryService.saveModel(model);

                BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
                ObjectNode editorNode = jsonConverter.convertToJson(bpmnModel);
                repositoryService
                    .addModelEditorSource(model.getId(), editorNode.toString().getBytes());
            }
        }
    }

    /**
     * 遍历线路图
     * @param procInstId
     * @return
     */
    @Override
    public List<String> getWorkflowProcessDepth(String procInstId, String taskId) {
        //当前流程是否已经结束
        if (isFinished(procInstId)) {
            throw new WorkflowException("实例ID为:{0}的该流程已结束", procInstId);
        }
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessDefinitionEntity procDefinition =
            (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(tasks.get(0).getProcessDefinitionId());
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefinition.getId());
        List<Process> processes = bpmnModel.getProcesses();
        Process process = processes.get(0);
        //所有节点
        int dep = getDeepPath(process.findFlowElementsOfType(StartEvent.class).get(0), process);
        //当前节点
        int nodeDepth =
            getNodeDepth(process.findFlowElementsOfType(StartEvent.class).get(0), process, task);
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(dep+1));
        list.add(String.valueOf(nodeDepth));
        return list;
    }

    //将EndEvent事件加进来
    private int getDeepPath(FlowNode flowNode, Process process) {
        int depth = flowNode instanceof UserTask ? 1 : 0;
        List<SequenceFlow> outgoingFlows = flowNode.getOutgoingFlows();
        if (null != outgoingFlows && !outgoingFlows.isEmpty()) {
            int[] subDeps = new int[outgoingFlows.size()];
            int bound = outgoingFlows.size();
            for (int i = 0; i < bound; i++) {
                SequenceFlow sequenceFlow = outgoingFlows.get(i);
                String targetRef = sequenceFlow.getTargetRef();
                subDeps[i] = getDeepPath((FlowNode) process.getFlowElement(targetRef), process);
            }
            depth += Arrays.stream(subDeps).max().getAsInt();
        }
       return depth;
    }


    //将EndEvent事件加进来
    private int getNodeDepth(FlowNode flowNode,Process process,Task task ){
        int depth = flowNode instanceof UserTask ? 1 : 0;
        if (task.getTaskDefinitionKey().equals(flowNode.getId()))
            return depth;
        List<SequenceFlow> outgoingFlows = flowNode.getOutgoingFlows();
        if (null != outgoingFlows && !outgoingFlows.isEmpty()) {
            for (SequenceFlow sequenceFlow : outgoingFlows) {
                String targetRef = sequenceFlow.getTargetRef();
                int subDep =
                    getNodeDepth((FlowNode) process.getFlowElement(targetRef), process, task);
                    return depth + subDep;
            }
        }
        return 0;
    }




}