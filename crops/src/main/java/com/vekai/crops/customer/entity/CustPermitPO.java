package com.vekai.crops.customer.entity;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CUST_PERMIT")
public class CustPermitPO  implements Serializable,Cloneable{

  @Id
  @GeneratedValue
  private String id;
  private String custId;
  private String userId;
  private String orgId;
  private String allowHold;
  private String allowBusiness;
  private String allowEdit;
  private String allowView;
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


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }


  public String getAllowHold() {
    return allowHold;
  }

  public void setAllowHold(String allowHold) {
    this.allowHold = allowHold;
  }


  public String getAllowBusiness() {
    return allowBusiness;
  }

  public void setAllowBusiness(String allowBusiness) {
    this.allowBusiness = allowBusiness;
  }


  public String getAllowEdit() {
    return allowEdit;
  }

  public void setAllowEdit(String allowEdit) {
    this.allowEdit = allowEdit;
  }


  public String getAllowView() {
    return allowView;
  }

  public void setAllowView(String allowView) {
    this.allowView = allowView;
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
