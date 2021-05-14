package com.vekai.showcase.batch.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="TENPAY_LOAN_REQ")
public class TenpayLoanReq implements Serializable,Cloneable{
    /** 序号 */
    @Id
    @GeneratedValue
    private Integer serialNo ;
    /** 基金公司商户号 */
    private String fundMerchantNo ;
    /** 基金代码 */
    private String fundCode ;
    /** 产品代码 */
    private String productCode ;
    /** 资产管理方名称 */
    private String assetManagerName ;
    /** 产品名称 */
    private String productName ;
    /** 产品成立日 */
    private String productIssueDate ;
    /** 产品起息日 */
    private String productStartInterestDate ;
    /** 产品到期日 */
    private String productExpiryDate ;
    /** 产品兑付日 */
    private String productHonourDate ;
    /** 产品类型 */
    private Integer productType ;
    /** 质押率 */
    private Integer impawnRatio ;
    /** 还款方式 */
    private Integer repaymentMethod ;
    /** 贷款基准利率 */
    private Integer loanBasicInterestRate ;
    /** 备注 */
    private String remarks ;
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

    /** 序号 */
    public Integer getSerialNo(){
        return this.serialNo;
    }
    /** 序号 */
    public void setSerialNo(Integer serialNo){
        this.serialNo = serialNo;
    }
    /** 基金公司商户号 */
    public String getFundMerchantNo(){
        return this.fundMerchantNo;
    }
    /** 基金公司商户号 */
    public void setFundMerchantNo(String fundMerchantNo){
        this.fundMerchantNo = fundMerchantNo;
    }
    /** 基金代码 */
    public String getFundCode(){
        return this.fundCode;
    }
    /** 基金代码 */
    public void setFundCode(String fundCode){
        this.fundCode = fundCode;
    }
    /** 产品代码 */
    public String getProductCode(){
        return this.productCode;
    }
    /** 产品代码 */
    public void setProductCode(String productCode){
        this.productCode = productCode;
    }
    /** 资产管理方名称 */
    public String getAssetManagerName(){
        return this.assetManagerName;
    }
    /** 资产管理方名称 */
    public void setAssetManagerName(String assetManagerName){
        this.assetManagerName = assetManagerName;
    }
    /** 产品名称 */
    public String getProductName(){
        return this.productName;
    }
    /** 产品名称 */
    public void setProductName(String productName){
        this.productName = productName;
    }
    /** 产品成立日 */
    public String getProductIssueDate(){
        return this.productIssueDate;
    }
    /** 产品成立日 */
    public void setProductIssueDate(String productIssueDate){
        this.productIssueDate = productIssueDate;
    }
    /** 产品起息日 */
    public String getProductStartInterestDate(){
        return this.productStartInterestDate;
    }
    /** 产品起息日 */
    public void setProductStartInterestDate(String productStartInterestDate){
        this.productStartInterestDate = productStartInterestDate;
    }
    /** 产品到期日 */
    public String getProductExpiryDate(){
        return this.productExpiryDate;
    }
    /** 产品到期日 */
    public void setProductExpiryDate(String productExpiryDate){
        this.productExpiryDate = productExpiryDate;
    }
    /** 产品兑付日 */
    public String getProductHonourDate(){
        return this.productHonourDate;
    }
    /** 产品兑付日 */
    public void setProductHonourDate(String productHonourDate){
        this.productHonourDate = productHonourDate;
    }
    /** 产品类型 */
    public Integer getProductType(){
        return this.productType;
    }
    /** 产品类型 */
    public void setProductType(Integer productType){
        this.productType = productType;
    }
    /** 质押率 */
    public Integer getImpawnRatio(){
        return this.impawnRatio;
    }
    /** 质押率 */
    public void setImpawnRatio(Integer impawnRatio){
        this.impawnRatio = impawnRatio;
    }
    /** 还款方式 */
    public Integer getRepaymentMethod(){
        return this.repaymentMethod;
    }
    /** 还款方式 */
    public void setRepaymentMethod(Integer repaymentMethod){
        this.repaymentMethod = repaymentMethod;
    }
    /** 贷款基准利率 */
    public Integer getLoanBasicInterestRate(){
        return this.loanBasicInterestRate;
    }
    /** 贷款基准利率 */
    public void setLoanBasicInterestRate(Integer loanBasicInterestRate){
        this.loanBasicInterestRate = loanBasicInterestRate;
    }
    /** 备注 */
    public String getRemarks(){
        return this.remarks;
    }
    /** 备注 */
    public void setRemarks(String remarks){
        this.remarks = remarks;
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

    @Override
    public String toString() {
        return "TenpayLoanReq{" +
                "serialNo=" + serialNo +
                ", fundMerchantNo='" + fundMerchantNo + '\'' +
                ", fundCode='" + fundCode + '\'' +
                ", productCode='" + productCode + '\'' +
                ", assetManagerName='" + assetManagerName + '\'' +
                ", productName='" + productName + '\'' +
                ", productIssueDate='" + productIssueDate + '\'' +
                ", productStartInterestDate='" + productStartInterestDate + '\'' +
                ", productExpiryDate='" + productExpiryDate + '\'' +
                ", productHonourDate='" + productHonourDate + '\'' +
                ", productType=" + productType +
                ", impawnRatio=" + impawnRatio +
                ", repaymentMethod=" + repaymentMethod +
                ", loanBasicInterestRate=" + loanBasicInterestRate +
                ", remarks='" + remarks + '\'' +
                ", revision=" + revision +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime=" + createdTime +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedTime=" + updatedTime +
                '}';
    }
}