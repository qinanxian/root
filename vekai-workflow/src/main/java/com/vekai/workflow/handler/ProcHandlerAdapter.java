package com.vekai.workflow.handler;

import com.vekai.workflow.entity.WorkflowCandidate;
import com.vekai.workflow.nopub.bom.BomContext;
import com.vekai.workflow.entity.WorkflowComment;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.model.enums.CommentActionEnum;
import com.vekai.workflow.model.enums.ProcFinishEnum;
import com.vekai.workflow.model.enums.TaskAuthChangeEnum;
import com.vekai.workflow.model.enums.TaskJumpEnum;

import java.util.List;

/**
 * 流程处理器默认适配器，解决只需要处理其中一个方法的情况
 */
public abstract class ProcHandlerAdapter implements ProcHandler {

    public void beforeTaskCommit(WorkflowProc proc, WorkflowTask srcTask) {

    }

    public void afterTaskCommit(WorkflowProc proc, WorkflowTask srcTask, List<WorkflowTask> dscTasks) {

    }

    public void beforeTaskJump(TaskJumpEnum jumpType, WorkflowProc proc, WorkflowTask srcTask) {

    }

    public void afterTaskJump(TaskJumpEnum jumpType, WorkflowProc proc, WorkflowTask srcTask, WorkflowTask dscTask) {

    }

    public void afterProcStart(WorkflowProc proc, List<WorkflowTask> tasks) {

    }

    public void afterProcFinished(ProcFinishEnum finishType, WorkflowProc proc, WorkflowTask task) {

    }

    public void afterCommentAction(CommentActionEnum commentActionEnum, WorkflowComment workflowComment) {

    }

    public void afterTaskAuthChange(TaskAuthChangeEnum taskAuthChangeEnum, WorkflowTask workflowTask, WorkflowCandidate workflowCandidate) {

    }

    public BomContext fillWorkflowParam(WorkflowProc proc, WorkflowTask task){
        return null;
    }
}
