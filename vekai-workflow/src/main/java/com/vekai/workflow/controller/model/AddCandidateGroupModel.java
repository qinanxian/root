package com.vekai.workflow.controller.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apachechen on 2018/3/21.
 */
public class AddCandidateGroupModel implements Serializable{

    private List<String> roleIds;
    private String taskId;

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
