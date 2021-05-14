package com.vekai.crops.customer.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CUST_BOND")
public class CustBondPO implements Serializable, Cloneable {

    @Id
    @GeneratedValue
    private String id;
    private String custId;
    private Date issueDate;
    private String bondType;
    private String bondCurrency;
    private Double bondAmount;
    private Double bondRate;
    private Integer bondTerm;
    private String isMarket;
    private String bourseName;
    private String remark;
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

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getBondType() {
        return bondType;
    }

    public void setBondType(String bondType) {
        this.bondType = bondType;
    }

    public String getBondCurrency() {
        return bondCurrency;
    }

    public void setBondCurrency(String bondCurrency) {
        this.bondCurrency = bondCurrency;
    }

    public Double getBondAmount() {
        return bondAmount;
    }

    public void setBondAmount(Double bondAmount) {
        this.bondAmount = bondAmount;
    }

    public Double getBondRate() {
        return bondRate;
    }

    public void setBondRate(Double bondRate) {
        this.bondRate = bondRate;
    }

    public Integer getBondTerm() {
        return bondTerm;
    }

    public void setBondTerm(Integer bondTerm) {
        this.bondTerm = bondTerm;
    }

    public String getIsMarket() {
        return isMarket;
    }

    public void setIsMarket(String isMarket) {
        this.isMarket = isMarket;
    }

    public String getBourseName() {
        return bourseName;
    }

    public void setBourseName(String bourseName) {
        this.bourseName = bourseName;
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
