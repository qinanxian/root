package com.vekai.workflow.service;

import com.vekai.workflow.model.HistoricTask;
import com.vekai.workflow.model.ProcComment;
import com.vekai.workflow.model.ProcTask;
import com.vekai.workflow.model.enums.TaskStatusEnum;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 流程实例进程的处理
 */
public interface WorkflowTaskService {
    /**
     * 任务提交
     * @param taskId
     * @param variables
     * @param userId
     * @return
     */
    List<ProcTask> commit(String taskId, Map<String, Object> variables, String userId, String comment);

    /**
     * 任务提交指定受理人
     * @param taskId
     * @param userId
     */
    List<ProcTask> commitAssignee(String taskId, Map<String, Object> variables, String userId, String comment, String assignee);

    /**
     * 流程作废
     * @param taskId
     * @param userId
     */
    void abolish(String taskId, String userId, String comment);

    /**
     * 否决
     * @param taskId
     * @param userId
     */
    void reject(String taskId, String userId, String comment);

    /**
     * 退回
     * @param srcTaskId 当前任务ID
     * @param dscTaskKey 目标任务定义KEY
     * @param userId 操作人
     * @param comment 意见
     * @return 目标任务对象
     */
    ProcTask backFront(String srcTaskId, String dscTaskKey, String userId, String comment);

    /**
     * 退回指定人
     * @param srcTaskId
     * @param dscTaskKey
     * @param userId
     * @param comment
     * @param assignee
     * @return
     */
    ProcTask backFrontAssignee(String srcTaskId, String dscTaskKey, String userId, String comment, String assignee);

    /**
     * 退回
     * @param taskId 当前任务ID
     * @param userId 操作人
     * @param comment 意见
     * @return 目标任务对象
     */
    ProcTask backOrigin(String taskId, String userId, String comment);

    /**
     * 退回修改
     * @param srcTaskId 当前任务ID
     * @param dscTaskKey 目标任务定义KEY
     * @param userId 操作人
     * @param comment 意见
     * @return 目标任务对象
     */
    ProcTask backTrack(String srcTaskId, String dscTaskKey, String userId, String comment);

    /**
     * 退回修改指定受理人
     * @param srcTaskId 当前任务ID
     * @param dscTaskKey 目标任务定义KEY
     * @param userId 操作人
     * @param comment 意见
     * @return 目标任务对象
     */
    ProcTask backTrackAssignee(String srcTaskId, String dscTaskKey, String userId, String comment, String assignee);

    /**
     * 流程退回上一节点（流程自然顺序）
     * @param taskId
     * @return
     */
    ProcTask backToPrev(String taskId, String userId, String comment);

    /**
     * 流程往后跑到任意节点
     * @param srcTaskId
     * @param dscTaskKey
     * @param taskStatusEnum
     * @return
     */
    ProcTask jumpBack(String srcTaskId, String dscTaskKey, String userId, TaskStatusEnum taskStatusEnum, String comment);

    /**
     * 流程往前跳到任意节点
     * @param srcTaskId
     * @param dscTaskKey
     * @param taskStatusEnum
     * @return
     */
    ProcTask jumpForward(String srcTaskId, String dscTaskKey, String userId, TaskStatusEnum taskStatusEnum, String comment);
    ProcTask jumpYForwardTask(String srcTaskId,String dscTaskKey,String userId, TaskStatusEnum taskStatusEnum, String comment);



    /**
     * 任务撤回
     * 将提交的任务,并且对方未处理(未签收)的任务退回到上一节点
     * @param taskId
     * @param userId
     * @param comment
     * @return
     */
    ProcTask retrieve(String taskId, String userId,String comment);
    /**
     * 任务撤回条件判定
     * 将提交的任务,并且对方未处理(未签收)的任务退回到上一节点
     * @param taskId
     * @return
     */
    Map<String, String> retrieveCondition(String taskId);

    List<HistoricActivityInstance> getAllowBackActivitys(String procInstId);

    /**
     * 签收任务
     * @param taskId
     * @param userId
     */
    void claimTask(String taskId, String userId);

    /**
     * 取消签收任务
     * @param taskId
     */
    void unClaimTask(String taskId);

    /**
     * 获取任务的受理人
     * @param taskId
     * @return
     */
    String getAssignee(String taskId);

    /**
     * 更改流程任务受理人(只有一个)
     * @param taskId
     * @param userId
     */
    void changeAssignee(String taskId, String userId);

