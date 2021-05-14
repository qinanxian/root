package com.vekai.crops.customer.entity;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Table(name = "CUST_INVOICE")
public class CustInvoicePO implements Serializable,Cloneable{

  @Id
  @GeneratedValue
  private String id;
  private String custId;
  private String invoiceTitle;
  private String taxQualification;
  private String enterpriseType;
  private String taxCode;
  private String invoiceAddress;
  private String invoiceTel;
  private String openingBank;
  private String accountNo;
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


  public String getInvoiceTitle() {
    return invoiceTitle;
  }

  public void setInvoiceTitle(String invoiceTitle) {
    this.invoiceTitle = invoiceTitle;
  }


  public String getTaxQualification() {
    return taxQualification;
  }

  public void setTaxQualification(String taxQualification) {
    this.taxQualification = taxQualification;
  }


  public String getEnterpriseType() {
    return enterpriseType;
  }

  public void setEnterpriseType(String enterpriseType) {
    this.enterpriseType = enterpriseType;
  }


  public String getTaxCode() {
    return taxCode;
  }

  public void setTaxCode(String taxCode) {
    this.taxCode = taxCode;
  }


  public String getInvoiceAddress() {
    return invoiceAddress;
  }

  public void setInvoiceAddress(String invoiceAddress) {
    this.invoiceAddress = invoiceAddress;
  }


  public String getInvoiceTel() {
    return invoiceTel;
  }

  public void setInvoiceTel(String invoiceTel) {
    this.invoiceTel = invoiceTel;
  }


  public String getOpeningBank() {
    return openingBank;
  }

  public void setOpeningBank(String openingBank) {
    this.openingBank = openingBank;
  }


  public String getAccountNo() {
    return accountNo;
  }

  public void setAccountNo(String accountNo) {
    this.accountNo = accountNo;
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
