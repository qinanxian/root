//package com.vekai.showcase.rules.bom;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.Date;
//import com.bstek.urule.model.Label;
//
///** 个人信用评级数据对象 */
//@Table(name="RATN_PERSON_CREDIT")
//public class PersonCreditBOM implements Serializable,Cloneable{
//    /** 评级号 */
//    @Id
//    @GeneratedValue
//    @Label("评级号")
//    private String id ;
//    /** 姓名 */
//    @Label("姓名")
//    private String personName ;
//    /** 性别 */
//    @Label("性别")
//    private String gender ;
//    /** 年龄 */
//    @Label("年龄")
//    private Integer age ;
//    /** 出生日期 */
//    @Label("出生日期")
//    private Date birth ;
//    /** 最高学历 */
//    @Label("最高学历")
//    private String degree ;
//    /** 婚姻状况 */
//    @Label("婚姻状况")
//    private String marital ;
//    /** 职称 */
//    @Label("职称")
//    private String jobGrade ;
//    /** 是否本地户籍 */
//    @Label("是否本地户籍")
//    private String isLocalCensus ;
//    /** 单位性质 */
//    @Label("单位性质")
//    private String companyNature ;
//    /** 发薪方式 */
//    @Label("发薪方式")
//    private String salaryPayChannel ;
//    /** 房产状况 */
//    @Label("房产状况")
//    private String houseStatus ;
//    /** 购车状况 */
//    @Label("购车状况")
//    private String carStatus ;
//    /** 负债率 */
//    @Label("负债率")
//    private Double debtRatio ;
//    /** 收入 */
//    @Label("收入")
//    private Double incomeAmt ;
//    /** 正在使用的信用卡数量 */
//    @Label("正在使用的信用卡数量")
//    private Integer activeCreditCardCount ;
//    /** 未销户信用卡总额度 */
//    @Label("未销户信用卡总额度")
//    private Double activeCreditCardTotalAmt ;
//    /** 半年内是否申请信用卡 */
//    @Label("半年内是否申请信用卡")
//    private String applyCreditCardCountIn6m ;
//    /** 信用卡透支比例 */
//    @Label("信用卡透支比例")
//    private Double creditOverdraftRatio ;
//    /** 贷款还款情况 */
//    @Label("贷款还款情况")
//    private String repaymentStatus ;
//    /** 不良信用连三 */
//    @Label("不良信用连三")
//    private String overdue24mSeries3 ;
//    /** 不良信用累六 */
//    @Label("不良信用累六")
//    private String overdue24mTotal6 ;
//    /** 不良信用连二 */
//    @Label("不良信用连二")
//    private String overdue12mSeries2 ;
//    /** 不良信用累三 */
//    @Label("不良信用累三")
//    private String overdue12mTotal3 ;
//    /** 信用报告查询情况 */
//    @Label("信用报告查询情况")
//    private String creditReportQueryCount ;
//    /** 乐观锁 */
//    @Label("乐观锁")
//    private Integer revision ;
//    /** 创建人 */
//    @Label("创建人")
//    private String createdBy ;
//    /** 创建时间 */
//    @Label("创建时间")
//    private Date createdTime ;
//    /** 更新人 */
//    @Label("更新人")
//    private String updatedBy ;
//    /** 更新时间 */
//    @Label("更新时间")
//    private Date updatedTime ;
//
//    /** 评级号 */
//    public String getId(){
//        return this.id;
//    }
//    /** 评级号 */
//    public void setId(String id){
//        this.id = id;
//    }
//    /** 姓名 */
//    public String getPersonName(){
//        return this.personName;
//    }
//    /** 姓名 */
//    public void setPersonName(String personName){
//        this.personName = personName;
//    }
//    /** 性别 */
//    public String getGender(){
//        return this.gender;
//    }
//    /** 性别 */
//    public void setGender(String gender){
//        this.gender = gender;
//    }
//    /** 年龄 */
//    public Integer getAge(){
//        return this.age;
//    }
//    /** 年龄 */
//    public void setAge(Integer age){
//        this.age = age;
//    }
//    /** 出生日期 */
//    public Date getBirth(){
//        return this.birth;
//    }
//    /** 出生日期 */
//    public void setBirth(Date birth){
//        this.birth = birth;
//    }
//    /** 最高学历 */
//    public String getDegree(){
//        return this.degree;
//    }
//    /** 最高学历 */
//    public void setDegree(String degree){
//        this.degree = degree;
//    }
//    /** 婚姻状况 */
//    public String getMarital(){
//        return this.marital;
//    }
//    /** 婚姻状况 */
//    public void setMarital(String marital){
//        this.marital = marital;
//    }
//    /** 职称 */
//    public String getJobGrade(){
//        return this.jobGrade;
//    }
//    /** 职称 */
//    public void setJobGrade(String jobGrade){
//        this.jobGrade = jobGrade;
//    }
//    /** 是否本地户籍 */
//    public String getIsLocalCensus(){
//        return this.isLocalCensus;
//    }
//    /** 是否本地户籍 */
//    public void setIsLocalCensus(String isLocalCensus){
//        this.isLocalCensus = isLocalCensus;
//    }
//    /** 单位性质 */
//    public String getCompanyNature(){
//        return this.companyNature;
//    }
//    /** 单位性质 */
//    public void setCompanyNature(String companyNature){
//        this.companyNature = companyNature;
//    }
//    /** 发薪方式 */
//    public String getSalaryPayChannel(){
//        return this.salaryPayChannel;
//    }
//    /** 发薪方式 */
//    public void setSalaryPayChannel(String salaryPayChannel){
//        this.salaryPayChannel = salaryPayChannel;
//    }
//    /** 房产状况 */
//    public String getHouseStatus(){
//        return this.houseStatus;
//    }
//    /** 房产状况 */
//    public void setHouseStatus(String houseStatus){
//        this.houseStatus = houseStatus;
//    }
//    /** 购车状况 */
//    public String getCarStatus(){
//        return this.carStatus;
//    }
//    /** 购车状况 */
//    public void setCarStatus(String carStatus){
//        this.carStatus = carStatus;
//    }
//    /** 负债率 */
//    public Double getDebtRatio(){
//        return this.debtRatio;
//    }
//    /** 负债率 */
//    public void setDebtRatio(Double debtRatio){
//        this.debtRatio = debtRatio;
//    }
//    /** 收入 */
//    public Double getIncomeAmt(){
//        return this.incomeAmt;
//    }
//    /** 收入 */
//    public void setIncomeAmt(Double incomeAmt){
//        this.incomeAmt = incomeAmt;
//    }
//    /** 正在使用的信用卡数量 */
//    public Integer getActiveCreditCardCount(){
//        return this.activeCreditCardCount;
//    }
//    /** 正在使用的信用卡数量 */
//    public void setActiveCreditCardCount(Integer activeCreditCardCount){
//        this.activeCreditCardCount = activeCreditCardCount;
//    }
//    /** 未销户信用卡总额度 */
//    public Double getActiveCreditCardTotalAmt(){
//        return this.activeCreditCardTotalAmt;
//    }
//    /** 未销户信用卡总额度 */
//    public void setActiveCreditCardTotalAmt(Double activeCreditCardTotalAmt){
//        this.activeCreditCardTotalAmt = activeCreditCardTotalAmt;
//    }
//    /** 半年内是否申请信用卡 */
//    public String getApplyCreditCardCountIn6m(){
//        return this.applyCreditCardCountIn6m;
//    }
//    /** 半年内是否申请信用卡 */
//    public void setApplyCreditCardCountIn6m(String applyCreditCardCountIn6m){
//        this.applyCreditCardCountIn6m = applyCreditCardCountIn6m;
//    }
//    /** 信用卡透支比例 */
//    public Double getCreditOverdraftRatio(){
//        return this.creditOverdraftRatio;
//    }
//    /** 信用卡透支比例 */
//    public void setCreditOverdraftRatio(Double creditOverdraftRatio){
//        this.creditOverdraftRatio = creditOverdraftRatio;
//    }
//    /** 贷款还款情况 */
//    public String getRepaymentStatus(){
//        return this.repaymentStatus;
//    }
//    /** 贷款还款情况 */
//    public void setRepaymentStatus(String repaymentStatus){
//        this.repaymentStatus = repaymentStatus;
//    }
//    /** 不良信用连三 */
//    public String getOverdue24mSeries3(){
//        return this.overdue24mSeries3;
//    }
//    /** 不良信用连三 */
//    public void setOverdue24mSeries3(String overdue24mSeries3){
//        this.overdue24mSeries3 = overdue24mSeries3;
//    }
//    /** 不良信用累六 */
//    public String getOverdue24mTotal6(){
//        return this.overdue24mTotal6;
//    }
//    /** 不良信用累六 */
//    public void setOverdue24mTotal6(String overdue24mTotal6){
//        this.overdue24mTotal6 = overdue24mTotal6;
//    }
//    /** 不良信用连二 */
//    public String getOverdue12mSeries2(){
//        return this.overdue12mSeries2;
//    }
//    /** 不良信用连二 */
//    public void setOverdue12mSeries2(String overdue12mSeries2){
//        this.overdue12mSeries2 = overdue12mSeries2;
//    }
//    /** 不良信用累三 */
//    public String getOverdue12mTotal3(){
//        return this.overdue12mTotal3;
//    }
//    /** 不良信用累三 */
//    public void setOverdue12mTotal3(String overdue12mTotal3){
//        this.overdue12mTotal3 = overdue12mTotal3;
//    }
//    /** 信用报告查询情况 */
//    public String getCreditReportQueryCount(){
//        return this.creditReportQueryCount;
//    }
//    /** 信用报告查询情况 */
//    public void setCreditReportQueryCount(String creditReportQueryCount){
//        this.creditReportQueryCount = creditReportQueryCount;
//    }
//    /** 乐观锁 */
//    public Integer getRevision(){
//        return this.revision;
//    }
//    /** 乐观锁 */
//    public void setRevision(Integer revision){
//        this.revision = revision;
//    }
//    /** 创建人 */
//    public String getCreatedBy(){
//        return this.createdBy;
//    }
//    /** 创建人 */
//    public void setCreatedBy(String createdBy){
//        this.createdBy = createdBy;
//    }
//    /** 创建时间 */
//    public Date getCreatedTime(){
//        return this.createdTime;
//    }
//    /** 创建时间 */
//    public void setCreatedTime(Date createdTime){
//        this.createdTime = createdTime;
//    }
//    /** 更新人 */
//    public String getUpdatedBy(){
//        return this.updatedBy;
//    }
//    /** 更新人 */
//    public void setUpdatedBy(String updatedBy){
//        this.updatedBy = updatedBy;
//    }
//    /** 更新时间 */
//    public Date getUpdatedTime(){
//        return this.updatedTime;
//    }
//    /** 更新时间 */
//    public void setUpdatedTime(Date updatedTime){
//        this.updatedTime = updatedTime;
//    }
//}
