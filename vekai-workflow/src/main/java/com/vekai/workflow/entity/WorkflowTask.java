package com.vekai.workflow.entity;




import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Table(name = "WKFL_TASK")
public class WorkflowTask {
    @Id
    private String taskId;
    private String procId;
    private String taskInstId;
    private String taskDefKey;
    private String taskName;
    private String owner;
    private String assignee;
    private Date arrivalTime;
    private Date finishTime;
    private String finishType;
    private int revision;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;
    private String isConcurrent;
    @Transient
    private List<WorkflowComment> workflowComments;
    @Transient
    private List<WorkflowSolicit> workflowSolicits;

    public String getIsConcurrent() {
        return isConcurrent;
    }

    public void setIsConcurrent(String isConcurrent) {
        this.isConcurrent = isConcurrent;
    }

    @Transient
    private WorkflowProc workflowProc;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskInstId() {
        return taskInstId;
    }

    public void setTaskInstId(String taskInstId) {
        this.taskInstId = taskInstId;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getFinishType() {
        return finishType;
    }

    public void setFinishType(String finishType) {
        this.finishType = finishType;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public WorkflowProc getWorkflowProc() {
        return workflowProc;
    }

    public void setWorkflowProc(WorkflowProc workflowProc) {
        this.workflowProc = workflowProc;
    }

    public List<WorkflowComment> getWorkflowComments() {
        return workflowComments;
    }

    public void setWorkflowComments(List<WorkflowComment> workflowComments) {
        this.workflowComments = workflowComments;
    }

    public List<WorkflowSolicit> getWorkflowSolicits() {
        return workflowSolicits;
    }

    public void setWorkflowSolicits(List<WorkflowSolicit> workflowSolicits) {
        this.workflowSolicits = workflowSolicits;
    }

    @Override
    public String toString() {
        return "WorkflowTask{" +
            "taskId='" + taskId + '\'' +
            ", procId='" + procId + '\'' +
            ", taskInstId='" + taskInstId + '\'' +
            ", taskDefKey='" + taskDefKey + '\'' +
            ", taskName='" + taskName + '\'' +
            ", owner='" + owner + '\'' +
            ", assignee='" + assignee + '\'' +
            ", arrivalTime=" + arrivalTime +
            ", finishTime=" + finishTime +
            ", finishType='" + finishType + '\'' +
            ", revision=" + revision +
            ", createdBy='" + createdBy + '\'' +
            ", createdTime=" + createdTime +
            ", updatedBy='" + updatedBy + '\'' +
            ", updatedTime=" + updatedTime +
            ", isConcurrent='" + isConcurrent + '\'' +
            ", workflowProc=" + workflowProc +
            '}';
    }
}
