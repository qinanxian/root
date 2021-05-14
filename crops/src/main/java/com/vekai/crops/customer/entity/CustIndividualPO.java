package com.vekai.crops.customer.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="CUST_IND")
public class CustIndividualPO implements Serializable,Cloneable{
    /** 客户ID */
    @Id
    @GeneratedValue
    private String custId ;
    /** 身份证号 */
    private String certId ;
    /** 身份证起始日 */
    private Date certEffectiveDate ;
    /** 身份证到期日 */
    private Date certExpirtyDate ;
    /** 身份证归属地 */
    private String certPlact ;
    /** 中文名 */
    private String chnName ;
    /** 英文名 */
    private String engName ;
    /** 性别 */
    private String gender ;
    /** 出生日期 */
    private Date birth ;
    /** 年龄 */
    private Integer age ;
    /** 身高 */
    private Double height ;
    /** 体重 */
    private Double weight ;
    /** 名族 */
    private String nation ;
    /** 政治面貌 */
    private String political ;
    /** 婚姻状况 */
    private String marital ;
    /** 配偶客户号 */
    private String spouseCustId ;
    /** 最高学历 */
    private String educationLevel ;
    /** 毕业院校 */
    private String graduatedFrom ;
    /** 籍贯（省） */
    private String domicilePlaceProvince ;
    /** 籍贯（市) */
    private String domicilePlaceCity ;
    /** 户籍地址 */
    private String domicilePlaceAddress ;
    /** 联系地址 */
    private String contactAddress ;
    /** 居住地址 */
    private String presentAddress ;
    /** 手机号 */
    private String cellPhoneNo ;
    /** 手机号归属地 */
    private String cellPhoneNoPlace ;
    /** 电子邮件 */
    private String email ;
    /** 职业 */
    private String workAs ;
    /** 职称 */
    private String jobTitle ;
    /** 单位所属行业 */
    private String companyIndustry ;
    /** 单位地址 */
    private String companyAddress ;
    /** 单位电话 */
    private String companyTelephone ;
    /** 单位性质 */
    private String companyNature ;
    /** 入职日期 */
    private String companyEntryDate ;
    /** 工龄 */
    private Integer seniority ;
    /** 是否交纳社保 */
    private String ispaySocialSecurity ;
    /** 月收入 */
    private Integer monthIncome ;
    /** 是否有驾照 */
    private String haveDriverLicense ;
    /** 驾照签发日期 */
    private Date driverLicenseIssueDate ;
    /** 拥有房产数 */
    private Integer ownerHouses ;
    /** 身份证核查通过 */
    private String certCheckPassed ;
    /** 人证一致核查通过 */
    private String personalIdentityPassed ;
    /** 是否征信授权 */
    private String isIcrAuthz ;
    /** 芝麻信用分 */
    private Integer zhimaCreditScore ;
    /** 暂存草稿 */
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

