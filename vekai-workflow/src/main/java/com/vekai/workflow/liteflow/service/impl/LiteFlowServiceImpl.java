package com.vekai.workflow.liteflow.service.impl;

import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.serialnum.finder.SerialNumberGeneratorFinder;
import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;
import com.vekai.workflow.entity.*;
import com.vekai.workflow.exception.WorkflowException;
import com.vekai.workflow.liteflow.entity.LiteflowForm;
import com.vekai.workflow.liteflow.entity.LiteflowResource;
import com.vekai.workflow.liteflow.entity.LiteflowTemplate;
import com.vekai.workflow.liteflow.handler.LiteHandler;
import com.vekai.workflow.liteflow.handler.LiteHandlerResolve;
import com.vekai.workflow.liteflow.model.LiteTaskDef;
import com.vekai.workflow.liteflow.parser.LiteFlowParse;
import com.vekai.workflow.liteflow.service.LiteFlowService;
import com.vekai.workflow.model.enums.ProcFinishEnum;
import com.vekai.workflow.model.enums.ProcStatusEnum;
import com.vekai.workflow.model.enums.TaskJumpEnum;
import com.vekai.workflow.model.enums.TaskStatusEnum;
import com.vekai.workflow.nopub.service.WorkflowEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: qyyao
 * @Description: 简式流程相关服务
 * @Date: Created in 09:51 13/03/2018
 */

