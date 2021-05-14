package com.vekai.appframe.policy.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="POLC_DEFINITION")
public class PolcDefinitionEntity implements Serializable,Cloneable{
    /** 产品ID */
    @Id
    @GeneratedValue
    private String policyId ;
    /** 产品代码 */
    private String policyCode ;
    /** 排序码 */
    private String sortCode ;
    /** 父产品 */
    private String parentPolicyCode ;
    /** 产品名称 */
    private String policyName ;
    /** 产品类型 */
    private String policyType ;
    /** 产品描述 */
    private String policyDesc ;
    /** 启用日期 */
    private Date issueDate ;
    /** 过期日期 */
    private Date expiryDate ;
    /** 产品状态;草稿，已上架，已下架 */
    private String policyStatus ;
    /** 乐观锁版本 */
    private Integer revision ;
    /** 创建人 */
    private String createdBy ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新人 */
    private String updatedBy ;
    /** 更新时间 */
    private Date updatedTime ;

    /** 产品ID */
    public String getPolicyId(){
        return this.policyId;
    }
    /** 产品ID */
    public void setPolicyId(String policyId){
        this.policyId = policyId;
    }
    /** 产品代码 */
    public String getPolicyCode(){
        return this.policyCode;
    }
    /** 产品代码 */
    public void setPolicyCode(String policyCode){
        this.policyCode = policyCode;
    }
    /** 排序码 */
    public String getSortCode(){
        return this.sortCode;
    }
    /** 排序码 */
    public void setSortCode(String sortCode){
        this.sortCode = sortCode;
    }
    /** 父产品 */
    public String getParentPolicyCode(){
        return this.parentPolicyCode;
    }
    /** 父产品 */
    public void setParentPolicyCode(String parentPolicyCode){
        this.parentPolicyCode = parentPolicyCode;
    }
    /** 产品名称 */
    public String getPolicyName(){
        return this.policyName;
    }
    /** 产品名称 */
    public void setPolicyName(String policyName){
        this.policyName = policyName;
    }
    /** 产品类型 */
    public String getPolicyType(){
        return this.policyType;
    }
    /** 产品类型 */
    public void setPolicyType(String policyType){
        this.policyType = policyType;
    }
    /** 产品描述 */
    public String getPolicyDesc(){
        return this.policyDesc;
    }
    /** 产品描述 */
    public void setPolicyDesc(String policyDesc){
        this.policyDesc = policyDesc;
    }
    /** 启用日期 */
    public Date getIssueDate(){
        return this.issueDate;
    }
    /** 启用日期 */
    public void setIssueDate(Date issueDate){
        this.issueDate = issueDate;
    }
    /** 过期日期 */
    public Date getExpiryDate(){
        return this.expiryDate;
    }
    /** 过期日期 */
    public void setExpiryDate(Date expiryDate){
        this.expiryDate = expiryDate;
    }
    /** 产品状态;草稿，已上架，已下架 */
    public String getPolicyStatus(){
        return this.policyStatus;
    }
    /** 产品状态;草稿，已上架，已下架 */
    public void setPolicyStatus(String policyStatus){
        this.policyStatus = policyStatus;
    }
    /** 乐观锁版本 */
    public Integer getRevision(){
        return this.revision;
    }
    /** 乐观锁版本 */
    public void setRevision(Integer revision){
        this.revision = revision;
    }
    /** 创建人 */
    public String getCreatedBy(){
        return this.createdBy;
    }
    /** 创建人 */
    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }
    /** 创建时间 */
    public Date getCreatedTime(){
        return this.createdTime;
    }
    /** 创建时间 */
    public void setCreatedTime(Date createdTime){
        this.createdTime = createdTime;
    }
    /** 更新人 */
    public String getUpdatedBy(){
        return this.updatedBy;
    }
    /** 更新人 */
    public void setUpdatedBy(String updatedBy){
        this.updatedBy = updatedBy;
    }
    /** 更新时间 */
    public Date getUpdatedTime(){
        return this.updatedTime;
    }
    /** 更新时间 */
    public void setUpdatedTime(Date updatedTime){
        this.updatedTime = updatedTime;
    }
}