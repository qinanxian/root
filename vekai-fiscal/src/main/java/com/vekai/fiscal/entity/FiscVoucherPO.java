package com.vekai.fiscal.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 记账凭证 */
@Table(name="FISC_VOUCHER")
public class FiscVoucherPO implements Serializable,Cloneable{
    /** 账套代码 */
    private String bookCode ;
    /** 会计期间ID */
    private String periodId ;
    /** 凭证ID */
    @Id
    @GeneratedValue
    private String voucherId ;
    /** 记账事件ID */
    private String eventId ;
    /** 序号;一个账套唯一 */
    private String voucherSeqNo ;
    /** 会计期间年份 */
    private Integer periodYear ;
    /** 会计期数 */
    private Integer periodTerm ;
    /** 业务发生日期 */
    private Date occurDate ;
    /** 凭证生成日期 */
    private Date voucherGenerateDate ;
    /** 凭证字;例如：记 */
    private String voucherWord ;
    /** 凭证号;一般来说，在月份（会计期间）唯一。凭证字号是在一个会计期间内，唯一确定一张凭证（包含借方，贷方）配对 */
    private Integer voucherNo ;
    /** 摘要说明 */
    private String entryNote ;
    /** 表内表外;代码:InOutSideTable: IN表内，OUT表外 */
    private String inOutSide ;
    /** 借方 */
    private Double debitAmt ;
    /** 贷方 */
    private Double creditAmt ;
    /** 原币金额 */
    private Double rawAmt ;
    /** 币别 */
    private String currency ;
    /** 汇率类型 */
    private String exchangeRateType ;
    /** 汇率 */
    private Double exchangeRate ;
    /** 备注说明 */
    private String remarkNote ;
    /** 状态 */
    private String status ;
    /** 科目类别;代码：GAAPEntryCategory ；资产，负债，权益，成本，损益，共同，表外 */
    private String categoryCode ;
    /** 外币核算 */
    private String foreignCurrency ;
    /** 期末调汇 */
    private String finalTransfer ;
    /** 往来业务核算 */
    private String tradeAccounting ;
    /** 审核 */
    private String isAudit ;
    /** 复核 */
    private String isReview ;
    /** 过账 */
    private String isPost ;
    /** 制单人 */
    private String maker ;
    /** 审核人 */
    private String checker ;
    /** 经办人 */
    private String operator ;
    /** 轻办机构 */
    private String operateOrg ;
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

