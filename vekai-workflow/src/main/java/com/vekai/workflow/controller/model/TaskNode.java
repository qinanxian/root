package com.vekai.workflow.controller.model;

import java.util.List;
import java.util.Map;



/**
 * @Author: qyyao
 * @Description:
 * @Date: Created in 16:48 18/05/2018
 */

public class TaskNode {
    private String procId;
    private Map<String,List<TaskComment>> taskStage;

    public String getProcId() {
        return procId;
    }
    public void setProcId(String procId) {
        this.procId = procId;
    }


    public Map<String, List<TaskComment>> getTaskStage() {
        return taskStage;
    }

    public void setTaskStage(
        Map<String, List<TaskComment>> taskStage) {
        this.taskStage = taskStage;
    }

    @Override
    public String toString() {
        return "TaskNode{" +
            "procId='" + procId + '\'' +
            ", taskStage=" + taskStage +
            '}';
    }
}
