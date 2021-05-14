package com.vekai.crops.customer.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CUST_STOCK")
public class CustStockPO implements Serializable, Cloneable {

    @Id
    @GeneratedValue
    private String id;
    private String custId;
    private Date ipoDate;
    private String stockType;
    private String bourseName;
    private String stockCode;
    private String stockName;
    private String ipoCurrency;
    private Double ipoAmount;
    private Double ipoNumber;
    private Double capitalStockNumber;
    private Double shareStockNumber;

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

    public Date getIpoDate() {
        return ipoDate;
    }

    public void setIpoDate(Date ipoDate) {
        this.ipoDate = ipoDate;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public String getBourseName() {
        return bourseName;
    }

    public void setBourseName(String bourseName) {
        this.bourseName = bourseName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getIpoCurrency() {
        return ipoCurrency;
    }

    public void setIpoCurrency(String ipoCurrency) {
        this.ipoCurrency = ipoCurrency;
    }

    public Double getIpoAmount() {
        return ipoAmount;
    }

    public void setIpoAmount(Double ipoAmount) {
        this.ipoAmount = ipoAmount;
    }

    public Double getIpoNumber() {
        return ipoNumber;
    }

    public void setIpoNumber(Double ipoNumber) {
        this.ipoNumber = ipoNumber;
    }

    public Double getCapitalStockNumber() {
        return capitalStockNumber;
    }

    public void setCapitalStockNumber(Double capitalStockNumber) {
        this.capitalStockNumber = capitalStockNumber;
    }

    public Double getShareStockNumber() {
        return shareStockNumber;
    }

    public void setShareStockNumber(Double shareStockNumber) {
        this.shareStockNumber = shareStockNumber;
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
