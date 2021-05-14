package com.vekai.lact.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 还款计划
 */
/** 收付款计划表 */
@Table(name="LACT_PAYMENT_SCHEDULE")
public class PaymentScheduleEntity implements Serializable,Cloneable{
    /** 测算表ID */
    private String loanId ;
    /** 分段ID */
    private String loanSegmentId ;
    /** 计划ID */
    @Id
    @GeneratedValue
    private String scheduleId ;
    /** 父计划ID */
    private String parentId ;
    /** 资金计划类型;Repayment:还款计划，Deposit:保证金 */
    private String paymentType ;
    /** 收入支出类型 */
    private String inOrOut ;
    /** 期次 */
    private Integer termTimes ;
    /** 结算日期 */
    private Date settleDate ;
    /** 还款日期 */
    private Date paymentDate ;
    /** 还款类型 */
    private String repaymentType ;
    /** 本金不含税 */
    private Double principalPure ;
    /** 本金税 */
    private Double principalTax ;
    /** 本金总额 */
    private Double principal ;
    /** 利息不含税 */
    private Double interestPure ;
    /** 利息税 */
    private Double interestTax ;
    /** 利息总额 */
    private Double interest ;
    /** 还款总额不含税 */
    private Double paymentAmtPure ;
    /** 还款总额税 */
    private Double paymentAmtTax ;
    /** 还款总额 */
    private Double paymentAmt ;
    /** 未回收成本 */
    private Double remainCost ;
    /** 实际执行利率 */
    private Double interestRate ;
    /** 税率 */
    private Double taxRate ;
    /** 顺延后还款日 */
    private Date gracePaymentDate ;
    /** 实际还款日 */
    private Date actualPaymentDate ;
    /** 实还本金 */
    private Double actualPaymentPrincipal ;
    /** 实还利息 */
    private Double actualPaymentInterest ;
    /** 实还总额 */
    private Double actualPaymentAmt ;
    /** 预计罚息 */
    private Double redictPenalInterest ;
    /** 期供实际还款日 */
    private Date actualSettleDate ;
    /** 调息调整比例 */
    private Double interestAdjustRatio ;
    /** 调息调整金额 */
    private Double interestAdjustAmt ;
    /** 调息金额(基数金额） */
    private Double interestAdjustRadix ;
    /** 当期资金占用 */
    private Double capitalOccupation ;
    /** 资金金额 */
    private Double totalAmt ;
    /** 是否计算收益 */
    private String isProfit ;
    /** 乐观锁 */
    private Integer revision ;
    /** 创建人 */
    private String createdBy ;
    /** 创建日期 */
    private Date createdTime ;
    /** 更新人 */
    private String updatedBy ;
    /** 更新日期 */
    private Date updatedTime ;

