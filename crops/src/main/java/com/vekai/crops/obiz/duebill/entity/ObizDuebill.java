package com.vekai.crops.obiz.duebill.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="OBIZ_DUEBILL")
public class ObizDuebill implements Serializable,Cloneable{
    /** 借据号 */
    @Id
    @GeneratedValue
    private String duebillId ;
    /** 测算表ID */
    private String lactLoanId ;
    /** 合同号 */
    private String contractId ;
    /** 申请号 */
    private String applicationId ;
    /** 业务编号 */
    private String businessNo ;
    /** 客户号 */
    private String custId ;
    /** 客户名 */
    private String custName ;
    /** 产品ID */
    private String policyId ;
    /** 资金方ID */
    private String funderId ;
    /** 执行年利率 */
    private Double interestRate ;
    /** 合同起始日 */
    private Date startDate ;
    /** 合同到期日 */
    private Date expiryDate ;
    /** 贷款月数 */
    private Integer termMonth ;
    /** 还款周期 */
    private String paymentPeriod ;
    /** 还款方式 */
    private String paymentMode ;
    /** 放款账号 */
    private String loanAccountNo ;
    /** 放款账号开户行 */
    private String loanAccountIssuer ;
    /** 放款账号开户名 */
    private String loanAccountOwner ;
    /** 还款账号 */
    private String paybackAccountNo ;
    /** 还款账号开户名 */
    private String paybackAccountIssuer ;
    /** 还款账号开户名 */
    private String paybackAccountOwner ;
    /** 借据金额 */
    private Double duebillAmt ;
    /** 借据余额 */
    private Double duebillBalance ;
    /** 正常余额 */
    private Double normalBalance ;
    /** 逾期余额 */
    private Double overdueBalance ;
    /** 逾期天数 */
    private Integer overdueDays ;
    /** 贷款发放状态;0.未发放
     1.发放中
     2.发放成功
     4.发放失败 */
    private String leaseStatus ;
    /** 借据状态 */
    private String duebillStatus ;
    /** 负责人 */
    private String operator ;
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

