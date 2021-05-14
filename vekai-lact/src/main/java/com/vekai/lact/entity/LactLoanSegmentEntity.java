package com.vekai.lact.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 债权核算分段对象
 */
@Table(name="LACT_LOAN_SEGMENT")
public class LactLoanSegmentEntity implements Serializable,Cloneable{
    /** 测算表ID */
    private String loanId ;
    /** 分段ID */
    @Id
    @GeneratedValue
    private String loanSegmentId ;
    /** 分段顺序号 */
    private Integer sortNo ;
    /** 分段类型 */
    private String segmentType ;
    /** 利率表现形式;Indication:明示利率，Implication:阴含利率 */
    private String rateAppearMode ;
    /** 还款方式;EqualLoan:等额本息,EqualPrincipal: 等额本金,DeterminatePrincipal:确定本金,ImplicationCustomize:隐含利率 */
    private String paymentMode ;
    /** 计息方式;D:按日,M:按月 */
    private String interestCalcMode ;
    /** 还款周期;W:周,TW:双周,M:月,TM:双月,Q:季,FM:四个月,HY:半年,Y:年 */
    private String paymentPeriod ;
    /** 开始日期 */
    private Date startDate ;
    /** 结束日期 */
    private Date endDate ;
    /** 期限月 */
    private Integer termMonth ;
    /** 期限零头天数 */
    private Integer termDay ;
    /** 零头天计算方式 */
    private String oddDayInterestType ;
    /** 本段债权金额 */
    private Double segmentLoanAmt ;
    /** 每期还本金;等额本金法才用 */
    private Double perTermPrincipal ;
    /** 每期还款金额;等额本息法才用 */
    private Double perTermPaymentAmt ;
    /** 本段总月数 */
    private Integer segmentMonths ;
    /** 本段总期数 */
    private Integer segmentTerms ;
    /** 本段还款总额 */
    private Double totalPaymentAmt ;
    /** 本段利息总额 */
    private Double totalInterestAmt ;
    /** 利率方式;代码:InterestRateMode: Float-浮动利率，Fixed-固定利率 */
    private String rateMode ;
    /** 基准利率类型 */
    private String baseRateType ;
    /** 基准利率档次 */
    private String baseRateGrade ;
    /** 基准利率 */
    private Double baseRate ;
    /** 利率浮动类型 */
    private String rateFloatType ;
    /** 利率浮动值 */
    private Double rateFloat ;
    /** 实际执行利率 */
    private Double interestRate ;
    /** 调息方式 */
    private String interestAdjustMode ;
    /** 调起起始说明 */
    private String interestAdjustStartType ;
    /** 罚息利率类型 */
    private String penalRateType ;
    /** 罚息利率浮动类型 */
    private String penalRateFloatType ;
    /** 罚息利率浮动值 */
    private Double penalRateFloat ;
    /** 罚息执行利率 */
    private Double penalInterestRate ;
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
    /** 分段顺序号 */
    public Integer getSortNo(){
        return this.sortNo;
    }
    /** 分段顺序号 */
    public void setSortNo(Integer sortNo){
        this.sortNo = sortNo;
    }
    /** 分段类型 */
    public String getSegmentType(){
        return this.segmentType;
    }
    /** 分段类型 */
    public void setSegmentType(String segmentType){
        this.segmentType = segmentType;
    }
    /** 利率表现形式;Indication:明示利率，Implication:阴含利率 */
    public String getRateAppearMode(){
        return this.rateAppearMode;
    }
    /** 利率表现形式;Indication:明示利率，Implication:阴含利率 */
    public void setRateAppearMode(String rateAppearMode){
        this.rateAppearMode = rateAppearMode;
    }
    /** 还款方式;EqualLoan:等额本息,EqualPrincipal: 等额本金,DeterminatePrincipal:确定本金,ImplicationCustomize:隐含利率 */
    public String getPaymentMode(){
        return this.paymentMode;
    }
    /** 还款方式;EqualLoan:等额本息,EqualPrincipal: 等额本金,DeterminatePrincipal:确定本金,ImplicationCustomize:隐含利率 */
    public void setPaymentMode(String paymentMode){
        this.paymentMode = paymentMode;
    }
    /** 计息方式;D:按日,M:按月 */
    public String getInterestCalcMode(){
        return this.interestCalcMode;
    }
    /** 计息方式;D:按日,M:按月 */
    public void setInterestCalcMode(String interestCalcMode){
        this.interestCalcMode = interestCalcMode;
    }
    /** 还款周期;W:周,TW:双周,M:月,TM:双月,Q:季,FM:四个月,HY:半年,Y:年 */
    public String getPaymentPeriod(){
        return this.paymentPeriod;
    }
    /** 还款周期;W:周,TW:双周,M:月,TM:双月,Q:季,FM:四个月,HY:半年,Y:年 */
    public void setPaymentPeriod(String paymentPeriod){
        this.paymentPeriod = paymentPeriod;
    }
    /** 开始日期 */
    public Date getStartDate(){
        return this.startDate;
    }
    /** 开始日期 */
    public void setStartDate(Date startDate){
        this.startDate = startDate;
    }
    /** 结束日期 */
    public Date getEndDate(){
        return this.endDate;
    }
    /** 结束日期 */
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }
    /** 期限月 */
    public Integer getTermMonth(){
        return this.termMonth;
    }
    /** 期限月 */
    public void setTermMonth(Integer termMonth){
        this.termMonth = termMonth;
    }
    /** 期限零头天数 */
    public Integer getTermDay(){
        return this.termDay;
    }
    /** 期限零头天数 */
    public void setTermDay(Integer termDay){
        this.termDay = termDay;
    }
    /** 零头天计算方式 */
    public String getOddDayInterestType(){
        return this.oddDayInterestType;
    }
    /** 零头天计算方式 */
    public void setOddDayInterestType(String oddDayInterestType){
        this.oddDayInterestType = oddDayInterestType;
    }
    /** 本段债权金额 */
    public Double getSegmentLoanAmt(){
        return this.segmentLoanAmt;
    }
    /** 本段债权金额 */
    public void setSegmentLoanAmt(Double segmentLoanAmt){
        this.segmentLoanAmt = segmentLoanAmt;
    }
    /** 每期还本金;等额本金法才用 */
    public Double getPerTermPrincipal(){
        return this.perTermPrincipal;
    }
    /** 每期还本金;等额本金法才用 */
    public void setPerTermPrincipal(Double perTermPrincipal){
        this.perTermPrincipal = perTermPrincipal;
    }
    /** 每期还款金额;等额本息法才用 */
    public Double getPerTermPaymentAmt(){
        return this.perTermPaymentAmt;
    }
    /** 每期还款金额;等额本息法才用 */
    public void setPerTermPaymentAmt(Double perTermPaymentAmt){
        this.perTermPaymentAmt = perTermPaymentAmt;
    }
    /** 本段总月数 */
    public Integer getSegmentMonths(){
        return this.segmentMonths;
    }
    /** 本段总月数 */
    public void setSegmentMonths(Integer segmentMonths){
        this.segmentMonths = segmentMonths;
    }
    /** 本段总期数 */
    public Integer getSegmentTerms(){
        return this.segmentTerms;
    }
    /** 本段总期数 */
    public void setSegmentTerms(Integer segmentTerms){
        this.segmentTerms = segmentTerms;
    }
    /** 本段还款总额 */
    public Double getTotalPaymentAmt(){
        return this.totalPaymentAmt;
    }
    /** 本段还款总额 */
    public void setTotalPaymentAmt(Double totalPaymentAmt){
        this.totalPaymentAmt = totalPaymentAmt;
    }
    /** 本段利息总额 */
    public Double getTotalInterestAmt(){
        return this.totalInterestAmt;
    }
    /** 本段利息总额 */
    public void setTotalInterestAmt(Double totalInterestAmt){
        this.totalInterestAmt = totalInterestAmt;
    }
    /** 利率方式;代码:InterestRateMode: Float-浮动利率，Fixed-固定利率 */
    public String getRateMode(){
        return this.rateMode;
    }
    /** 利率方式;代码:InterestRateMode: Float-浮动利率，Fixed-固定利率 */
    public void setRateMode(String rateMode){
        this.rateMode = rateMode;
    }
    /** 基准利率类型 */
    public String getBaseRateType(){
        return this.baseRateType;
    }
    /** 基准利率类型 */
    public void setBaseRateType(String baseRateType){
        this.baseRateType = baseRateType;
    }
    /** 基准利率档次 */
    public String getBaseRateGrade(){
        return this.baseRateGrade;
    }
    /** 基准利率档次 */
    public void setBaseRateGrade(String baseRateGrade){
        this.baseRateGrade = baseRateGrade;
    }
    /** 基准利率 */
    public Double getBaseRate(){
        return this.baseRate;
    }
    /** 基准利率 */
    public void setBaseRate(Double baseRate){
        this.baseRate = baseRate;
    }
    /** 利率浮动类型 */
    public String getRateFloatType(){
        return this.rateFloatType;
    }
    /** 利率浮动类型 */
    public void setRateFloatType(String rateFloatType){
        this.rateFloatType = rateFloatType;
    }
    /** 利率浮动值 */
    public Double getRateFloat(){
        return this.rateFloat;
    }
    /** 利率浮动值 */
    public void setRateFloat(Double rateFloat){
        this.rateFloat = rateFloat;
    }
    /** 实际执行利率 */
    public Double getInterestRate(){
        return this.interestRate;
    }
    /** 实际执行利率 */
    public void setInterestRate(Double interestRate){
        this.interestRate = interestRate;
    }
    /** 调息方式 */
    public String getInterestAdjustMode(){
        return this.interestAdjustMode;
    }
    /** 调息方式 */
    public void setInterestAdjustMode(String interestAdjustMode){
        this.interestAdjustMode = interestAdjustMode;
    }
    /** 调起起始说明 */
    public String getInterestAdjustStartType(){
        return this.interestAdjustStartType;
    }
    /** 调起起始说明 */
    public void setInterestAdjustStartType(String interestAdjustStartType){
        this.interestAdjustStartType = interestAdjustStartType;
    }
    /** 罚息利率类型 */
    public String getPenalRateType(){
        return this.penalRateType;
    }
    /** 罚息利率类型 */
    public void setPenalRateType(String penalRateType){
        this.penalRateType = penalRateType;
    }
    /** 罚息利率浮动类型 */
    public String getPenalRateFloatType(){
        return this.penalRateFloatType;
    }
    /** 罚息利率浮动类型 */
    public void setPenalRateFloatType(String penalRateFloatType){
        this.penalRateFloatType = penalRateFloatType;
    }
    /** 罚息利率浮动值 */
    public Double getPenalRateFloat(){
        return this.penalRateFloat;
    }
    /** 罚息利率浮动值 */
    public void setPenalRateFloat(Double penalRateFloat){
        this.penalRateFloat = penalRateFloat;
    }
    /** 罚息执行利率 */
    public Double getPenalInterestRate(){
        return this.penalInterestRate;
    }
    /** 罚息执行利率 */
    public void setPenalInterestRate(Double penalInterestRate){
        this.penalInterestRate = penalInterestRate;
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