    /**
     * 给任务添加候选人
     * @param taskId
     * @param userId
     */
    void addCandidateUser(String taskId, String userId);

    /**
     * 删除任务的候选人
     * @param taskId
     * @param userId
     */
    void deleteCandidateUser(String taskId, String userId);
    void addCandidateGroup(String taskId, String groupId);
    void deleteCandidateGroup(String taskId, String groupId);

    List<String> getCandidateGroups(String taskId);
    List<String> getCandidateUsers(String taskId, boolean includeCandidateGroup);
    List<String> getCandidateGroupUsers(String taskId);

    /**
     * 获取任务实例
     * @param taskId
     * @return
     */
    ProcTask getTask(String taskId);

    /**
     * 判断是否是发起岗位任务
     * @param taskId
     * @return
     */
    boolean isFirstTask(String taskId);

    /**
     * 获取当前存活任务(多个)
     * @param procInstId
     * @return
     */
    List<ProcTask> getLatestTasks(String procInstId);

    /**
     * 获取当前存活任务(单个)
     * @param procInstId
     * @return
     */
    ProcTask getLatestTask(String procInstId);

    /**
     * 获取任务的前一个任务对象
     * @param taskId
     * @return
     */
    HistoricTask getForeHistTask(String taskId);
    /**
     * 判断流程是否处于并行阶段
     * @param taskId
     * @return
     */
    boolean taskInParallelBranch(String taskId);

    void addComment(String taskId, String userId, TaskStatusEnum taskStatusEnum, String comment);
    void deleteComment(String commentId);
    void updateComment(String commentId, String comment);
    List<ProcComment> getTaskComments(String taskId);

    /**
     * 获取历史任务实例
     * @param taskId
     * @return
     */
    HistoricTask getHistTask(String taskId);

    /**
     * 根据流程实例ID获取历史任务实例
     * @param procInstId 流程实例Id
     * @param isFinished 是否是已完成任务
     * @return
     */
    List<HistoricTask> getHistTasks(String procInstId, boolean isFinished);

    /**
     * 获取发起岗位任务
     * @param procInstId
     * @return
     */
    HistoricTask getFirstHistTask(String procInstId);

    /**
     * 获取指定任务的任务定义
     * @param taskId
     * @return
     */
    TaskDefinition getTaskDefinition(String taskId);

    /**
     * 通过流程实例号任务定义KEY查询任务定义对象
     * @param procInstId
     * @param taskDefKey
     * @return
     */
    TaskDefinition getTaskDefinition(String procInstId, String taskDefKey);

    /**
     * 获取任务表单定义属性
     * @param taskId
     * @return
     */
    List<FormProperty> getTaskFormProperties(String taskId);

    /**
     * 查询历史任务的表单定义属性
     * @param taskId
     * @return
     */
    List<FormProperty> getHistTaskFormProperties(String taskId);

    /**
    * 判断流程是否结束
    * @param procInstId
    */
    boolean isProcInstFinished(String procInstId);

    /**
     * 获取未完成的流程历史任务节点（去重并排序）
     * @param procInstId
     * @return
     */
    List<HistoricTask> getHistTasks(String procInstId);


    /**
     * 转他人处理
     * @param taskId
     * @param userId
     * @param comment
     */
    void deliverTo(String taskId, String userId, String loginUserId, String comment);

    /**
     * 征求他人意见
     * @param userIds
     * @param taskId
     * @param comment
     * @param srcUserId
     */
    void takeAdvice(List<String> userIds,String taskId,String comment,String srcUserId);

    /**
     * 判断任务是否处于征求他人意见状态中
     * @param taskId
     * @return
     */
    boolean feedbackStatus(String taskId);

    /**
     * 征求意见-反馈意见
     * @param taskId
     * @param comment
     * @param userId
     */
    void feedback(String taskId,String comment,String userId);

    /**
     * 查询任务出线
     * @param taskId
     * @return
     */
    ArrayList<ArrayList<PvmActivity>> getActivityLines(String taskId);

    /**
     * 获取任务接下来的任务集合
     * @param taskId
     * @return
     */
    Set<PvmActivity> getNextUserActivities(String taskId);


    List<PvmActivity> getNextActivities(String taskId);

    /**
     * 获取任务之前的任务集合
     * @param taskId
     * @return
     */
    Set<PvmActivity> getForeUserActivities(String taskId);

    List<ActivityImpl> getAllForwardActivities(String procInstId);
}
