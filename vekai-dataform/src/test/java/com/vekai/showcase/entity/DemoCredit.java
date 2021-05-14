package com.vekai.showcase.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Table(name = "DEMO_WKFL_CREDIT")
public class DemoCredit implements Serializable {
    @Id
    @GeneratedValue
    private String planId;
    private String planName;
    private String planType;
    private Long prodPolicy;
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

    /** 状态 */
    private String status ;
    /** 暂存草稿;YesNo */
    private String isDraft ;
    /** 是否删除;YesNo */
    private String isDelete ;
    /** 租户 */
    private Long tenantId ;
    /** 乐观锁 */
    private Integer revision ;
    /** 创建人 */
    private Long createdUser ;
    /** 创建机构 */
    private Long createdOrg ;
    /** 创建日期 */
    private String createdDate ;
    /** 创建时间 */
    private String createdTime ;
    /** 更新人 */
    private Long updatedUser ;
    /** 更新机构 */
    private Long updatedOrg ;
    /** 更新日期 */
    private String updatedDate ;
    /** 更新时间 */
    private String updatedTime ;
    /** 备注 */
    private String remark ;

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

    public Long getProdPolicy() {
        return prodPolicy;
    }

    public void setProdPolicy(Long prodPolicy) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(String isDraft) {
        this.isDraft = isDraft;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public Long getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(Long createdUser) {
        this.createdUser = createdUser;
    }

    public Long getCreatedOrg() {
        return createdOrg;
    }

    public void setCreatedOrg(Long createdOrg) {
        this.createdOrg = createdOrg;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(Long updatedUser) {
        this.updatedUser = updatedUser;
    }

    public Long getUpdatedOrg() {
        return updatedOrg;
    }

    public void setUpdatedOrg(Long updatedOrg) {
        this.updatedOrg = updatedOrg;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
