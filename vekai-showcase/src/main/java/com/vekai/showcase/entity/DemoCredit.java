package com.vekai.showcase.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "DEMO_WKFL_CREDIT")
public class DemoCredit implements Serializable {
    @Id
    @GeneratedValue(generator = "com.amarsoft.amix.showcase.workflow.entity.DemoCredit.planId")
    private String planId;
    private String planName;
    private String planType;
    private String prodPolicy;
    private String custId;
    private String custName;
    @Transient
    private String certType;
    @Transient
    private String certId;
    private Double planAmt;
    private Double planTerm;
    private String gbIndustry;
    private String currency;
    private String purpose;
    private String planRemark;
    private String planStatus;
    private String ownerId;
    private Integer revision;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getProdPolicy() {
        return prodPolicy;
    }

    public void setProdPolicy(String prodPolicy) {
        this.prodPolicy = prodPolicy;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public Double getPlanAmt() {
        return planAmt;
    }

    public void setPlanAmt(Double planAmt) {
        this.planAmt = planAmt;
    }

    public Double getPlanTerm() {
        return planTerm;
    }

    public void setPlanTerm(Double planTerm) {
        this.planTerm = planTerm;
    }

    public String getGbIndustry() {
        return gbIndustry;
    }

    public void setGbIndustry(String gbIndustry) {
        this.gbIndustry = gbIndustry;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPlanRemark() {
        return planRemark;
    }

    public void setPlanRemark(String planRemark) {
        this.planRemark = planRemark;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
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
