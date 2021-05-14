package com.vekai.workflow.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "WKFL_CANDIDATE")
public class WorkflowCandidate {
    @Id
    @GeneratedValue
    private String candidateId;
    private String taskId;
    private String candidateType;
    private String userId;
    private String sourceType;
    private String scopeType;
    private String scopeRoleId;
    private String scopeOrgId;
    private String scopeTeamId;
    private String revision;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCandidateType() {
        return candidateType;
    }

    public void setCandidateType(String candidateType) {
        this.candidateType = candidateType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public String getScopeRoleId() {
        return scopeRoleId;
    }

    public void setScopeRoleId(String scopeRoleId) {
        this.scopeRoleId = scopeRoleId;
    }

    public String getScopeOrgId() {
        return scopeOrgId;
    }

    public void setScopeOrgId(String scopeOrgId) {
        this.scopeOrgId = scopeOrgId;
    }

    public String getScopeTeamId() {
        return scopeTeamId;
    }

    public void setScopeTeamId(String scopeTeamId) {
        this.scopeTeamId = scopeTeamId;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
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

    @Override
    public String toString() {
        return "WorkflowCandidate{" +
                "candidateId='" + candidateId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", candidateType='" + candidateType + '\'' +
                ", userId='" + userId + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", scopeType='" + scopeType + '\'' +
                ", scopeRoleId='" + scopeRoleId + '\'' +
                ", scopeOrgId='" + scopeOrgId + '\'' +
                ", scopeTeamId='" + scopeTeamId + '\'' +
                ", revision='" + revision + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime=" + createdTime +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedTime=" + updatedTime +
                '}';
    }
}
