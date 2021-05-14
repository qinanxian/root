package com.vekai.crops.obiz.application.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="OBIZ_APPLICATION")
public class ObizApplication implements Serializable,Cloneable{
    /** 申请号 */
    @Id
    @GeneratedValue
    private String applicationId ;
    /** 业务编号 */
    private String businessNo ;
    /** 主贷人客户号 */
    private String custId ;
    /** 主贷人客户名 */
    private String custName ;
    /** 产品ID */
    private String policyId ;
    /** 额度号 */
    private String limitId ;
    /** 资金方ID */
    private String funderId ;
    /** 主贷人手机号 */
    private String custCellphone ;
    /** 主贷人证件类型 */
    private String custCertType ;
    /** 主贷人证件号 */
    private String custCertId ;
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
    /** 业务备注 */
    private String appNote ;
    /** 业务来源;公司，自有，经销商，保险公司，其他 */
    private String channelFrom ;
    /** 业务发生地 */
    private String occurLocation ;
    /** 申请金额 */
    private Double applyAmt ;
    /** 执行年利率 */
    private String interestRate ;
    /** 贷款月数 */
    private String termMonth ;
    /** 服务费 */
    private String serviceFee ;
    /** 还款周期 */
    private String paymentPeriod ;
    /** 还款方式 */
    private String paymentMode ;
    /** 业务阶段里程 */
    private String appMilestone ;
    /** 业务状态 */
    private String appStatus ;
    /** 申请人 */
    private String proposer ;
    /** 申请流程 */
    private String workflowKey ;
    /** 显示模板 */
    private String dataformId ;
    /** 调查报告模板 */
    private String inquireDocDefKey ;
    /** 调查报告ID */
    private String inquireDocId ;
    /** 合同文本模板 */
    private String contractDocDefKey ;
    /** 业务资料清单模板 */
    private String dossierDefKey ;
    /** 业务资料清单ID */
    private String dossierId ;
    /** 里程碑定义KEY */
    private String landmarkDefKey ;
    /** 里程碑实例 */
    private String landmarkId ;
    /** 风控规则模型 */
    private String riskManageModelKey ;
    /** 是否草稿 */
    private String isDraft ;
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
    /** 主贷人客户号 */
    public String getCustId(){
        return this.custId;
    }
    /** 主贷人客户号 */
    public void setCustId(String custId){
        this.custId = custId;
    }
    /** 主贷人客户名 */
    public String getCustName(){
        return this.custName;
    }
    /** 主贷人客户名 */
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
    /** 额度号 */
    public String getLimitId(){
        return this.limitId;
    }
    /** 额度号 */
    public void setLimitId(String limitId){
        this.limitId = limitId;
    }
    /** 资金方ID */
    public String getFunderId(){
        return this.funderId;
    }
    /** 资金方ID */
    public void setFunderId(String funderId){
        this.funderId = funderId;
    }
    /** 主贷人手机号 */
    public String getCustCellphone(){
        return this.custCellphone;
    }
    /** 主贷人手机号 */
    public void setCustCellphone(String custCellphone){
        this.custCellphone = custCellphone;
    }
    /** 主贷人证件类型 */
    public String getCustCertType(){
        return this.custCertType;
    }
    /** 主贷人证件类型 */
    public void setCustCertType(String custCertType){
        this.custCertType = custCertType;
    }
    /** 主贷人证件号 */
    public String getCustCertId(){
        return this.custCertId;
    }
    /** 主贷人证件号 */
    public void setCustCertId(String custCertId){
        this.custCertId = custCertId;
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
    /** 业务备注 */
    public String getAppNote(){
        return this.appNote;
    }
    /** 业务备注 */
    public void setAppNote(String appNote){
        this.appNote = appNote;
    }
    /** 业务来源;公司，自有，经销商，保险公司，其他 */
    public String getChannelFrom(){
        return this.channelFrom;
    }
    /** 业务来源;公司，自有，经销商，保险公司，其他 */
    public void setChannelFrom(String channelFrom){
        this.channelFrom = channelFrom;
    }
    /** 业务发生地 */
    public String getOccurLocation(){
        return this.occurLocation;
    }
    /** 业务发生地 */
    public void setOccurLocation(String occurLocation){
        this.occurLocation = occurLocation;
    }
    /** 申请金额 */
    public Double getApplyAmt(){
        return this.applyAmt;
    }
    /** 申请金额 */
    public void setApplyAmt(Double applyAmt){
        this.applyAmt = applyAmt;
    }
    /** 执行年利率 */
    public String getInterestRate(){
        return this.interestRate;
    }
    /** 执行年利率 */
    public void setInterestRate(String interestRate){
        this.interestRate = interestRate;
    }
    /** 贷款月数 */
    public String getTermMonth(){
        return this.termMonth;
    }
    /** 贷款月数 */
    public void setTermMonth(String termMonth){
        this.termMonth = termMonth;
    }
    /** 服务费 */
    public String getServiceFee(){
        return this.serviceFee;
    }
    /** 服务费 */
    public void setServiceFee(String serviceFee){
        this.serviceFee = serviceFee;
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
    /** 业务阶段里程 */
    public String getAppMilestone(){
        return this.appMilestone;
    }
    /** 业务阶段里程 */
    public void setAppMilestone(String appMilestone){
        this.appMilestone = appMilestone;
    }
    /** 业务状态 */
    public String getAppStatus(){
        return this.appStatus;
    }
    /** 业务状态 */
    public void setAppStatus(String appStatus){
        this.appStatus = appStatus;
    }
    /** 申请人 */
    public String getProposer(){
        return this.proposer;
    }
    /** 申请人 */
    public void setProposer(String proposer){
        this.proposer = proposer;
    }
    /** 申请流程 */
    public String getWorkflowKey(){
        return this.workflowKey;
    }
    /** 申请流程 */
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
    /** 业务资料清单模板 */
    public String getDossierDefKey(){
        return this.dossierDefKey;
    }
    /** 业务资料清单模板 */
    public void setDossierDefKey(String dossierDefKey){
        this.dossierDefKey = dossierDefKey;
    }
    /** 业务资料清单ID */
    public String getDossierId(){
        return this.dossierId;
    }
    /** 业务资料清单ID */
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
    /** 风控规则模型 */
    public String getRiskManageModelKey(){
        return this.riskManageModelKey;
    }
    /** 风控规则模型 */
    public void setRiskManageModelKey(String riskManageModelKey){
        this.riskManageModelKey = riskManageModelKey;
    }
    /** 是否草稿 */
    public String getIsDraft(){
        return this.isDraft;
    }
    /** 是否草稿 */
    public void setIsDraft(String isDraft){
        this.isDraft = isDraft;
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