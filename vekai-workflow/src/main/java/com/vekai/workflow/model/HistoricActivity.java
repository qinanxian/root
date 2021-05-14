package com.vekai.workflow.model;

import org.activiti.engine.history.HistoricActivityInstance;

import java.io.Serializable;
import java.util.Date;

public class HistoricActivity implements HistoricActivityInstance, Serializable {
    private HistoricActivityInstance historicActivityInstance;

    private String assignee;
    private Date endTime;


    public HistoricActivity(HistoricActivityInstance historicActivityInstance) {
        this.historicActivityInstance=historicActivityInstance;

        this.assignee = historicActivityInstance.getAssignee();
        this.endTime = historicActivityInstance.getEndTime();
    }

    @Override
    public String getId() {
        return historicActivityInstance.getId();
    }

    @Override
    public String getActivityId() {
        return historicActivityInstance.getActivityId();
    }

    @Override
    public String getActivityName() {
        return historicActivityInstance.getActivityName();
    }

    @Override
    public String getActivityType() {
        return historicActivityInstance.getActivityType();
    }

    @Override
    public String getProcessDefinitionId() {
        return historicActivityInstance.getProcessDefinitionId();
    }

    @Override
    public String getProcessInstanceId() {
        return historicActivityInstance.getProcessInstanceId();
    }

    @Override
    public String getExecutionId() {
        return historicActivityInstance.getExecutionId();
    }

    @Override
    public String getTaskId() {
        return historicActivityInstance.getTaskId();
    }

    @Override
    public String getCalledProcessInstanceId() {
        return historicActivityInstance.getCalledProcessInstanceId();
    }

    @Override
    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee){
        this.assignee=assignee;
    }

    @Override
    public Date getStartTime() {
        return historicActivityInstance.getStartTime();
    }

    @Override
    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime=endTime;
    }

    @Override
    public Long getDurationInMillis() {
        return historicActivityInstance.getDurationInMillis();
    }

    @Override
    public String getTenantId() {
        return historicActivityInstance.getTenantId();
    }

    @Override
    public Date getTime() {
        return historicActivityInstance.getTime();
    }
}
