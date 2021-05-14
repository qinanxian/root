package com.vekai.lact.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name="LACT_LOAN")
public class LactLoanEntity implements Serializable,Cloneable{
    /** 测算表ID */
    @Id
    @GeneratedValue
    private String loanId ;
    /** 测算表文本号 */
    private String loanCode ;
    /** 测算表版本号 */
    private String loanRevision ;
    /** 简要说明 */
    private String intro ;
    /** 段数 */
    private Integer segments ;
    /** 币种 */
    private String currency ;
    /** 总金额 */
    private Double totalAmt ;
    /** 首付金额 */
    private Double firstPayAmt ;
    /** 债权金额 */
    private Double loanAmt ;
    /** 期限;原始期限 */
    private Integer terms ;
    /** 期限月 */
    private Integer termMonth ;
    /** 零头天数 */
    private Integer termDay ;
    /** 开始日 */
    private Date startDate ;
    /** 到期日 */
    private Date expiryDate ;
    /** 付款时点;Prepay:先付,Postpay:后付 */
    private String payPointTime ;
    /** 余值 */
    private Double residualAmt ;
    /** 余值是否计息 */
    private String residualGenInterest ;
    /** 预计XIRR */
    private Double predictXirr ;
    /** 实际XIRR */
    private Double actualXirr ;
    /** 总资金占用 */
    private Double capitalTotalCapture ;
    /** 还款总额 */
    private Double totalPaymentAmt ;
    /** 利息总额 */
    private Double totalInterestAmt ;
    /** 存续占用 */
    private Double durationOccuption ;
    /** 累计存续占用 */
    private Double durationTotalOccupation ;
    /** 存续收入 */
    private Double durationIncome ;
    /** 累计存续收入 */
    private Double durationTotalIncome ;
    /** 年基础天数;360，365，366，0（实际天数） */
    private Integer yearDays ;
    /** 总还款次数 */
    private Integer totalPaymentTimes ;
    /** 年还款次数;等期情况下才使用 */
    private Integer yearPaymentTimes ;
    /** 是否设置宽限期 */
    private String graceExist ;
    /** 锁定期数 */
    private Integer paidTerms;
    /** 结息当日是否包含;默认:Y */
    private String interestSettleDateContain ;
    /** 结息日;默认21号 */
    private Integer interestSettleDay ;
    /** 还本频率 */
    private String payPrincipalPeriod ;
    /** 还本时点;代码：PayTimePoint FixedDay-指定日，MonthEnd-月末 */
    private String payPrincipalPoint ;
    /** 还本日 */
    private Integer payPrincipalDay ;
    /** 还息频率 */
    private String payInterestPeriod ;
    /** 还息时点;代码：PayTimePoint FixedDay-指定日，MonthEnd-月末 */
    private String payInterestPoint ;
    /** 还息日 */
    private Integer payInterestDay ;
    /** 还本息是否一致;包含还本息频率，时点，天是否都一致 */
    private String payPrincipalInterestSync;
    /** 还款时点;本息一致时，使用 */
    private String payPoint ;
    /** 还款日;本息一致时，使用 */
    private Integer payFixedDay ;
    /** 日期头尾计算方式;HeadTailContain: 10-算头不算尾，01-算尾不算头，11-算头又算尾 ，00-不算头不算尾 */
    private String headTailContain ;
    /** 还款日节假日处理方式;代码:HolidayTreatment Delay-顺延，Pre-提前，Ignore-不变 */
    private String payHolidayTreat ;
    /** 结息日节假日处理方式 */
    private String interestSettleHolidayTreat ;
    /** 已还本金 */
    private Double paidPrincipal ;
    /** 已还金额 */
    private Double paidTotalAmt ;
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
    /** 测算表文本号 */
    public String getLoanCode(){
        return this.loanCode;
    }
    /** 测算表文本号 */
    public void setLoanCode(String loanCode){
        this.loanCode = loanCode;
    }
    /** 测算表版本号 */
    public String getLoanRevision(){
        return this.loanRevision;
    }
    /** 测算表版本号 */
    public void setLoanRevision(String loanRevision){
        this.loanRevision = loanRevision;
    }
    /** 简要说明 */
    public String getIntro(){
        return this.intro;
    }
    /** 简要说明 */
    public void setIntro(String intro){
        this.intro = intro;
    }
    /** 段数 */
    public Integer getSegments(){
        return this.segments;
    }
    /** 段数 */
    public void setSegments(Integer segments){
        this.segments = segments;
    }
    /** 币种 */
    public String getCurrency(){
        return this.currency;
    }
    /** 币种 */
    public void setCurrency(String currency){
        this.currency = currency;
    }
    /** 总金额 */
    public Double getTotalAmt(){
        return this.totalAmt;
    }
    /** 总金额 */
    public void setTotalAmt(Double totalAmt){
        this.totalAmt = totalAmt;
    }
    /** 首付金额 */
    public Double getFirstPayAmt(){
        return this.firstPayAmt;
    }
    /** 首付金额 */
    public void setFirstPayAmt(Double firstPayAmt){
        this.firstPayAmt = firstPayAmt;
    }
    /** 债权金额 */
    public Double getLoanAmt(){
        return this.loanAmt;
    }
    /** 债权金额 */
    public void setLoanAmt(Double loanAmt){
        this.loanAmt = loanAmt;
    }
    /** 期限;原始期限 */
    public Integer getTerms(){
        return this.terms;
    }
    /** 期限;原始期限 */
    public void setTerms(Integer terms){
        this.terms = terms;
    }
    /** 期限月 */
    public Integer getTermMonth(){
        return this.termMonth;
    }
    /** 期限月 */
    public void setTermMonth(Integer termMonth){
        this.termMonth = termMonth;
    }
    /** 零头天数 */
    public Integer getTermDay(){
        return this.termDay;
    }
    /** 零头天数 */
    public void setTermDay(Integer termDay){
        this.termDay = termDay;
    }
    /** 开始日 */
    public Date getStartDate(){
        return this.startDate;
    }
    /** 开始日 */
    public void setStartDate(Date startDate){
        this.startDate = startDate;
    }
    /** 到期日 */
    public Date getExpiryDate(){
        return this.expiryDate;
    }
    /** 到期日 */
    public void setExpiryDate(Date expiryDate){
        this.expiryDate = expiryDate;
    }
    /** 付款时点;Prepay:先付,Postpay:后付 */
    public String getPayPointTime(){
        return this.payPointTime;
    }
    /** 付款时点;Prepay:先付,Postpay:后付 */
    public void setPayPointTime(String payPointTime){
        this.payPointTime = payPointTime;
    }
    /** 余值 */
    public Double getResidualAmt(){
        return this.residualAmt;
    }
    /** 余值 */
    public void setResidualAmt(Double residualAmt){
        this.residualAmt = residualAmt;
    }
    /** 余值是否计息 */
    public String getResidualGenInterest(){
        return this.residualGenInterest;
    }
    /** 余值是否计息 */
    public void setResidualGenInterest(String residualGenInterest){
        this.residualGenInterest = residualGenInterest;
    }
    /** 预计XIRR */
    public Double getPredictXirr(){
        return this.predictXirr;
    }
    /** 预计XIRR */
    public void setPredictXirr(Double predictXirr){
        this.predictXirr = predictXirr;
    }
    /** 实际XIRR */
    public Double getActualXirr(){
        return this.actualXirr;
    }
    /** 实际XIRR */
    public void setActualXirr(Double actualXirr){
        this.actualXirr = actualXirr;
    }
    /** 总资金占用 */
    public Double getCapitalTotalCapture(){
        return this.capitalTotalCapture;
    }
    /** 总资金占用 */
    public void setCapitalTotalCapture(Double capitalTotalCapture){
        this.capitalTotalCapture = capitalTotalCapture;
    }
    /** 还款总额 */
    public Double getTotalPaymentAmt(){
        return this.totalPaymentAmt;
    }
    /** 还款总额 */
    public void setTotalPaymentAmt(Double totalPaymentAmt){
        this.totalPaymentAmt = totalPaymentAmt;
    }
    /** 利息总额 */
    public Double getTotalInterestAmt(){
        return this.totalInterestAmt;
    }
    /** 利息总额 */
    public void setTotalInterestAmt(Double totalInterestAmt){
        this.totalInterestAmt = totalInterestAmt;
    }
    /** 存续占用 */
    public Double getDurationOccuption(){
        return this.durationOccuption;
    }
    /** 存续占用 */
    public void setDurationOccuption(Double durationOccuption){
        this.durationOccuption = durationOccuption;
    }
    /** 累计存续占用 */
    public Double getDurationTotalOccupation(){
        return this.durationTotalOccupation;
    }
    /** 累计存续占用 */
    public void setDurationTotalOccupation(Double durationTotalOccupation){
        this.durationTotalOccupation = durationTotalOccupation;
    }
    /** 存续收入 */
    public Double getDurationIncome(){
        return this.durationIncome;
    }
    /** 存续收入 */
    public void setDurationIncome(Double durationIncome){
        this.durationIncome = durationIncome;
    }
    /** 累计存续收入 */
    public Double getDurationTotalIncome(){
        return this.durationTotalIncome;
    }
    /** 累计存续收入 */
    public void setDurationTotalIncome(Double durationTotalIncome){
        this.durationTotalIncome = durationTotalIncome;
    }
    /** 年基础天数;360，365，366，0（实际天数） */
    public Integer getYearDays(){
        return this.yearDays;
    }
    /** 年基础天数;360，365，366，0（实际天数） */
    public void setYearDays(Integer yearDays){
        this.yearDays = yearDays;
    }
    /** 总还款次数 */
    public Integer getTotalPaymentTimes(){
        return this.totalPaymentTimes;
    }
    /** 总还款次数 */
    public void setTotalPaymentTimes(Integer totalPaymentTimes){
        this.totalPaymentTimes = totalPaymentTimes;
    }
    /** 年还款次数;等期情况下才使用 */
    public Integer getYearPaymentTimes(){
        return this.yearPaymentTimes;
    }
    /** 年还款次数;等期情况下才使用 */
    public void setYearPaymentTimes(Integer yearPaymentTimes){
        this.yearPaymentTimes = yearPaymentTimes;
    }
    /** 是否设置宽限期 */
    public String getGraceExist(){
        return this.graceExist;
    }
    /** 是否设置宽限期 */
    public void setGraceExist(String graceExist){
        this.graceExist = graceExist;
    }
    /** 锁定期数 */
    public Integer getPaidTerms(){
        return this.paidTerms;
    }
    /** 锁定期数 */
    public void setPaidTerms(Integer paidTerms){
        this.paidTerms = paidTerms;
    }
    /** 结息当日是否包含;默认:Y */
    public String getInterestSettleDateContain(){
        return this.interestSettleDateContain;
    }
    /** 结息当日是否包含;默认:Y */
    public void setInterestSettleDateContain(String interestSettleDateContain){
        this.interestSettleDateContain = interestSettleDateContain;
    }
    /** 结息日;默认21号 */
    public Integer getInterestSettleDay(){
        return this.interestSettleDay;
    }
    /** 结息日;默认21号 */
    public void setInterestSettleDay(Integer interestSettleDay){
        this.interestSettleDay = interestSettleDay;
    }
    /** 还本频率 */
    public String getPayPrincipalPeriod(){
        return this.payPrincipalPeriod;
    }
    /** 还本频率 */
    public void setPayPrincipalPeriod(String payPrincipalPeriod){
        this.payPrincipalPeriod = payPrincipalPeriod;
    }
    /** 还本时点;代码：PayTimePoint FixedDay-指定日，MonthEnd-月末 */
    public String getPayPrincipalPoint(){
        return this.payPrincipalPoint;
    }
    /** 还本时点;代码：PayTimePoint FixedDay-指定日，MonthEnd-月末 */
    public void setPayPrincipalPoint(String payPrincipalPoint){
        this.payPrincipalPoint = payPrincipalPoint;
    }
    /** 还本日 */
    public Integer getPayPrincipalDay(){
        return this.payPrincipalDay;
    }
    /** 还本日 */
    public void setPayPrincipalDay(Integer payPrincipalDay){
        this.payPrincipalDay = payPrincipalDay;
    }
    /** 还息频率 */
    public String getPayInterestPeriod(){
        return this.payInterestPeriod;
    }
    /** 还息频率 */
    public void setPayInterestPeriod(String payInterestPeriod){
        this.payInterestPeriod = payInterestPeriod;
    }
    /** 还息时点;代码：PayTimePoint FixedDay-指定日，MonthEnd-月末 */
    public String getPayInterestPoint(){
        return this.payInterestPoint;
    }
    /** 还息时点;代码：PayTimePoint FixedDay-指定日，MonthEnd-月末 */
    public void setPayInterestPoint(String payInterestPoint){
        this.payInterestPoint = payInterestPoint;
    }
    /** 还息日 */
    public Integer getPayInterestDay(){
        return this.payInterestDay;
    }
    /** 还息日 */
    public void setPayInterestDay(Integer payInterestDay){
        this.payInterestDay = payInterestDay;
    }
    /** 还本息是否一致;包含还本息频率，时点，天是否都一致 */
    public String getPayPrincipalInterestSync(){
        return this.payPrincipalInterestSync;
    }
    /** 还本息是否一致;包含还本息频率，时点，天是否都一致 */
    public void setPayPrincipalInterestSync(String payPrincipalInterestSync){
        this.payPrincipalInterestSync = payPrincipalInterestSync;
    }
    /** 还款时点;本息一致时，使用 */
    public String getPayPoint(){
        return this.payPoint;
    }
    /** 还款时点;本息一致时，使用 */
    public void setPayPoint(String payPoint){
        this.payPoint = payPoint;
    }
    /** 还款日;本息一致时，使用 */
    public Integer getPayFixedDay(){
        return this.payFixedDay;
    }
    /** 还款日;本息一致时，使用 */
    public void setPayFixedDay(Integer payFixedDay){
        this.payFixedDay = payFixedDay;
    }
    /** 日期头尾计算方式;HeadTailContain: 10-算头不算尾，01-算尾不算头，11-算头又算尾 ，00-不算头不算尾 */
    public String getHeadTailContain(){
        return this.headTailContain;
    }
    /** 日期头尾计算方式;HeadTailContain: 10-算头不算尾，01-算尾不算头，11-算头又算尾 ，00-不算头不算尾 */
    public void setHeadTailContain(String headTailContain){
        this.headTailContain = headTailContain;
    }
    /** 还款日节假日处理方式;代码:HolidayTreatment Delay-顺延，Pre-提前，Ignore-不变 */
    public String getPayHolidayTreat(){
        return this.payHolidayTreat;
    }
    /** 还款日节假日处理方式;代码:HolidayTreatment Delay-顺延，Pre-提前，Ignore-不变 */
    public void setPayHolidayTreat(String payHolidayTreat){
        this.payHolidayTreat = payHolidayTreat;
    }
    /** 结息日节假日处理方式 */
    public String getInterestSettleHolidayTreat(){
        return this.interestSettleHolidayTreat;
    }
    /** 结息日节假日处理方式 */
    public void setInterestSettleHolidayTreat(String interestSettleHolidayTreat){
        this.interestSettleHolidayTreat = interestSettleHolidayTreat;
    }
    /** 已还本金 */
    public Double getPaidPrincipal(){
        return this.paidPrincipal;
    }
    /** 已还本金 */
    public void setPaidPrincipal(Double paidPrincipal){
        this.paidPrincipal = paidPrincipal;
    }
    /** 已还金额 */
    public Double getPaidTotalAmt(){
        return this.paidTotalAmt;
    }
    /** 已还金额 */
    public void setPaidTotalAmt(Double paidTotalAmt){
        this.paidTotalAmt = paidTotalAmt;
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