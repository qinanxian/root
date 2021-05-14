package com.vekai.workflow.service.impl;

import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.auth.service.AuthService;
import com.vekai.workflow.activiti.command.DeleteRunningTaskCmd;
import com.vekai.workflow.activiti.command.JDJumpTaskCmd;
import com.vekai.workflow.activiti.command.JumpActivityCmd;
import com.vekai.workflow.activiti.command.ParallelJumpTaskCmd;
import com.vekai.workflow.entity.*;
import com.vekai.workflow.exception.WorkflowException;
import com.vekai.workflow.handler.ProcHandler;
import com.vekai.workflow.handler.ProcHandlerResolve;
import com.vekai.workflow.model.*;
import com.vekai.workflow.model.enums.*;
import com.vekai.workflow.nopub.service.WorkflowBomParser;
import com.vekai.workflow.nopub.service.WorkflowEntityService;
import com.vekai.workflow.service.WorkflowTaskService;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.form.DefaultTaskFormHandler;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.form.FormPropertyImpl;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.PropertyNotFoundException;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程任务的相关操作
 *
 * @author 左晓敏 <xmzuo@amarsoft.com>
 * @author 陈文楷 <wkchen@amarsoft.com>
 * @author 姚启扬 <qyyao@amarsoft.com>
 * @author 郑汉   <hzheng2@amarsoft.com>
 * @date 2017-12-26
 */
@Component
public class WorkflowTaskServiceImpl implements WorkflowTaskService {
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
    protected ManagementService managementService;
    @Autowired
    protected FormService formService;
    @Autowired
    private WorkflowEntityService workflowEntityService;
    @Autowired
    private ProcHandlerResolve procHandlerResolve;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private WorkflowBomParser workflowBomParser;
    @Autowired
    private AuthService authService;
    @Autowired
    private BeanCruder beanCruder;


