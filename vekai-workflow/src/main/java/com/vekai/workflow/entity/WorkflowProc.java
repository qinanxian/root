package com.vekai.workflow.entity;


import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by apachechen on 2017/12/28.
 */
@Table(name = "WKFL_PROC")
public class WorkflowProc implements Serializable{
    @Id
    private String procId;
    private String objectType;
    private String objectId;
    private String procInstId;
    private String procDefKey;
    private String procDefId;
    private String procName;
    private String summary;
    private String sponsor;
    @Transient
    private String sponsorName;
    private Date startTime;
    private Date finishTime;
    private String status;
    private String lastTaskJson;
    private String lastTaskDesc;
    private String lastAssigneeDesc;
    private String firstComment;
    private String lastComment;
    private String pageParam;
    private String flowType;
    @Transient
    private List<WorkflowTask> latestTasks;
    private String liteFlowDef;
    private String handler;
    private String curMaxDepth;
    private String maxDepth;

    public List<WorkflowTask> getLatestTasks() {
        return latestTasks;
    }

    public void setLatestTasks(List<WorkflowTask> latestTasks) {
        this.latestTasks = latestTasks;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    private String revision;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastTaskJson() {
        return lastTaskJson;
    }

    public void setLastTaskJson(String lastTaskJson) {
        this.lastTaskJson = lastTaskJson;
    }

    public String getLastTaskDesc() {
        return lastTaskDesc;
    }

    public void setLastTaskDesc(String lastTaskDesc) {
        this.lastTaskDesc = lastTaskDesc;
    }

    public String getLastAssigneeDesc() {
        return lastAssigneeDesc;
    }

    public void setLastAssigneeDesc(String lastAssigneeDesc) {
        this.lastAssigneeDesc = lastAssigneeDesc;
    }

    public String getFirstComment() {
        return firstComment;
    }

    public void setFirstComment(String firstComment) {
        this.firstComment = firstComment;
    }

    public String getLastComment() {
        return lastComment;
    }

    public void setLastComment(String lastComment) {
        this.lastComment = lastComment;
    }

    public String getPageParam() {
        return pageParam;
    }

    public void setPageParam(String pageParam) {
        this.pageParam = pageParam;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public String getLiteFlowDef() {
        return liteFlowDef;
    }

    public void setLiteFlowDef(String liteFlowDef) {
        this.liteFlowDef = liteFlowDef;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
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

    public String getCurMaxDepth() {
        return curMaxDepth;
    }

    public void setCurMaxDepth(String curMaxDepth) {
        this.curMaxDepth = curMaxDepth;
    }

    public String getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(String maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override public String toString() {
        return "WorkflowProc{" +
            "procId='" + procId + '\'' +
            ", objectType='" + objectType + '\'' +
            ", objectId='" + objectId + '\'' +
            ", procInstId='" + procInstId + '\'' +
            ", procDefKey='" + procDefKey + '\'' +
            ", procDefId='" + procDefId + '\'' +
            ", procName='" + procName + '\'' +
            ", summary='" + summary + '\'' +
            ", sponsor='" + sponsor + '\'' +
            ", sponsorName='" + sponsorName + '\'' +
            ", startTime=" + startTime +
            ", finishTime=" + finishTime +
            ", status='" + status + '\'' +
            ", lastTaskJson='" + lastTaskJson + '\'' +
            ", lastTaskDesc='" + lastTaskDesc + '\'' +
            ", lastAssigneeDesc='" + lastAssigneeDesc + '\'' +
            ", firstComment='" + firstComment + '\'' +
            ", lastComment='" + lastComment + '\'' +
            ", pageParam='" + pageParam + '\'' +
            ", flowType='" + flowType + '\'' +
            ", latestTasks=" + latestTasks +
            ", liteFlowDef='" + liteFlowDef + '\'' +
            ", handler='" + handler + '\'' +
            ", curMaxDepth='" + curMaxDepth + '\'' +
            ", maxDepth='" + maxDepth + '\'' +
            ", revision='" + revision + '\'' +
            ", createdBy='" + createdBy + '\'' +
            ", createdTime=" + createdTime +
            ", updatedBy='" + updatedBy + '\'' +
            ", updatedTime=" + updatedTime +
            '}';
    }
}