    /** 账套代码 */
    public String getBookCode(){
        return this.bookCode;
    }
    /** 账套代码 */
    public void setBookCode(String bookCode){
        this.bookCode = bookCode;
    }
    /** 会计期间ID */
    public String getPeriodId(){
        return this.periodId;
    }
    /** 会计期间ID */
    public void setPeriodId(String periodId){
        this.periodId = periodId;
    }
    /** 凭证ID */
    public String getVoucherId(){
        return this.voucherId;
    }
    /** 凭证ID */
    public void setVoucherId(String voucherId){
        this.voucherId = voucherId;
    }
    /** 记账事件ID */
    public String getEventId(){
        return this.eventId;
    }
    /** 记账事件ID */
    public void setEventId(String eventId){
        this.eventId = eventId;
    }
    /** 序号;一个账套唯一 */
    public String getVoucherSeqNo(){
        return this.voucherSeqNo;
    }
    /** 序号;一个账套唯一 */
    public void setVoucherSeqNo(String voucherSeqNo){
        this.voucherSeqNo = voucherSeqNo;
    }
    /** 会计期间年份 */
    public Integer getPeriodYear(){
        return this.periodYear;
    }
    /** 会计期间年份 */
    public void setPeriodYear(Integer periodYear){
        this.periodYear = periodYear;
    }
    /** 会计期数 */
    public Integer getPeriodTerm(){
        return this.periodTerm;
    }
    /** 会计期数 */
    public void setPeriodTerm(Integer periodTerm){
        this.periodTerm = periodTerm;
    }
    /** 业务发生日期 */
    public Date getOccurDate(){
        return this.occurDate;
    }
    /** 业务发生日期 */
    public void setOccurDate(Date occurDate){
        this.occurDate = occurDate;
    }
    /** 凭证字;例如：记 */
    public String getVoucherWord(){
        return this.voucherWord;
    }
    /** 凭证字;例如：记 */
    public void setVoucherWord(String voucherWord){
        this.voucherWord = voucherWord;
    }
    /** 凭证号;一般来说，在月份（会计期间）唯一。凭证字号是在一个会计期间内，唯一确定一张凭证（包含借方，贷方）配对 */
    public Integer getVoucherNo(){
        return this.voucherNo;
    }
    /** 凭证号;一般来说，在月份（会计期间）唯一。凭证字号是在一个会计期间内，唯一确定一张凭证（包含借方，贷方）配对 */
    public void setVoucherNo(Integer voucherNo){
        this.voucherNo = voucherNo;
    }
    /** 摘要说明 */
    public String getEntryNote(){
        return this.entryNote;
    }
    /** 摘要说明 */
    public void setEntryNote(String entryNote){
        this.entryNote = entryNote;
    }
    /** 表内表外;代码:InOutSideTable: IN表内，OUT表外 */
    public String getInOutSide(){
        return this.inOutSide;
    }
    /** 表内表外;代码:InOutSideTable: IN表内，OUT表外 */
    public void setInOutSide(String inOutSide){
        this.inOutSide = inOutSide;
    }
    /** 借方 */
    public Double getDebitAmt(){
        return this.debitAmt;
    }
    /** 借方 */
    public void setDebitAmt(Double debitAmt){
        this.debitAmt = debitAmt;
    }
    /** 贷方 */
    public Double getCreditAmt(){
        return this.creditAmt;
    }
    /** 贷方 */
    public void setCreditAmt(Double creditAmt){
        this.creditAmt = creditAmt;
    }
    /** 原币金额 */
    public Double getRawAmt(){
        return this.rawAmt;
    }
    /** 原币金额 */
    public void setRawAmt(Double rawAmt){
        this.rawAmt = rawAmt;
    }
    /** 币别 */
    public String getCurrency(){
        return this.currency;
    }
    /** 币别 */
    public void setCurrency(String currency){
        this.currency = currency;
    }
    /** 汇率 */
    public Double getExchangeRate(){
        return this.exchangeRate;
    }
    /** 汇率 */
    public void setExchangeRate(Double exchangeRate){
        this.exchangeRate = exchangeRate;
    }
    /** 备注说明 */
    public String getRemarkNote(){
        return this.remarkNote;
    }
    /** 备注说明 */
    public void setRemarkNote(String remarkNote){
        this.remarkNote = remarkNote;
    }
    /** 状态 */
    public String getStatus(){
        return this.status;
    }
    /** 状态 */
    public void setStatus(String status){
        this.status = status;
    }
    /** 科目类别;代码：GAAPEntryCategory ；资产，负债，权益，成本，损益，共同，表外 */
    public String getCategoryCode(){
        return this.categoryCode;
    }
    /** 科目类别;代码：GAAPEntryCategory ；资产，负债，权益，成本，损益，共同，表外 */
    public void setCategoryCode(String categoryCode){
        this.categoryCode = categoryCode;
    }
    /** 外币核算 */
    public String getForeignCurrency(){
        return this.foreignCurrency;
    }
    /** 外币核算 */
    public void setForeignCurrency(String foreignCurrency){
        this.foreignCurrency = foreignCurrency;
    }
    /** 期末调汇 */
    public String getFinalTransfer(){
        return this.finalTransfer;
    }
    /** 期末调汇 */
    public void setFinalTransfer(String finalTransfer){
        this.finalTransfer = finalTransfer;
    }
    /** 往来业务核算 */
    public String getTradeAccounting(){
        return this.tradeAccounting;
    }
    /** 往来业务核算 */
    public void setTradeAccounting(String tradeAccounting){
        this.tradeAccounting = tradeAccounting;
    }
    /** 审核 */
    public String getIsAudit(){
        return this.isAudit;
    }
    /** 审核 */
    public void setIsAudit(String isAudit){
        this.isAudit = isAudit;
    }
    /** 复核 */
    public String getIsReview(){
        return this.isReview;
    }
    /** 复核 */
    public void setIsReview(String isReview){
        this.isReview = isReview;
    }
    /** 过账 */
    public String getIsPost(){
        return this.isPost;
    }
    /** 过账 */
    public void setIsPost(String isPost){
        this.isPost = isPost;
    }
    /** 制单人 */
    public String getMaker(){
        return this.maker;
    }
    /** 制单人 */
    public void setMaker(String maker){
        this.maker = maker;
    }
    /** 审核人 */
    public String getChecker(){
        return this.checker;
    }
    /** 审核人 */
    public void setChecker(String checker){
        this.checker = checker;
    }
    /** 经办人 */
    public String getOperator(){
        return this.operator;
    }
    /** 经办人 */
    public void setOperator(String operator){
        this.operator = operator;
    }
    /** 轻办机构 */
    public String getOperateOrg(){
        return this.operateOrg;
    }
    /** 轻办机构 */
    public void setOperateOrg(String operateOrg){
        this.operateOrg = operateOrg;
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

    public String getExchangeRateType() {
        return exchangeRateType;
    }

    public void setExchangeRateType(String exchangeRateType) {
        this.exchangeRateType = exchangeRateType;
    }

    public Date getVoucherGenerateDate() {
        return voucherGenerateDate;
    }

    public void setVoucherGenerateDate(Date voucherGenerateDate) {
        this.voucherGenerateDate = voucherGenerateDate;
    }
}
