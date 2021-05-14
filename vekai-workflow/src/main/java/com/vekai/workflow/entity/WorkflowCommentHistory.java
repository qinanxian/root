package com.vekai.workflow.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by apachechen on 2018/1/2.
 */
@Table(name = "WKFL_COMMENT_HIST")
public class WorkflowCommentHistory {
    @Id
    private String commentId;
    private String commentInstId;
    private String taskId;
    private String procId;
    private String commentType;
    private String message;
    private String userId;
    private String action;
    private String actionIntro;
    private String revision;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentInstId() {
        return commentInstId;
    }

    public void setCommentInstId(String commentInstId) {
        this.commentInstId = commentInstId;
    }

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

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionIntro() {
        return actionIntro;
    }

    public void setActionIntro(String actionIntro) {
        this.actionIntro = actionIntro;
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
        return "WorkflowComment{" +
            "commentId='" + commentId + '\'' +
            ", commentInstId='" + commentInstId + '\'' +
            ", taskId='" + taskId + '\'' +
            ", procId='" + procId + '\'' +
            ", commentType='" + commentType + '\'' +
            ", message='" + message + '\'' +
            ", userId='" + userId + '\'' +
            ", action='" + action + '\'' +
            ", actionIntro='" + actionIntro + '\'' +
            ", revision='" + revision + '\'' +
            ", createdBy='" + createdBy + '\'' +
            ", createdTime=" + createdTime +
            ", updatedBy='" + updatedBy + '\'' +
            ", updatedTime=" + updatedTime +
            '}';
    }
}
