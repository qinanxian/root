package com.vekai.workflow.model;

import org.activiti.engine.runtime.ProcessInstance;

import java.io.Serializable;
import java.util.Map;

/**
 * 工作流实例进程
 */
public class ProcInstance implements ProcessInstance,Serializable {
    private ProcessInstance processInstance;

    public ProcInstance(){

    }

    public ProcInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    @Override
    public String getProcessDefinitionId() {
        return processInstance.getProcessDefinitionId();
    }

    @Override
    public String getProcessDefinitionName() {
        return processInstance.getProcessDefinitionName();
    }

    @Override
    public String getProcessDefinitionKey() {
        return processInstance.getProcessDefinitionKey();
    }

    @Override
    public Integer getProcessDefinitionVersion() {
        return processInstance.getProcessDefinitionVersion();
    }

    @Override
    public String getDeploymentId() {
        return processInstance.getDeploymentId();
    }

    @Override
    public String getBusinessKey() {
        return processInstance.getBusinessKey();
    }

    @Override
    public boolean isSuspended() {
        return processInstance.isSuspended();
    }

    @Override
    public Map<String, Object> getProcessVariables() {
        return processInstance.getProcessVariables();
    }

    @Override
    public String getTenantId() {
        return processInstance.getTenantId();
    }

    @Override
    public String getName() {
        return processInstance.getName();
    }

    @Override
    public String getDescription() {
        return processInstance.getDescription();
    }

    @Override
    public String getLocalizedName() {
        return processInstance.getLocalizedName();
    }

    @Override
    public String getLocalizedDescription() {
        return processInstance.getLocalizedDescription();
    }

    @Override
    public String getId() {
        return processInstance.getId();
    }

    @Override
    public boolean isEnded() {
        return processInstance.isEnded();
    }

    @Override
    public String getActivityId() {
        return processInstance.getActivityId();
    }

    @Override
    public String getProcessInstanceId() {
        return processInstance.getProcessInstanceId();
    }

    @Override
    public String getParentId() {
        return processInstance.getParentId();
    }

    @Override
    public String getSuperExecutionId() {
        return processInstance.getSuperExecutionId();
    }
}
