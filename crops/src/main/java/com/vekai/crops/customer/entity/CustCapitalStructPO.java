package com.vekai.crops.customer.entity;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CUST_CAPITAL_STRUCT")
public class CustCapitalStructPO implements Serializable, Cloneable {

  @Id
  @GeneratedValue
  private String relationId;
  private String stockholderType;
  private String stockholderName;
  private String stockholderCertType;
  private String stockholderCertId;
  private String stockholderCustId;
  private String investmentWay;
  private String investmentCurrency;
  private Double investmentRate;
  private Double investmentAmount;
  private Date latestDate;
  private String stockWarrantCode;
  private String intro;
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


  public String getStockholderType() {
    return stockholderType;
  }

  public void setStockholderType(String stockholderType) {
    this.stockholderType = stockholderType;
  }


  public String getStockholderName() {
    return stockholderName;
  }

  public void setStockholderName(String stockholderName) {
    this.stockholderName = stockholderName;
  }


  public String getStockholderCertType() {
    return stockholderCertType;
  }

  public void setStockholderCertType(String stockholderCertType) {
    this.stockholderCertType = stockholderCertType;
  }


  public String getStockholderCertId() {
    return stockholderCertId;
  }

  public void setStockholderCertId(String stockholderCertId) {
    this.stockholderCertId = stockholderCertId;
  }


  public String getStockholderCustId() {
    return stockholderCustId;
  }

  public void setStockholderCustId(String stockholderCustId) {
    this.stockholderCustId = stockholderCustId;
  }


  public String getInvestmentWay() {
    return investmentWay;
  }

  public void setInvestmentWay(String investmentWay) {
    this.investmentWay = investmentWay;
  }


  public String getInvestmentCurrency() {
    return investmentCurrency;
  }

  public void setInvestmentCurrency(String investmentCurrency) {
    this.investmentCurrency = investmentCurrency;
  }


  public Double getInvestmentRate() {
    return investmentRate;
  }

  public void setInvestmentRate(Double investmentRate) {
    this.investmentRate = investmentRate;
  }


  public Double getInvestmentAmount() {
    return investmentAmount;
  }

  public void setInvestmentAmount(Double investmentAmount) {
    this.investmentAmount = investmentAmount;
  }


  public Date getLatestDate() {
    return latestDate;
  }

  public void setLatestDate(Date latestDate) {
    this.latestDate = latestDate;
  }


  public String getStockWarrantCode() {
    return stockWarrantCode;
  }

  public void setStockWarrantCode(String stockWarrantCode) {
    this.stockWarrantCode = stockWarrantCode;
  }


  public String getIntro() {
    return intro;
  }

  public void setIntro(String intro) {
    this.intro = intro;
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
