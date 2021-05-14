package com.vekai.appframe.taxrate.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 税率 */
@Table(name="FNAT_TAX_RATE")
public class FnatTaxRate implements Serializable,Cloneable{
    /** 标识号 */
    @Id
    @GeneratedValue
    private String taxRateId ;
    /** 税率类型 */
    private String taxRateType ;
    /** 排序代码 */
    private String sortCode ;
    /** 生效日期 */
    private Date effectDate ;
    /** 税率 */
    private Double taxRate ;
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
    public String getTaxRateId(){
        return this.taxRateId;
    }
    /** 标识号 */
    public void setTaxRateId(String taxRateId){
        this.taxRateId = taxRateId;
    }
    /** 税率类型 */
    public String getTaxRateType(){
        return this.taxRateType;
    }
    /** 税率类型 */
    public void setTaxRateType(String taxRateType){
        this.taxRateType = taxRateType;
    }
    /** 排序代码 */
    public String getSortCode(){
        return this.sortCode;
    }
    /** 排序代码 */
    public void setSortCode(String sortCode){
        this.sortCode = sortCode;
    }
    /** 生效日期 */
    public Date getEffectDate(){
        return this.effectDate;
    }
    /** 生效日期 */
    public void setEffectDate(Date effectDate){
        this.effectDate = effectDate;
    }
    /** 税率 */
    public Double getTaxRate(){
        return this.taxRate;
    }
    /** 税率 */
    public void setTaxRate(Double taxRate){
        this.taxRate = taxRate;
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