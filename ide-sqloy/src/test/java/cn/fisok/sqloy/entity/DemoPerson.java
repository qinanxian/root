package cn.fisok.sqloy.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="DEMO_PERSON")
public class DemoPerson implements Serializable,Cloneable{
    /** 个人编号 */
    @Id
    @GeneratedValue
    private Long id ;
    /** 用户代号 */
    private String code ;
    /** 名称 */
    private String name ;
    /** 中文名 */
    private String chnName ;
    /** 英文名 */
    private String engName ;
    /** 头像 */
    private String avatar ;
    /** 性别 */
    private String gender ;
    /** 出生日期 */
    private Date birth ;
    /** 身高 */
    private Integer height ;
    /** 体重 */
    private Integer weight ;
    /** 名族 */
    private String nation ;
    /** 政治面貌 */
    private String political ;
    /** 婚姻状况 */
    private String marital ;
    /** 最高学历 */
    private String educationLevel ;
    /** 毕业院校 */
    private String graduatedFrom ;
    /** 籍贯（省） */
    private String domicilePlaceProvince ;
    /** 籍贯（市） */
    private String domicilePlaceCity ;
    /** 户籍地址 */
    private String domicilePlaceAddress ;
    /** 居住地址 */
    private String presentAddress ;
    /** 手机号 */
    private String mobilePhone ;
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
    /** 单位邮编 */
    private String companyPostcode ;
    /** 入职日期 */
    private String entryDate ;
    /** 个人月收入 */
    private Double monthIncome ;
    /** 家庭月收入 */
    private Double familyMonthIncome ;
    /** 家庭年收入 */
    private Double familyYearIncome ;
    /** 家庭每月固定支出 */
    private Double familyMonthCost ;
    /** 爱好 */
    private String hobby ;
    /** 备注 */
    private String remark ;
    /** 状态 */
    private String status ;
    @Transient
    protected List<PersonAddress> addresses;
    @Transient
    protected Long viewTimes;

    /** 个人编号 */
    public Long getId(){
        return this.id;
    }
    /** 个人编号 */
    public void setId(Long id){
        this.id = id;
    }
    /** 用户代号 */
    public String getCode(){
        return this.code;
    }
    /** 用户代号 */
    public void setCode(String code){
        this.code = code;
    }
    /** 名称 */
    public String getName(){
        return this.name;
    }
    /** 名称 */
    public void setName(String name){
        this.name = name;
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
    /** 头像 */
    public String getAvatar(){
        return this.avatar;
    }
    /** 头像 */
    public void setAvatar(String avatar){
        this.avatar = avatar;
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
    /** 身高 */
    public Integer getHeight(){
        return this.height;
    }
    /** 身高 */
    public void setHeight(Integer height){
        this.height = height;
    }
    /** 体重 */
    public Integer getWeight(){
        return this.weight;
    }
    /** 体重 */
    public void setWeight(Integer weight){
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
    /** 籍贯（市） */
    public String getDomicilePlaceCity(){
        return this.domicilePlaceCity;
    }
    /** 籍贯（市） */
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
    /** 居住地址 */
    public String getPresentAddress(){
        return this.presentAddress;
    }
    /** 居住地址 */
    public void setPresentAddress(String presentAddress){
        this.presentAddress = presentAddress;
    }
    /** 手机号 */
    public String getMobilePhone(){
        return this.mobilePhone;
    }
    /** 手机号 */
    public void setMobilePhone(String mobilePhone){
        this.mobilePhone = mobilePhone;
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
    /** 单位邮编 */
    public String getCompanyPostcode(){
        return this.companyPostcode;
    }
    /** 单位邮编 */
    public void setCompanyPostcode(String companyPostcode){
        this.companyPostcode = companyPostcode;
    }
    /** 入职日期 */
    public String getEntryDate(){
        return this.entryDate;
    }
    /** 入职日期 */
    public void setEntryDate(String entryDate){
        this.entryDate = entryDate;
    }
    /** 个人月收入 */
    public Double getMonthIncome(){
        return this.monthIncome;
    }
    /** 个人月收入 */
    public void setMonthIncome(Double monthIncome){
        this.monthIncome = monthIncome;
    }
    /** 家庭月收入 */
    public Double getFamilyMonthIncome(){
        return this.familyMonthIncome;
    }
    /** 家庭月收入 */
    public void setFamilyMonthIncome(Double familyMonthIncome){
        this.familyMonthIncome = familyMonthIncome;
    }
    /** 家庭年收入 */
    public Double getFamilyYearIncome(){
        return this.familyYearIncome;
    }
    /** 家庭年收入 */
    public void setFamilyYearIncome(Double familyYearIncome){
        this.familyYearIncome = familyYearIncome;
    }
    /** 家庭每月固定支出 */
    public Double getFamilyMonthCost(){
        return this.familyMonthCost;
    }
    /** 家庭每月固定支出 */
    public void setFamilyMonthCost(Double familyMonthCost){
        this.familyMonthCost = familyMonthCost;
    }
    /** 爱好 */
    public String getHobby(){
        return this.hobby;
    }
    /** 爱好 */
    public void setHobby(String hobby){
        this.hobby = hobby;
    }
    /** 备注 */
    public String getRemark(){
        return this.remark;
    }
    /** 备注 */
    public void setRemark(String remark){
        this.remark = remark;
    }
    /** 状态 */
    public String getStatus(){
        return this.status;
    }
    /** 状态 */
    public void setStatus(String status){
        this.status = status;
    }

    public List<PersonAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<PersonAddress> addresses) {
        this.addresses = addresses;
    }

    public Long getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(Long viewTimes) {
        this.viewTimes = viewTimes;
    }

    @Transient
    public String getCompleteName(){
        return chnName+"("+engName+")";
    }
}
