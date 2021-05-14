package com.vekai.crops.othApplications.zryJurisDiction.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Table(name="ZRY_FINANCING_ORDER_INFO")
public class ZryFinancingOrderInfo implements Serializable,Cloneable{
    /** 申贷编号 */
    @Id
    @GeneratedValue(generator = "JDBC")
    private String loanId ;
    /** 支行 */
    private String branch ;
    /** 网点 */
    private String outlets ;
    /** 信贷操作员 */
    private String creditOperator ;
    /** 分发状态 */
    private String distributionStatus ;
    /** 融资标的ID */
    private String bidNum ;
    /** 企业ID */
    private String companyId ;
    /** 融资标的名 */
    private String bidName ;
    /** 融资标的时间戳 */
    private String bidDate ;
    /** 融资标的金额 */
    private String bidAmt ;
    /** 金额币种 */
    private String currency ;
    /** 金额单位 */
    private String unit ;
    /** 合同已付款 */
    private String paidAmt ;
    /** 质保金 */
    private String bondAmt ;
    /** 合同预付款 */
    private String advanceAmt ;
    /** 项目已签订合同 */
    private String isSign ;
    /** 融资标的类型 */
    private String isBid ;
    /** 采购人所属地编码 */
    private String pchrBlngRgonCd ;
    /** 采购人所属地 */
    private String pchrBlngRgon ;
    /** 采购人 */
    private String purchaseName ;
    /** 采购项目支付资金属性 */
    private String budgetNature ;
    /** 项目所属品目 */
    private String purchaseItem ;
    /** 申请银行 */
    private String applyBank ;
    /** 申请贷款产品 */
    private String applyProCode ;
    /** 拟申贷金额 */
    private String applyAmount ;
    /** 申贷资金用途 */
    private String applyUse ;
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

    /** 申贷编号 */
    public String getLoanId(){
        return this.loanId;
    }
    /** 申贷编号 */
    public void setLoanId(String loanId){
        this.loanId = loanId;
    }
    /** 支行 */
    public String getBranch(){
        return this.branch;
    }
    /** 支行 */
    public void setBranch(String branch){
        this.branch = branch;
    }
    /** 网点 */
    public String getOutlets(){
        return this.outlets;
    }
    /** 网点 */
    public void setOutlets(String outlets){
        this.outlets = outlets;
    }
    /** 信贷操作员 */
    public String getCreditOperator(){
        return this.creditOperator;
    }
    /** 信贷操作员 */
    public void setCreditOperator(String creditOperator){
        this.creditOperator = creditOperator;
    }
    /** 分发状态 */
    public String getDistributionStatus(){
        return this.distributionStatus;
    }
    /** 分发状态 */
    public void setDistributionStatus(String distributionStatus){
        this.distributionStatus = distributionStatus;
    }
    /** 融资标的ID */
    public String getBidNum(){
        return this.bidNum;
    }
    /** 融资标的ID */
    public void setBidNum(String bidNum){
        this.bidNum = bidNum;
    }
    /** 企业ID */
    public String getCompanyId(){
        return this.companyId;
    }
    /** 企业ID */
    public void setCompanyId(String companyId){
        this.companyId = companyId;
    }
    /** 融资标的名 */
    public String getBidName(){
        return this.bidName;
    }
    /** 融资标的名 */
    public void setBidName(String bidName){
        this.bidName = bidName;
    }
    /** 融资标的时间戳 */
    public String getBidDate(){
        return this.bidDate;
    }
    /** 融资标的时间戳 */
    public void setBidDate(String bidDate){
        this.bidDate = bidDate;
    }
    /** 融资标的金额 */
    public String getBidAmt(){
        return this.bidAmt;
    }
    /** 融资标的金额 */
    public void setBidAmt(String bidAmt){
        this.bidAmt = bidAmt;
    }
    /** 金额币种 */
    public String getCurrency(){
        return this.currency;
    }
    /** 金额币种 */
    public void setCurrency(String currency){
        this.currency = currency;
    }
    /** 金额单位 */
    public String getUnit(){
        return this.unit;
    }
    /** 金额单位 */
    public void setUnit(String unit){
        this.unit = unit;
    }
    /** 合同已付款 */
    public String getPaidAmt(){
        return this.paidAmt;
    }
    /** 合同已付款 */
    public void setPaidAmt(String paidAmt){
        this.paidAmt = paidAmt;
    }
    /** 质保金 */
    public String getBondAmt(){
        return this.bondAmt;
    }
    /** 质保金 */
    public void setBondAmt(String bondAmt){
        this.bondAmt = bondAmt;
    }
    /** 合同预付款 */
    public String getAdvanceAmt(){
        return this.advanceAmt;
    }
    /** 合同预付款 */
    public void setAdvanceAmt(String advanceAmt){
        this.advanceAmt = advanceAmt;
    }
    /** 项目已签订合同 */
    public String getIsSign(){
        return this.isSign;
    }
    /** 项目已签订合同 */
    public void setIsSign(String isSign){
        this.isSign = isSign;
    }
    /** 融资标的类型 */
    public String getIsBid(){
        return this.isBid;
    }
    /** 融资标的类型 */
    public void setIsBid(String isBid){
        this.isBid = isBid;
    }
    /** 采购人所属地编码 */
    public String getPchrBlngRgonCd(){
        return this.pchrBlngRgonCd;
    }
    /** 采购人所属地编码 */
    public void setPchrBlngRgonCd(String pchrBlngRgonCd){
        this.pchrBlngRgonCd = pchrBlngRgonCd;
    }
    /** 采购人所属地 */
    public String getPchrBlngRgon(){
        return this.pchrBlngRgon;
    }
    /** 采购人所属地 */
    public void setPchrBlngRgon(String pchrBlngRgon){
        this.pchrBlngRgon = pchrBlngRgon;
    }
    /** 采购人 */
    public String getPurchaseName(){
        return this.purchaseName;
    }
    /** 采购人 */
    public void setPurchaseName(String purchaseName){
        this.purchaseName = purchaseName;
    }
    /** 采购项目支付资金属性 */
    public String getBudgetNature(){
        return this.budgetNature;
    }
    /** 采购项目支付资金属性 */
    public void setBudgetNature(String budgetNature){
        this.budgetNature = budgetNature;
    }
    /** 项目所属品目 */
    public String getPurchaseItem(){
        return this.purchaseItem;
    }
    /** 项目所属品目 */
    public void setPurchaseItem(String purchaseItem){
        this.purchaseItem = purchaseItem;
    }
    /** 申请银行 */
    public String getApplyBank(){
        return this.applyBank;
    }
    /** 申请银行 */
    public void setApplyBank(String applyBank){
        this.applyBank = applyBank;
    }
    /** 申请贷款产品 */
    public String getApplyProCode(){
        return this.applyProCode;
    }
    /** 申请贷款产品 */
    public void setApplyProCode(String applyProCode){
        this.applyProCode = applyProCode;
    }
    /** 拟申贷金额 */
    public String getApplyAmount(){
        return this.applyAmount;
    }
    /** 拟申贷金额 */
    public void setApplyAmount(String applyAmount){
        this.applyAmount = applyAmount;
    }
    /** 申贷资金用途 */
    public String getApplyUse(){
        return this.applyUse;
    }
    /** 申贷资金用途 */
    public void setApplyUse(String applyUse){
        this.applyUse = applyUse;
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
