package com.vekai.dataform.mapper.impl.po;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by luyu on 2017/12/28.
 */
@Table(name = "FOWK_DATAFORM_FILTER")
public class DataFormFilterPO {
    @Id
    private String dataformId;
    @Id
    private String code;
    private String name;
    @Column(name = "NAME_I18N_CODE")
    private String nameI18nCode;
    private String bindFor;
    private String enabled;
    private String quick;
    private String sortCode;
    private String comparePattern;
    private long revision;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    public String getDataformId() {
        return dataformId;
    }

    public void setDataformId(String dataformId) {
        this.dataformId = dataformId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameI18nCode() {
        return nameI18nCode;
    }

    public void setNameI18nCode(String nameI18nCode) {
        this.nameI18nCode = nameI18nCode;
    }

    public String getBindFor() {
        return bindFor;
    }

    public void setBindFor(String bindFor) {
        this.bindFor = bindFor;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getQuick() {
        return quick;
    }

    public void setQuick(String quick) {
        this.quick = quick;
    }

    public String getComparePattern() {
        return comparePattern;
    }

    public void setComparePattern(String comparePattern) {
        this.comparePattern = comparePattern;
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

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }
}
