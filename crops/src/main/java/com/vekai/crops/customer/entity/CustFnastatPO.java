package com.vekai.crops.customer.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="CUST_FNASTAT")
public class CustFnastatPO implements Serializable,Cloneable{
	@Id
	@GeneratedValue
	private String serialNo;
	private String custId;
	private String fnastatDate;
	private String fnastatScope;
	private String fnastatPeriod;
	private String currency;
	private String currencyUnit;
	private String fnastatStatus;
	private String auditStatus;
	private String remark;
	private String createdBy;
	private Date createdTime;
	private String updatedBy;
	private Date updatedTime;
	private String fnastatClass;
	private String accountingFirm;
	private String auditOpinion;
	
	
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getFnastatDate() {
		return fnastatDate;
	}
	public void setFnastatDate(String fnastatDate) {
		this.fnastatDate = fnastatDate;
	}
	public String getFnastatScope() {
		return fnastatScope;
	}
	public void setFnastatScope(String fnastatScope) {
		this.fnastatScope = fnastatScope;
	}
	public String getFnastatPeriod() {
		return fnastatPeriod;
	}
	public void setFnastatPeriod(String fnastatPeriod) {
		this.fnastatPeriod = fnastatPeriod;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrencyUnit() {
		return currencyUnit;
	}
	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}
	public String getFnastatStatus() {
		return fnastatStatus;
	}
	public void setFnastatStatus(String fnastatStatus) {
		this.fnastatStatus = fnastatStatus;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getFnastatClass() {
		return fnastatClass;
	}
	public void setFnastatClass(String fnastatClass) {
		this.fnastatClass = fnastatClass;
	}
	public String getAccountingFirm() {
		return accountingFirm;
	}
	public void setAccountingFirm(String accountingFirm) {
		this.accountingFirm = accountingFirm;
	}
	public String getAuditOpinion() {
		return auditOpinion;
	}
	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}
	
}
