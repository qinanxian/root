package com.vekai.crops.product.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="PRODUCT_INFO")
public class ProductInfoPO implements Serializable,Cloneable{
    /** 编号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 产品名称 */
    private String productName ;
    /** 应用编号 */
    private String appId ;
    /** 额度上限 */
    private Double maxLimit ;
    /** 产品基础年利率 */
    private Double baseYearRate ;
    /** 还款方式 */
    private String repayType ;
    /** 产品期限 */
    private String loanTerm ;
    /** 模型编号 */
    private String modelId ;
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
    /** 产品名称 */
    public String getProductName(){
        return this.productName;
    }
    /** 产品名称 */
    public void setProductName(String productName){
        this.productName = productName;
    }
    /** 应用编号 */
    public String getAppId(){
        return this.appId;
    }
    /** 应用编号 */
    public void setAppId(String appId){
        this.appId = appId;
    }
    /** 额度上限 */
    public Double getMaxLimit(){
        return this.maxLimit;
    }
    /** 额度上限 */
    public void setMaxLimit(Double maxLimit){
        this.maxLimit = maxLimit;
    }
    /** 产品基础年利率 */
    public Double getBaseYearRate(){
        return this.baseYearRate;
    }
    /** 产品基础年利率 */
    public void setBaseYearRate(Double baseYearRate){
        this.baseYearRate = baseYearRate;
    }
    /** 还款方式 */
    public String getRepayType(){
        return this.repayType;
    }
    /** 还款方式 */
    public void setRepayType(String repayType){
        this.repayType = repayType;
    }
    /** 产品期限 */
    public String getLoanTerm(){
        return this.loanTerm;
    }
    /** 产品期限 */
    public void setLoanTerm(String loanTerm){
        this.loanTerm = loanTerm;
    }
    /** 模型编号 */
    public String getModelId(){
        return this.modelId;
    }
    /** 模型编号 */
    public void setModelId(String modelId){
        this.modelId = modelId;
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