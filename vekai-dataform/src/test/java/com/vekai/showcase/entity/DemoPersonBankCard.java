package com.vekai.showcase.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "DEMO_PERSON_BANK_CARD")
public class DemoPersonBankCard implements Serializable,Cloneable {
    @Id
    @GeneratedValue
    private Long uid;
    private Long personId;
    private String cardNo;
    private String cardType;
    private String issueBank;
    private String issueBankName;
    private Date issueDate;
    private Date expiredDate;
    private String cardUseType;
    private String cardRemark;
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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getIssueBank() {
        return issueBank;
    }

    public void setIssueBank(String issueBank) {
        this.issueBank = issueBank;
    }

    public String getIssueBankName() {
        return issueBankName;
    }

    public void setIssueBankName(String issueBankName) {
        this.issueBankName = issueBankName;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getCardUseType() {
        return cardUseType;
    }

    public void setCardUseType(String cardUseType) {
        this.cardUseType = cardUseType;
    }

    public String getCardRemark() {
        return cardRemark;
    }

    public void setCardRemark(String cardRemark) {
        this.cardRemark = cardRemark;
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
