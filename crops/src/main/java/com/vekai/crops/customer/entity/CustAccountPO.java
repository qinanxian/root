package com.vekai.crops.customer.entity;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Table(name = "CUST_ACCOUNT")
public class CustAccountPO implements Serializable,Cloneable {

  @Id
  @GeneratedValue
  private String id;
  private String custId;
  private String accountType;
  private String accountName;
  private String accountNo;
  private String headerBankName;
  private String bankCity;
  private String bankBranchName;
  private String bankFullName;
  private String isDefault;
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


  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }


  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }


  public String getAccountNo() {
    return accountNo;
  }

  public void setAccountNo(String accountNo) {
    this.accountNo = accountNo;
  }


  public String getHeaderBankName() {
    return headerBankName;
  }

  public void setHeaderBankName(String headerBankName) {
    this.headerBankName = headerBankName;
  }


  public String getBankCity() {
    return bankCity;
  }

  public void setBankCity(String bankCity) {
    this.bankCity = bankCity;
  }


  public String getBankBranchName() {
    return bankBranchName;
  }

  public void setBankBranchName(String bankBranchName) {
    this.bankBranchName = bankBranchName;
  }


  public String getBankFullName() {
    return bankFullName;
  }

  public void setBankFullName(String bankFullName) {
    this.bankFullName = bankFullName;
  }


  public String getIsDefault() {
    return isDefault;
  }

  public void setIsDefault(String isDefault) {
    this.isDefault = isDefault;
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
