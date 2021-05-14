package com.vekai.crops.obiz.tradetally.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="OBIZ_TRADE_TALLY")
public class ObizTradeTally implements Serializable,Cloneable{
    /** 交易ID */
    @Id
    @GeneratedValue
    private String tradeId ;
    /** 交易事件 */
    private String tradeEvent ;
    /** 交易类型 */
    private String tradeType ;
    /** 交易摘要 */
    private String tradeIntro ;
    /** 申请号 */
    private String applicationId ;
    /** 业务编号 */
    private String businessNo ;
    /** 合同号 */
    private String contractId ;
    /** 借据号 */
    private String duebillId ;
    /** 客户号 */
    private String custId ;
    /** 客户名 */
    private String custName ;
    /** 测算表ID */
    private String lactLoanId ;
    /** 银行卡号 */
    private String bankCardNo ;
    /** 开户行 */
    private String bankCardIssuer ;
    /** 开户名 */
    private String bankCardOwner ;
    /** 交易金额 */
    private Double tradeAmt ;
    /** 科目代码 */
    private String entryCode ;
    /** 科目名 */
    private String entryName ;
    /** 借贷方向 */
    private String direction ;
    /** 数据来源 */
    private String dataFrom ;
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

    /** 交易ID */
    public String getTradeId(){
        return this.tradeId;
    }
    /** 交易ID */
    public void setTradeId(String tradeId){
        this.tradeId = tradeId;
    }
    /** 交易事件 */
    public String getTradeEvent(){
        return this.tradeEvent;
    }
    /** 交易事件 */
    public void setTradeEvent(String tradeEvent){
        this.tradeEvent = tradeEvent;
    }
    /** 交易类型 */
    public String getTradeType(){
        return this.tradeType;
    }
    /** 交易类型 */
    public void setTradeType(String tradeType){
        this.tradeType = tradeType;
    }
    /** 交易摘要 */
    public String getTradeIntro(){
        return this.tradeIntro;
    }
    /** 交易摘要 */
    public void setTradeIntro(String tradeIntro){
        this.tradeIntro = tradeIntro;
    }
    /** 申请号 */
    public String getApplicationId(){
        return this.applicationId;
    }
    /** 申请号 */
    public void setApplicationId(String applicationId){
        this.applicationId = applicationId;
    }
    /** 业务编号 */
    public String getBusinessNo(){
        return this.businessNo;
    }
    /** 业务编号 */
    public void setBusinessNo(String businessNo){
        this.businessNo = businessNo;
    }
    /** 合同号 */
    public String getContractId(){
        return this.contractId;
    }
    /** 合同号 */
    public void setContractId(String contractId){
        this.contractId = contractId;
    }
    /** 借据号 */
    public String getDuebillId(){
        return this.duebillId;
    }
    /** 借据号 */
    public void setDuebillId(String duebillId){
        this.duebillId = duebillId;
    }
    /** 客户号 */
    public String getCustId(){
        return this.custId;
    }
    /** 客户号 */
    public void setCustId(String custId){
        this.custId = custId;
    }
    /** 客户名 */
    public String getCustName(){
        return this.custName;
    }
    /** 客户名 */
    public void setCustName(String custName){
        this.custName = custName;
    }
    /** 测算表ID */
    public String getLactLoanId(){
        return this.lactLoanId;
    }
    /** 测算表ID */
    public void setLactLoanId(String lactLoanId){
        this.lactLoanId = lactLoanId;
    }
    /** 银行卡号 */
    public String getBankCardNo(){
        return this.bankCardNo;
    }
    /** 银行卡号 */
    public void setBankCardNo(String bankCardNo){
        this.bankCardNo = bankCardNo;
    }
    /** 开户行 */
    public String getBankCardIssuer(){
        return this.bankCardIssuer;
    }
    /** 开户行 */
    public void setBankCardIssuer(String bankCardIssuer){
        this.bankCardIssuer = bankCardIssuer;
    }
    /** 开户名 */
    public String getBankCardOwner(){
        return this.bankCardOwner;
    }
    /** 开户名 */
    public void setBankCardOwner(String bankCardOwner){
        this.bankCardOwner = bankCardOwner;
    }
    /** 交易金额 */
    public Double getTradeAmt(){
        return this.tradeAmt;
    }
    /** 交易金额 */
    public void setTradeAmt(Double tradeAmt){
        this.tradeAmt = tradeAmt;
    }
    /** 科目代码 */
    public String getEntryCode(){
        return this.entryCode;
    }
    /** 科目代码 */
    public void setEntryCode(String entryCode){
        this.entryCode = entryCode;
    }
    /** 科目名 */
    public String getEntryName(){
        return this.entryName;
    }
    /** 科目名 */
    public void setEntryName(String entryName){
        this.entryName = entryName;
    }
    /** 借贷方向 */
    public String getDirection(){
        return this.direction;
    }
    /** 借贷方向 */
    public void setDirection(String direction){
        this.direction = direction;
    }
    /** 数据来源 */
    public String getDataFrom(){
        return this.dataFrom;
    }
    /** 数据来源 */
    public void setDataFrom(String dataFrom){
        this.dataFrom = dataFrom;
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