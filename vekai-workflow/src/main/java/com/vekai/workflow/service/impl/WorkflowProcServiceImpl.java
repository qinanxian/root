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
 * ???????????????,??????,????????????
 *
 * @author ????????? <xmzuo@amarsoft.com>
 * @author ????????? <wkchen@amarsoft.com>
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
        Validate.notBlank(key, "????????????????????????key??????");
        String objectId = (String) variables.get("ObjectId");
        String objectType = (String) variables.get("ObjectType");
        String summary = (String) variables.get("Summary");
        Map<String, Object> pageParam = (Map<String, Object>) variables.get("PageParam");
        Validate.notBlank(objectId, "????????????????????????ObjectId??????");
        Validate.notBlank(objectType, "????????????????????????ObjectType??????");
        Validate.notBlank(summary, "????????????????????????Summary??????");

        String pageParamJSON = "";
        if (null != pageParam) {
            pageParamJSON = JSONKit.toJsonString(pageParam);
            // ???PageParam??????,??????????????????Activiti?????????,????????????????????????????????????,???Activiti??????????????????
            variables.remove("PageParam");
        }

        List<ProcHandler> procHandlers = procHandlerResolve.findHandlers(key);

        // ??????????????????
        Map<String, Object> vars = workflowBomParser.parse(key, procHandlers, null, null);
        if (null != vars)
            variables.putAll(vars);

        logger.info("??????????????????,KEY:" + key + " ??????:" + variables.toString() + "????????????:" + userId);

        //????????????????????????modal ??????????????????
        long isDeploy = repositoryService.createDeploymentQuery().processDefinitionKey(key).count();
        if (isDeploy == 0) {
            //?????????modal ????????????
            long isHaveProcess = repositoryService.createModelQuery().modelKey(key).count();
            if (isHaveProcess == 0) {
                logger.warn(key + "???????????????????????????!");
                throw new WorkflowException("{0}?????????????????????!", key);
            } else {
                logger.warn(key + "??????????????????!");
                throw new WorkflowException("{0}??????????????????!", key);
            }
        }
        Validate.notBlank(userId, "????????????????????????!");
        identityService.setAuthenticatedUserId(userId);
        ProcessInstance processInstance = null;

        //activiti ??????
        if (variables.size() > 0) {
            //?????????
            processInstance = runtimeService.startProcessInstanceByKey(key, variables);
        } else {
            //????????????
            processInstance = runtimeService.startProcessInstanceByKey(key);
        }

        String procInstId = processInstance.getId();

        logger.info("????????????????????????,????????????id:{}", procInstId);


        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();


        //????????????????????????????????????????????????
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

            //???????????? wkfl_proc ????????????
            for (ProcHandler procHandler : procHandlers) {
                WorkflowProc workflowProc = new WorkflowProc();
                workflowProc.setObjectType(objectType);
                workflowProc.setObjectId(objectId);
                workflowProc.setProcId(procInstId);
                workflowProc.setProcInstId(procInstId);
                workflowProc.setProcDefKey(key);

                //????????????
                if (null != pageParamJSON && !"".equals(pageParamJSON)) {
                    workflowProc.setPageParam(pageParamJSON);
                }
                workflowProc.setProcDefId(processInstance.getProcessDefinitionId());

                //?????????????????????,ACT_HI_PROCINST??????NAME_?????????????????????
                //?????????Activiti???????????????????????????????????????????????????
                workflowProc
                    .setProcName(
                        ((ExecutionEntity) processInstance).getProcessDefinition().getName());
                workflowProc.setSummary(summary);
                workflowProc.setSponsor(userId);
                workflowProc.setFlowType("full");
                workflowProc.setStartTime(DateKit.now());
                workflowProc.setStatus(ProcStatusEnum.PRESUBMIT.getName());

                //?????????????????????????????????????????????
                //?????????????????????,?????????????????????????????????(??????)??????????????????????????????
                procHandler.afterProcStart(workflowProc, workflowTasks);
            }

        }

        return new ProcInstance(processInstance);
    }

    @Transactional
    @Override
    public void suspend(String procInstId) {
        Validate.notBlank(procInstId, "????????????????????????procInstId??????");
        logger.info("??????????????????,ProcInstId:" + procInstId);
        runtimeService.suspendProcessInstanceById(procInstId);
    }

    @Transactional
    @Override
    public void activate(String procInstId) {
        Validate.notBlank(procInstId, "????????????????????????????????????procInstId??????");
        logger.info("????????????????????????,ProcInstId:" + procInstId);
        runtimeService.activateProcessInstanceById(procInstId);
    }

    @Override
    public boolean isSuspended(String procInstId) {
        Validate.notBlank(procInstId, "????????????procInstId??????");
        ExecutionEntity exec =
            (ExecutionEntity) runtimeService.createExecutionQuery().processInstanceId(procInstId)
                .singleResult();
        return exec.isSuspended();
    }

    @Override
    public ProcInstance getProcInst(String procInstId) {
        Validate.notBlank(procInstId, "????????????procInstId??????");
        ProcessInstance processInstance =
            runtimeService.createProcessInstanceQuery().processInstanceId(procInstId)
                .singleResult();
        return new ProcInstance(processInstance);
    }


    @Override
    public Map<String, Object> getProcVariables(String procInstId) {
        Validate.notBlank(procInstId, "????????????procInstId??????");
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
        Validate.notBlank(procInstId, "????????????procInstId??????");
        HistoricProcessInstance histProcInst = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(procInstId).singleResult();
        if (histProcInst == null)
            throw new WorkflowException("????????????:{0}?????????", procInstId);
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
        Validate.notBlank(procInstId, "????????????procInstId??????");
        boolean isHave =
            historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId)
                .count() > 0;
        if (isHave) {

            return runtimeService.createExecutionQuery().processInstanceId(procInstId).count() <= 0;
        } else {
            throw new WorkflowException("??????????????????:{0}", procInstId);
        }
    }

    @Override
    public List<ProcComment> getProcComments(String procInstId) {
        Validate.notBlank(procInstId, "????????????procInstId??????");
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
            throw new WorkflowException("?????????????????????");
        }
        return procComments;
    }

    @Override
    public boolean inParallelBranch(String procInstId) {
        Validate.notBlank(procInstId, "????????????procInstId??????");
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(procInstId).list();
        if (0 == taskList.size())
            throw new WorkflowException("????????????????????????");
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
        Validate.notBlank(procInstId, "????????????procInstId??????");
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
        Validate.notBlank(procInstId, "????????????procInstId??????");
        if (isFinished) {
            // ????????????????????????
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
            logger.error("???????????????????????????XML???????????????", e);
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
            logger.error("???????????????????????????XML???????????????", e);
            throw new WorkflowException(e);
        }
    }

    private void operateForModel(Model model, BpmnModel bpmnModel) {
        if (bpmnModel.getMainProcess() == null || bpmnModel.getMainProcess().getId() == null) {
            logger.error("BPMN???MainProcess????????????");
            throw new WorkflowException("BPMN???MainProcess????????????");
        } else {
            if (bpmnModel.getLocationMap().isEmpty()) {
                logger.error("BPMN???LocationMap??????");
                throw new WorkflowException("BPMN???MainProcess????????????");
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
     * ???????????????
     * @param procInstId
     * @return
     */
    @Override
    public List<String> getWorkflowProcessDepth(String procInstId, String taskId) {
        //??????????????????????????????
        if (isFinished(procInstId)) {
            throw new WorkflowException("??????ID???:{0}?????????????????????", procInstId);
        }
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessDefinitionEntity procDefinition =
            (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(tasks.get(0).getProcessDefinitionId());
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefinition.getId());
        List<Process> processes = bpmnModel.getProcesses();
        Process process = processes.get(0);
        //????????????
        int dep = getDeepPath(process.findFlowElementsOfType(StartEvent.class).get(0), process);
        //????????????
        int nodeDepth =
            getNodeDepth(process.findFlowElementsOfType(StartEvent.class).get(0), process, task);
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(dep+1));
        list.add(String.valueOf(nodeDepth));
        return list;
    }

    //???EndEvent???????????????
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


    //???EndEvent???????????????
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