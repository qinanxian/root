package com.vekai.crops.business.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="BUSINESS_LIMIT")
public class BusinessLimitPO implements Serializable,Cloneable{
    /** 编号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 应用编号 */
    private String appId ;
    /** 客户编号 */
    private String customerId ;
    /** 银行卡信息编号 */
    private String cardId ;
    /** 额度金额（元） */
    private Double limitAmount ;
    /** 额度期数（月） */
    private String limitTerm ;
    /** 日贷款利率（‰） */
    private Double dayRate ;
    /** 贷款用途' */
    private String loanPurpose ;
    /** 还款方式 */
    private String repayType ;
    /** 起始日期 */
    private String startDate ;
    /** 到期日期 */
    private String endDate ;
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

    /** 编号 */
    public String getId(){
        return this.id;
    }
    /** 编号 */
    public void setId(String id){
        this.id = id;
    }
    /** 应用编号 */
    public String getAppId(){
        return this.appId;
    }
    /** 应用编号 */
    public void setAppId(String appId){
        this.appId = appId;
    }
    /** 客户编号 */
    public String getCustomerId(){
        return this.customerId;
    }
    /** 客户编号 */
    public void setCustomerId(String customerId){
        this.customerId = customerId;
    }
    /** 银行卡信息编号 */
    public String getCardId(){
        return this.cardId;
    }
    /** 银行卡信息编号 */
    public void setCardId(String cardId){
        this.cardId = cardId;
    }
    /** 额度金额（元） */
    public Double getLimitAmount(){
        return this.limitAmount;
    }
    /** 额度金额（元） */
    public void setLimitAmount(Double limitAmount){
        this.limitAmount = limitAmount;
    }
    /** 额度期数（月） */
    public String getLimitTerm(){
        return this.limitTerm;
    }
    /** 额度期数（月） */
    public void setLimitTerm(String limitTerm){
        this.limitTerm = limitTerm;
    }
    /** 日贷款利率（‰） */
    public Double getDayRate(){
        return this.dayRate;
    }
    /** 日贷款利率（‰） */
    public void setDayRate(Double dayRate){
        this.dayRate = dayRate;
    }
    /** 贷款用途' */
    public String getLoanPurpose(){
        return this.loanPurpose;
    }
    /** 贷款用途' */
    public void setLoanPurpose(String loanPurpose){
        this.loanPurpose = loanPurpose;
    }
    /** 还款方式 */
    public String getRepayType(){
        return this.repayType;
    }
    /** 还款方式 */
    public void setRepayType(String repayType){
        this.repayType = repayType;
    }
    /** 起始日期 */
    public String getStartDate(){
        return this.startDate;
    }
    /** 起始日期 */
    public void setStartDate(String startDate){
        this.startDate = startDate;
    }
    /** 到期日期 */
    public String getEndDate(){
        return this.endDate;
    }
    /** 到期日期 */
    public void setEndDate(String endDate){
        this.endDate = endDate;
    }
    /** 乐观锁 */
    public Integer getRevision(){
        return this.revision;
    }
    /** 乐观锁 */
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