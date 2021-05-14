package com.vekai.crops.othApplications.custWishes.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Table(name="ELECTRONIC_INTEND")
public class ElectronicIntend implements Serializable,Cloneable{

    /** 编号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 姓名 */
    private String certName ;
    /** 身份证号 */
    private String certId ;
    /** 企业名称 */
    private String enterpriseName ;
    /** 经办人和法人是否一致 */
    private String areConsistent ;
    /** 申请时间 */
    private String creationTime ;
    /** 申请状态 */
    private String currentState ;
    /** 借款借据 */
    private String loanNode ;
    /** 交易对手信息 */
    private String counterparty ;
    /** 提款申请书 */
    private String drawingsFrom ;
    /** 乐观锁 */
    private Integer revision ;
    /** 创建人 */
    private String createdBy ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新人 */
    private String updatedBy ;
    /** 更新时间 */
    private Date updatedTime ;
    /** 借款人类型 */
    private String borrowerType ;
    /** 客户经理名称 */
    private String managerName ;
    /** 客户经理OA号 */
    private String managerAccunt ;

    /** 编号 */
    public String getId() {
        return id;
    }
    /** 编号 */
    public void setId(String id) {
        this.id = id;
    }
    /** 姓名 */
    public String getCertName() {
        return certName;
    }
    /** 姓名 */
    public void setCertName(String certName) {
        this.certName = certName;
    }
    /** 身份证号 */
    public String getCertId() {
        return certId;
    }
    /** 身份证号 */
    public void setCertId(String certId) {
        this.certId = certId;
    }
    /** 企业名称 */
    public String getEnterpriseName() {
        return enterpriseName;
    }
    /** 企业名称 */
    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }
    /** 经办人和法人是否一致 */
    public String getAreConsistent() {
        return areConsistent;
    }
    /** 经办人和法人是否一致 */
    public void setAreConsistent(String areConsistent) {
        this.areConsistent = areConsistent;
    }
    /** 申请时间 */
    public String getCreationTime() {
        return creationTime;
    }
    /** 申请时间 */
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
    /** 申请状态 */
    public String getCurrentState() {
        return currentState;
    }
    /** 申请状态 */
    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
    /** 借款借据 */
    public String getLoanNode() {
        return loanNode;
    }
    /** 借款借据 */
    public void setLoanNode(String loanNode) {
        this.loanNode = loanNode;
    }
    /** 交易对手信息 */
    public String getCounterparty() {
        return counterparty;
    }
    /** 交易对手信息 */
    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }
    /** 提款申请书 */
    public String getDrawingsFrom() {
        return drawingsFrom;
    }
    /** 提款申请书 */
    public void setDrawingsFrom(String drawingsFrom) {
        this.drawingsFrom = drawingsFrom;
    }
    /** 乐观锁 */
    public Integer getRevision() {
        return revision;
    }
    /** 乐观锁 */
    public void setRevision(Integer revision) {
        this.revision = revision;
    }
    /** 创建人 */
    public String getCreatedBy() {
        return createdBy;
    }
    /** 创建人 */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /** 创建时间 */
    public Date getCreatedTime() {
        return createdTime;
    }
    /** 创建时间 */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    /** 更新人 */
    public String getUpdatedBy() {
        return updatedBy;
    }
    /** 更新人 */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    /** 更新时间 */
    public Date getUpdatedTime() {
        return updatedTime;
    }
    /** 更新时间 */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
    /** 借款人类型 */
    public String getBorrowerType() {
        return borrowerType;
    }
    /** 借款人类型 */
    public void setBorrowerType(String borrowerType) {
        this.borrowerType = borrowerType;
    }
    /** 客户经理姓名 */
    public String getManagerName() {
        return managerName;
    }
    /** 客户经理姓名 */
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    /** 客户经理OA号 */
    public String getManagerAccunt() {
        return managerAccunt;
    }
    /** 客户经理OA号 */
    public void setManagerAccunt(String managerAccunt) {
        this.managerAccunt = managerAccunt;
    }

}