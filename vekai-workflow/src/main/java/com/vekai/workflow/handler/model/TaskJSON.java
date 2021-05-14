package com.vekai.workflow.handler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.activiti.engine.task.Task;

public class TaskJSON {
    @JsonIgnore
    private Task task;

    public TaskJSON(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public String getId() {
        return task.getId();
    }

    public String getName() {
        return task.getName();
    }

    public String getAssignee() {
        return task.getAssignee();
    }

}
