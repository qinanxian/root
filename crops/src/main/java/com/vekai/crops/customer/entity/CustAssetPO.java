package com.vekai.crops.customer.entity;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CUST_ASSET")
public class CustAssetPO implements Serializable,Cloneable {
  @Id
  @GeneratedValue
  private String id;
  private String custId;
  private String assetType;
  private String assetName;
  private String location;
  private String certificateCode;
  private Double netBookValue;
  private Double currentValue;
  private String useStatus;
  private String isMortgage;
  private String mortgagee;
  private Double mortgageAmount;
  private Date puchaseDate;
  private Double puchaseAmount;
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


  public String getAssetType() {
    return assetType;
  }

  public void setAssetType(String assetType) {
    this.assetType = assetType;
  }


  public String getAssetName() {
    return assetName;
  }

  public void setAssetName(String assetName) {
    this.assetName = assetName;
  }


  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }


  public String getCertificateCode() {
    return certificateCode;
  }

  public void setCertificateCode(String certificateCode) {
    this.certificateCode = certificateCode;
  }


  public Double getNetBookValue() {
    return netBookValue;
  }

  public void setNetBookValue(Double netBookValue) {
    this.netBookValue = netBookValue;
  }


  public Double getCurrentValue() {
    return currentValue;
  }

  public void setCurrentValue(Double currentValue) {
    this.currentValue = currentValue;
  }


  public String getUseStatus() {
    return useStatus;
  }

  public void setUseStatus(String useStatus) {
    this.useStatus = useStatus;
  }


  public String getIsMortgage() {
    return isMortgage;
  }

  public void setIsMortgage(String isMortgage) {
    this.isMortgage = isMortgage;
  }


  public String getMortgagee() {
    return mortgagee;
  }

  public void setMortgagee(String mortgagee) {
    this.mortgagee = mortgagee;
  }


  public Double getMortgageAmount() {
    return mortgageAmount;
  }

  public void setMortgageAmount(Double mortgageAmount) {
    this.mortgageAmount = mortgageAmount;
  }


  public Date getPuchaseDate() {
    return puchaseDate;
  }

  public void setPuchaseDate(Date puchaseDate) {
    this.puchaseDate = puchaseDate;
  }


  public Double getPuchaseAmount() {
    return puchaseAmount;
  }

  public void setPuchaseAmount(Double puchaseAmount) {
    this.puchaseAmount = puchaseAmount;
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
