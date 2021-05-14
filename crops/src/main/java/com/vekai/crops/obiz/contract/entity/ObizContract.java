package com.vekai.crops.obiz.contract.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="OBIZ_CONTRACT")
public class ObizContract implements Serializable,Cloneable{
    /** 合同号 */
    @Id
    @GeneratedValue
    private String contractId ;
    /** 业务编号 */
    private String businessNo ;
    /** 申请号 */
    private String applicationId ;
    /** 测算表号 */
    private String lactLoanId ;
    /** 客户号 */
    private String custId ;
    /** 客户名 */
    private String custName ;
    /** 额度号 */
    private String limitId ;
    /** 产品ID */
    private String policyId ;
    /** 资金方ID */
    private String funderId ;
    /** 合同金额 */
    private Double contractAmt ;
    /** 合同余额 */
    private Double contractBalance ;
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
    /** 服务费 */
    private Double serviceFee ;
    /** 主贷人银行卡号 */
    private String bankCardNo ;
    /** 开户行 */
    private String bankCardIssuer ;
    /** 开户名 */
    private String bankCardOwner ;
    /** 负责人 */
    private String operator ;
    /** 部门经理 */
    private String leader ;
    /** 合同状态 */
    private String contractStatus ;
    /** 业务备注 */
    private String businessNote ;
    /** 业务来源;公司，自有，经销商，保险公司，其他 */
    private String channelFrom ;
    /** 合同流程 */
    private String workflowKey ;
    /** 显示模板 */
    private String dataformId ;
    /** 调查报告模板 */
    private String inquireDocDefKey ;
    /** 调查报告ID */
    private String inquireDocId ;
    /** 合同文本模板 */
    private String contractDocDefKey ;
    /** 合同文本ID */
    private String contractDocId ;
    /** 业务资料清单 */
    private String dossierDefKey ;
    /** 业务资料ID */
    private String dossierId ;
    /** 里程碑定义KEY */
    private String landmarkDefKey ;
    /** 里程碑实例 */
    private String landmarkId ;
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

    /** 合同号 */
    public String getContractId(){
        return this.contractId;
    }
    /** 合同号 */
    public void setContractId(String contractId){
        this.contractId = contractId;
    }
    /** 业务编号 */
    public String getBusinessNo(){
        return this.businessNo;
    }
    /** 业务编号 */
    public void setBusinessNo(String businessNo){
        this.businessNo = businessNo;
    }
    /** 申请号 */
    public String getApplicationId(){
        return this.applicationId;
    }
    /** 申请号 */
    public void setApplicationId(String applicationId){
        this.applicationId = applicationId;
    }
    /** 测算表号 */
    public String getLactLoanId(){
        return this.lactLoanId;
    }
    /** 测算表号 */
    public void setLactLoanId(String lactLoanId){
        this.lactLoanId = lactLoanId;
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
    /** 额度号 */
    public String getLimitId(){
        return this.limitId;
    }
    /** 额度号 */
    public void setLimitId(String limitId){
        this.limitId = limitId;
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
    /** 合同金额 */
    public Double getContractAmt(){
        return this.contractAmt;
    }
    /** 合同金额 */
    public void setContractAmt(Double contractAmt){
        this.contractAmt = contractAmt;
    }
    /** 合同余额 */
    public Double getContractBalance(){
        return this.contractBalance;
    }
    /** 合同余额 */
    public void setContractBalance(Double contractBalance){
        this.contractBalance = contractBalance;
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
    /** 服务费 */
    public Double getServiceFee(){
        return this.serviceFee;
    }
    /** 服务费 */
    public void setServiceFee(Double serviceFee){
        this.serviceFee = serviceFee;
    }
    /** 主贷人银行卡号 */
    public String getBankCardNo(){
        return this.bankCardNo;
    }
    /** 主贷人银行卡号 */
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
    /** 负责人 */
    public String getOperator(){
        return this.operator;
    }
    /** 负责人 */
    public void setOperator(String operator){
        this.operator = operator;
    }
    /** 部门经理 */
    public String getLeader(){
        return this.leader;
    }
    /** 部门经理 */
    public void setLeader(String leader){
        this.leader = leader;
    }
    /** 合同状态 */
    public String getContractStatus(){
        return this.contractStatus;
    }
    /** 合同状态 */
    public void setContractStatus(String contractStatus){
        this.contractStatus = contractStatus;
    }
    /** 业务备注 */
    public String getBusinessNote(){
        return this.businessNote;
    }
    /** 业务备注 */
    public void setBusinessNote(String businessNote){
        this.businessNote = businessNote;
    }
    /** 业务来源;公司，自有，经销商，保险公司，其他 */
    public String getChannelFrom(){
        return this.channelFrom;
    }
    /** 业务来源;公司，自有，经销商，保险公司，其他 */
    public void setChannelFrom(String channelFrom){
        this.channelFrom = channelFrom;
    }
    /** 合同流程 */
    public String getWorkflowKey(){
        return this.workflowKey;
    }
    /** 合同流程 */
    public void setWorkflowKey(String workflowKey){
        this.workflowKey = workflowKey;
    }
    /** 显示模板 */
    public String getDataformId(){
        return this.dataformId;
    }
    /** 显示模板 */
    public void setDataformId(String dataformId){
        this.dataformId = dataformId;
    }
    /** 调查报告模板 */
    public String getInquireDocDefKey(){
        return this.inquireDocDefKey;
    }
    /** 调查报告模板 */
    public void setInquireDocDefKey(String inquireDocDefKey){
        this.inquireDocDefKey = inquireDocDefKey;
    }
    /** 调查报告ID */
    public String getInquireDocId(){
        return this.inquireDocId;
    }
    /** 调查报告ID */
    public void setInquireDocId(String inquireDocId){
        this.inquireDocId = inquireDocId;
    }
    /** 合同文本模板 */
    public String getContractDocDefKey(){
        return this.contractDocDefKey;
    }
    /** 合同文本模板 */
    public void setContractDocDefKey(String contractDocDefKey){
        this.contractDocDefKey = contractDocDefKey;
    }
    /** 合同文本ID */
    public String getContractDocId(){
        return this.contractDocId;
    }
    /** 合同文本ID */
    public void setContractDocId(String contractDocId){
        this.contractDocId = contractDocId;
    }
    /** 业务资料清单 */
    public String getDossierDefKey(){
        return this.dossierDefKey;
    }
    /** 业务资料清单 */
    public void setDossierDefKey(String dossierDefKey){
        this.dossierDefKey = dossierDefKey;
    }
    /** 业务资料ID */
    public String getDossierId(){
        return this.dossierId;
    }
    /** 业务资料ID */
    public void setDossierId(String dossierId){
        this.dossierId = dossierId;
    }
    /** 里程碑定义KEY */
    public String getLandmarkDefKey(){
        return this.landmarkDefKey;
    }
    /** 里程碑定义KEY */
    public void setLandmarkDefKey(String landmarkDefKey){
        this.landmarkDefKey = landmarkDefKey;
    }
    /** 里程碑实例 */
    public String getLandmarkId(){
        return this.landmarkId;
    }
    /** 里程碑实例 */
    public void setLandmarkId(String landmarkId){
        this.landmarkId = landmarkId;
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