    @Transactional
    @Override
    public List<ProcTask> commit(String taskId, Map<String, Object> variables, String userId,
                                 String comment) {
        Validate.notBlank(taskId, "流程提交传入参数taskId为空");
        Validate.notBlank(userId, "流程提交传入参数userId为空");
        Validate.notBlank(comment, "流程提交传入参数comment为空");
        logger.info("任务:" + taskId + "提交" + ",操作用户:" + userId);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance processInstance =
            runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
                .singleResult();

        List<ProcHandler> procHandlers = procHandlerResolve
            .findHandlers(processInstance.getProcessDefinitionKey());

        String procInstId = processInstance.getId();
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procInstId);
        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);
        // 流程参数解析
        Map<String, Object> vars = workflowBomParser
            .parse(processInstance.getProcessDefinitionKey(), procHandlers, workflowProc,
                workflowTask);
        if (null != vars)
            variables.putAll(vars);

        this.addComment(taskId, userId, TaskStatusEnum.COMPLETED, comment);

        List<Task> tasks = new ArrayList<Task>();
        /**
         * 判断是否有历史记录
         */
        List<HistoricTaskInstance> finishedHisTaskInstances =
            historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
                .finished().orderByTaskCreateTime().asc().list();
        boolean isDoneBackTrack = false;
        if (!finishedHisTaskInstances.isEmpty()) {
            // 退回修改任务,点对点提交处理
            tasks = backTrackTaskJump(task, userId, finishedHisTaskInstances, comment);
            if (tasks != null)
                isDoneBackTrack = true;
        }

        if (!isDoneBackTrack) {
            taskService.complete(taskId, variables);
            tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        }

        List<ProcTask> procTask = new ArrayList<>();
        for (Task cTask : tasks) {
            procTask.add(new ProcTask(cTask));
        }

        List<WorkflowTask> latestWKTaskList = this.getLatestWKTaskList(procInstId);

        boolean procInstFinished = this.isProcInstFinished(procInstId);


        if(procInstFinished) {
            workflowProc.setFinishTime(DateKit.now());
            workflowProc.setStatus(ProcStatusEnum.FINISHED.getName());
        } else {
            workflowProc.setStatus(ProcStatusEnum.APPROVING.getName());
        }

        if (this.isFirstTask(taskId))
            workflowProc.setFirstComment(comment);
        workflowProc.setLastComment(comment);
        workflowTask.setOwner(userId);
        workflowTask.setAssignee(userId);
        workflowTask.setFinishTime(DateKit.now());
        workflowTask.setFinishType(TaskStatusEnum.COMPLETED.getName());
        workflowEntityService.updateWorkflowTask(workflowTask);
        for (ProcHandler procHandler : procHandlers) {
            procHandler.afterTaskCommit(workflowProc, workflowTask, latestWKTaskList);
        }

        if (procInstFinished) {
            for (ProcHandler procHandler : procHandlers) {
                   procHandler.afterProcFinished(ProcFinishEnum.COMPLETED, workflowProc, workflowTask);
            }
        }

        // 重新查询数据,防止Handler将任务处理掉
        tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        procTask.clear();
        for (Task cTask : tasks) {
            procTask.add(new ProcTask(cTask));
        }

        return procTask;
    }

    @Transactional
    @Override
    public List<ProcTask> commitAssignee(String taskId, Map<String, Object> variables,
        String userId, String comment, String assignee) {
        List<ProcTask> procTasks = this.commit(taskId, variables, userId, comment);
        if (procTasks == null || procTasks.size() == 0)
            return procTasks;
        if (procTasks.size() > 1) {
            throw new WorkflowException("任务存在多个,无法进行指定受理人操作");
        } else {
            ProcTask procTask = procTasks.get(0);
            /**
             * 如果是退回修改的话,受理人已经设置
             * 此处以指定的受理人为准
             */
            if(null!=procTask.getAssignee()&&!"".equals(procTask.getAssignee())) {
                this.unClaimTask(procTask.getId());
            }
            this.claimTask(procTask.getId(), assignee);
            return this.getLatestTasks(procTask.getProcessInstanceId());
        }
    }


    @Transactional
    @Override
    public void abolish(String taskId, String userId, String comment) {
        Validate.notBlank(taskId, "流程作废传入参数taskId为空");
        Validate.notBlank(userId, "流程作废传入参数userId为空");
        Validate.notBlank(comment, "流程作废传入参数comment为空");
        logger.info("流程作废,TaskId:" + taskId + "操作用户:" + userId);
        TaskEntity taskEntity =
            (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
        this.addComment(taskId, userId, TaskStatusEnum.ABOLISHED, comment);
        String processInstanceId = taskEntity.getProcessInstanceId();
        deleteTask(taskEntity, TaskStatusEnum.ABOLISHED);


        //在删除运行中的流程实例之前查出所需要的ProcessInstance
        ProcessInstance procInst =
            runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
                .singleResult();
        /**
         * 无需考虑并行任务情况,删除流程操作会清理掉流程相关的执行实例
         * 删除运行中的ProcessInstance
         */
        runtimeService.deleteProcessInstance(processInstanceId, TaskStatusEnum.ABOLISHED.getName());




        List<ProcHandler> handlers =
            procHandlerResolve.findHandlers(procInst.getProcessDefinitionKey());
        HistoricTaskInstance histTaskInst =
            historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);
        workflowTask.setFinishType(TaskStatusEnum.ABOLISHED.getName());
        workflowTask.setFinishTime(histTaskInst.getEndTime());
        for (ProcHandler handler : handlers) {
            WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(processInstanceId);
            workflowProc.setStatus(ProcStatusEnum.ABOLISHED.getName());
            workflowProc.setFinishTime(DateKit.now());
            handler.afterProcFinished(ProcFinishEnum.ABOLISHED, workflowProc, workflowTask);
        }
    }

    @Transactional
    @Override
    public void reject(String taskId, String userId, String comment) {
        /**
         * 流程否决逻辑 1. 设置任务评论 2. 当前任务的删除理由设置为否决 3. 流程删除
         */
        Validate.notBlank(taskId, "流程否决传入参数taskId为空");
        Validate.notBlank(userId, "流程否决传入参数userId为空");
        Validate.notBlank(comment, "流程否决传入参数comment为空");
        logger.trace("流程否决:" + taskId + "设置任务删除理由");
        TaskEntity taskEntity =
            (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
        this.addComment(taskId, userId, TaskStatusEnum.REJECT, comment);
        String processInstanceId = taskEntity.getProcessInstanceId();
        deleteTask(taskEntity, TaskStatusEnum.REJECT);

        //在删除运行中的流程实例之前查出所需要的ProcessInstance
        ProcessInstance procInst =
            runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
                .singleResult();

        

        /**
         * 无需考虑并行任务情况,删除流程操作会清理掉流程相关的执行实例
         * 删除运行中的ProcessInstance
         */
        runtimeService.deleteProcessInstance(processInstanceId, TaskStatusEnum.REJECT.getName());

        List<ProcHandler> handlers =
            procHandlerResolve.findHandlers(procInst.getProcessDefinitionKey());
        HistoricTaskInstance histTaskInst =
            historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(processInstanceId);
        workflowProc.setStatus(ProcStatusEnum.REJECT.getName());
        workflowProc.setFinishTime(DateKit.now());
        workflowProc.setLastAssigneeDesc(AuthHolder.getUser().getName());
        workflowProc.setLastComment(comment);
        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);
        workflowTask.setOwner(userId);
        workflowTask.setAssignee(userId);
        workflowTask.setFinishType(TaskStatusEnum.REJECT.getName());
        workflowTask.setFinishTime(histTaskInst.getEndTime());
        for (ProcHandler handler : handlers) {
            handler.afterProcFinished(ProcFinishEnum.REJECT, workflowProc, workflowTask);
        }
    }

    @Transactional
    @Override
    public ProcTask backFront(String srcTaskId, String dscTaskKey, String userId, String comment) {
        return this.jumpBack(srcTaskId, dscTaskKey, userId, TaskStatusEnum.BACKFRONT, comment);
    }

    @Transactional
    @Override
    public ProcTask backFrontAssignee(String srcTaskId, String dscTaskKey, String userId,
        String comment, String assignee) {
        ProcTask procTask =
            this.jumpBack(srcTaskId, dscTaskKey, userId, TaskStatusEnum.BACKFRONT, comment);
        this.changeAssignee(procTask.getId(), assignee);
        return this.getTask(procTask.getId());
    }

    @Transactional
    @Override
    public ProcTask backOrigin(String taskId, String userId, String comment) {
        ProcTask task = this.getTask(taskId);
        HistoricTask firstHistTask = this.getFirstHistTask(task.getProcessInstanceId());
        return this.jumpBack(task.getId(), firstHistTask.getTaskDefinitionKey(), userId,
            TaskStatusEnum.BACKORIGIN, comment);
    }

    @Transactional
    @Override
    public ProcTask backTrack(String srcTaskId, String dscTaskKey, String userId, String comment) {
        return this.jumpBack(srcTaskId, dscTaskKey, userId, TaskStatusEnum.BACKTRACK, comment);
    }

    @Transactional
    @Override
    public ProcTask backTrackAssignee(String srcTaskId, String dscTaskKey, String userId,
        String comment, String assignee) {
        ProcTask procTask =
            this.jumpBack(srcTaskId, dscTaskKey, userId, TaskStatusEnum.BACKTRACK, comment);
        this.changeAssignee(procTask.getId(), assignee);
        return this.getTask(procTask.getId());
    }

    @Transactional
    @Override
    public ProcTask backToPrev(String taskId, String userId, String comment) {
        PvmActivity taskPvmActivity = this.getTaskPvmActivity(taskId);
        // 获取当前任务的入线
        List<PvmTransition> incomings = taskPvmActivity.getIncomingTransitions();

        // 获取入线前面的节点
        PvmActivity prevPvmActivity = incomings.get(0).getSource();

        if(!"userTask".equals(prevPvmActivity.getProperty("type"))){
            throw new WorkflowException("疑似任务:{0}的前一个不为任务节点,无法进行任务退回", taskId);
        }
        String dscTaskKey=prevPvmActivity.getId();
        return this.backFront(taskId,dscTaskKey, userId, comment);
    }

    @Transactional
    @Override
    public ProcTask jumpBack(String srcTaskId, String dscTaskKey, String userId,
        TaskStatusEnum taskStatusEnum, String comment) {
        Validate.notBlank(srcTaskId, "流程往后跑到任意节点传入参数srcTaskId为空");
        Validate.notBlank(dscTaskKey, "流程往后跑到任意节点传入参数dscTaskKey为空");
        Validate.notBlank(taskStatusEnum.getName(), "流程往后跑到任意节点传入参数taskStatusEnum为空");
        Validate.notBlank(comment, "流程往后跑到任意节点传入参数comment为空");
        this.addComment(srcTaskId, userId, taskStatusEnum, comment);
        Task task = doTaskJumpBack(srcTaskId, dscTaskKey, taskStatusEnum, userId);

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(task.getProcessInstanceId()).singleResult();

        String procInstId = processInstance.getProcessInstanceId();
        HistoricTaskInstance histTaskInst =
            historyService.createHistoricTaskInstanceQuery().taskId(srcTaskId).singleResult();
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procInstId);
        // 任务状态转流程状态
        this.switchTaskStatusToProcStatus(workflowProc, taskStatusEnum);

        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(srcTaskId);
        workflowTask.setFinishType(taskStatusEnum.getName());
        workflowTask.setFinishTime(histTaskInst.getEndTime());
        workflowTask.setAssignee(userId);
        workflowTask.setOwner(userId);

        WorkflowTask dscWorkflowTask = new WorkflowTask();
        dscWorkflowTask.setProcId(task.getProcessInstanceId());
        dscWorkflowTask.setTaskId(task.getId());
        dscWorkflowTask.setTaskInstId(task.getId());
        dscWorkflowTask.setAssignee(task.getAssignee());
        dscWorkflowTask.setOwner(task.getOwner());
        dscWorkflowTask.setTaskDefKey(task.getTaskDefinitionKey());
        dscWorkflowTask.setTaskName(task.getName());
        dscWorkflowTask.setArrivalTime(task.getCreateTime());


        // 调用任务跳转所触发的Handler
        this.invokeTaskJumpHandler(processInstance.getProcessDefinitionKey(),workflowProc,
                workflowTask, dscWorkflowTask, taskStatusEnum);

        return new ProcTask(task);
    }

    @Transactional
    @Override
    public ProcTask jumpForward(String srcTaskId, String dscTaskKey, String userId,
        TaskStatusEnum taskStatusEnum, String comment) {
        Validate.notBlank(srcTaskId, "流程往前跳到任意节点传入参数srcTaskId为空");
        Validate.notBlank(dscTaskKey, "流程往前跳到任意节点传入参数dscTaskKey为空");
        Validate.notBlank(taskStatusEnum.getName(), "流程往前跳到任意节点传入参数taskStatusEnum为空");
        Validate.notBlank(comment, "流程往前跳到任意节点传入参数comment为空");
        this.claimTask(srcTaskId, userId);
        if ("".equals(comment))
            this.addComment(srcTaskId, userId, taskStatusEnum, comment);
        Task task = doTaskJumpForward(srcTaskId, dscTaskKey, taskStatusEnum);

        return new ProcTask(task);
    }

    @Transactional
    @Override
    public ProcTask jumpYForwardTask(String srcTaskId, String dscTaskKey, String userId,
        TaskStatusEnum taskStatusEnum, String comment) {
        Validate.notBlank(srcTaskId, "流程往前跳到任意节点传入参数srcTaskId为空");
        Validate.notBlank(dscTaskKey, "流程往前跳到任意节点传入参数dscTaskKey为空");
        Validate.notBlank(taskStatusEnum.getName(), "流程往前跳到任意节点传入参数taskStatusEnum为空");
        Validate.notBlank(comment, "流程往前跳到任意节点传入参数comment为空");
        this.claimTask(srcTaskId, userId);
        if ("".equals(comment))
            this.addComment(srcTaskId, userId, taskStatusEnum, comment);
        Task task = doTaskJumpYForward(srcTaskId, dscTaskKey);





        //还需要考虑节点向前跳转后的外挂表的问题(wkfl)
        WorkflowTask newTask =new WorkflowTask();
        String procInstId=task.getProcessInstanceId();
        newTask.setTaskId(task.getId());
        newTask.setTaskDefKey(task.getTaskDefinitionKey());
        newTask.setTaskInstId(task.getId());
        newTask.setArrivalTime(task.getCreateTime());
        newTask.setProcId(procInstId);
        newTask.setTaskName(task.getName());
        newTask.setIsConcurrent("N");
        //任务受理人未做设值 需要可以从bpmnModel中获取

        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procInstId);

        newTask.setWorkflowProc(workflowProc);
        beanCruder.save(newTask);

        return new ProcTask(task);
    }

    @Transactional
    @Override
    public ProcTask retrieve(String taskId, String userId, String comment) {
        Map<String, String> map = this.retrieveCondition(taskId);
        String status = map.get("status");
        String msg = map.get("msg");
        if (null == status || null == msg)
            throw new WorkflowException("任务撤回操作:撤回条件检测异常");
        if ("0".equals(status))
            throw new WorkflowException(msg);


        String dscTaskKey = map.get("dscTaskKey");
        String srcTaskId = map.get("srcTaskId");
        if (null == dscTaskKey || null == srcTaskId || "".equals(dscTaskKey) || ""
            .equals(srcTaskId))
            throw new WorkflowException("任务撤回操作:获取任务跳转关键数据出错");

        // 任务跳转
        TaskStatusEnum taskStatusEnum = TaskStatusEnum.RETRIEVE;
        return this.jumpBack(srcTaskId, dscTaskKey, userId, taskStatusEnum, comment);
    }


    public Map<String, String> retrieveCondition(String taskId) {
        Map<String, String> map = new HashMap<>();
        // 操作发起点的历史任务
        HistoricTask originHistTask = this.getHistTask(taskId);
        String deleteReason = originHistTask.getDeleteReason();
        Date endTime = originHistTask.getEndTime();
        String dscTaskKey = originHistTask.getTaskDefinitionKey();

        if (deleteReason == null || "".equals(deleteReason) || null == endTime) {
            map.put("msg", "任务撤回操作发起点应为历史任务");
            map.put("status", "0");
            return map;
        }

        if (TaskStatusEnum.RETRIEVE.getName().equals(deleteReason)) {
            map.put("msg", "任务撤回操作发起点的任务状态不能为'撤回'");
            map.put("status", "0");
            return map;
        }

        String procInstId = originHistTask.getProcessInstanceId();
        HistoricProcessInstance histProcInst = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procInstId).singleResult();
        if (histProcInst == null) {
            map.put("msg", "流程实例:" + procInstId + "不存在");
            map.put("status", "0");
            return map;
        }

        if (histProcInst.getDeleteReason() != null && !"".equals(histProcInst.getDeleteReason())) {
            map.put("msg", "流程实例:" + procInstId + "已结束,无法进行任务撤回操作");
            map.put("status", "0");
            return map;
        }

        List<HistoricTaskInstance> histTasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(procInstId).orderByTaskCreateTime().asc().list();
        // 查找出操作发起点历史所在的历史任务位置
        int originHistTaskIndex = 0;
        for (int i = 0; i < histTasks.size(); i++) {
            if (histTasks.get(i).getTaskDefinitionKey().equals(dscTaskKey)) {
                originHistTaskIndex = i;
            }
        }

        if (histTasks.size() <= (originHistTaskIndex + 1)) {
            map.put("msg", "任务撤回操作:待撤回的源任务不存在");
            map.put("status", "0");
            return map;
        }

        // 待撤回的源任务
        HistoricTaskInstance srcHistTask = histTasks.get(originHistTaskIndex + 1);
        if (null == srcHistTask) {
            map.put("msg", "任务撤回操作:待撤回的源任务不存在");
            map.put("status", "0");
            return map;
        }

        String srcHistTaskDeleteReason = srcHistTask.getDeleteReason();
        Date srcHistTaskEndTime = srcHistTask.getEndTime();
        if (srcHistTaskDeleteReason != null && !"".equals(srcHistTaskDeleteReason)
                && null != srcHistTaskEndTime) {
            map.put("msg", "任务撤回操作:待撤回的源任务已被提交");
            map.put("status", "0");
            return map;
        }

        String srcTaskId = srcHistTask.getId();

        Integer commentCount = beanCruder.selectCount("select count(1) from wkfl_comment where task_id=:taskId", "taskId", srcTaskId);
        if(commentCount>0){
            map.put("msg", "任务撤回操作:待撤回的源任务正在处理中");
            map.put("status", "0");
            return map;
        }

        Integer solicitCount = beanCruder.selectCount("select count(1) from wkfl_solicit where task_id=:taskId", "taskId", srcTaskId);
        if(solicitCount>0){
            map.put("msg", "任务撤回操作:待撤回的源任务正在进行征求他人意见");
            map.put("status", "0");
            return map;
        }

        boolean srcTaskInParallel = this.taskInParallelBranch(srcTaskId);
        if(srcTaskInParallel){
            map.put("msg", "任务撤回操作:待撤回的源任务处于并行任务节点中");
            map.put("status", "0");
            return map;
        }


        map.put("dscTaskKey", dscTaskKey);
        map.put("srcTaskId", srcTaskId);
        map.put("status", "1");
        map.put("msg", "");

        return map;
    }


    @Transactional
    @Override
    public void claimTask(String taskId, String userId) {
        Validate.notBlank(taskId, "任务签收传入参数taskId为空");
        Validate.notBlank(userId, "任务签收传入参数userId为空");
        logger.info("任务:" + taskId + "签收用户:" + userId);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
            throw new WorkflowException("任务:{0}不存在或已被处理", taskId);
        String assignee = task.getAssignee();
        if (assignee != null && !"".equals(assignee)) {
            if (!userId.equals(assignee))
                throw new WorkflowException("任务:{0}已被用户{1}签收", taskId, assignee);
        }
        String owner = task.getOwner();
        if (owner != null && !"".equals(owner)) {
            if (!userId.equals(owner))
                throw new WorkflowException("任务:{0}所有者为:{1},用户:{2}无法进行签收操作", taskId, owner, userId);
        }

        List<String> candidateGroups = this.getCandidateGroups(taskId);
        for (String group:candidateGroups) {
            taskService.deleteCandidateGroup(taskId, group);
        }
        List<String> candidateUsers = this.getCandidateUsers(taskId, false);
        for (String user:candidateUsers) {
            taskService.deleteCandidateUser(taskId, user);
        }
        taskService.claim(taskId, userId);

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(task.getProcessInstanceId()).singleResult();
        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);
        workflowTask.setOwner(userId);
        workflowTask.setAssignee(userId);

        for (ProcHandler procHandler : procHandlerResolve
            .findHandlers(processInstance.getProcessDefinitionKey())) {
            procHandler.afterTaskAuthChange(TaskAuthChangeEnum.CLAIMTASK, workflowTask, null);
        }


    }

    @Transactional
    @Override
    public void unClaimTask(String taskId) {
        Validate.notBlank(taskId, "任务取消签收传入参数taskId为空");
        logger.info("任务:{}取消签收", taskId);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (null == task)
            throw new WorkflowException("不存在任务:{}", taskId);
        taskService.unclaim(task.getId());

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(task.getProcessInstanceId()).singleResult();
        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);
        workflowTask.setOwner(null);
        workflowTask.setAssignee(null);
        for (ProcHandler procHandler : procHandlerResolve
            .findHandlers(processInstance.getProcessDefinitionKey())) {
            procHandler.afterTaskAuthChange(TaskAuthChangeEnum.UNCLAIMTASK, workflowTask, null);
        }
    }

    @Override
    public String getAssignee(String taskId) {
        Validate.notBlank(taskId, "传入参数taskId为空");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return task.getAssignee();
    }

    @Transactional
    @Override
    public void changeAssignee(String taskId, String userId) {
        Validate.notBlank(taskId, "更改受理人传入参数taskId为空");
        Validate.notBlank(userId, "更改受理人传入参数userId为空");
        logger.info("任务:" + taskId + "更改受理人:" + userId);
        //原生表已经更改受理人
        taskService.setAssignee(taskId, userId);

        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(workflowTask.getProcId()).singleResult();
        WorkflowCandidate workflowCandidate =
            workflowEntityService.getCandidateByTaskIdAndUserId(taskId, userId);
        workflowTask.setTaskId(taskId);
        workflowTask.setOwner(userId);
        workflowTask.setAssignee(userId);

        for (ProcHandler procHandler : procHandlerResolve
            .findHandlers(processInstance.getProcessDefinitionKey())) {
            procHandler.afterTaskAuthChange(TaskAuthChangeEnum.CHANGEASSIGNEE, workflowTask,
                workflowCandidate);
        }
    }

    @Transactional
    @Override
    public void addCandidateUser(String taskId, String userId) {
        Validate.notBlank(taskId, "增加候选用户传入参数taskId为空");
        Validate.notBlank(userId, "增加候选用户传入参数userId为空");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (null != task.getAssignee()) {
            throw new WorkflowException("已存在受理人，不可添加候选人!");
        }
        logger.info("任务:" + taskId + "增加候选用户:" + userId);
        taskService.addCandidateUser(taskId, userId);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(task.getProcessInstanceId()).singleResult();
        WorkflowCandidate workflowCandidate = new WorkflowCandidate();
        workflowCandidate.setUserId(userId);
        workflowCandidate.setTaskId(taskId);
        workflowCandidate.setCandidateType("candidate");
        for (ProcHandler procHandler : procHandlerResolve
            .findHandlers(processInstance.getProcessDefinitionKey())) {
            procHandler
                .afterTaskAuthChange(TaskAuthChangeEnum.ADDCANDIDATEUSER,
                    workflowEntityService.getWorkflowTask(taskId), workflowCandidate);
        }
    }

    @Transactional
    @Override
    public void deleteCandidateUser(String taskId, String userId) {
        Validate.notBlank(taskId, "移除候选用户传入参数taskId为空");
        Validate.notBlank(userId, "移除候选用户传入参数userId为空");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        logger.info("任务:" + taskId + "移除候选用户:" + userId);
        taskService.deleteCandidateUser(taskId, userId);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(task.getProcessInstanceId()).singleResult();
        WorkflowCandidate workflowCandidate =
            workflowEntityService.getWorkflowCandidateByTaskIdAndUserId(taskId, userId);
        if (null != workflowCandidate) {
            for (ProcHandler procHandler : procHandlerResolve
                .findHandlers(processInstance.getProcessDefinitionKey())) {
                procHandler
                    .afterTaskAuthChange(TaskAuthChangeEnum.DELETECANDIDATEUSER,
                        workflowEntityService.getWorkflowTask(taskId),
                        workflowCandidate);
            }
        }
    }

    @Transactional
    @Override
    public void addCandidateGroup(String taskId, String groupId) {
        Validate.notBlank(taskId, "增加候选组传入参数taskId为空");
        Validate.notBlank(groupId, "增加候选组传入参数groupId为空");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (null != task.getAssignee()) {
            throw new WorkflowException("已存在受理人，不可添加候选组!");
        }
        logger.info("任务:" + taskId + "增加候选组:" + groupId);
        taskService.addCandidateGroup(taskId, groupId);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(task.getProcessInstanceId()).singleResult();
        WorkflowCandidate workflowCandidate = new WorkflowCandidate();
        workflowCandidate.setTaskId(taskId);
        workflowCandidate.setScopeRoleId(groupId);
        workflowCandidate.setCandidateType("candidate");
        for (ProcHandler procHandler : procHandlerResolve
            .findHandlers(processInstance.getProcessDefinitionKey())) {
            procHandler
                .afterTaskAuthChange(TaskAuthChangeEnum.ADDCANDIDATEGROUP,
                    workflowEntityService.getWorkflowTask(taskId), workflowCandidate);
        }
    }

    @Transactional
    @Override
    public void deleteCandidateGroup(String taskId, String groupId) {
        Validate.notBlank(taskId, "移除候选组传入参数taskId为空");
        Validate.notBlank(groupId, "移除候选组传入参数groupId为空");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        logger.info("任务:" + taskId + "移除候选组:" + groupId);
        taskService.deleteCandidateGroup(taskId, groupId);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(task.getProcessInstanceId()).singleResult();
        WorkflowCandidate workflowCandidate =
            workflowEntityService.getWorkflowCandidateGroupByTaskIdAndGroupId(taskId, groupId);
        for (ProcHandler procHandler : procHandlerResolve
            .findHandlers(processInstance.getProcessDefinitionKey())) {
            procHandler
                .afterTaskAuthChange(TaskAuthChangeEnum.DELETECANDIDATEGROUP,
                    workflowEntityService.getWorkflowTask(taskId),
                    workflowCandidate);
        }
    }

    @Override
    public List<String> getCandidateGroups(String taskId) {
        Validate.notBlank(taskId, "传入参数taskId为空");
        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(taskId);
        if (identityLinks == null)
            return null;
        List<String> groupIds = new ArrayList<String>();
        for (IdentityLink il : identityLinks) {
            String groupId = il.getGroupId();
            if (groupId == null)
                continue;
            if (groupIds.contains(groupId))
                continue;
            groupIds.add(groupId);
        }
        return groupIds;
    }

    @Override
    public List<String> getCandidateUsers(String taskId, boolean includeCandidateGroup) {
        Validate.notBlank(taskId, "传入参数taskId为空");
        List<String> userIds = new ArrayList<String>();
        Task task = getTask(taskId);
        String assignee = task.getAssignee();
        if (!StringUtils.isBlank(assignee)) { // 存在受理人
            return userIds;
        }
        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(taskId);
        if (identityLinks == null)
            return null;
        if (includeCandidateGroup) {
            List<String> groupUsersIds = this.getCandidateGroupUsers(taskId);
            for (String groupUserId : groupUsersIds) {
                if (!userIds.contains(groupUserId))
                    userIds.add(groupUserId);
            }
        }
        for (IdentityLink il : identityLinks) {
            if ("candidate".equals(il.getType())) {
                String userId = il.getUserId();
                if (userId == null)
                    continue;
                if (userIds.contains(userId))
                    continue;
                userIds.add(userId);
            }

        }
        return userIds;
    }


    @Override
    public List<String> getCandidateGroupUsers(String taskId) {
        Validate.notBlank(taskId, "传入参数taskId为空");
        List<String> candidateGroups = getCandidateGroups(taskId);
        List<String> userIds = new ArrayList<String>();
        if (null != candidateGroups) {
            for (String groupId : candidateGroups) {
                List<User> users = identityService.createUserQuery().memberOfGroup(groupId).list();
                for (User user : users) {
                    String userId = user.getId();
                    userIds.add(userId);
                }
            }
        }
        return userIds;
    }

    @Override
    public ProcTask getTask(String taskId) {
        Validate.notBlank(taskId, "传入参数taskId为空");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return new ProcTask(task);
    }

    /**
     * @param taskId
     * @return
     */
    @Override
    public boolean isFirstTask(String taskId) {
        Validate.notBlank(taskId, "传入参数taskId为空");
        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
//        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(task.getProcessInstanceId());
        List<HistoricTaskInstance> historicTaskInstances =
            historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).orderByTaskCreateTime().finished()
                .asc().list();
        if(historicTaskInstances.size()<=0){    //历史记录一条完成的都没有，说明是第一岗位
            return true;
        }else{      //如果有历史记录，则完成的第一条和当前定义KEY相同，也是第一岗
            HistoricTaskInstance historicTaskInstance = historicTaskInstances.get(0);
            String taskKey = task.getTaskDefinitionKey();
            return historicTaskInstance.getTaskDefinitionKey().equals(taskKey);
        }
    }

    @Override
    public List<ProcTask> getLatestTasks(String procInstId) {
        Validate.notBlank(procInstId, "传入参数procInstId为空");
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
        List<ProcTask> procTasks = new ArrayList<ProcTask>();
        for (Task task : tasks) {
            procTasks.add(new ProcTask(task));
        }
        return procTasks;
    }

    @Override
    public ProcTask getLatestTask(String procInstId) {
        Validate.notBlank(procInstId, "传入参数procInstId为空");
        Task task = taskService.createTaskQuery().processInstanceId(procInstId).singleResult();
        return new ProcTask(task);
    }

    @Override
    public HistoricTask getForeHistTask(String taskId) {
        //        ProcTask task = this.getTask(taskId);
        //        Set<PvmActivity> pvmActivitys = this.getForeUserActivities(taskId);
        //
        //        List<HistoricTaskInstance> histTasks = historyService
        // .createHistoricTaskInstanceQuery()
        //                .processInstanceId(task.getProcessInstanceId()).orderByTaskCreateTime()
        // .desc().list();
        throw new WorkflowException("功能尚未开发");
    }

    @Transactional
    @Override
    public void addComment(String taskId, String userId, TaskStatusEnum taskStatusEnum,
        String comment) {
        Validate.notBlank(taskId, "流程往前跳到任意节点传入参数taskId为空");
        Validate.notBlank(userId, "流程往前跳到任意节点传入参数userId为空");
        Validate.notBlank(taskStatusEnum.getName(), "流程往前跳到任意节点传入参数taskStatusEnum为空");
        Validate.notBlank(comment, "流程往前跳到任意节点传入参数comment为空");
        if (StringUtils.isBlank(userId))
            throw new WorkflowException("添加用户评论,用户名称为空");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        identityService.setAuthenticatedUserId(userId);
        Comment cmnt = taskService
            .addComment(taskId, task.getProcessInstanceId(), taskStatusEnum.getName(), comment);

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(task.getProcessInstanceId()).singleResult();

        WorkflowComment workflowComment = new WorkflowComment();
        workflowComment.setCommentId(cmnt.getId());
        workflowComment.setProcId(cmnt.getProcessInstanceId());
        workflowComment.setTaskId(taskId);
        workflowComment.setUserId(userId);
        workflowComment.setMessage(comment);
        workflowComment.setCommentInstId(cmnt.getId());
        workflowComment.setCommentType(taskStatusEnum.getName());
        workflowComment.setProcId(task.getProcessInstanceId());
        for (ProcHandler procHandler : procHandlerResolve
            .findHandlers(processInstance.getProcessDefinitionKey())) {
            procHandler
                .afterCommentAction(CommentActionEnum.ADD, workflowComment);
        }
    }

    /**
     * 删除意见
     *
     * @param commentId
     */

    @Transactional
    @Override
    public void deleteComment(String commentId) {
        Validate.notBlank(commentId, "删除意见传入参数commentId为空");
        Comment comment = taskService.getComment(commentId);
        Task task = taskService.createTaskQuery().taskId(comment.getTaskId()).singleResult();
        if (null == comment)
            throw new WorkflowException("该意见不存在，或已被删除");
        taskService.deleteComment(commentId);
        if (null!=task){
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId()).singleResult();
            WorkflowComment workflowComment = workflowEntityService.getWorkflowComment(commentId);
            for (ProcHandler procHandler : procHandlerResolve
                    .findHandlers(processInstance.getProcessDefinitionKey())) {
                procHandler
                        .afterCommentAction(CommentActionEnum.DELETE, workflowComment);
            }
        }

    }

    /**
     * 更新意见
     *
     * @param commentId
     * @param comment
     */

    @Transactional
    @Override
    public void updateComment(String commentId, String comment) {
        Validate.notBlank(commentId, "删除意见传入参数commentId为空");
        Validate.notBlank(comment, "删除意见传入参数comment为空");
        Comment commentEnt = taskService.getComment(commentId);
        Task task = taskService.createTaskQuery().taskId(commentEnt.getTaskId()).singleResult();
        if (null == commentEnt)
            throw new WorkflowException("该意见不存在，或已被删除");
        String sql = "UPDATE ACT_HI_COMMENT SET MESSAGE_=?,FULL_MSG_=? WHERE ID_=?";
        /**
         * 暂且不考虑MESSAGE_字段的长度限制
         */
        jdbcTemplate.update(sql, new Object[] {comment, comment, commentId});

        if(null!=task){
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId()).singleResult();
            WorkflowComment workflowComment = workflowEntityService.getWorkflowComment(commentId);
            workflowComment.setMessage(comment);
            for (ProcHandler procHandler : procHandlerResolve.findHandlers(processInstance.getProcessDefinitionKey())) {
                procHandler
                        .afterCommentAction(CommentActionEnum.UPDATE, workflowComment);
            }
//           taskService.delegateTask();
        }

    }

    @Override
    public List<ProcComment> getTaskComments(String taskId) {
        Validate.notBlank(taskId, "传入参数taskId为空");
        String sql =
            "SELECT ID_,TYPE_,TIME_,USER_ID_,TASK_ID_,PROC_INST_ID_,ACTION_,MESSAGE_,FULL_MSG_  " +
                "FROM ACT_HI_COMMENT WHERE ACTION_ = 'AddComment' AND TASK_ID_ = ? ORDER BY TIME_"
                + " ASC";
        List<Comment> comments =
            jdbcTemplate.query(sql, new Object[] {taskId}, new CommentMapper());
        List<ProcComment> procComments = new ArrayList<ProcComment>();
        for (Comment comment : comments) {
            procComments.add(new ProcComment(comment));
        }
        return procComments;
    }


    @Override
    public HistoricTask getHistTask(String taskId) {
        Validate.notBlank(taskId, "传入参数taskId为空");
        HistoricTaskInstance histTaskInst =
            historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        return new HistoricTask(histTaskInst);
    }

    @Override
    public List<HistoricTask> getHistTasks(String procInstId, boolean isFinished) {
        Validate.notBlank(procInstId, "传入参数procInstId为空");
        List<HistoricTaskInstance> histTaskInsts = new ArrayList<HistoricTaskInstance>();
        if (isFinished) {
            histTaskInsts =
                historyService.createHistoricTaskInstanceQuery().processInstanceId(procInstId)
                    .finished().list();
        } else {
            histTaskInsts =
                historyService.createHistoricTaskInstanceQuery().processInstanceId(procInstId)
                    .unfinished().list();
        }

        List<HistoricTask> historicTasks = new ArrayList<HistoricTask>();
        for (HistoricTaskInstance histTaskInst : histTaskInsts) {
            historicTasks.add(new HistoricTask(histTaskInst));
        }
        return historicTasks;
    }

    @Override
    public HistoricTask getFirstHistTask(String procInstId) {
        Validate.notBlank(procInstId, "传入参数procInstId为空");
        // 查询已完成的第一个任务
        List<HistoricTaskInstance> hisTasks = historyService.createHistoricTaskInstanceQuery()
            .processInstanceId(procInstId).orderByTaskCreateTime().finished().asc().list();
        if (hisTasks.isEmpty())
            throw new WorkflowException("流程尚未存在任何历史任务");

        return new HistoricTask(hisTasks.get(0));
    }

    @Override
    public boolean taskInParallelBranch(String taskId) {
        Validate.notBlank(taskId, "传入参数taskId为空");
        Task curTask = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (curTask == null)
            throw new WorkflowException("当前任务不存在");
        String executionId = curTask.getExecutionId();
        ExecutionEntity srcExec =
            (ExecutionEntity) runtimeService.createExecutionQuery().executionId(executionId)
                .singleResult();
        return srcExec.isConcurrent();
    }

    @Override
    public TaskDefinition getTaskDefinition(String taskId) {
        Validate.notBlank(taskId, "传入参数taskId为空");
        HistoricTaskInstance hisTaskIns =
            historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        return findTaskDefinitionByKey(hisTaskIns.getProcessDefinitionId(),
            hisTaskIns.getTaskDefinitionKey());
    }

    @Override
    public TaskDefinition getTaskDefinition(String procInstId, String taskDefKey) {
        Validate.notBlank(procInstId, "流程实例ID传入为空");
        Validate.notBlank(taskDefKey, "任务定义KEY传入为空");

        HistoricProcessInstance hisProcInst = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(procInstId).singleResult();
        if (null == hisProcInst)
            throw new WorkflowException("不存在ID:{0}对应的流程实例对象", procInstId);

        return findTaskDefinitionByKey(hisProcInst.getProcessDefinitionId(), taskDefKey);
    }

    private TaskDefinition findTaskDefinitionByKey(String procDefId, String taskDefKey) {
        ProcessDefinitionEntity processDefinitionEntity =
            (ProcessDefinitionEntity) repositoryService
                .getProcessDefinition(procDefId);
        List<ActivityImpl> activityImpls = processDefinitionEntity.getActivities();

        for (PvmActivity pvmAct : activityImpls) {
            ActivityImpl activityImpl = (ActivityImpl) pvmAct;
            if ("userTask".equals(activityImpl.getProperty("type"))) {
                ActivityBehavior activityBehavior = activityImpl.getActivityBehavior();

                TaskDefinition taskDefinition =
                    getTaskDefinitionByActivityBehavior(activityBehavior);
                if (taskDefinition != null && taskDefinition.getKey()
                    .equals(taskDefKey)) {
                    return taskDefinition;
                }
            }
        }
        return null;
    }

    private TaskDefinition getTaskDefinitionByActivityBehavior(ActivityBehavior activityBehavior) {
        UserTaskActivityBehavior userTaskActivityBehavior = null;

        /**
         * 多实例任务获取任务定义特殊处理
         */
        if (activityBehavior instanceof UserTaskActivityBehavior) {
            userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
        } else if (activityBehavior instanceof ParallelMultiInstanceBehavior) {
            ParallelMultiInstanceBehavior parallelMultiInstanceBehavior =
                (ParallelMultiInstanceBehavior) activityBehavior;
            AbstractBpmnActivityBehavior innerActivityBehavior =
                parallelMultiInstanceBehavior.getInnerActivityBehavior();
            userTaskActivityBehavior = (UserTaskActivityBehavior) innerActivityBehavior;
        }


        TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
        return taskDefinition;
    }

    @Override
    public List<FormProperty> getTaskFormProperties(String taskId) {
        Validate.notBlank(taskId, "传入参数taskId为空");
        List<FormProperty> formProperties = formService.getTaskFormData(taskId).getFormProperties();
        List<FormProperty> formPropertys = new ArrayList<>();
        for (FormProperty formProperty : formProperties) {
            formPropertys.add(new ProcFormProperty(formProperty));
        }
        return formPropertys;
    }

    public List<FormProperty> getHistTaskFormProperties(String taskId) {
        HistoricTaskInstance hisTaskInst =
            historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if (null == hisTaskInst)
            throw new WorkflowException("历史任务:{}不存在", taskId);
        String taskDefKey = hisTaskInst.getTaskDefinitionKey();
        ProcessDefinitionEntity procDef = (ProcessDefinitionEntity) repositoryService
            .getProcessDefinition(hisTaskInst.getProcessDefinitionId());
        List<ActivityImpl> activitys = procDef.getActivities();
        ActivityImpl curActivity = null;
        for (ActivityImpl activity : activitys) {
            if (taskDefKey.equals(activity.getId())) {
                curActivity = activity;
                break;
            }
        }

        TaskDefinition taskDef =
            getTaskDefinitionByActivityBehavior(curActivity.getActivityBehavior());
        DefaultTaskFormHandler tfh = (DefaultTaskFormHandler) taskDef.getTaskFormHandler();
        List<FormPropertyHandler> taskFormHandlers = tfh.getFormPropertyHandlers();
        List<FormProperty> taskForms = new ArrayList<FormProperty>();
        for (FormPropertyHandler handler : taskFormHandlers) {
            taskForms.add(new FormPropertyImpl(handler));
        }
        return taskForms;
    }

    /**
     * 判断上一步是否是退回修改操作
     * 退回修改任务直接进行点对点提交
     */

    private List<Task> backTrackTaskJump(Task task, String userId,
        List<HistoricTaskInstance> finishedHisTaskInstances, String comment) {
        Validate.notBlank(userId, "传入参数userId为空");
        String processInstanceId = task.getProcessInstanceId();
//        String taskDefinitionKey = ((TaskEntity) task).getTaskDefinitionKey();

        HistoricTaskInstance aimHistTaskInst =
            finishedHisTaskInstances.get(finishedHisTaskInstances.size() - 1);

        // 目标任务定义key
        String aimTaskDefKey = aimHistTaskInst.getTaskDefinitionKey();
        String aimAsssignee = aimHistTaskInst.getAssignee();

        WorkflowTask aimWorkflowTask = workflowEntityService.getWorkflowTask(aimHistTaskInst.getId());

        // 退回修改任务直接提交到退回点
        if (TaskStatusEnum.BACKTRACK.getName().equals(aimHistTaskInst.getDeleteReason())) {
//            List<HistoricActivityInstance> histActivitys =
//                historyService.createHistoricActivityInstanceQuery()
//                    .processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime()
//                    .asc().list();
            HistoricProcessInstance histProcInst = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

            // 获取流程所有的Activity定义
            ProcessDefinitionEntity processDefinitionEntity =
                    (ProcessDefinitionEntity) repositoryService
                            .getProcessDefinition(histProcInst.getProcessDefinitionId());
            List<ActivityImpl> activityImpls = processDefinitionEntity.getActivities();

            // 判断是否是并行任务
            if("Y".equals(aimWorkflowTask.getIsConcurrent())){
                /**
                 * 如果是并行任务,任务将点对点提交到并行网关前一个任务
                 */
                // 找出目标任务的Activity定义
                PvmActivity aimActivity = null;
                for (PvmActivity pvmAct : activityImpls) {
                    if (aimHistTaskInst.getTaskDefinitionKey().equals(pvmAct.getId())) {
                        aimActivity = pvmAct;
                    }
                }

                /**
                 * 此段逻辑不好用,可在WKFL_TASk表中添加"并行任务的父任务Key"
                 */
                // 获取目标节点的入线
                List<PvmTransition> incomings = aimActivity.getIncomingTransitions();
                PvmActivity gatewayAvtivity = incomings.get(0).getSource();
                // 网关入线
                List<PvmTransition> gatewayIncoming = gatewayAvtivity.getIncomingTransitions();
                // 网关前面的用户任务
                PvmActivity frontGatewayActivity = gatewayIncoming.get(0).getSource();


                // 将网关前面的任务设置为目标任务
                aimTaskDefKey = frontGatewayActivity.getId();
                for (HistoricTaskInstance histTask:finishedHisTaskInstances) {
                    if(aimTaskDefKey.equals(histTask.getTaskDefinitionKey())){
                        aimAsssignee = histTask.getAssignee();
                    }
                }

                // 判断目标节点和待提交任务节点是否是同一个位置,则不用进行跳转
                if(aimTaskDefKey.equals(task.getTaskDefinitionKey())){
                    return null;
                }
            }


            ProcTask aimTask =
                jumpForward(task.getId(), aimTaskDefKey, userId, TaskStatusEnum.FORWARD,
                    comment);
            // 设置原有的受理人
            aimTask.setAssignee(aimAsssignee);
            taskService.saveTask(aimTask.getTask());
            return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        }
        return null;
    }

    /**
     * 非历史节点跳转
     *
     * @param curTaskId
     * @param aimTaskDefKey
     * @param deleteReason
     * @return
     */
    private Task doTaskJumpForward(String curTaskId, String aimTaskDefKey,
        TaskStatusEnum deleteReason) {
        Validate.notBlank(curTaskId, "非历史节点跳转传入参数curTaskId为空");
        Validate.notBlank(aimTaskDefKey, "非历史节点跳转传入参数aimTaskDefKey为空");
        Validate.notBlank(deleteReason.getName(), "非历史节点跳转传入参数deleteReason为空");
        logger.debug("任务跳转,源任务ID:{},目标任务定义KEY:{}", curTaskId, aimTaskDefKey);
        Task curTask = taskService.createTaskQuery().taskId(curTaskId).singleResult();
        if (curTask == null)
            throw new WorkflowException("当前任务不存在");
        String processDefintionId = curTask.getProcessDefinitionId();
        Map<String, Object> vars = null;
        ReadOnlyProcessDefinition processDefinitionEntity =
            (ReadOnlyProcessDefinition) repositoryService
                .getProcessDefinition(processDefintionId);
        // 当前执行流
        String executionId = curTask.getExecutionId();
        // 当前节点
        // BPMN中userTask节点的id值
        String curTaskDefKey = curTask.getTaskDefinitionKey();
        ActivityImpl currentActivity =
            (ActivityImpl) processDefinitionEntity.findActivity(curTaskDefKey);

        // 目标节点
        ActivityImpl destinationActivity =
            (ActivityImpl) processDefinitionEntity.findActivity(aimTaskDefKey);

        if (curTaskDefKey.equals(aimTaskDefKey))
            throw new WorkflowException("跳转节点与当前节点为同一位置,无法进行退回");

        // 执行回退命令
        execTaskJumpCMD(executionId, destinationActivity, vars, currentActivity,
            deleteReason.getName());

        List<Task> runtimeTasks =
            taskService.createTaskQuery().processInstanceId(curTask.getProcessInstanceId()).list();
        Task jumpTask = null;
        for (Task task : runtimeTasks) {
            if (aimTaskDefKey.equals(task.getTaskDefinitionKey())) {
                jumpTask = task;
                break;
            }
        }
        if (jumpTask == null)
            throw new WorkflowException("退回任务查询失败!");

        return jumpTask;
    }

    /**
     * 历史任务跳转
     *
     * @param curTaskId
     * @param aimTaskDefKey
     * @param deleteReason
     * @return
     */
    private Task doTaskJumpBack(String curTaskId, String aimTaskDefKey,
        TaskStatusEnum deleteReason, String userId) {
        Validate.notBlank(curTaskId, "历史任务跳转传入参数curTaskId为空");
        Validate.notBlank(aimTaskDefKey, "历史任务跳转传入参数aimTaskDefKey为空");
        Validate.notBlank(deleteReason.getName(), "历史任务跳转传入参数deleteReason为空");
        logger.trace("任务跳转", curTaskId, aimTaskDefKey);
        Task curTask = taskService.createTaskQuery().taskId(curTaskId).singleResult();
        if (curTask == null)
            throw new WorkflowException("当前任务不存在");
        String processDefintionId = curTask.getProcessDefinitionId();
        String procInstId = curTask.getProcessInstanceId();
        ProcessInstance processInstance =
                runtimeService.createProcessInstanceQuery().processInstanceId(procInstId)
                        .singleResult();
        List<ProcHandler> procHandlers = procHandlerResolve
                .findHandlers(processInstance.getProcessDefinitionKey());
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procInstId);
        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(curTaskId);
        workflowTask.setFinishType(TaskStatusEnum.FORWARD.getName());
        Map<String, Object> vars = workflowBomParser
                .parse(processInstance.getProcessDefinitionKey(), procHandlers, workflowProc,
                        workflowTask);

        ReadOnlyProcessDefinition processDefinitionEntity =
                (ReadOnlyProcessDefinition) repositoryService
                        .getProcessDefinition(processDefintionId);
        // 当前执行流
        String executionId = curTask.getExecutionId();
        // 当前节点
        // BPMN中userTask节点的id值
        String curTaskDefKey = curTask.getTaskDefinitionKey();
        ActivityImpl currentActivity =
                (ActivityImpl) processDefinitionEntity.findActivity(curTaskDefKey);

        // 查询历史任务
        List<HistoricTaskInstance> aimTasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(curTask.getProcessInstanceId()).taskDefinitionKey(aimTaskDefKey)
                .list();
        HistoricTaskInstance aimTask = null;
        if (aimTasks == null || aimTasks.size() == 0) {
            throw new WorkflowException("历史任务节点不存在,无法进行任务回退操作!");
        } else {
            aimTask = aimTasks.get(0);
        }
        // 移除历史任务中退回的历史任务----------------此处使用了流程删除理由的判断注意和流程退回中的标记一致
        // 目标节点
        ActivityImpl destinationActivity =
                (ActivityImpl) processDefinitionEntity.findActivity(aimTaskDefKey);

        if (curTaskDefKey.equals(aimTaskDefKey))
            throw new WorkflowException("退回节点与当前节点为同一位置,无法进行退回");

        /**
         * 跳转前的执行流实例
         */
        ExecutionEntity srcExec =
                (ExecutionEntity) runtimeService.createExecutionQuery().executionId(executionId)
                        .singleResult();
        curTask.setAssignee(userId);
        curTask.setOwner(userId);
        taskService.saveTask(curTask);


        String processInstanceId = curTask.getProcessInstanceId();
        List<Task> taskList =
                taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();

        Task jumpTask = null;
        if (taskList!=null&&taskList.size()>1){
            execTaskYJumpCMD(executionId,processInstanceId,destinationActivity,vars,currentActivity,
                    deleteReason.getName());

            //处理并行任务的外挂表
            taskList.removeIf(task -> task.getTaskDefinitionKey().equals(curTaskDefKey));
            for (Task task:taskList){
                WorkflowTask updateTask = workflowEntityService.getWorkflowTask(task.getId());
                updateTask.setFinishType(TaskStatusEnum.FORWARD.getName());
                updateTask.setAssignee(curTask.getAssignee());
                updateTask.setOwner(curTask.getOwner());
                workflowEntityService.updateWorkflowTask(updateTask);
            }
            jumpTask =taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        }else {
            // 执行回退命令
            execTaskJumpCMD(executionId, destinationActivity, vars, currentActivity,
                    deleteReason.getName());

            /**
             * 查询出当前的任务
             * 情况1.退回线路中不存在网关
             * 情况2.退回线路中存在网关,清除网关(目标任务最近的)后面与待退回任务平行的任务
             */
            List<Task> runtimeTasks =
                    taskService.createTaskQuery().processInstanceId(curTask.getProcessInstanceId()).list();
            for (Task task : runtimeTasks) {
                if (aimTaskDefKey.equals(task.getTaskDefinitionKey())) {
                    jumpTask = task;
                    break;
                }
            }
            if (jumpTask == null)
                throw new WorkflowException("退回任务查询失败!");

            /**
             * 跳转后的执行流实例
             * 判断任务是否并行任务,excution表的IS_CONCURRENT_字段
             * 存在平行任务情况,清理退回平行任务
             */
            ExecutionEntity aimExec =
                    (ExecutionEntity) runtimeService.createExecutionQuery().executionId(executionId)
                            .singleResult();
            if (srcExec!=null){
                if (srcExec.isConcurrent() && srcExec.getParentId().equals(aimExec.getParentId())) {
                    cleanParallelGatewayJumpTask(runtimeTasks, jumpTask.getProcessInstanceId(),
                            aimTaskDefKey, deleteReason);
                }
            }


        }


        /**
         * 设置任务原有的受理人
         * 即使是多次退回,任务的原受理人是不会变的
         */
        List<Task> newRuntimeTasks =
                taskService.createTaskQuery().processInstanceId(curTask.getProcessInstanceId()).list();
        if (newRuntimeTasks!=null&&newRuntimeTasks.size()==1&&!StringUtils.isBlank(aimTask.getAssignee())){
            jumpTask.setAssignee(aimTask.getAssignee());
            taskService.saveTask(jumpTask);
        }

        WorkflowTask curWorkflowTask = workflowEntityService.getWorkflowTask(curTaskId);
        curWorkflowTask.setFinishType(TaskStatusEnum.FORWARD.getName());
        curWorkflowTask.setAssignee(curTask.getAssignee());
        curWorkflowTask.setOwner(curTask.getOwner());
        workflowEntityService.updateWorkflowTask(curWorkflowTask);


        return jumpTask;
    }

    /**
     * 清理掉存在并行网关的流程中,回退过程中的平行任务(比如说一个任务退回,一个任务还在原地)
     * 简单的处理方式:
     * 1. 时间升序,查找出目标任务到最终历史任务活动节点区间(排除掉目标任务)
     * 2. 在区间中,查找出目标任务后面最近的一个网关
     * 3. 以此网关的时间为判断依据,后面所有的活动节点与当前存活任务匹配,将交集的任务进行删除操作
     */
    private void cleanParallelGatewayJumpTask(List<Task> runtimeTasks, String processInstanceId,
        String aimTaskDefKey, TaskStatusEnum taskStatusEnum) {
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc()
            .list();

        // 查找退回区间
        List<HistoricActivityInstance> srcAimSection = new ArrayList<>();
        boolean isSrcAim = false;
        for (HistoricActivityInstance hisActInt : list) {
            if (hisActInt.getActivityId().equals(aimTaskDefKey))
                isSrcAim = true;
            if (isSrcAim)
                srcAimSection.add(hisActInt);
        }

        // 从网关到最后的活动节点区间
        List<HistoricActivityInstance> cleanSection = new ArrayList<>();
        boolean isClean = false;
        /**
         * 搜寻出退回区间内所有非活动节点
         * 如果有并行网关则标记存储
         */
        for (HistoricActivityInstance hisActInt : srcAimSection) {
            if (("parallelGateway").equals(hisActInt.getActivityType())||("inclusiveGateway").equals(hisActInt.getActivityType()))
                isClean = true;
            if (isClean && !hisActInt.getActivityId().equals(aimTaskDefKey))
                cleanSection.add(hisActInt);
        }
        if (cleanSection.size() > 0) {
            Set<Task> clearTasks = new HashSet<Task>();
            for (HistoricActivityInstance hisActInt : cleanSection) {
                for (Task task : runtimeTasks) {
                    // 判断是否是同一个任务定义
                    if (task.getTaskDefinitionKey().equals(hisActInt.getActivityId())) {
                        clearTasks.add(task);
                    }
                }
            }
            HistoricProcessInstance histProcInst = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

            WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(processInstanceId);
            // 任务状态转流程状态
            this.switchTaskStatusToProcStatus(workflowProc, taskStatusEnum);

            for (Task task : clearTasks) {
                this.addComment(task.getId(), WorkflowConstant.SYSTEM_NAME, taskStatusEnum,
                    "并行任务发生退回,系统自动退回");
                deleteTask((TaskEntity) task, taskStatusEnum);
                deleteExecution(task.getExecutionId());


                HistoricTaskInstance histTaskInst =
                        historyService.createHistoricTaskInstanceQuery().taskId(task.getId()).singleResult();

                WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(task.getId());
                workflowTask.setFinishType(taskStatusEnum.getName());
                workflowTask.setFinishTime(histTaskInst.getEndTime());
                workflowTask.setAssignee( WorkflowConstant.SYSTEM_NAME);
                workflowTask.setOwner( WorkflowConstant.SYSTEM_NAME);

                // 新任务未产生,所以为null
                WorkflowTask dscWorkflowTask = null;

                // 调用任务跳转所触发的Handler
                this.invokeTaskJumpHandler(histProcInst.getProcessDefinitionKey(),workflowProc,
                        workflowTask, dscWorkflowTask, taskStatusEnum);

                clearParallelGatewayJumpTaskNotify(task);
            }

            if (clearTasks.size() > 0) {
                /**
                 * 删除执行实例,防止在接下来的网关判断出错(主要清理提交掉的并行任务关联的执行流)
                 * 1. 不在使用中的,IS_ACTIVE_=0
                 * 2. 非流程实例的,IS_SCOPE_=0
                 * 3. 并行的,IS_CONCURRENT_=1
                 */
                String sql = "DELETE FROM " + managementService.getTableName(Execution.class)
                    + " WHERE IS_ACTIVE_=0 AND IS_SCOPE_=0 AND IS_CONCURRENT_=1 AND "
                    + "PROC_INST_ID_=#{procInstId}";
                runtimeService.createNativeExecutionQuery().sql(sql)
                    .parameter("procInstId", processInstanceId).singleResult();
            }

        }
    }

    private void clearParallelGatewayJumpTaskNotify(Task task) {
        logger.info("任务回退操作经过并行网关,清理回退任务的平行任务:" + task.toString());
    }

    private void deleteTask(TaskEntity taskEntity, TaskStatusEnum deleteReason) {
        logger.trace("任务:" + taskEntity.getId() + "设置任务删除理由");
        CommandExecutor commandExecutor =
            ((RuntimeServiceImpl) runtimeService).getCommandExecutor();
        commandExecutor.execute(new DeleteRunningTaskCmd(taskEntity, deleteReason));
    }

    private void deleteExecution(String executionId) {
        /**
         * 删除任务的执行流实例(主要清理未提交任务的执行流)
         */
        String sql =
            "DELETE FROM " + managementService.getTableName(Execution.class) + " WHERE ID_=#{id}";
        runtimeService.createNativeExecutionQuery().sql(sql).parameter("id", executionId)
            .singleResult();
    }

    private boolean execTaskJumpCMD(String executionId, ActivityImpl destinationActivity,
        Map<String, Object> vars,
        ActivityImpl currentActivity, String deleteReason) {
        CommandExecutor commandExecutor =
            ((RuntimeServiceImpl) runtimeService).getCommandExecutor();
        commandExecutor
            .execute(new JDJumpTaskCmd(executionId, destinationActivity, vars, currentActivity,
                deleteReason));
        return true;
    }


    private boolean execTaskYJumpCMD(String executionId,String parentId,ActivityImpl destinationActivity,
                                     Map<String, Object> vars,ActivityImpl currentActivity, String deleteReason){
        CommandExecutor commandExecutor =
                ((RuntimeServiceImpl) runtimeService).getCommandExecutor();
        commandExecutor
                .execute(new ParallelJumpTaskCmd(executionId, parentId, destinationActivity, vars,
                        currentActivity,deleteReason));
        return true;
    }






    private Task doTaskJumpYForward(String curTaskId, String aimTaskDefKey){
        logger.debug("任务跳转,源任务ID:{},目标任务定义KEY:{}", curTaskId, aimTaskDefKey);
        Task curTask = taskService.createTaskQuery().taskId(curTaskId).singleResult();
        if (curTask == null)
            throw new WorkflowException("当前任务不存在");

        //对当前任务节点(wkfl_task)进行'commit'处理
        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(curTaskId);
        workflowTask.setFinishType(TaskStatusEnum.COMPLETED.getName());
        workflowTask.setFinishTime(DateKit.now());
        beanCruder.save(workflowTask);
        //节点跳转
        managementService.executeCommand(new JumpActivityCmd(curTask.getProcessInstanceId(),aimTaskDefKey));
        List<Task> runtimeTasks =
            taskService.createTaskQuery().processInstanceId(curTask.getProcessInstanceId()).list();
        Task jumpTask = null;
        for (Task task : runtimeTasks) {
            if (aimTaskDefKey.equals(task.getTaskDefinitionKey())) {
                jumpTask = task;
                break;
            }
        }
        if(jumpTask==null){
            throw new WorkflowException("流程节点向前跳转失败");
        }

        return jumpTask;
    }

    private List<WorkflowTask> getLatestWKTaskList(String procInstId) {
        List<WorkflowTask> latestWKTaskList = new ArrayList<>();
        List<ProcTask> procTaskList = this.getLatestTasks(procInstId);
        procTaskList.forEach(task -> {
            WorkflowTask wkTask = new WorkflowTask();
            wkTask.setProcId(task.getProcessInstanceId());
            wkTask.setTaskId(task.getId());
            wkTask.setTaskInstId(task.getId());
            wkTask.setTaskDefKey(task.getTaskDefinitionKey());
            wkTask.setTaskName(task.getName());
            wkTask.setOwner(task.getOwner());
            wkTask.setAssignee(task.getAssignee());
            wkTask.setArrivalTime(task.getCreateTime());
            //新的任务生成时判断当前任务处理人是否为委托申请中的同流程委托申请人,


            // 判断任务是否是并行任务
            ExecutionEntity execEntity =
                    (ExecutionEntity) runtimeService.createExecutionQuery().executionId(task.getExecutionId())
                            .singleResult();
            if (execEntity.isConcurrent()) {
                wkTask.setIsConcurrent("Y");
            } else {
                wkTask.setIsConcurrent("N");
            }

            latestWKTaskList.add(wkTask);
        });
        return latestWKTaskList;
    }

    private WorkflowTask getLatestWKTask(String procId) {
        ProcTask task = this.getLatestTask(procId);
        WorkflowTask wkTask = new WorkflowTask();
        wkTask.setProcId(task.getProcessInstanceId());
        wkTask.setTaskId(task.getId());
        wkTask.setTaskInstId(task.getId());
        wkTask.setTaskDefKey(task.getTaskDefinitionKey());
        wkTask.setTaskName(task.getName());
        wkTask.setOwner(task.getOwner());
        wkTask.setAssignee(task.getAssignee());
        wkTask.setArrivalTime(task.getCreateTime());
        return wkTask;
    }

    /**
     * 任务状态转流程状态
     * @param taskStatusEnum
     */
    private WorkflowProc switchTaskStatusToProcStatus(WorkflowProc workflowProc, TaskStatusEnum taskStatusEnum){
        switch (taskStatusEnum) {
            case BACKORIGIN:
                workflowProc.setStatus(ProcStatusEnum.BACKORIGIN.getName());
                break;
            case BACKTRACK:
                workflowProc.setStatus(ProcStatusEnum.BACKTRACK.getName());
                break;
            case BACKFRONT:
                workflowProc.setStatus(ProcStatusEnum.BACKFRONT.getName());
                break;
            case RETRIEVE:
                workflowProc.setStatus(ProcStatusEnum.RETRIEVE.getName());
                break;
            default:
                break;
        }
        return workflowProc;
    }

    /**
     * 调用任务退回所触发的流程handler
     * @param key
     * @param workflowProc
     * @param workflowTask
     * @param dscWorkflowTask
     * @param taskStatusEnum
     */
    private void invokeTaskJumpHandler(String key, WorkflowProc workflowProc, WorkflowTask workflowTask,WorkflowTask dscWorkflowTask,TaskStatusEnum taskStatusEnum){
        for (ProcHandler procHandler : procHandlerResolve
                .findHandlers(key)) {
            switch (taskStatusEnum) {
                case BACKORIGIN:
                    procHandler.afterTaskJump(TaskJumpEnum.BACKORIGIN, workflowProc, workflowTask,
                            dscWorkflowTask);
                    break;
                case BACKTRACK:
                    procHandler.afterTaskJump(TaskJumpEnum.BACKTRACK, workflowProc, workflowTask,
                            dscWorkflowTask);
                    break;
                case BACKFRONT:
                    procHandler.afterTaskJump(TaskJumpEnum.BACKFRONT, workflowProc, workflowTask,
                            dscWorkflowTask);
                    break;
                case RETRIEVE:
                    procHandler.afterTaskJump(TaskJumpEnum.RETRIEVE, workflowProc, workflowTask,
                            dscWorkflowTask);
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public boolean isProcInstFinished(String procInstId) {
        boolean isHave =
            historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).count() > 0;
        if (isHave) {
            return runtimeService.createExecutionQuery().processInstanceId(procInstId).count() <= 0;
        } else {
            throw new WorkflowException("不存在此流程:{0}", procInstId);
        }
    }

    @Override
    public List<HistoricTask> getHistTasks(String procInstId) {
        Validate.notBlank(procInstId, "传入参数procInstId为空");
//        List<HistoricTaskInstance> histTaskInsts = new ArrayList<>();
        List<HistoricTaskInstance> histTaskInsts =
            historyService.createHistoricTaskInstanceQuery().processInstanceId(procInstId)
                .finished().orderByTaskCreateTime().asc().list();

        List<HistoricTask> historicTasks = new ArrayList<>();
        for (HistoricTaskInstance histTaskInst : histTaskInsts) {
            historicTasks.add(new HistoricTask(histTaskInst));
        }
        return historicTasks;
    }

    @Transactional
    @Override
    public void deliverTo(String taskId, String userId, String loginUserId, String comment) {
        Validate.notBlank(taskId, "传入参数taskId为空");
        Validate.notBlank(userId, "传入参数userId为空");
        logger.info("任务:{}转他人处理:{}", taskId, userId);
        this.addComment(taskId, loginUserId, TaskStatusEnum.DELIVERTO, comment);
        this.changeAssignee(taskId, userId);
    }


    @Override
    @Transactional
    public void takeAdvice(List<String> userIds, String taskId, String comment, String srcUserId) {
        this.unClaimTask(taskId);
        //判断该任务是否已被签收(特别是在存在候选人/候选组的情况下)
        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);
        String assignee = workflowTask.getAssignee();
        String owner = workflowTask.getOwner();
        String ownerName = authService.getUserNameById(owner);
        String assignerName = authService.getUserNameById(assignee);
        if (null !=owner&& !owner.equals(srcUserId)) {
            throw new WorkflowException("无法处理本任务,该任务所有人是:{0}", ownerName);
        }
        //双重验证是为了防止只设置了 assignee 的情况
        if (null!=assignee&& !assignee.equals(srcUserId)) {
            throw new WorkflowException("无法处理本任务,已被:{0}签收", assignerName);
        }
        //设置任务持有人以及将被征询人加入到候选人列表
        workflowTask.setOwner(srcUserId);
        String userName = authService.getUserNameById(srcUserId);
        for (String userId : userIds) {
            if (userId != null && !"".equals(userId)) {
                this.addCandidateUser(taskId,userId);
                String askForName = authService.getUserNameById(userId);
                //将征询意见保存到征询意见表中
                WorkflowSolicit workflowSolicit = new WorkflowSolicit();
                workflowSolicit.setProcId(workflowTask.getProcId());
                workflowSolicit.setSolicitor(srcUserId);
                workflowSolicit.setSolicitComment(comment);
                workflowSolicit.setTaskId(taskId);
                workflowSolicit.setAskFor(userId);
                String summary =
                    userName +  " 向 " + askForName + " 征求意见";
                workflowSolicit.setSolicitSummary(summary);
                beanCruder.insert(workflowSolicit);
            } else {
                throw new WorkflowException("{0}为空,被征求人不存在", userId);
            }
        }
        beanCruder.update(workflowTask);
    }


    @Override
    public boolean feedbackStatus(String taskId) {
        ValidateKit.notBlank(taskId, "任务Id不能为空");
        boolean isFeedBackStatus = false;
        String userId = AuthHolder.getUser().getId();

        //无论任务并行与否 任务都可能处在 池模式
        //加一层判断判断该任务下是否存在 被征询意见列表

        List<WorkflowSolicit> workflowSolicits =
            workflowEntityService.getWorkflowSolicitByTaskId(taskId);
        //尝试改为Java 8 的 写法
        if (null!=workflowSolicits){
            for (WorkflowSolicit workflowSolicit : workflowSolicits) {
                String askForId = workflowSolicit.getAskFor();
                if (null!=userId&&userId.equals(askForId)){
                    isFeedBackStatus =true;
                }
            }
        }
        return isFeedBackStatus;
    }

    @Override
    @Transactional
    public void feedback(String taskId, String comment, String userId) {
        //被征求人填写征求意见
        ValidateKit.notBlank(taskId,"传入的taskId 为空");
        ValidateKit.notBlank(userId,"传入的userId 为空");
        ValidateKit.notBlank(comment,"传入的comment 为空");

        //判断任务是否已经被处理
        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);
        if (null!=workflowTask.getFinishType()){
            throw new WorkflowException("该任务:{0}已被处理,请刷新待处理任务列表",taskId);
        }

        WorkflowSolicit workflowSolicit =
            workflowEntityService.getWorkflowSolicitByTaskIdAndUserId(taskId, userId);
        if (null!=workflowSolicit){
            workflowSolicit.setReplyComment(comment);
            beanCruder.update(workflowSolicit);
        }else {
            throw new WorkflowException("该taskId:{0}对应的征询意见不存在",taskId);
        }

        //将被征求人从候选列表移除
        this.deleteCandidateUser(taskId,userId);
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

    @SuppressWarnings("unchecked")
    protected <T> T parseExpression(String el, Map<String, Object> variables,
        Class<T> expectedType) {
        /**
         * 参数解析需要注意的问题:
         * 表达式:${planSum == '否'}
         *
         * 大致过程
         * 1. 获取表达式中的参数数值和类型等,例如:planSum
         * 2. 将与参数对比的值转化为参数的数值类型,例如:如果planSum是Double类型,它会将'否'转化为Double类型
         * 3. 比较
         *
         * 如果表达式参数值和要比较的值类型不一致就会出现转化异常的问题
         */

        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();

        if (variables != null && variables.size() > 0) {
            for (String name : variables.keySet()) {
                Object value = variables.get(name);
                context.setVariable(name, factory.createValueExpression(value, value.getClass()));
            }
        }

        ValueExpression e = factory.createValueExpression(context, el, expectedType);
        return (T) e.getValue(context);
    }

    /**
     * 流程分支上的条件语句，判断条件是否成立
     *
     * @param el
     * @param variables
     * @return
     */
    protected Boolean isExpressionCondition(String el, Map<String, Object> variables) {
        Boolean parseExprResult = null;

        try {
            parseExprResult = parseExpression(el, variables, Boolean.class);
        } catch (Exception e) {
            if (e instanceof PropertyNotFoundException) {
                // 参数不存在时,不进行向下计算
                parseExprResult = null;
            } else {
                throw new WorkflowException(e);
            }
        }
        return parseExprResult;
    }

    @Override
    public ArrayList<ArrayList<PvmActivity>> getActivityLines(String taskId) {
        Task task = getTask(taskId);
        ProcessDefinitionEntity processDefinitionEntity =
            (ProcessDefinitionEntity) repositoryService
                .getProcessDefinition(task.getProcessDefinitionId());
        List<ActivityImpl> activityImpls = processDefinitionEntity.getActivities();
        PvmActivity start = null;
        for (PvmActivity pvmAct : activityImpls) {
            if ("startEvent".equals(pvmAct.getProperty("type"))) {
                start = pvmAct;
            }
        }
        ArrayList<PvmActivity> line = new ArrayList<PvmActivity>();
        ArrayList<PvmActivity> preActivities = new ArrayList<PvmActivity>();
        ArrayList<ArrayList<PvmActivity>> lines = new ArrayList<ArrayList<PvmActivity>>();
        Map<String, Object> variables = runtimeService.getVariables(task.getProcessInstanceId());
        /**
         * 此处需要增加最新参数的获取
         */
        ArrayList<ArrayList<PvmActivity>> lines2 =
            getActivityLines(start, 0, line, preActivities, lines, variables);
        return lines2;
    }

    private ArrayList<ArrayList<PvmActivity>> getActivityLines(
        PvmActivity pvmActivity, int index, ArrayList<PvmActivity> line,
        ArrayList<PvmActivity> preActivities,
        ArrayList<ArrayList<PvmActivity>> lines,
        Map<String, Object> variables) {
        PvmActivity current = pvmActivity;
        ArrayList<PvmActivity> nextActivities = new ArrayList<PvmActivity>();

        List<PvmTransition> outgoings = pvmActivity.getOutgoingTransitions();
        if ("parallelGateway".equals(current.getProperty("type")) && outgoings.size() > 1) {
            for (PvmTransition outgoing : outgoings) {
                nextActivities.add(outgoing.getDestination());
            }
        } else if ("exclusiveGateway".equals(current.getProperty("type")) && outgoings.size() > 1) {
            for (PvmTransition outgoing : outgoings) {
                Object conditionText = outgoing.getProperty("conditionText");
                String conditionTextStr = conditionText.toString();

                Boolean parseExprResult = isExpressionCondition(conditionTextStr, variables);

                if (!"".equals(conditionTextStr) && null != parseExprResult && parseExprResult) {
                    nextActivities.add(outgoing.getDestination());
                }
            }
        } else if (outgoings.size() == 1) {
            nextActivities.add(outgoings.get(0).getDestination());
        }

        line.add(current);

        if (nextActivities.size() == 0) {
            lines.add(line);
            return lines;
        }

        if (nextActivities.size() > 1) {
            preActivities = cloneLine(line);
        }

        while (index < nextActivities.size()) {
            getActivityLines(nextActivities.get(index), 0, line, preActivities, lines, variables);
            index++;
            line = cloneLine(preActivities);
        }
        return lines;
    }

    private ArrayList<PvmActivity> cloneLine(ArrayList<PvmActivity> sources) {
        ArrayList<PvmActivity> rtns = new ArrayList<PvmActivity>();
        for (PvmActivity source : sources) {
            rtns.add(source);
        }
        return rtns;
    }

    @Override
    public Set<PvmActivity> getNextUserActivities(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
            throw new WorkflowException("任务:{0}不存在", taskId);
        String activityId = task.getTaskDefinitionKey();
        Set<PvmActivity> nextPvmActivities = new HashSet<PvmActivity>();
        ArrayList<ArrayList<PvmActivity>> lines = getActivityLines(taskId);
        begin:
        for (ArrayList<PvmActivity> line : lines) {
            for (int i = 0; i < line.size(); i++) {
                PvmActivity current = line.get(i);
                if (activityId.equals(current.getId())) {
                    for (int j = i + 1; j < line.size(); j++) {
                        if ("userTask".equals(line.get(j).getProperty("type"))) {
                            nextPvmActivities.add(line.get(j));
                            continue begin;
                        }
                    }
                    continue begin;
                }
            }
        }
        return nextPvmActivities;
    }

    @Override
    public Set<PvmActivity> getForeUserActivities(String taskId) {
        String activityId = getTask(taskId).getTaskDefinitionKey();
        Set<PvmActivity> forePvmActivities = new HashSet<PvmActivity>();
        ArrayList<ArrayList<PvmActivity>> lines = getActivityLines(taskId);
        begin:
        for (ArrayList<PvmActivity> line : lines) {
            for (int i = 0; i < line.size(); i++) {
                PvmActivity current = line.get(i);
                if (activityId.equals(current.getId())) {
                    for (int j = i - 1; j >= 0; j--) {
                        if ("userTask".equals(line.get(j).getProperty("type"))) {
                            forePvmActivities.add(line.get(j));
                            continue begin;
                        }
                    }
                    continue begin;
                }
            }
        }
        return forePvmActivities;
    }


    public List<PvmActivity> getNextActivities(String taskId) {
        ArrayList<PvmActivity> resultActivities = new ArrayList<>();

        // 找出当前任务的Activity定义
        PvmActivity pvmActivity = this.getTaskPvmActivity(taskId);

        // 任务出线
        List<PvmTransition> outgoings = pvmActivity.getOutgoingTransitions();

        Set<PvmActivity> nextActivities = new HashSet<>();
        for (PvmTransition outgoing : outgoings) {
            nextActivities.add(outgoing.getDestination());
        }

        ArrayList<PvmActivity> pvmActivities = new ArrayList<>(nextActivities);

        /**
         * 当存在判断网关时进行解析
         */
        if(pvmActivities.size()==1){
            PvmActivity current = pvmActivities.get(0);
            List<PvmTransition> exlsOutgoings = current.getOutgoingTransitions();
            if ("exclusiveGateway".equals(current.getProperty("type")) && exlsOutgoings.size() > 1) {
                ProcTask task = this.getTask(taskId);
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(task.getProcessInstanceId()).singleResult();

                List<ProcHandler> procHandlers = procHandlerResolve
                        .findHandlers(processInstance.getProcessDefinitionKey());

                WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(processInstance.getId());
                WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);

                // 流程参数解析
                Map<String, Object> vars = workflowBomParser
                        .parse(processInstance.getProcessDefinitionKey(), procHandlers, workflowProc,
                                workflowTask);

                for (PvmTransition outgoing : exlsOutgoings) {
                    Object conditionText = outgoing.getProperty("conditionText");
                    String conditionTextStr = conditionText.toString();

                    Boolean parseExprResult = isExpressionCondition(conditionTextStr, vars);

                    if (!"".equals(conditionTextStr) && null != parseExprResult && parseExprResult) {
                        for (PvmTransition pvmTransition : outgoing.getDestination().getOutgoingTransitions()) {
                            resultActivities.add(pvmTransition.getDestination());
                        };
                        return resultActivities;
                    }
                }

            }
        }


        resultActivities.addAll(pvmActivities);
        return resultActivities;
    }

    /**
     * 查找任务的PvmActivity
     * @param taskId
     * @return
     */
    private PvmActivity getTaskPvmActivity(String taskId){
        TaskDefinition taskDefinition = this.getTaskDefinition(taskId);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        // 获取流程所有的Activity定义
        ProcessDefinitionEntity processDefinitionEntity =
                (ProcessDefinitionEntity) repositoryService
                        .getProcessDefinition(task.getProcessDefinitionId());
        List<ActivityImpl> activityImpls = processDefinitionEntity.getActivities();

        // 找出当前任务的Activity定义
        PvmActivity pvmActivity = null;
        for (PvmActivity pvmAct : activityImpls) {
            if (taskDefinition.getKey().equals(pvmAct.getId())) {
                pvmActivity = pvmAct;
            }
        }

        if(pvmActivity==null) {
            throw new WorkflowException("查找任务:{0}的PvmActivity为空", taskId);
        }

        return pvmActivity;
    }

    @Override
    public List<HistoricActivityInstance> getAllowBackActivitys(String procInstId) {
        List<HistoricActivityInstance> hisActivityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInstId).finished()
                .orderByHistoricActivityInstanceStartTime().asc().list();
        if(hisActivityInstances.size()==0) return null;

        String sql = "select * from "+managementService.getTableName(Task.class)+" where proc_inst_id_=#{procInstId}";
        List<Task> runtimeTasks = taskService.createNativeTaskQuery().sql(sql).parameter("procInstId", procInstId).list();

        List<HistoricActivityInstance> allowBackActivitys = new ArrayList<>();
        for (HistoricActivityInstance hisActivityInstance:hisActivityInstances) {
            if(("userTask").equals(hisActivityInstance.getActivityType())) {
                boolean isContinue=false;
                // 排除掉当前只在运行的任务的活动实例
                for (Task task:runtimeTasks) {
                    if(((TaskEntity)task).getTaskDefinitionKey().equals(hisActivityInstance.getActivityId())) {
                        isContinue = true;
                        continue;
                    }
                }
                if(isContinue) continue;
                if(!isHaveHisActivity(allowBackActivitys, hisActivityInstance)) allowBackActivitys.add(hisActivityInstance);
            }
        }
        return allowBackActivitys;
    }

    /**
     *获取所有未来节点
     */
    @Override
    public List<ActivityImpl> getAllForwardActivities(String procInstId){
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procInstId);
        if (null==workflowProc){
            return null;
        }
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
        ProcessDefinitionEntity procDefinition =
            (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(tasks.get(0).getProcessDefinitionId());
        List<ActivityImpl> activities = procDefinition.getActivities();
        //所有任务节点
        List<ActivityImpl> activityList =
            activities.stream().filter(activity -> activity.getProperty("type").equals("userTask"))
                .collect(Collectors.toList());
        //历史任务节点
        List<HistoricActivityInstance> backActivities = getAllowBackActivitys(procInstId);
        //当前任务节点
        Execution execution =
            runtimeService.createExecutionQuery().processInstanceId(procInstId).singleResult();
        String curActivityId = execution.getActivityId();

        //去除当前节点和历史节点
        Iterator<ActivityImpl> iterator = activityList.iterator();
        if (backActivities.size()!=0){
            for (HistoricActivityInstance historicActivityInstance:backActivities){
                while (iterator.hasNext()) {
                    ActivityImpl activity = iterator.next();
                    if (activity.getId().equals(curActivityId)||activity.getId().equals(historicActivityInstance.getActivityId())) {
                        iterator.remove();
                    }
                }
            }
        }else {
            while (iterator.hasNext()){
                ActivityImpl activity = iterator.next();
                if (activity.getId().equals(curActivityId)){
                    iterator.remove();
                }
            }
        }


        //这种情况下  iterator 在 remove 时更具优势
        //        List<ActivityImpl> tempList = new ArrayList<>();
        //        tempList.addAll(activityList);
        //        if (activityList!=null&&!activityList.isEmpty()){
        //            for (ActivityImpl activity:activityList){
        //                if (backActivities.size()!=0){
        //                    for (HistoricActivityInstance instance:backActivities){
        //                        if (activity.getId().equals(curActivityId)||activity.getId().equals(instance.getActivityId())){
        //                            tempList.remove(activity);
        //                        }
        //                    }
        //                }else {
        //                    if (activity.getId().equals(curActivityId)){
        //                        tempList.remove(activity);
        //                    }
        //                }
        //            }
        //            activityList=tempList;
        //        }

        return activityList;
    }

    /**
     * 判断集合中是否存在相同ActivityId的历史活动节点
     * @param activitys
     * @param activity
     * @return
     */
    private boolean isHaveHisActivity(List<HistoricActivityInstance> activitys, HistoricActivityInstance activity) {
        for (HistoricActivityInstance historicActivityInstance : activitys) {
            if(historicActivityInstance.getActivityId().equals(activity.getActivityId())){
                return true;
            }
        }
        return false;
    }


//
//    public static <T> List<T> copyIterator(Iterator<T> iterator){
//        List<T> copy = new ArrayList<>();
//        while (iterator.hasNext())
//            copy.add(iterator.next());
//        return copy;
//    }

}