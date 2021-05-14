package com.vekai.base.dict.model;

import java.io.Serializable;

public class DictItemEntry implements Serializable {
    private String code;
    private String name;
    private String namePinyin;
    private String nameI18nCode;
    private String value;
    private String sortCode;
    private int hotspot;
    private String correlation;
    private String status;
    private String summary;

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

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public String getNameI18nCode() {
        return nameI18nCode;
    }

    public void setNameI18nCode(String nameI18nCode) {
        this.nameI18nCode = nameI18nCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public void setHotspot(int hotspot) {
        this.hotspot = hotspot;
    }

    public String getCorrelation() {
        return correlation;
    }

    public void setCorrelation(String correlation) {
        this.correlation = correlation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getHotspot() {
        return hotspot;
    }

}