    /** 客户ID */
    public String getCustId(){
        return this.custId;
    }
    /** 客户ID */
    public void setCustId(String custId){
        this.custId = custId;
    }
    /** 身份证号 */
    public String getCertId(){
        return this.certId;
    }
    /** 身份证号 */
    public void setCertId(String certId){
        this.certId = certId;
    }
    /** 身份证起始日 */
    public Date getCertEffectiveDate(){
        return this.certEffectiveDate;
    }
    /** 身份证起始日 */
    public void setCertEffectiveDate(Date certEffectiveDate){
        this.certEffectiveDate = certEffectiveDate;
    }
    /** 身份证到期日 */
    public Date getCertExpirtyDate(){
        return this.certExpirtyDate;
    }
    /** 身份证到期日 */
    public void setCertExpirtyDate(Date certExpirtyDate){
        this.certExpirtyDate = certExpirtyDate;
    }
    /** 身份证归属地 */
    public String getCertPlact(){
        return this.certPlact;
    }
    /** 身份证归属地 */
    public void setCertPlact(String certPlact){
        this.certPlact = certPlact;
    }
    /** 中文名 */
    public String getChnName(){
        return this.chnName;
    }
    /** 中文名 */
    public void setChnName(String chnName){
        this.chnName = chnName;
    }
    /** 英文名 */
    public String getEngName(){
        return this.engName;
    }
    /** 英文名 */
    public void setEngName(String engName){
        this.engName = engName;
    }
    /** 性别 */
    public String getGender(){
        return this.gender;
    }
    /** 性别 */
    public void setGender(String gender){
        this.gender = gender;
    }
    /** 出生日期 */
    public Date getBirth(){
        return this.birth;
    }
    /** 出生日期 */
    public void setBirth(Date birth){
        this.birth = birth;
    }
    /** 年龄 */
    public Integer getAge(){
        return this.age;
    }
    /** 年龄 */
    public void setAge(Integer age){
        this.age = age;
    }
    /** 身高 */
    public Double getHeight(){
        return this.height;
    }
    /** 身高 */
    public void setHeight(Double height){
        this.height = height;
    }
    /** 体重 */
    public Double getWeight(){
        return this.weight;
    }
    /** 体重 */
    public void setWeight(Double weight){
        this.weight = weight;
    }
    /** 名族 */
    public String getNation(){
        return this.nation;
    }
    /** 名族 */
    public void setNation(String nation){
        this.nation = nation;
    }
    /** 政治面貌 */
    public String getPolitical(){
        return this.political;
    }
    /** 政治面貌 */
    public void setPolitical(String political){
        this.political = political;
    }
    /** 婚姻状况 */
    public String getMarital(){
        return this.marital;
    }
    /** 婚姻状况 */
    public void setMarital(String marital){
        this.marital = marital;
    }
    /** 配偶客户号 */
    public String getSpouseCustId(){
        return this.spouseCustId;
    }
    /** 配偶客户号 */
    public void setSpouseCustId(String spouseCustId){
        this.spouseCustId = spouseCustId;
    }
    /** 最高学历 */
    public String getEducationLevel(){
        return this.educationLevel;
    }
    /** 最高学历 */
    public void setEducationLevel(String educationLevel){
        this.educationLevel = educationLevel;
    }
    /** 毕业院校 */
    public String getGraduatedFrom(){
        return this.graduatedFrom;
    }
    /** 毕业院校 */
    public void setGraduatedFrom(String graduatedFrom){
        this.graduatedFrom = graduatedFrom;
    }
    /** 籍贯（省） */
    public String getDomicilePlaceProvince(){
        return this.domicilePlaceProvince;
    }
    /** 籍贯（省） */
    public void setDomicilePlaceProvince(String domicilePlaceProvince){
        this.domicilePlaceProvince = domicilePlaceProvince;
    }
    /** 籍贯（市) */
    public String getDomicilePlaceCity(){
        return this.domicilePlaceCity;
    }
    /** 籍贯（市) */
    public void setDomicilePlaceCity(String domicilePlaceCity){
        this.domicilePlaceCity = domicilePlaceCity;
    }
    /** 户籍地址 */
    public String getDomicilePlaceAddress(){
        return this.domicilePlaceAddress;
    }
    /** 户籍地址 */
    public void setDomicilePlaceAddress(String domicilePlaceAddress){
        this.domicilePlaceAddress = domicilePlaceAddress;
    }
    /** 联系地址 */
    public String getContactAddress(){
        return this.contactAddress;
    }
    /** 联系地址 */
    public void setContactAddress(String contactAddress){
        this.contactAddress = contactAddress;
    }
    /** 居住地址 */
    public String getPresentAddress(){
        return this.presentAddress;
    }
    /** 居住地址 */
    public void setPresentAddress(String presentAddress){
        this.presentAddress = presentAddress;
    }
    /** 手机号 */
    public String getCellPhoneNo(){
        return this.cellPhoneNo;
    }
    /** 手机号 */
    public void setCellPhoneNo(String cellPhoneNo){
        this.cellPhoneNo = cellPhoneNo;
    }
    /** 手机号归属地 */
    public String getCellPhoneNoPlace(){
        return this.cellPhoneNoPlace;
    }
    /** 手机号归属地 */
    public void setCellPhoneNoPlace(String cellPhoneNoPlace){
        this.cellPhoneNoPlace = cellPhoneNoPlace;
    }
    /** 电子邮件 */
    public String getEmail(){
        return this.email;
    }
    /** 电子邮件 */
    public void setEmail(String email){
        this.email = email;
    }
    /** 职业 */
    public String getWorkAs(){
        return this.workAs;
    }
    /** 职业 */
    public void setWorkAs(String workAs){
        this.workAs = workAs;
    }
    /** 职称 */
    public String getJobTitle(){
        return this.jobTitle;
    }
    /** 职称 */
    public void setJobTitle(String jobTitle){
        this.jobTitle = jobTitle;
    }
    /** 单位所属行业 */
    public String getCompanyIndustry(){
        return this.companyIndustry;
    }
    /** 单位所属行业 */
    public void setCompanyIndustry(String companyIndustry){
        this.companyIndustry = companyIndustry;
    }
    /** 单位地址 */
    public String getCompanyAddress(){
        return this.companyAddress;
    }
    /** 单位地址 */
    public void setCompanyAddress(String companyAddress){
        this.companyAddress = companyAddress;
    }
    /** 单位电话 */
    public String getCompanyTelephone(){
        return this.companyTelephone;
    }
    /** 单位电话 */
    public void setCompanyTelephone(String companyTelephone){
        this.companyTelephone = companyTelephone;
    }
    /** 单位性质 */
    public String getCompanyNature(){
        return this.companyNature;
    }
    /** 单位性质 */
    public void setCompanyNature(String companyNature){
        this.companyNature = companyNature;
    }
    /** 入职日期 */
    public String getCompanyEntryDate(){
        return this.companyEntryDate;
    }
    /** 入职日期 */
    public void setCompanyEntryDate(String companyEntryDate){
        this.companyEntryDate = companyEntryDate;
    }
    /** 工龄 */
    public Integer getSeniority(){
        return this.seniority;
    }
    /** 工龄 */
    public void setSeniority(Integer seniority){
        this.seniority = seniority;
    }
    /** 是否交纳社保 */
    public String getIspaySocialSecurity(){
        return this.ispaySocialSecurity;
    }
    /** 是否交纳社保 */
    public void setIspaySocialSecurity(String ispaySocialSecurity){
        this.ispaySocialSecurity = ispaySocialSecurity;
    }
    /** 月收入 */
    public Integer getMonthIncome(){
        return this.monthIncome;
    }
    /** 月收入 */
    public void setMonthIncome(Integer monthIncome){
        this.monthIncome = monthIncome;
    }
    /** 是否有驾照 */
    public String getHaveDriverLicense(){
        return this.haveDriverLicense;
    }
    /** 是否有驾照 */
    public void setHaveDriverLicense(String haveDriverLicense){
        this.haveDriverLicense = haveDriverLicense;
    }
    /** 驾照签发日期 */
    public Date getDriverLicenseIssueDate(){
        return this.driverLicenseIssueDate;
    }
    /** 驾照签发日期 */
    public void setDriverLicenseIssueDate(Date driverLicenseIssueDate){
        this.driverLicenseIssueDate = driverLicenseIssueDate;
    }
    /** 拥有房产数 */
    public Integer getOwnerHouses(){
        return this.ownerHouses;
    }
    /** 拥有房产数 */
    public void setOwnerHouses(Integer ownerHouses){
        this.ownerHouses = ownerHouses;
    }
    /** 身份证核查通过 */
    public String getCertCheckPassed(){
        return this.certCheckPassed;
    }
    /** 身份证核查通过 */
    public void setCertCheckPassed(String certCheckPassed){
        this.certCheckPassed = certCheckPassed;
    }
    /** 人证一致核查通过 */
    public String getPersonalIdentityPassed(){
        return this.personalIdentityPassed;
    }
    /** 人证一致核查通过 */
    public void setPersonalIdentityPassed(String personalIdentityPassed){
        this.personalIdentityPassed = personalIdentityPassed;
    }
    /** 是否征信授权 */
    public String getIsIcrAuthz(){
        return this.isIcrAuthz;
    }
    /** 是否征信授权 */
    public void setIsIcrAuthz(String isIcrAuthz){
        this.isIcrAuthz = isIcrAuthz;
    }
    /** 芝麻信用分 */
    public Integer getZhimaCreditScore(){
        return this.zhimaCreditScore;
    }
    /** 芝麻信用分 */
    public void setZhimaCreditScore(Integer zhimaCreditScore){
        this.zhimaCreditScore = zhimaCreditScore;
    }
    /** 暂存草稿 */
    public String getIsDraft(){
        return this.isDraft;
    }
    /** 暂存草稿 */
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