@Service
public class LiteFlowServiceImpl implements LiteFlowService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final static int EXPR_LENGTH = 2;
    private Boolean isLastTask;

    private final static String WKFL_TASK_TABLE = "WKFL_TASK";
    private final static String WKFL_COMMENT_TABLE = "WKFL_COMMENT";
    private final static String WKFL_COMMENT_HIST_TABLE = "WKFL_COMMENT_HIST";
    private final static String WKFL_LITE_RESOURCE_TABLE = "WKFL_LITE_RESOURCE";
    private final static String WKFL_LITE_FORM_TABLE = "WKFL_LITE_FORM";
    private final static String WKFL_LITE_TEMPLATE_TABLE = "WKFL_LITE_TEMPLATE";

    private final static String GENERATOR_PROC_ID =
        "com.amarsoft.amix.workflow.entity.WorkflowProcess.procId";
    private final static String GENERATOR_TASK_ID =
        "com.amarsoft.amix.workflow.entity.WorkflowTask.taskId";
    private final static String GENERATOR_COMMENT_ID =
        "com.amarsoft.amix.workflow.entity.WorkflowComment.commentId";

    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private LiteFlowParse liteFlowParse;
    @Autowired
    SerialNumberGeneratorFinder serialNumberGeneratorFinder;
    @Autowired
    private WorkflowEntityService workflowEntityService;
    @Autowired
    private LiteHandlerResolve liteHandlerResolve;


    /**
     * 简式流程启动
     *
     * @param expr      简式流程表达式
     * @param variables 参数
     * @param userId
     * @return
     */

    @Transactional
    @Override
    public WorkflowProc start(String expr, String procDefKey, Map<String, Object> variables,
                              String userId) {
        ValidateKit.notBlank(expr, "简式流程启动传入的expr为空");
        ValidateKit.notBlank(procDefKey, "简式流程启动传入的procDefKey为空");
        String objectId = (String) variables.get("ObjectId");
        String objectType = (String) variables.get("ObjectType");
        String summary = (String) variables.get("Summary");
        ValidateKit.notBlank(objectId, "简式流程启动传入参数ObjectId为空");
        ValidateKit.notBlank(objectType, "简式流程启动传入参数ObjectType为空");
        ValidateKit.notBlank(summary, "简式流程启动传入参数Summary为空");

        logger.info("简式流程实例启动,KEY:" + expr + " 参数:" + variables.toString() + "启动用户:" + userId);

        WorkflowProc workflowProc = new WorkflowProc();
        String procId = this.getGeneratorId(GENERATOR_PROC_ID);

        workflowProc.setProcId(procId);
        workflowProc.setProcInstId(procId);
        workflowProc.setProcDefKey(procDefKey);
        workflowProc.setObjectType(objectType);
        workflowProc.setProcName(summary);
        workflowProc.setSummary(summary);
        workflowProc.setSponsor(userId);
        workflowProc.setFlowType("lite");
        workflowProc.setStartTime(DateKit.now());
        workflowProc.setCreatedBy(userId);
        workflowProc.setLiteFlowDef(expr);
        workflowProc.setStatus(ProcStatusEnum.PRESUBMIT.getName());
        beanCruder.insert(workflowProc);

        List<WorkflowTask> liteTaskList = this.createLiteTask(expr, null, procId);
        liteTaskList.forEach(workflowTask -> beanCruder.insert(workflowTask));
        workflowProc.setLatestTasks(liteTaskList);

        List<LiteHandler> procHandlers = liteHandlerResolve.findHandlers(procDefKey);
        for (LiteHandler liteHandler : procHandlers) {
            liteHandler.afterProcStart(workflowProc, liteTaskList);
        }
        return workflowProc;
    }

    /**
     * 任务提交
     *
     * @param taskId    当前任务Id
     * @param variables
     * @param userId
     * @param comment   意见文本
     * @return
     */
    @Transactional
    @Override
    public List<WorkflowTask> commit(String taskId, Map<String, Object> variables, String userId,
        String comment) {
        ValidateKit.notBlank(taskId, "传入的taskId为空");
        ValidateKit.notBlank(userId, "流程提交传入参数userId为空");
        ValidateKit.notBlank(comment, "流程提交传入参数comment为空");
        logger.info("任务:" + taskId + "提交" + ",操作用户:" + userId);

        List<WorkflowTask> liteTask = new ArrayList<>();
        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);
        if (null != workflowTask) {
            //任务可被多人处理时判断任务是否已被签收
            String assignee = workflowTask.getAssignee();
            String owner = workflowTask.getOwner();

            if (null != assignee || null != owner) {
                if (!userId.equals(assignee)) {
                    throw new WorkflowException("该任务:{0}已经被用户:{1}签收", taskId, owner);
                }
                if (!owner.equals(userId)) {
                    throw new WorkflowException("任务:{0}所有者为:{1},用户:{2}无法进行签收操作", taskId, owner,
                        userId);
                }
            }

            //更新proc 状态 以及 最新意见
            String procId = workflowTask.getProcId();
            List<WorkflowTask> workflowTaskList =
                workflowEntityService.getWorkflowTaskByProcId(procId);
            WorkflowProc workflowProc =
                beanCruder.selectOneById(WorkflowProc.class, procId);
            //首个任务
            if (workflowTaskList.size() == 1) {
                workflowProc.setStatus(ProcStatusEnum.APPROVING.getName());
                //设置首个意见
                workflowProc.setFirstComment(comment);
            } else {
                //设置最新意见
                workflowProc.setLastComment(comment);
            }

            //更新任务
            workflowTask = this.updateTask(taskId, userId);

            //在任务提交的同时 完成该任务的意见存储
            this.addComment(taskId, userId, comment, TaskStatusEnum.COMPLETED);
            //更新wkfl_proc 表中 lastComment 字段
            //如果处于最后阶段 不再创建任务

            liteTask = this.createLiteTask(null, taskId, workflowTask.getProcId());
            liteTask.forEach(task -> task.setCreatedBy(userId));
            if (liteTask.size() != 0) {
                beanCruder.insert(liteTask);
            }
            List<LiteHandler> procHandlers =
                liteHandlerResolve.findHandlers(workflowProc.getProcDefKey());
            for (LiteHandler liteHandler : procHandlers) {
                liteHandler.afterTaskCommit(workflowProc, workflowTask, liteTask);
            }
            //判断任务是否全部完成
            //正常状态下的任务
            workflowTaskList = workflowEntityService.getWorkflowTaskByProcId(procId);
            boolean isAllCompleted = workflowTaskList.stream()
                .allMatch(workflowTask1 -> TaskStatusEnum.COMPLETED.getName()
                    .equals(workflowTask1.getFinishType()));
            //有驳回操作的任务
            //判断任务 是否为最后一条&&状态 为completed;

            String expr = workflowProc.getLiteFlowDef();
            List<LiteTaskDef> liteTaskDefList = liteFlowParse.expressionParse(expr);
            int size = liteTaskDefList.size();
            if (size > 1) {
                String taskDefKey = liteTaskDefList.get(size - 1).getTaskDefKey();
                String curTaskDefKey = workflowTask.getTaskDefKey();
                isLastTask = curTaskDefKey.equals(taskDefKey) && TaskStatusEnum.COMPLETED
                    .getName().equals(workflowTask.getFinishType());
            }


            //任务全部完成
            if (isAllCompleted && workflowTaskList.size() > 1 || isLastTask) {
                workflowProc.setFinishTime(DateKit.now());
                workflowProc.setStatus(ProcStatusEnum.FINISHED.getName());
                workflowProc.setUpdatedBy(userId);
                //将该实例的所有相关意见复制到wkfl_hi_comment表中
                //并将wkfl_comment表中的数据删除
                workflowEntityService.copyCommentsHistory(procId);
                //任务操作同上
                workflowEntityService.copyTasks2History(procId);
                for (LiteHandler liteHandler : procHandlers) {
                    liteHandler
                        .afterProcFinished(ProcFinishEnum.COMPLETED, workflowProc,
                            workflowTask);
                }
            }
            workflowEntityService.updateWorkflowProc(workflowProc);
        }
        return liteTask;
    }

    /**
     * 提交时更新任务
     *
     * @param taskId
     * @param userId
     */

    private WorkflowTask updateTask(String taskId, String userId) {
        ValidateKit.notBlank(taskId, "任务签收传入参数taskId为空");
        ValidateKit.notBlank(userId, "任务签收传入参数userId为空");
        logger.info("任务:" + taskId + "签收用户:" + userId);
        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);
        if (null == workflowTask) {
            throw new WorkflowException("任务:{0}不存在或已被处理", taskId);
        }


        //更新任务状态
        workflowTask.setOwner(userId);
        workflowTask.setAssignee(userId);
        workflowTask.setUpdatedBy(userId);
        workflowTask.setFinishTime(DateKit.now());
        workflowTask.setFinishType(TaskStatusEnum.COMPLETED.getName());
        workflowEntityService.updateWorkflowTask(workflowTask);
        return workflowTask;
    }

    /**
     * 流程否决
     *
     * @param taskId
     * @param userId
     * @param comment
     */
    @Transactional
    @Override
    public void reject(String taskId, String userId, String comment) {
        ValidateKit.notBlank(taskId, "流程否决传入参数taskId为空");
        ValidateKit.notBlank(userId, "流程否决传入参数userId为空");
        ValidateKit.notBlank(comment, "流程否决传入参数comment为空");
        logger.trace("流程否决:" + taskId + "设置任务删除理由");


        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);
        if (null == workflowTask) {
            throw new WorkflowException("该taskId:{0}对应的任务不存在", taskId);
        }
        String procId = workflowTask.getProcId();
        if (null != procId) {
            workflowTask.setFinishType(TaskStatusEnum.REJECT.getName());
            workflowTask.setFinishTime(DateKit.now());
            workflowTask.setUpdatedBy(userId);
            workflowTask.setOwner(userId);
            workflowTask.setAssignee(userId);
            workflowEntityService.updateWorkflowTask(workflowTask);
            //该任务的意见文本设置为reject
            this.addComment(taskId, userId, comment, TaskStatusEnum.REJECT);

            //将该实例的所有相关意见复制到wkfl_hi_comment表中
            //并将wkfl_comment表中的数据删除
            workflowEntityService.copyCommentsHistory(procId);

            //任务操作同上
            workflowEntityService.copyTasks2History(procId);

            //更新流程实例状态
            WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procId);
            workflowProc.setUpdatedBy(userId);
            workflowProc.setLastComment(comment);
            workflowProc.setStatus(ProcStatusEnum.REJECT.getName());
            workflowProc.setFinishTime(DateKit.now());
            workflowEntityService.updateWorkflowProc(workflowProc);
            List<LiteHandler> procHandlers =
                liteHandlerResolve.findHandlers(workflowProc.getProcDefKey());
            for (LiteHandler liteHandler : procHandlers) {
                liteHandler.afterProcFinished(ProcFinishEnum.REJECT, workflowProc, workflowTask);
            }
        }
    }


    /**
     * 流程废弃
     *
     * @param taskId
     * @param userId
     * @param comment
     */
    @Transactional
    @Override
    public void abolish(String taskId, String userId, String comment) {
        ValidateKit.notBlank(taskId, "传入参数taskId为空");
        ValidateKit.notBlank(userId, "传入参数taskId为空");
        ValidateKit.notBlank(comment, "传入参数comment为空");

        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);
        String assigner = workflowTask.getAssignee();
        if (null != assigner && !userId.equals(assigner)) {
            throw new WorkflowException("该任务已被:{0}处理或签收", assigner);
        }
        String procId = workflowTask.getProcId();
        if (null != procId) {
            //更新任务
            workflowTask.setOwner(userId);
            workflowTask.setAssignee(userId);
            workflowTask.setUpdatedBy(userId);
            workflowTask.setFinishType(TaskStatusEnum.ABOLISHED.getName());
            workflowTask.setFinishTime(DateKit.now());
            workflowEntityService.updateWorkflowTask(workflowTask);

            //意见信息设置
            this.addComment(taskId, userId, comment, TaskStatusEnum.ABOLISHED);

            workflowEntityService.copyCommentsHistory(procId);
            workflowEntityService.copyTasks2History(procId);

            //更新流程实例
            WorkflowProc workflowProc =
                workflowEntityService.getWorkflowProc(workflowTask.getProcId());
            workflowProc.setLastComment(comment);
            workflowProc.setStatus(ProcStatusEnum.ABOLISHED.getName());
            workflowProc.setFinishTime(DateKit.now());
            workflowProc.setUpdatedTime(DateKit.now());
            workflowEntityService.updateWorkflowProc(workflowProc);
            List<LiteHandler> procHandlers =
                liteHandlerResolve.findHandlers(workflowProc.getProcDefKey());
            for (LiteHandler liteHandler : procHandlers) {
                liteHandler.afterProcFinished(ProcFinishEnum.ABOLISHED, workflowProc, workflowTask);
            }
        }
    }



    /**
     * 任务驳回操作
     *
     * @param srcTaskId  当前任务Id
     * @param dscTaskKey 目标任务定义key
     * @param userId
     * @param comment
     * @return
     */
    @Transactional
    @Override public WorkflowTask backOrigin(String srcTaskId, String dscTaskKey, String userId,
        String comment) {
        ValidateKit.notBlank(srcTaskId, "传入的参数srcTaskId为空");
        ValidateKit.notBlank(dscTaskKey, "传入的参数dscTaskKey为空");
        ValidateKit.notBlank(userId, "传入的参数userId为空");
        ValidateKit.notBlank(comment, "传入的参数comment为空");

        //对当前任务进行处理
        //1:判断任务是否已经被处理
        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(srcTaskId);
        if (null == workflowTask) {
            WorkflowTaskHistory taskHistory =
                workflowEntityService.getWorkflowTaskHistory(srcTaskId);
            if (null == taskHistory) {
                throw new WorkflowException("该taskId:{0}对应的任务不存在", srcTaskId);
            } else {
                throw new WorkflowException("该任务已被{0}处理,无法驳回", taskHistory.getAssignee());
            }
        }

        String assignee = workflowTask.getAssignee();
        if (null != assignee && !userId.equals(assignee)) {
            throw new WorkflowException("该任务已被{0}处理,无法驳回", assignee);
        }

        //若任务处于并行状态 则不允许退回操作
        String taskDefKey = workflowTask.getTaskDefKey();
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(workflowTask.getProcId());
        String expr = workflowProc.getLiteFlowDef();
        List<LiteTaskDef> liteTaskDefList = liteFlowParse.expressionParse(expr);
        for (LiteTaskDef liteTaskDef : liteTaskDefList) {
            if (liteTaskDef.getTaskDefKey().equals(taskDefKey)) {
                if (liteTaskDef.isParallel()) {
                    throw new WorkflowException("该任务taskId:{0}处于并行阶段,无法执行驳回操作",
                        workflowTask.getTaskId());
                }
            }
        }

        //任务状态更新0
        workflowTask.setAssignee(userId);
        workflowTask.setOwner(userId);
        workflowTask.setFinishType(TaskStatusEnum.BACKORIGIN.getName());
        workflowTask.setFinishTime(DateKit.now());
        workflowTask.setUpdatedBy(userId);
        workflowEntityService.updateWorkflowTask(workflowTask);
        //该任务的意见文本设置为backOrgin
        this.addComment(srcTaskId, userId, comment, TaskStatusEnum.BACKORIGIN);


        //实例状态更新
        workflowProc.setUpdatedBy(userId);
        workflowProc.setStatus(ProcStatusEnum.BACKORIGIN.getName());
        workflowProc.setFinishTime(DateKit.now());
        workflowEntityService.updateWorkflowProc(workflowProc);


        boolean isTaskDefKey = liteTaskDefList.stream()
            .anyMatch(liteTaskDef -> liteTaskDef.getTaskDefKey().equals(dscTaskKey));
        if (!isTaskDefKey) {
            throw new WorkflowException("任务定义key:{0}不存在", dscTaskKey);
        }


        //退回到T1阶段

        //T1 只有提交才会产生新的任务
        //所以说退回到T1阶段 实际上就是流程的重新发起
        if (dscTaskKey.equals(liteTaskDefList.get(0).getTaskDefKey())) {
            List<WorkflowTask> liteTaskList =
                this.createLiteTask(expr, null, workflowProc.getProcId());
            liteTaskList.forEach(task -> beanCruder.insert(task));
            workflowProc.setLatestTasks(liteTaskList);

            List<LiteHandler> procHandlers =
                liteHandlerResolve.findHandlers(workflowProc.getProcDefKey());
            procHandlers
                .forEach(liteHandler -> liteHandler.afterProcStart(workflowProc, liteTaskList));
        } else {
            List<WorkflowTask> workflowTaskList = new ArrayList<>();
            liteTaskDefList = liteFlowParse.expressionParse(workflowProc.getLiteFlowDef());
            for (int i = 0; i < liteTaskDefList.size(); i++) {
                if (dscTaskKey.equals(liteTaskDefList.get(i).getTaskDefKey())) {
                    workflowTaskList =
                        this.createNextStageTask(workflowProc.getProcId(), workflowTaskList,
                            liteTaskDefList, i - 1);
                }
            }
            workflowTaskList.forEach(task -> task.setCreatedBy(userId));
            if (workflowTaskList.size() != 0) {
                beanCruder.insert(workflowTaskList);
            }
        }
        List<LiteHandler> procHandlers =
            liteHandlerResolve.findHandlers(workflowProc.getProcDefKey());
        procHandlers
            .forEach(liteHandler -> liteHandler.afterTaskJump(TaskJumpEnum.BACKTRACK, workflowProc,
                workflowTask));
        return workflowTask;
    }

    /**
     * 任务生成
     *
     * @param expr
     * @param srcTaskId 当前任务Id
     * @param procId    流程实例Id
     * @return
     */

    private List<WorkflowTask> createLiteTask(String expr, String srcTaskId,
        String procId) {
        List<WorkflowTask> workflowTaskList = new ArrayList<>();

        WorkflowTask workflowTask;
        List<LiteTaskDef> liteTaskDefList;
        ValidateKit.notBlank(procId, "传入的参数procId为空");
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procId);
        if (null == workflowProc) {
            throw new WorkflowException("该id:{0}对应流程实例不存在", procId);
        }

        //创建第一个任务
        if (null != expr) {
            liteTaskDefList = liteFlowParse.expressionParse(expr);
            LiteTaskDef liteTaskDef = liteTaskDefList.get(0);
            List<String> candidates = liteTaskDef.getCandidates();
            workflowTask = new WorkflowTask();
            for (String userName : candidates) {
                workflowTask.setOwner(userName);
                workflowTask.setAssignee(userName);
                workflowTask.setCreatedBy(userName);
            }
            workflowTask.setTaskDefKey(liteTaskDef.getTaskDefKey());
            workflowTask.setTaskName(liteTaskDef.getTaskName());
            workflowTask.setProcId(procId);
            String taskId = this.getGeneratorId(GENERATOR_TASK_ID);
            workflowTask.setTaskId(taskId);
            workflowTask.setTaskInstId(taskId);
            workflowTask.setArrivalTime(DateKit.now());
            workflowTaskList.add(workflowTask);

        } else {
            //根据传入参数expr的有无 以及上个任务状态的变更
            //推动新任务的创建

            WorkflowTask currentTask = workflowEntityService.getWorkflowTask(srcTaskId);
            liteTaskDefList = liteFlowParse.expressionParse(workflowProc.getLiteFlowDef());
            String taskDefKey = currentTask.getTaskDefKey();

            for (int i = 0; i < liteTaskDefList.size(); i++) {
                if (taskDefKey.equals(liteTaskDefList.get(i).getTaskDefKey())) {
                    //查找出当前所有的任务
                    List<WorkflowTask> workflowTasks =
                        beanCruder.selectList(WorkflowTask.class,
                            "select * from " + WKFL_TASK_TABLE
                                + " where PROC_ID =:procId and TASK_DEF_KEY=:taskDefKey",
                            MapKit.mapOf("procId", procId, "taskDefKey", taskDefKey));
                    boolean isAllCompleted = workflowTasks.stream().allMatch(
                        workflowTask1 -> TaskStatusEnum.COMPLETED.getName()
                            .equals(workflowTask1.getFinishType()));
                    if (Boolean.TRUE.equals(isAllCompleted) || (workflowTasks.size() == 1
                        && TaskStatusEnum.COMPLETED.getName()
                        .equals(currentTask.getFinishType()))) {
                        //create new task;
                        workflowTaskList =
                            this.createNextStageTask(procId, workflowTaskList, liteTaskDefList, i);
                    }
                }
            }
        }
        return workflowTaskList;
    }

    private List<WorkflowTask> createNextStageTask(String procId,
        List<WorkflowTask> workflowTaskList,
        List<LiteTaskDef> liteTaskDefList, int i) {
        WorkflowTask workflowTask;
        WorkflowCandidate workflowCandidate;
        //下一阶段是否并行
        //若并行 创建多个任务
        if (liteTaskDefList.size() > (i + 1)) {
            LiteTaskDef nextLiteTaskDef = liteTaskDefList.get(i + 1);
            List<String> candidates =
                nextLiteTaskDef.getCandidates();
            if (nextLiteTaskDef.isParallel()) {
                //解析表达式
                for (String candidate : candidates) {
                    String taskId = this.getGeneratorId(GENERATOR_TASK_ID);
                    if (candidate.contains("r:")) {
                        workflowTask = new WorkflowTask();
                        //向 wkfl_candidate 表中插入数据
                        workflowCandidate = new WorkflowCandidate();
                        String[] split = candidate.split("r:");
                        if (split.length < EXPR_LENGTH) {
                            throw new WorkflowException("该部分表达式:{0}格式错误",
                                candidate);
                        }
                        workflowCandidate.setScopeRoleId(split[1]);
                        workflowCandidate.setTaskId(taskId);
                        workflowCandidate.setCandidateType("candidate");
                        beanCruder.insert(workflowCandidate);
                    } else {
                        workflowTask = new WorkflowTask();
                        workflowTask.setAssignee(candidate);
                        workflowTask.setOwner(candidate);
                    }
                    workflowTask.setTaskId(taskId);
                    workflowTask.setTaskInstId(taskId);
                    workflowTask.setTaskName(nextLiteTaskDef.getTaskName());
                    workflowTask.setTaskDefKey(nextLiteTaskDef.getTaskDefKey());
                    workflowTask.setProcId(procId);
                    workflowTask.setArrivalTime(DateKit.now());
                    workflowTaskList.add(workflowTask);
                }
            } else {
                //非并行
                //生成一条新的任务
                String taskId = this.getGeneratorId(GENERATOR_TASK_ID);
                workflowTask = new WorkflowTask();
                if (candidates.size() == 1) {
                    workflowTask.setAssignee(candidates.get(0));
                    workflowTask.setOwner(candidates.get(0));
                } else if (candidates.size() > 1) {
                    //向 wkfl_candidate 表中插入多条数据
                    for (String candidate : candidates) {
                        if (candidate.contains("r:")) {
                            workflowCandidate = new WorkflowCandidate();
                            String[] split = candidate.split("r:");
                            if (split.length < EXPR_LENGTH) {
                                throw new WorkflowException("该部分表达式:{0}格式错误",
                                    candidate);
                            }
                            workflowCandidate.setScopeRoleId(split[1]);
                        } else {
                            workflowCandidate = new WorkflowCandidate();
                            workflowCandidate.setUserId(candidate);
                        }
                        workflowCandidate.setTaskId(taskId);
                        workflowCandidate.setCandidateType("candidate");
                        beanCruder.insert(workflowCandidate);
                    }
                }
                workflowTask.setTaskId(taskId);
                workflowTask.setTaskInstId(taskId);
                workflowTask.setTaskName(nextLiteTaskDef.getTaskName());
                workflowTask.setTaskDefKey(nextLiteTaskDef.getTaskDefKey());
                workflowTask.setProcId(procId);
                workflowTask.setArrivalTime(DateKit.now());
                workflowTaskList.add(workflowTask);
            }

        }
        return workflowTaskList;
    }

    /**
     * 添加意见
     * comment
     *
     * @param taskId
     * @param userId
     * @param comment
     */
    @Override
    public void addComment(String taskId, String userId, String comment,
        TaskStatusEnum statusEnum) {

        //当前任务  添加意见
        WorkflowComment workflowComment = new WorkflowComment();
        String commentId = this.getGeneratorId(GENERATOR_COMMENT_ID);
        workflowComment.setCommentId(commentId);
        workflowComment.setCommentInstId(commentId);
        workflowComment.setTaskId(taskId);
        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);
        if (null != workflowTask) {
            workflowComment.setProcId(workflowTask.getProcId());
        }
        workflowComment.setMessage(comment);
        workflowComment.setUserId(userId);
        workflowComment.setCommentType(statusEnum.getName());
        workflowComment.setCreatedBy(userId);
        workflowComment.setCreatedTime(DateKit.now());
        beanCruder.insert(workflowComment);
    }


    //获取流程首个意见
    @Override
    public WorkflowComment firstComment(String taskId) {
        ValidateKit.notBlank(taskId, "传入的参数taskId为空");
        WorkflowTask workflowTask = workflowEntityService.getWorkflowTask(taskId);
        String procId = workflowTask.getProcId();
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procId);
        WorkflowComment workflowComment = null;
        if (null != workflowProc) {
            if (null != workflowProc.getFinishTime()) {
                workflowComment = beanCruder
                    .selectOne(WorkflowComment.class,
                        "SELECT  * from " + WKFL_COMMENT_HIST_TABLE + " WHERE PROC_ID =:procId"
                            + "ORDER BY CREATED_TIME LIMIT 1;", MapKit.mapOf("procId", procId));
            } else {
                workflowComment = beanCruder.selectOne(WorkflowComment.class,
                    "SELECT  * from " + WKFL_COMMENT_TABLE + " WHERE PROC_ID =:procId"
                        + "ORDER BY CREATED_TIME LIMIT 1;", MapKit.mapOf("procId", procId));
            }
        }
        return workflowComment;
    }

    /**
     * 通过generator生成规则
     * 获取Id
     *
     * @return
     */
    private String getGeneratorId(String str) {
        SerialNumberGenerator<String> generatorId = serialNumberGeneratorFinder.find(str);
        return generatorId.next(str);
    }




    /**
     * 获取资源列表
     *
     * @param procDefKey
     * @return
     */
    @Transactional
    @Override
    public List<LiteflowResource> getResourceList(String procDefKey) {
        ValidateKit.notBlank(procDefKey, "传入的参数proDefKey为空");
        List<LiteflowResource> resourceList =
            beanCruder.selectList(LiteflowResource.class,
                "select RESOURCE_ID,PROC_DEF_KEY,SORT_CODE,TYPE_ as type,NAME,ACTION,ACTION_HINT,"
                    + "ICON_ as icon,STYLE_ as style,IS_EXPANDED,REVISION from "
                    + WKFL_LITE_RESOURCE_TABLE + " where PROC_DEF_KEY=:procDefKey",
                MapKit.mapOf("procDefKey", procDefKey));
        if (null == resourceList || resourceList.isEmpty()) {
            throw new WorkflowException("该key:{0}查询的流程资源不存在", procDefKey);
        }
        return resourceList;
    }



    /**
     * 获取流程审批表单
     *
     * @return
     */
    @Override
    public List<LiteflowForm> getLiteForm() {
        List<LiteflowForm> formList = beanCruder
            .selectList(LiteflowForm.class, "select * from " + WKFL_LITE_FORM_TABLE);
        if (null == formList || formList.isEmpty()) {
            throw new WorkflowException("审批表单列表为空");
        }
        return formList;
    }

    /**
     * 获取表达式预设模板
     *
     * @param category
     * @return
     */
    @Override
    public List<LiteflowTemplate> getTemplate(String category) {
        List<LiteflowTemplate> templateList;
        if (null == category || "".equals(category)) {
            templateList = beanCruder
                .selectList(LiteflowTemplate.class, "select * from " + WKFL_LITE_TEMPLATE_TABLE);
        } else {
            templateList = beanCruder.selectList(LiteflowTemplate.class,
                "select * from " + WKFL_LITE_TEMPLATE_TABLE + " where CATEGORY=:category",
                MapKit.mapOf("category", category));
        }
        if (null == templateList || templateList.size() == 0) {
            throw new WorkflowException("预设模板不存在");
        }
        return templateList;
    }



    @Transactional
    @Override
    public void saveTemplate(String name, String expr) {
        ValidateKit.notBlank(name, "模板名称为空");
        String expression = liteFlowParse.getExpression(expr);
        LiteflowTemplate liteflowTemplate = new LiteflowTemplate();
        liteflowTemplate.setName(name);
        liteflowTemplate.setDefExplicit(expression);
        liteflowTemplate.setDefImplicit(expr);
        beanCruder.insert(liteflowTemplate);
    }


    @Transactional
    @Override
    public void updateTemplate(String templateId, String expr) {
        ValidateKit.notBlank(templateId, "传入的预设模板Id为空");
        LiteflowTemplate liteflowTemplate = beanCruder.selectOne(LiteflowTemplate.class,
            "select * from " + WKFL_LITE_TEMPLATE_TABLE + " where TEMPLATE_ID =:templateId",
            MapKit.mapOf("templateId", templateId));
        if (null==liteflowTemplate){
            throw new WorkflowException("该id:{0}对应的预设模板不存在",templateId);
        }
        String expression = liteFlowParse.getExpression(expr);
        liteflowTemplate.setDefExplicit(expression);
        liteflowTemplate.setDefImplicit(expr);
        beanCruder.update(liteflowTemplate);
    }
}
