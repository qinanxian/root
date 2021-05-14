package com.vekai.workflow.controller.model;

import org.activiti.engine.history.HistoricTaskInstance;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class HistoricTaskDesc implements HistoricTaskInstance,Serializable {
    private HistoricTaskInstance histTaskInst;

    /**
     * 流程任务历史轨迹
     */
    private String traceDesc;

    public HistoricTaskDesc() {
    }

    public HistoricTaskDesc(HistoricTaskInstance histTaskInst) {
        this.histTaskInst = histTaskInst;
    }

    @Override
    public String getDeleteReason() {
        return histTaskInst.getDeleteReason();
    }

    @Override
    public Date getStartTime() {
        return histTaskInst.getStartTime();
    }

    @Override
    public Date getEndTime() {
        return histTaskInst.getEndTime();
    }

    @Override
    public Long getDurationInMillis() {
        return histTaskInst.getDurationInMillis();
    }

    @Override
    public Long getWorkTimeInMillis() {
        return histTaskInst.getWorkTimeInMillis();
    }

    @Override
    public Date getClaimTime() {
        return histTaskInst.getClaimTime();
    }

    @Override
    public void setLocalizedName(String name) {

    }

    @Override
    public void setLocalizedDescription(String description) {

    }

    @Override
    public Date getTime() {
        return histTaskInst.getTime();
    }

    @Override
    public String getId() {
        return histTaskInst.getId();
    }

    @Override
    public String getName() {
        return histTaskInst.getName();
    }

    @Override
    public String getDescription() {
        return histTaskInst.getDescription();
    }

    @Override
    public int getPriority() {
        return histTaskInst.getPriority();
    }

    @Override
    public String getOwner() {
        return histTaskInst.getOwner();
    }

    @Override
    public String getAssignee() {
        return histTaskInst.getAssignee();
    }

    @Override
    public String getProcessInstanceId() {
        return histTaskInst.getProcessInstanceId();
    }

    @Override
    public String getExecutionId() {
        return histTaskInst.getExecutionId();
    }

    @Override
    public String getProcessDefinitionId() {
        return histTaskInst.getProcessDefinitionId();
    }

    @Override
    public Date getCreateTime() {
        return histTaskInst.getCreateTime();
    }

    @Override
    public String getTaskDefinitionKey() {
        return histTaskInst.getTaskDefinitionKey();
    }

    @Override
    public Date getDueDate() {
        return histTaskInst.getDueDate();
    }

    @Override
    public String getCategory() {
        return histTaskInst.getCategory();
    }

    @Override
    public String getParentTaskId() {
        return histTaskInst.getParentTaskId();
    }

    @Override
    public String getTenantId() {
        return histTaskInst.getTenantId();
    }

    @Override
    public String getFormKey() {
        return histTaskInst.getFormKey();
    }

    @Override
    public Map<String, Object> getTaskLocalVariables() {
        return histTaskInst.getTaskLocalVariables();
    }

    @Override
    public Map<String, Object> getProcessVariables() {
        return histTaskInst.getProcessVariables();
    }

    public String getTraceDesc() {
        return traceDesc;
    }

    public void setTraceDesc(String traceDesc) {
        this.traceDesc = traceDesc;
    }
}
