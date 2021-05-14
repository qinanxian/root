package com.vekai.fiscal.entity;

import javax.persistence.Table;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 会计期间
 */
@Table(name = "FISC_BOOK_PERIOD")
public class FiscBookPeriodPO implements Serializable, Cloneable {
    /**
     * 期间ID
     */
    @Id
    @GeneratedValue
    private String periodId;
    /**
     * 账套代码
     */
    private String bookCode;
    /**
     * 会计期间;PERIOD_YEAR-PERIOD_TERM
     */
    private Integer periodCode;
    /**
     * 会计期间年份
     */
    private Integer periodYear;
    /**
     * 会计期数
     */
    private Integer periodTerm;
    /**
     * 开始日期
     */
    private Date startDate;
    /**
     * 结束日期
     */
    private Date endDate;
    /**
     * 是否为当前期间
     */
    private String isInUse;
    /**
     * 乐观锁
     */
    private Integer revision;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新人
     */
    private String updatedBy;
    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 期间ID
     */
    public String getPeriodId() {
        return this.periodId;
    }

    /**
     * 期间ID
     */
    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    /**
     * 账套代码
     */
    public String getBookCode() {
        return this.bookCode;
    }

    /**
     * 账套代码
     */
    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    /**
     * 会计期间;PERIOD_YEAR-PERIOD_TERM
     */
    public Integer getPeriodCode() {
        return this.periodCode;
    }

    /**
     * 会计期间;PERIOD_YEAR-PERIOD_TERM
     */
    public void setPeriodCode(Integer periodCode) {
        this.periodCode = periodCode;
    }

    /**
     * 会计期间年份
     */
    public Integer getPeriodYear() {
        return this.periodYear;
    }

    /**
     * 会计期间年份
     */
    public void setPeriodYear(Integer periodYear) {
        this.periodYear = periodYear;
    }

    /**
     * 会计期数
     */
    public Integer getPeriodTerm() {
        return this.periodTerm;
    }

    /**
     * 会计期数
     */
    public void setPeriodTerm(Integer periodTerm) {
        this.periodTerm = periodTerm;
    }

    /**
     * 开始日期
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * 开始日期
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 结束日期
     */
    public Date getEndDate() {
        return this.endDate;
    }

    /**
     * 结束日期
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 是否为当前期间
     */
    public String getIsInUse() {
        return this.isInUse;
    }

    /**
     * 是否为当前期间
     */
    public void setIsInUse(String isInUse) {
        this.isInUse = isInUse;
    }

    /**
     * 乐观锁
     */
    public Integer getRevision() {
        return this.revision;
    }

    /**
     * 乐观锁
     */
    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    /**
     * 创建人
     */
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * 创建人
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * 创建时间
     */
    public Date getCreatedTime() {
        return this.createdTime;
    }

    /**
     * 创建时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 更新人
     */
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * 更新人
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * 更新时间
     */
    public Date getUpdatedTime() {
        return this.updatedTime;
    }

    /**
     * 更新时间
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}