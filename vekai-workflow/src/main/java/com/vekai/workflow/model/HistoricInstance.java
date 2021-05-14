package com.vekai.workflow.model;

import org.activiti.engine.history.HistoricProcessInstance;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class HistoricInstance implements HistoricProcessInstance,Serializable {
    private HistoricProcessInstance historicProcessInstance;
    private String startUserName;

    public HistoricInstance() {
    }

    public HistoricInstance(HistoricProcessInstance historicProcessInstance) {
        this.historicProcessInstance = historicProcessInstance;
    }

    @Override
    public String getId() {
        return historicProcessInstance.getId();
    }

    @Override
    public String getBusinessKey() {
        return historicProcessInstance.getBusinessKey();
    }

    @Override
    public String getProcessDefinitionId() {
        return historicProcessInstance.getProcessDefinitionId();
    }

    @Override
    public String getProcessDefinitionName() {
        return historicProcessInstance.getProcessDefinitionName();
    }

    @Override
    public String getProcessDefinitionKey() {
        return historicProcessInstance.getProcessDefinitionKey();
    }

    @Override
    public Integer getProcessDefinitionVersion() {
        return historicProcessInstance.getProcessDefinitionVersion();
    }

    @Override
    public String getDeploymentId() {
        return historicProcessInstance.getDeploymentId();
    }

    @Override
    public Date getStartTime() {
        return historicProcessInstance.getStartTime();
    }

    @Override
    public Date getEndTime() {
        return historicProcessInstance.getEndTime();
    }

    @Override
    public Long getDurationInMillis() {
        return historicProcessInstance.getDurationInMillis();
    }

    @Override
    @Deprecated
    public String getEndActivityId() {
        return historicProcessInstance.getEndActivityId();
    }

    @Override
    public String getStartUserId() {
        return historicProcessInstance.getStartUserId();
    }

    @Override
    public String getStartActivityId() {
        return historicProcessInstance.getStartActivityId();
    }

    @Override
    public String getDeleteReason() {
        return historicProcessInstance.getDeleteReason();
    }

    @Override
    public String getSuperProcessInstanceId() {
        return historicProcessInstance.getSuperProcessInstanceId();
    }

    @Override
    public String getTenantId() {
        return historicProcessInstance.getTenantId();
    }

    @Override
    public String getName() {
        return historicProcessInstance.getName();
    }

    @Override
    public String getDescription() {
        return historicProcessInstance.getDescription();
    }

    @Override
    public void setLocalizedName(String s) {
        historicProcessInstance.setLocalizedName(s);
    }

    @Override
    public void setLocalizedDescription(String s) {
        historicProcessInstance.setLocalizedDescription(s);
    }

    @Override
    public Map<String, Object> getProcessVariables() {
        return historicProcessInstance.getProcessVariables();
    }

    public String getStartUserName() {
        return startUserName;
    }

    public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }
}
