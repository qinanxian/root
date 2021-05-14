package com.vekai.auth.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/** 法人主体 */
@Table(name="AUTH_CORP")
public class Corp implements Serializable,Cloneable{
    /** 法人ID */
    @Id
    @GeneratedValue
    private String corpId ;
    /** 主体公司名称 */
    private String corpName ;
    /** 简称 */
    private String corpShortName ;
    /** 主体公司纳税性质;代码：CompanyTaxNature；一般纳税人，小规模纳税人（税率会不一定) */
    private String taxNature ;
    /** 税号 */
    private String taxNo ;
    /** 税率 */
    private Double raxRate ;
    /** 统一社会信用代码 */
    private String unifiedCreditCode ;
    /** 地址 */
    private String address ;
    /** 电话 */
    private String telephone ;
    /** 传真 */
    private String faxNo ;
    /** 电子邮件 */
    private String email ;
    /** 银行账号 */
    private String bankAccountNo ;
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

    /** 法人ID */
    public String getCorpId(){
        return this.corpId;
    }
    /** 法人ID */
    public void setCorpId(String corpId){
        this.corpId = corpId;
    }
    /** 主体公司名称 */
    public String getCorpName(){
        return this.corpName;
    }
    /** 主体公司名称 */
    public void setCorpName(String corpName){
        this.corpName = corpName;
    }
    /** 简称 */
    public String getCorpShortName(){
        return this.corpShortName;
    }
    /** 简称 */
    public void setCorpShortName(String corpShortName){
        this.corpShortName = corpShortName;
    }
    /** 主体公司纳税性质;代码：CompanyTaxNature；一般纳税人，小规模纳税人（税率会不一定) */
    public String getTaxNature(){
        return this.taxNature;
    }
    /** 主体公司纳税性质;代码：CompanyTaxNature；一般纳税人，小规模纳税人（税率会不一定) */
    public void setTaxNature(String taxNature){
        this.taxNature = taxNature;
    }
    /** 税号 */
    public String getTaxNo(){
        return this.taxNo;
    }
    /** 税号 */
    public void setTaxNo(String taxNo){
        this.taxNo = taxNo;
    }
    /** 税率 */
    public Double getRaxRate(){
        return this.raxRate;
    }
    /** 税率 */
    public void setRaxRate(Double raxRate){
        this.raxRate = raxRate;
    }
    /** 统一社会信用代码 */
    public String getUnifiedCreditCode(){
        return this.unifiedCreditCode;
    }
    /** 统一社会信用代码 */
    public void setUnifiedCreditCode(String unifiedCreditCode){
        this.unifiedCreditCode = unifiedCreditCode;
    }
    /** 地址 */
    public String getAddress(){
        return this.address;
    }
    /** 地址 */
    public void setAddress(String address){
        this.address = address;
    }
    /** 电话 */
    public String getTelephone(){
        return this.telephone;
    }
    /** 电话 */
    public void setTelephone(String telephone){
        this.telephone = telephone;
    }
    /** 传真 */
    public String getFaxNo(){
        return this.faxNo;
    }
    /** 传真 */
    public void setFaxNo(String faxNo){
        this.faxNo = faxNo;
    }
    /** 电子邮件 */
    public String getEmail(){
        return this.email;
    }
    /** 电子邮件 */
    public void setEmail(String email){
        this.email = email;
    }
    /** 银行账号 */
    public String getBankAccountNo(){
        return this.bankAccountNo;
    }
    /** 银行账号 */
    public void setBankAccountNo(String bankAccountNo){
        this.bankAccountNo = bankAccountNo;
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
