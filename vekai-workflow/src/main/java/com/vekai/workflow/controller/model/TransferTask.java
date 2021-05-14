package com.vekai.workflow.controller.model;

import java.io.Serializable;

/**
 * Created by apachechen on 2018/3/14.
 */
public class TransferTask implements Serializable {

    private String taskId;
    private String userId;
    private String comment;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
