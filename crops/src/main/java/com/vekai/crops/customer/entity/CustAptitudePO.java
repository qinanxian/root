package com.vekai.crops.customer.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CUST_APTITUDE")
public class CustAptitudePO implements Serializable, Cloneable {

    @Id
    @GeneratedValue
    private String id;
    private String custId;
    private String mainField;
    private String aptiType;
    private String aptiCode;
    private Date effectiveDate;
    private Date expirtyDate;
    private String aptiIntro;
    private String aptiLevel;
    private Integer peopleNumber;
    private String status;

    private long revision;
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

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getMainField() {
        return mainField;
    }

    public void setMainField(String mainField) {
        this.mainField = mainField;
    }

    public String getAptiType() {
        return aptiType;
    }

    public void setAptiType(String aptiType) {
        this.aptiType = aptiType;
    }

    public String getAptiCode() {
        return aptiCode;
    }

    public void setAptiCode(String aptiCode) {
        this.aptiCode = aptiCode;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpirtyDate() {
        return expirtyDate;
    }

    public void setExpirtyDate(Date expirtyDate) {
        this.expirtyDate = expirtyDate;
    }

    public String getAptiIntro() {
        return aptiIntro;
    }

    public void setAptiIntro(String aptiIntro) {
        this.aptiIntro = aptiIntro;
    }

    public String getAptiLevel() {
        return aptiLevel;
    }

    public void setAptiLevel(String aptiLevel) {
        this.aptiLevel = aptiLevel;
    }

    public Integer getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(Integer peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getRevision() {
        return revision;
    }

    public void setRevision(long revision) {
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
