package com.vekai.workflow.model;

import org.activiti.engine.task.Comment;

import java.io.Serializable;
import java.util.Date;


/**
 * 工作流实例进程的意见
 */
public class ProcComment implements Comment,Serializable {
    private Comment comment;

    public ProcComment() {
    }
    public ProcComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    public String getId() {
        return comment.getId();
    }

    @Override
    public String getUserId() {
        return comment.getUserId();
    }

    @Override
    public Date getTime() {
        return comment.getTime();
    }

    @Override
    public String getTaskId() {
        return comment.getTaskId();
    }

    @Override
    public String getProcessInstanceId() {
        return comment.getProcessInstanceId();
    }

    @Override
    public String getType() {
        return comment.getType();
    }

    @Override
    public String getFullMessage() {
        return comment.getFullMessage();
    }
}
