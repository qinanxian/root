package com.vekai.workflow.entity;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "WKFL_SOLICIT")
public class WorkflowSolicit {

    @Id
    @GeneratedValue
    private String solicitId;
    private String procId;
    private String taskId;
    private String solicitSummary;
    private String solicitor;
    private String solicitComment;
    private String askFor;
    private String replyComment;
    private int revision;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;



    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getSolicitId() {
        return solicitId;
    }

    public void setSolicitId(String solicitId) {
        this.solicitId = solicitId;
    }

    public String getSolicitSummary() {
        return solicitSummary;
    }

    public void setSolicitSummary(String solicitSummary) {
        this.solicitSummary = solicitSummary;
    }

    public String getSolicitor() {
        return solicitor;
    }

    public void setSolicitor(String solicitor) {
        this.solicitor = solicitor;
    }

    public String getSolicitComment() {
        return solicitComment;
    }

    public void setSolicitComment(String solicitComment) {
        this.solicitComment = solicitComment;
    }

    public String getAskFor() {
        return askFor;
    }

    public void setAskFor(String askFor) {
        this.askFor = askFor;
    }

    public String getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(String replyComment) {
        this.replyComment = replyComment;
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
}
