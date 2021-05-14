package com.vekai.crops.customer.entity;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CUST_RELATION")
public class CustRelationPO implements Serializable,Cloneable{
  @Id
  @GeneratedValue
  private String id;
  private String custId;
  private String relationship;
  private String relationCustId;
  private String relationCustName;
  private String relationCertType;
  private String relationCertId;
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


  public String getRelationship() {
    return relationship;
  }

  public void setRelationship(String relationship) {
    this.relationship = relationship;
  }


  public String getRelationCustId() {
    return relationCustId;
  }

  public void setRelationCustId(String relationCustId) {
    this.relationCustId = relationCustId;
  }


  public String getRelationCustName() {
    return relationCustName;
  }

  public void setRelationCustName(String relationCustName) {
    this.relationCustName = relationCustName;
  }


  public String getRelationCertType() {
    return relationCertType;
  }

  public void setRelationCertType(String relationCertType) {
    this.relationCertType = relationCertType;
  }


  public String getRelationCertId() {
    return relationCertId;
  }

  public void setRelationCertId(String relationCertId) {
    this.relationCertId = relationCertId;
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
