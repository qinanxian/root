package com.vekai.crops.customer.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CUST_INVEST")
public class CustInvestPO implements Serializable,Cloneable {

  @Id
  @GeneratedValue
  private String relationId;
  private String investType;
  private String warrantNumber;
  private Double investRatio;
  private String investCurrency;
  private String remark;
  private long revision;
  private String createdBy;
  private Date createdTime;
  private String updatedBy;
  private Date updatedTime;


  public String getRelationId() {
    return relationId;
  }

  public void setRelationId(String relationId) {
    this.relationId = relationId;
  }


  public String getInvestType() {
    return investType;
  }

  public void setInvestType(String investType) {
    this.investType = investType;
  }


  public String getWarrantNumber() {
    return warrantNumber;
  }

  public void setWarrantNumber(String warrantNumber) {
    this.warrantNumber = warrantNumber;
  }


  public Double getInvestRatio() {
    return investRatio;
  }

  public void setInvestRatio(Double investRatio) {
    this.investRatio = investRatio;
  }


  public String getInvestCurrency() {
    return investCurrency;
  }

  public void setInvestCurrency(String investCurrency) {
    this.investCurrency = investCurrency;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
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
