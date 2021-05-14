package com.vekai.fnat.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 基准利率档次
 */
public class BaseInterestRate implements Serializable,Cloneable{
    /** 标识号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 利率类型 */
    private String rateType ;
    /** 排序代码 */
    private String sortCode ;
    /** 摘要说明 */
    private String summary ;
    /** 币种代码 */
    private String currency ;
    /** 生效日期 */
    private Date effectDate ;
    /** 期限代码 */
    private String termCode ;
    /** 年化利率 */
    private Double annualRate ;
    /** 状态 */
    private String status ;
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

    /** 标识号 */
    public String getId(){
        return this.id;
    }
    /** 标识号 */
    public void setId(String id){
        this.id = id;
    }
    /** 利率类型 */
    public String getRateType(){
        return this.rateType;
    }
    /** 利率类型 */
    public void setRateType(String rateType){
        this.rateType = rateType;
    }
    /** 排序代码 */
    public String getSortCode(){
        return this.sortCode;
    }
    /** 排序代码 */
    public void setSortCode(String sortCode){
        this.sortCode = sortCode;
    }
    /** 摘要说明 */
    public String getSummary(){
        return this.summary;
    }
    /** 摘要说明 */
    public void setSummary(String summary){
        this.summary = summary;
    }
    /** 币种代码 */
    public String getCurrency(){
        return this.currency;
    }
    /** 币种代码 */
    public void setCurrency(String currency){
        this.currency = currency;
    }
    /** 生效日期 */
    public Date getEffectDate(){
        return this.effectDate;
    }
    /** 生效日期 */
    public void setEffectDate(Date effectDate){
        this.effectDate = effectDate;
    }
    /** 期限代码 */
    public String getTermCode(){
        return this.termCode;
    }
    /** 期限代码 */
    public void setTermCode(String termCode){
        this.termCode = termCode;
    }
    /** 年化利率 */
    public Double getAnnualRate(){
        return this.annualRate;
    }
    /** 年化利率 */
    public void setAnnualRate(Double annualRate){
        this.annualRate = annualRate;
    }
    /** 状态 */
    public String getStatus(){
        return this.status;
    }
    /** 状态 */
    public void setStatus(String status){
        this.status = status;
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
