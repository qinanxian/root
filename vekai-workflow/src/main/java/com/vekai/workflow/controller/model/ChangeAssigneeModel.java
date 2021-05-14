package com.vekai.workflow.controller.model;

import java.io.Serializable;

/**
 * Created by apachechen on 2018/3/21.
 */
public class ChangeAssigneeModel implements Serializable{

    private String userId;
    private String taskId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
