package com.vekai.workflow.handler;

import com.vekai.workflow.nopub.bom.BomContext;
import com.vekai.workflow.entity.WorkflowCandidate;
import com.vekai.workflow.entity.WorkflowComment;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.model.enums.CommentActionEnum;
import com.vekai.workflow.model.enums.ProcFinishEnum;
import com.vekai.workflow.model.enums.TaskAuthChangeEnum;
import com.vekai.workflow.model.enums.TaskJumpEnum;

import java.util.List;

/**
 * 流程绑定的业务处理类
 */
public interface ProcHandler {

    /**
     * 正常提交之前
     * @param proc
     * @param srcTask
     */
    void beforeTaskCommit(WorkflowProc proc,WorkflowTask srcTask);

    /**
     * 正常提交之后
     * @param proc
     * @param srcTask
     * @param dscTasks
     */
    void afterTaskCommit(WorkflowProc proc,WorkflowTask srcTask,List<WorkflowTask> dscTasks);

    /**
     * 跳转之前
     * @param jumpType
     * @param proc
     * @param srcTask
     */
    void beforeTaskJump(TaskJumpEnum jumpType,WorkflowProc proc,WorkflowTask srcTask);

    /**
     * 跳转之后
     * @param jumpType
     * @param proc
     * @param srcTask
     * @param dscTask
     */
    void afterTaskJump(TaskJumpEnum jumpType,WorkflowProc proc,WorkflowTask srcTask,WorkflowTask dscTask);

    /**
     * 流程启动之后
     */
    void afterProcStart(WorkflowProc proc,List<WorkflowTask> tasks);

    /**
     * 流程完成之后
     * @param finishType 完成类型
     */
    void afterProcFinished(ProcFinishEnum finishType,WorkflowProc proc,WorkflowTask task);

    /**
     * 流程意见操作
     * @param commentActionEnum 操作动作
     * @param workflowComment
     */
    void afterCommentAction(CommentActionEnum commentActionEnum,WorkflowComment workflowComment);

    /**
     * 任务权限变更
     * @param taskAuthChangeEnum 变更类型
     * @param workflowCandidate
     */
    void afterTaskAuthChange(TaskAuthChangeEnum taskAuthChangeEnum, WorkflowTask workflowTask, WorkflowCandidate workflowCandidate);

    /**
     * 流程业务参数填充
     * @param proc
     * @param task
     * @return
     */
    BomContext fillWorkflowParam(WorkflowProc proc, WorkflowTask task);
}
