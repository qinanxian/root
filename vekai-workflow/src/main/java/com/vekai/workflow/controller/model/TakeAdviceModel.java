package com.vekai.workflow.controller.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apachechen on 2018/3/16.
 */
public class TakeAdviceModel implements Serializable {

    private List<String> userIds;
    private String taskId;
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
