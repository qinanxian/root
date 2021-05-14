package com.vekai.showcase.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "DEMO_PERSON")
public class DemoPerson implements Serializable,Cloneable {
    @Id
    private String id;
    private String code;
    private String name;
    private String chnName;
    private String engName;
    private String avatar;
    private String gender;
    private Date birth;
    private Double height;
    private Double weight;
    private String nation;
    private String political;
    private String marital;
    private String educationLevel;
    private String graduatedFrom;
    private String domicilePlaceProvince;
    private String domicilePlaceCity;
    private String domicilePlaceAddress;
    private String presentAddress;
    private String mobilePhone;
    private String email;
    private String workAs;
    private String jobTitle;
    private String companyIndustry;
    private String companyAddress;
    private String companyPostcode;
    private Date entryDate;
    private Double monthIncome;
    private Double familyMonthIncome;
    private Double familyYearIncome;
    private Double familyMonthCost;
    private String hobby;
    private String remark;
    private String status;
    private Long revision;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }


    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }


    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }


    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }


    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }


    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }


    public String getMarital() {
        return marital;
    }

    public void setMarital(String marital) {
        this.marital = marital;
    }


    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }


    public String getGraduatedFrom() {
        return graduatedFrom;
    }

    public void setGraduatedFrom(String graduatedFrom) {
        this.graduatedFrom = graduatedFrom;
    }


    public String getDomicilePlaceProvince() {
        return domicilePlaceProvince;
    }

    public void setDomicilePlaceProvince(String domicilePlaceProvince) {
        this.domicilePlaceProvince = domicilePlaceProvince;
    }


    public String getDomicilePlaceCity() {
        return domicilePlaceCity;
    }

    public void setDomicilePlaceCity(String domicilePlaceCity) {
        this.domicilePlaceCity = domicilePlaceCity;
    }


    public String getDomicilePlaceAddress() {
        return domicilePlaceAddress;
    }

    public void setDomicilePlaceAddress(String domicilePlaceAddress) {
        this.domicilePlaceAddress = domicilePlaceAddress;
    }


    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }


    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getWorkAs() {
        return workAs;
    }

    public void setWorkAs(String workAs) {
        this.workAs = workAs;
    }


    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }


    public String getCompanyIndustry() {
        return companyIndustry;
    }

    public void setCompanyIndustry(String companyIndustry) {
        this.companyIndustry = companyIndustry;
    }


    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }


    public String getCompanyPostcode() {
        return companyPostcode;
    }

    public void setCompanyPostcode(String companyPostcode) {
        this.companyPostcode = companyPostcode;
    }


    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }


    public Double getMonthIncome() {
        return monthIncome;
    }

    public void setMonthIncome(Double monthIncome) {
        this.monthIncome = monthIncome;
    }


    public Double getFamilyMonthIncome() {
        return familyMonthIncome;
    }

    public void setFamilyMonthIncome(Double familyMonthIncome) {
        this.familyMonthIncome = familyMonthIncome;
    }


    public Double getFamilyYearIncome() {
        return familyYearIncome;
    }

    public void setFamilyYearIncome(Double familyYearIncome) {
        this.familyYearIncome = familyYearIncome;
    }


    public Double getFamilyMonthCost() {
        return familyMonthCost;
    }

    public void setFamilyMonthCost(Double familyMonthCost) {
        this.familyMonthCost = familyMonthCost;
    }


    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }


    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }


    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

}
