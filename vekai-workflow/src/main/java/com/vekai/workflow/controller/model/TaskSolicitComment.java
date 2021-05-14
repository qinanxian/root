package com.vekai.workflow.controller.model;

import java.util.Date;

/**
 * @Author: qyyao
 * @Description:
 * @Date: Created in 18:02 18/05/2018
 */
public class TaskSolicitComment extends TaskComment{
    private String solicitorSummary;
    private String userName;
    private String solicitor;
    private String solicitReply;
    private String solicitComment;
    private Date createdTime;
    private Date updatedTime;

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getSolicitor() {
        return solicitor;
    }

    public void setSolicitor(String solicitor) {
        this.solicitor = solicitor;
    }

    public String getSolicitReply() {
        return solicitReply;
    }

    public String getSolicitComment() {
        return solicitComment;
    }

    public void setSolicitComment(String solicitComment) {
        this.solicitComment = solicitComment;
    }

    public void setSolicitReply(String solicitReply) {
        this.solicitReply = solicitReply;
    }

    @Override public Date getCreatedTime() {
        return createdTime;
    }

    @Override public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }


    public String getSolicitorSummary() {
        return solicitorSummary;
    }

    public void setSolicitorSummary(String solicitorSummary) {
        this.solicitorSummary = solicitorSummary;
    }

    @Override public String getUserName() {
        return userName;
    }

    @Override public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override public String toString() {
        return "TaskSolicitComment{" +
            "solicitorSummary='" + solicitorSummary + '\'' +
            ", userName='" + userName + '\'' +
            ", solicitor='" + solicitor + '\'' +
            ", solicitReply='" + solicitReply + '\'' +
            ", solicitComment='" + solicitComment + '\'' +
            ", createdTime=" + createdTime +
            ", updatedTime=" + updatedTime +
            '}';
    }
}
