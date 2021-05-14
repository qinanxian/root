package com.vekai.base.detector.entity;

import java.io.Serializable;
import java.util.Date;

public class DetectorItemPO implements Serializable {
    protected String detectorCode;
    protected String itemCode;
    protected String groupCode;
    protected String sortCode;
    protected String itemName;
    protected String itemStatus;
    protected String failureProcessMode;
    protected String successMessage;
    protected String failMessage;
    protected String execCondition;
    protected String execType;
    protected String execScript;
    protected Long revision;
    protected String createdBy;
    protected Date createdTime;
    protected String updatedBy;
    protected Date updatedTime;

    public String getDetectorCode() {
        return detectorCode;
    }

    public void setDetectorCode(String detectorCode) {
        this.detectorCode = detectorCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getFailureProcessMode() {
        return failureProcessMode;
    }

    public void setFailureProcessMode(String failureProcessMode) {
        this.failureProcessMode = failureProcessMode;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public void setFailMessage(String failMessage) {
        this.failMessage = failMessage;
    }

    public String getExecCondition() {
        return execCondition;
    }

    public void setExecCondition(String execCondition) {
        this.execCondition = execCondition;
    }

    public String getExecType() {
        return execType;
    }

    public void setExecType(String execType) {
        this.execType = execType;
    }

    public String getExecScript() {
        return execScript;
    }

    public void setExecScript(String execScript) {
        this.execScript = execScript;
    }

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
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