    /** 借据号 */
    public String getDuebillId(){
        return this.duebillId;
    }
    /** 借据号 */
    public void setDuebillId(String duebillId){
        this.duebillId = duebillId;
    }
    /** 测算表ID */
    public String getLactLoanId(){
        return this.lactLoanId;
    }
    /** 测算表ID */
    public void setLactLoanId(String lactLoanId){
        this.lactLoanId = lactLoanId;
    }
    /** 合同号 */
    public String getContractId(){
        return this.contractId;
    }
    /** 合同号 */
    public void setContractId(String contractId){
        this.contractId = contractId;
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
    /** 产品ID */
    public String getPolicyId(){
        return this.policyId;
    }
    /** 产品ID */
    public void setPolicyId(String policyId){
        this.policyId = policyId;
    }
    /** 资金方ID */
    public String getFunderId(){
        return this.funderId;
    }
    /** 资金方ID */
    public void setFunderId(String funderId){
        this.funderId = funderId;
    }
    /** 执行年利率 */
    public Double getInterestRate(){
        return this.interestRate;
    }
    /** 执行年利率 */
    public void setInterestRate(Double interestRate){
        this.interestRate = interestRate;
    }
    /** 合同起始日 */
    public Date getStartDate(){
        return this.startDate;
    }
    /** 合同起始日 */
    public void setStartDate(Date startDate){
        this.startDate = startDate;
    }
    /** 合同到期日 */
    public Date getExpiryDate(){
        return this.expiryDate;
    }
    /** 合同到期日 */
    public void setExpiryDate(Date expiryDate){
        this.expiryDate = expiryDate;
    }
    /** 贷款月数 */
    public Integer getTermMonth(){
        return this.termMonth;
    }
    /** 贷款月数 */
    public void setTermMonth(Integer termMonth){
        this.termMonth = termMonth;
    }
    /** 还款周期 */
    public String getPaymentPeriod(){
        return this.paymentPeriod;
    }
    /** 还款周期 */
    public void setPaymentPeriod(String paymentPeriod){
        this.paymentPeriod = paymentPeriod;
    }
    /** 还款方式 */
    public String getPaymentMode(){
        return this.paymentMode;
    }
    /** 还款方式 */
    public void setPaymentMode(String paymentMode){
        this.paymentMode = paymentMode;
    }
    /** 放款账号 */
    public String getLoanAccountNo(){
        return this.loanAccountNo;
    }
    /** 放款账号 */
    public void setLoanAccountNo(String loanAccountNo){
        this.loanAccountNo = loanAccountNo;
    }
    /** 放款账号开户行 */
    public String getLoanAccountIssuer(){
        return this.loanAccountIssuer;
    }
    /** 放款账号开户行 */
    public void setLoanAccountIssuer(String loanAccountIssuer){
        this.loanAccountIssuer = loanAccountIssuer;
    }
    /** 放款账号开户名 */
    public String getLoanAccountOwner(){
        return this.loanAccountOwner;
    }
    /** 放款账号开户名 */
    public void setLoanAccountOwner(String loanAccountOwner){
        this.loanAccountOwner = loanAccountOwner;
    }
    /** 还款账号 */
    public String getPaybackAccountNo(){
        return this.paybackAccountNo;
    }
    /** 还款账号 */
    public void setPaybackAccountNo(String paybackAccountNo){
        this.paybackAccountNo = paybackAccountNo;
    }
    /** 还款账号开户名 */
    public String getPaybackAccountIssuer(){
        return this.paybackAccountIssuer;
    }
    /** 还款账号开户名 */
    public void setPaybackAccountIssuer(String paybackAccountIssuer){
        this.paybackAccountIssuer = paybackAccountIssuer;
    }
    /** 还款账号开户名 */
    public String getPaybackAccountOwner(){
        return this.paybackAccountOwner;
    }
    /** 还款账号开户名 */
    public void setPaybackAccountOwner(String paybackAccountOwner){
        this.paybackAccountOwner = paybackAccountOwner;
    }
    /** 借据金额 */
    public Double getDuebillAmt(){
        return this.duebillAmt;
    }
    /** 借据金额 */
    public void setDuebillAmt(Double duebillAmt){
        this.duebillAmt = duebillAmt;
    }
    /** 借据余额 */
    public Double getDuebillBalance(){
        return this.duebillBalance;
    }
    /** 借据余额 */
    public void setDuebillBalance(Double duebillBalance){
        this.duebillBalance = duebillBalance;
    }
    /** 正常余额 */
    public Double getNormalBalance(){
        return this.normalBalance;
    }
    /** 正常余额 */
    public void setNormalBalance(Double normalBalance){
        this.normalBalance = normalBalance;
    }
    /** 逾期余额 */
    public Double getOverdueBalance(){
        return this.overdueBalance;
    }
    /** 逾期余额 */
    public void setOverdueBalance(Double overdueBalance){
        this.overdueBalance = overdueBalance;
    }
    /** 逾期天数 */
    public Integer getOverdueDays(){
        return this.overdueDays;
    }
    /** 逾期天数 */
    public void setOverdueDays(Integer overdueDays){
        this.overdueDays = overdueDays;
    }
    /** 贷款发放状态;0.未发放
     1.发放中
     2.发放成功
     4.发放失败 */
    public String getLeaseStatus(){
        return this.leaseStatus;
    }
    /** 贷款发放状态;0.未发放
     1.发放中
     2.发放成功
     4.发放失败 */
    public void setLeaseStatus(String leaseStatus){
        this.leaseStatus = leaseStatus;
    }
    /** 借据状态 */
    public String getDuebillStatus(){
        return this.duebillStatus;
    }
    /** 借据状态 */
    public void setDuebillStatus(String duebillStatus){
        this.duebillStatus = duebillStatus;
    }
    /** 负责人 */
    public String getOperator(){
        return this.operator;
    }
    /** 负责人 */
    public void setOperator(String operator){
        this.operator = operator;
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