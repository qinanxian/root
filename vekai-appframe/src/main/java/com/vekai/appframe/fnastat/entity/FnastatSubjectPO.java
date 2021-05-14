package com.vekai.appframe.fnastat.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by apachechen on 2018/1/30.
 */
@Table(name = "CONF_FNASTAT_SUBJECT")
public class FnastatSubjectPO implements Serializable, Cloneable{

    @Id
    private String fnastatDefId;
    @Id
    private String subjectId;
    private String subjectName;
    private String subjectUnifiedCode;
    private String displayOrder;
    private String subjectDescribe;
    private String subjectStyle;
    private String dataColumns;
    private String persistence;
    @Column(name = "VALUE1_EXPRESSION")
    private String value1Expression;
    @Column(name = "VALUE2_EXPRESSION")
    private String value2Expression;
    @Column(name = "VALUE3_EXPRESSION")
    private String value3Expression;
    @Column(name = "VALUE4_EXPRESSION")
    private String value4Expression;
    @Column(name = "VALUE5_EXPRESSION")
    private String value5Expression;
    @Column(name = "VALUE6_EXPRESSION")
    private String value6Expression;
    @Column(name = "VALUE7_EXPRESSION")
    private String value7Expression;
    @Column(name = "VALUE8_EXPRESSION")
    private String value8Expression;
    @Column(name = "VALUE9_EXPRESSION")
    private String value9Expression;
    @Column(name = "VALUE10_EXPRESSION")
    private String value10Expression;
    @Column(name = "VALUE11_EXPRESSION")
    private String value11Expression;
    @Column(name = "VALUE12_EXPRESSION")
    private String value12Expression;
    @Column(name = "VALUE13_EXPRESSION")
    private String value13Expression;
    @Column(name = "VALUE14_EXPRESSION")
    private String value14Expression;
    @Column(name = "VALUE15_EXPRESSION")
    private String value15Expression;
    @Column(name = "VALUE16_EXPRESSION")
    private String value16Expression;
    @Column(name = "VALUE17_EXPRESSION")
    private String value17Expression;
    @Column(name = "VALUE18_EXPRESSION")
    private String value18Expression;

    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    public String getFnastatDefId() {
        return fnastatDefId;
    }

    public void setFnastatDefId(String fnastatDefId) {
        this.fnastatDefId = fnastatDefId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectUnifiedCode() {
        return subjectUnifiedCode;
    }

    public void setSubjectUnifiedCode(String subjectUnifiedCode) {
        this.subjectUnifiedCode = subjectUnifiedCode;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getSubjectDescribe() {
        return subjectDescribe;
    }

    public void setSubjectDescribe(String subjectDescribe) {
        this.subjectDescribe = subjectDescribe;
    }

    public String getSubjectStyle() {
        return subjectStyle;
    }

    public void setSubjectStyle(String subjectStyle) {
        this.subjectStyle = subjectStyle;
    }

    public String getDataColumns() {
        return dataColumns;
    }

    public void setDataColumns(String dataColumns) {
        this.dataColumns = dataColumns;
    }

    public String getPersistence() {
        return persistence;
    }

    public void setPersistence(String persistence) {
        this.persistence = persistence;
    }

    public String getValue1Expression() {
        return value1Expression;
    }

    public void setValue1Expression(String value1Expression) {
        this.value1Expression = value1Expression;
    }

    public String getValue2Expression() {
        return value2Expression;
    }

    public void setValue2Expression(String value2Expression) {
        this.value2Expression = value2Expression;
    }

    public String getValue3Expression() {
        return value3Expression;
    }

    public void setValue3Expression(String value3Expression) {
        this.value3Expression = value3Expression;
    }

    public String getValue4Expression() {
        return value4Expression;
    }

    public void setValue4Expression(String value4Expression) {
        this.value4Expression = value4Expression;
    }

    public String getValue5Expression() {
        return value5Expression;
    }

    public void setValue5Expression(String value5Expression) {
        this.value5Expression = value5Expression;
    }

    public String getValue6Expression() {
        return value6Expression;
    }

    public void setValue6Expression(String value6Expression) {
        this.value6Expression = value6Expression;
    }

    public String getValue7Expression() {
        return value7Expression;
    }

    public void setValue7Expression(String value7Expression) {
        this.value7Expression = value7Expression;
    }

    public String getValue8Expression() {
        return value8Expression;
    }

    public void setValue8Expression(String value8Expression) {
        this.value8Expression = value8Expression;
    }

    public String getValue9Expression() {
        return value9Expression;
    }

    public void setValue9Expression(String value9Expression) {
        this.value9Expression = value9Expression;
    }

    public String getValue10Expression() {
        return value10Expression;
    }

    public void setValue10Expression(String value10Expression) {
        this.value10Expression = value10Expression;
    }

    public String getValue11Expression() {
        return value11Expression;
    }

    public void setValue11Expression(String value11Expression) {
        this.value11Expression = value11Expression;
    }

    public String getValue12Expression() {
        return value12Expression;
    }

    public void setValue12Expression(String value12Expression) {
        this.value12Expression = value12Expression;
    }

    public String getValue13Expression() {
        return value13Expression;
    }

    public void setValue13Expression(String value13Expression) {
        this.value13Expression = value13Expression;
    }

    public String getValue14Expression() {
        return value14Expression;
    }

    public void setValue14Expression(String value14Expression) {
        this.value14Expression = value14Expression;
    }

    public String getValue15Expression() {
        return value15Expression;
    }

    public void setValue15Expression(String value15Expression) {
        this.value15Expression = value15Expression;
    }

    public String getValue16Expression() {
        return value16Expression;
    }

    public void setValue16Expression(String value16Expression) {
        this.value16Expression = value16Expression;
    }

    public String getValue17Expression() {
        return value17Expression;
    }

    public void setValue17Expression(String value17Expression) {
        this.value17Expression = value17Expression;
    }

    public String getValue18Expression() {
        return value18Expression;
    }

    public void setValue18Expression(String value18Expression) {
        this.value18Expression = value18Expression;
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
