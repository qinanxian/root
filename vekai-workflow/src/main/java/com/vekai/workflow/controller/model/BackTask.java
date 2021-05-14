package com.vekai.workflow.controller.model;

import java.io.Serializable;

/**
 * Created by apachechen on 2018/3/13.
 */
public class BackTask implements Serializable{

    private String taskId;
    private String dscTaskKey;
    private String comment;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDscTaskKey() {
        return dscTaskKey;
    }

    public void setDscTaskKey(String dscTaskKey) {
        this.dscTaskKey = dscTaskKey;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
