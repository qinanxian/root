package com.vekai.fiscal.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 账套 */
@Table(name="FISC_BOOK")
public class FiscBookPO implements Serializable,Cloneable{
    /** 账套代码 */
    @Id
    @GeneratedValue
    private String bookCode ;
    /** 账套名称 */
    private String bookName ;
    /** 账套说明 */
    private String bookIntro ;
    /** 启用会计年度 */
    private Integer startYear ;
    /** 启用会计期间 */
    private Integer startTerm ;
    /** 结束会计年度 */
    private Integer endYear ;
    /** 当前会计年度 */
    private Integer currentYear ;
    /** 当前会计期间 */
    private Integer currentTerm ;
    /** 会计准制度则;科目体系 */
    private String gaapDef ;
    /** 会计准制度则名称 */
    private String gaapName ;
    /** 凭证字;代码：VoucherWord；记，收、付、转，现收、现付、银收、银付，转 */
    private String voucherWord ;
    /** 主体公司ID */
    private String mainCorpId ;
    /** 主体公司名称 */
    private String mainCorpName ;
    /** 主体公司纳税性质;代码：CompanyTaxNature；一般纳税人，小规模纳税人（税率会不一定) */
    private String taxNature ;
    /** 税号 */
    private String taxNo ;
    /** 税率 */
    private Double raxRate ;
    /** 本位币;Currency */
    private String currency ;
    /** 小数位数 */
    private Integer scalePrecision ;
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

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    /** 账套代码 */
    public String getBookCode(){
        return this.bookCode;
    }
    /** 账套代码 */
    public void setBookCode(String bookCode){
        this.bookCode = bookCode;
    }
    /** 账套名称 */
    public String getBookName(){
        return this.bookName;
    }
    /** 账套名称 */
    public void setBookName(String bookName){
        this.bookName = bookName;
    }
    /** 账套说明 */
    public String getBookIntro(){
        return this.bookIntro;
    }
    /** 账套说明 */
    public void setBookIntro(String bookIntro){
        this.bookIntro = bookIntro;
    }
    /** 启用会计年度 */
    public Integer getStartYear(){
        return this.startYear;
    }
    /** 启用会计年度 */
    public void setStartYear(Integer startYear){
        this.startYear = startYear;
    }
    /** 启用会计期间 */
    public Integer getStartTerm(){
        return this.startTerm;
    }
    /** 启用会计期间 */
    public void setStartTerm(Integer startTerm){
        this.startTerm = startTerm;
    }
    /** 当前会计年度 */
    public Integer getCurrentYear(){
        return this.currentYear;
    }
    /** 当前会计年度 */
    public void setCurrentYear(Integer currentYear){
        this.currentYear = currentYear;
    }
    /** 当前会计期间 */
    public Integer getCurrentTerm(){
        return this.currentTerm;
    }
    /** 当前会计期间 */
    public void setCurrentTerm(Integer currentTerm){
        this.currentTerm = currentTerm;
    }
    /** 会计准制度则;科目体系 */
    public String getGaapDef(){
        return this.gaapDef;
    }
    /** 会计准制度则;科目体系 */
    public void setGaapDef(String gaapDef){
        this.gaapDef = gaapDef;
    }
    /** 会计准制度则名称 */
    public String getGaapName(){
        return this.gaapName;
    }
    /** 会计准制度则名称 */
    public void setGaapName(String gaapName){
        this.gaapName = gaapName;
    }
    /** 凭证字;代码：VoucherWord；记，收、付、转，现收、现付、银收、银付，转 */
    public String getVoucherWord(){
        return this.voucherWord;
    }
    /** 凭证字;代码：VoucherWord；记，收、付、转，现收、现付、银收、银付，转 */
    public void setVoucherWord(String voucherWord){
        this.voucherWord = voucherWord;
    }
    /** 主体公司ID */
    public String getMainCorpId(){
        return this.mainCorpId;
    }
    /** 主体公司ID */
    public void setMainCorpId(String mainCorpId){
        this.mainCorpId = mainCorpId;
    }
    /** 主体公司名称 */
    public String getMainCorpName(){
        return this.mainCorpName;
    }
    /** 主体公司名称 */
    public void setMainCorpName(String mainCorpName){
        this.mainCorpName = mainCorpName;
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
    /** 本位币;Currency */
    public String getCurrency(){
        return this.currency;
    }
    /** 本位币;Currency */
    public void setCurrency(String currency){
        this.currency = currency;
    }
    /** 小数位数 */
    public Integer getScalePrecision(){
        return this.scalePrecision;
    }
    /** 小数位数 */
    public void setScalePrecision(Integer scalePrecision){
        this.scalePrecision = scalePrecision;
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