package com.vekai.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 工作流实例进程的任务项
 */
public class ProcTask implements Task,Serializable {

    @JsonIgnore
    private Task task;

    public ProcTask(){

    }
    public ProcTask(Task task) {
        this.task = task;
    }

    public Task getTask(){
        return this.task;
    }

    @Override
    public void setName(String s) {
        task.setName(s);
    }

    @Override
    public void setLocalizedName(String s) {
        task.setLocalizedName(s);
    }

    @Override
    public void setDescription(String s) {
        task.setDescription(s);
    }

    @Override
    public void setLocalizedDescription(String s) {
        task.setLocalizedDescription(s);
    }

    @Override
    public void setPriority(int i) {
        task.setPriority(i);
    }

    @Override
    public void setOwner(String s) {
        task.setOwner(s);
    }

    @Override
    public void setAssignee(String s) {
        task.setAssignee(s);
    }

    @Override
    public DelegationState getDelegationState() {
        return task.getDelegationState();
    }

    @Override
    public void setDelegationState(DelegationState delegationState) {
        task.setDelegationState(delegationState);
    }

    @Override
    public void setDueDate(Date date) {
        task.setDueDate(date);
    }

    @Override
    public void setCategory(String s) {
        task.setCategory(s);
    }

    @Override
    public void delegate(String s) {
        task.delegate(s);
    }

    @Override
    public void setParentTaskId(String s) {
        task.setParentTaskId(s);
    }

    @Override
    public void setTenantId(String s) {
        task.setTenantId(s);
    }

    @Override
    public void setFormKey(String s) {
        task.setFormKey(s);
    }

    @Override
    public boolean isSuspended() {
        return task.isSuspended();
    }

    @Override
    public String getId() {
        return task.getId();
    }

    @Override
    public String getName() {
        return task.getName();
    }

    @Override
    public String getDescription() {
        return task.getDescription();
    }

    @Override
    public int getPriority() {
        return task.getPriority();
    }

    @Override
    public String getOwner() {
        return task.getOwner();
    }

    @Override
    public String getAssignee() {
        return task.getAssignee();
    }

    @Override
    public String getProcessInstanceId() {
        return task.getProcessInstanceId();
    }

    @Override
    public String getExecutionId() {
        return task.getExecutionId();
    }

    @Override
    public String getProcessDefinitionId() {
        return task.getProcessDefinitionId();
    }

    @Override
    public Date getCreateTime() {
        return task.getCreateTime();
    }

    @Override
    public String getTaskDefinitionKey() {
        return task.getTaskDefinitionKey();
    }

    @Override
    public Date getDueDate() {
        return task.getDueDate();
    }

    @Override
    public String getCategory() {
        return task.getCategory();
    }

    @Override
    public String getParentTaskId() {
        return task.getParentTaskId();
    }

    @Override
    public String getTenantId() {
        return task.getTenantId();
    }

    @Override
    public String getFormKey() {
        return task.getFormKey();
    }

    @Override
    public Map<String, Object> getTaskLocalVariables() {
        return task.getTaskLocalVariables();
    }

    @Override
    public Map<String, Object> getProcessVariables() {
        return task.getProcessVariables();
    }
}