    /** 测算表ID */
    public String getLoanId(){
        return this.loanId;
    }
    /** 测算表ID */
    public void setLoanId(String loanId){
        this.loanId = loanId;
    }
    /** 分段ID */
    public String getLoanSegmentId(){
        return this.loanSegmentId;
    }
    /** 分段ID */
    public void setLoanSegmentId(String loanSegmentId){
        this.loanSegmentId = loanSegmentId;
    }
    /** 计划ID */
    public String getScheduleId(){
        return this.scheduleId;
    }
    /** 计划ID */
    public void setScheduleId(String scheduleId){
        this.scheduleId = scheduleId;
    }
    /** 父计划ID */
    public String getParentId(){
        return this.parentId;
    }
    /** 父计划ID */
    public void setParentId(String parentId){
        this.parentId = parentId;
    }
    /** 资金计划类型;Repayment:还款计划，Deposit:保证金 */
    public String getPaymentType(){
        return this.paymentType;
    }
    /** 资金计划类型;Repayment:还款计划，Deposit:保证金 */
    public void setPaymentType(String paymentType){
        this.paymentType = paymentType;
    }
    /** 收入支出类型 */
    public String getInOrOut(){
        return this.inOrOut;
    }
    /** 收入支出类型 */
    public void setInOrOut(String inOrOut){
        this.inOrOut = inOrOut;
    }
    /** 期次 */
    public Integer getTermTimes(){
        return this.termTimes;
    }
    /** 期次 */
    public void setTermTimes(Integer termTimes){
        this.termTimes = termTimes;
    }
    /** 结算日期 */
    public Date getSettleDate(){
        return this.settleDate;
    }
    /** 结算日期 */
    public void setSettleDate(Date settleDate){
        this.settleDate = settleDate;
    }
    /** 还款日期 */
    public Date getPaymentDate(){
        return this.paymentDate;
    }
    /** 还款日期 */
    public void setPaymentDate(Date paymentDate){
        this.paymentDate = paymentDate;
    }
    /** 还款类型 */
    public String getRepaymentType(){
        return this.repaymentType;
    }
    /** 还款类型 */
    public void setRepaymentType(String repaymentType){
        this.repaymentType = repaymentType;
    }
    /** 本金不含税 */
    public Double getPrincipalPure(){
        return this.principalPure;
    }
    /** 本金不含税 */
    public void setPrincipalPure(Double principalPure){
        this.principalPure = principalPure;
    }
    /** 本金税 */
    public Double getPrincipalTax(){
        return this.principalTax;
    }
    /** 本金税 */
    public void setPrincipalTax(Double principalTax){
        this.principalTax = principalTax;
    }
    /** 本金总额 */
    public Double getPrincipal(){
        return this.principal;
    }
    /** 本金总额 */
    public void setPrincipal(Double principal){
        this.principal = principal;
    }
    /** 利息不含税 */
    public Double getInterestPure(){
        return this.interestPure;
    }
    /** 利息不含税 */
    public void setInterestPure(Double interestPure){
        this.interestPure = interestPure;
    }
    /** 利息税 */
    public Double getInterestTax(){
        return this.interestTax;
    }
    /** 利息税 */
    public void setInterestTax(Double interestTax){
        this.interestTax = interestTax;
    }
    /** 利息总额 */
    public Double getInterest(){
        return this.interest;
    }
    /** 利息总额 */
    public void setInterest(Double interest){
        this.interest = interest;
    }
    /** 还款总额不含税 */
    public Double getPaymentAmtPure(){
        return this.paymentAmtPure;
    }
    /** 还款总额不含税 */
    public void setPaymentAmtPure(Double paymentAmtPure){
        this.paymentAmtPure = paymentAmtPure;
    }
    /** 还款总额税 */
    public Double getPaymentAmtTax(){
        return this.paymentAmtTax;
    }
    /** 还款总额税 */
    public void setPaymentAmtTax(Double paymentAmtTax){
        this.paymentAmtTax = paymentAmtTax;
    }
    /** 还款总额 */
    public Double getPaymentAmt(){
        return this.paymentAmt;
    }
    /** 还款总额 */
    public void setPaymentAmt(Double paymentAmt){
        this.paymentAmt = paymentAmt;
    }
    /** 未回收成本 */
    public Double getRemainCost(){
        return this.remainCost;
    }
    /** 未回收成本 */
    public void setRemainCost(Double remainCost){
        this.remainCost = remainCost;
    }
    /** 实际执行利率 */
    public Double getInterestRate(){
        return this.interestRate;
    }
    /** 实际执行利率 */
    public void setInterestRate(Double interestRate){
        this.interestRate = interestRate;
    }
    /** 税率 */
    public Double getTaxRate(){
        return this.taxRate;
    }
    /** 税率 */
    public void setTaxRate(Double taxRate){
        this.taxRate = taxRate;
    }
    /** 顺延后还款日 */
    public Date getGracePaymentDate(){
        return this.gracePaymentDate;
    }
    /** 顺延后还款日 */
    public void setGracePaymentDate(Date gracePaymentDate){
        this.gracePaymentDate = gracePaymentDate;
    }
    /** 实际还款日 */
    public Date getActualPaymentDate(){
        return this.actualPaymentDate;
    }
    /** 实际还款日 */
    public void setActualPaymentDate(Date actualPaymentDate){
        this.actualPaymentDate = actualPaymentDate;
    }
    /** 实还本金 */
    public Double getActualPaymentPrincipal(){
        return this.actualPaymentPrincipal;
    }
    /** 实还本金 */
    public void setActualPaymentPrincipal(Double actualPaymentPrincipal){
        this.actualPaymentPrincipal = actualPaymentPrincipal;
    }
    /** 实还利息 */
    public Double getActualPaymentInterest(){
        return this.actualPaymentInterest;
    }
    /** 实还利息 */
    public void setActualPaymentInterest(Double actualPaymentInterest){
        this.actualPaymentInterest = actualPaymentInterest;
    }
    /** 实还总额 */
    public Double getActualPaymentAmt(){
        return this.actualPaymentAmt;
    }
    /** 实还总额 */
    public void setActualPaymentAmt(Double actualPaymentAmt){
        this.actualPaymentAmt = actualPaymentAmt;
    }
    /** 预计罚息 */
    public Double getRedictPenalInterest(){
        return this.redictPenalInterest;
    }
    /** 预计罚息 */
    public void setRedictPenalInterest(Double redictPenalInterest){
        this.redictPenalInterest = redictPenalInterest;
    }
    /** 期供实际还款日 */
    public Date getActualSettleDate(){
        return this.actualSettleDate;
    }
    /** 期供实际还款日 */
    public void setActualSettleDate(Date actualSettleDate){
        this.actualSettleDate = actualSettleDate;
    }
    /** 调息调整比例 */
    public Double getInterestAdjustRatio(){
        return this.interestAdjustRatio;
    }
    /** 调息调整比例 */
    public void setInterestAdjustRatio(Double interestAdjustRatio){
        this.interestAdjustRatio = interestAdjustRatio;
    }
    /** 调息调整金额 */
    public Double getInterestAdjustAmt(){
        return this.interestAdjustAmt;
    }
    /** 调息调整金额 */
    public void setInterestAdjustAmt(Double interestAdjustAmt){
        this.interestAdjustAmt = interestAdjustAmt;
    }
    /** 调息金额(基数金额） */
    public Double getInterestAdjustRadix(){
        return this.interestAdjustRadix;
    }
    /** 调息金额(基数金额） */
    public void setInterestAdjustRadix(Double interestAdjustRadix){
        this.interestAdjustRadix = interestAdjustRadix;
    }
    /** 当期资金占用 */
    public Double getCapitalOccupation(){
        return this.capitalOccupation;
    }
    /** 当期资金占用 */
    public void setCapitalOccupation(Double capitalOccupation){
        this.capitalOccupation = capitalOccupation;
    }
    /** 资金金额 */
    public Double getTotalAmt(){
        return this.totalAmt;
    }
    /** 资金金额 */
    public void setTotalAmt(Double totalAmt){
        this.totalAmt = totalAmt;
    }
    /** 是否计算收益 */
    public String getIsProfit(){
        return this.isProfit;
    }
    /** 是否计算收益 */
    public void setIsProfit(String isProfit){
        this.isProfit = isProfit;
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
    /** 创建日期 */
    public Date getCreatedTime(){
        return this.createdTime;
    }
    /** 创建日期 */
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
    /** 更新日期 */
    public Date getUpdatedTime(){
        return this.updatedTime;
    }
    /** 更新日期 */
    public void setUpdatedTime(Date updatedTime){
        this.updatedTime = updatedTime;
    }
}
