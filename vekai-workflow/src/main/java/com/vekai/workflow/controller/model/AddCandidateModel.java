package com.vekai.workflow.controller.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apachechen on 2018/3/21.
 */
public class AddCandidateModel implements Serializable {

    private List<String> userIds;
    private String taskId;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
