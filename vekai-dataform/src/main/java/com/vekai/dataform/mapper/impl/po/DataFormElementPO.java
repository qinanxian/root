package com.vekai.dataform.mapper.impl.po;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "FOWK_DATAFORM_ELEMENT")
public class DataFormElementPO {
    @Id
    private String dataformId;
    @Id
    private String code;
    private String primaryKey;
    private String primaryKeyGenerator;
    private String sortCode;
    private String name;
    @Column(name = "NAME_I18N_CODE")
    private String nameI18nCode;
    private String placeholder;
    @Column(name = "COLUMN_")
    private String column;
    @Column(name = "TABLE_")
    private String table;
    private String updateable;
    private String persist;
    private String dataType;
    private String defaultValue;
    private String summaryExpression;
    private String enabled;
    private String sortable;
    @Column(name = "GROUP_")
    private String group;
    @Column(name = "GROUP_I18N_CODE")
    private String groupI18nCode;
    private String subGroup;
    @Column(name = "SUB_GROUP_I18N_CODE")
    private String subGroupI18nCode;
    private Integer limitedLength;
    private double multiplier;
    private String readonly;
    private String required;
    private String dataFormat;
    private String maskFormat;
    private Integer decimalDigits;
    private String textAlign;
    private String editStyle;
    private String dictCodeMode;
    private String dictCodeExpr;
    private String dictCodeLazy;
    private String dictCodeTreeLeafOnly;
    private String dictCodeTreeFull;
    private String prefix;
    private String suffix;
    private String tips;
    @Column(name = "TIPS_I18N_CODE")
    private String tipsI18nCode;
    private String note;
    @Column(name = "NOTE_I18N_CODE")
    private String noteI18nCode;
    private String visible;
    private String rank;
    private String mediaQuery;
    private String htmlStyle;
    private long colspan = 1;
    private String eventExpr;
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


    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }


    public String getPrimaryKeyGenerator() {
        return primaryKeyGenerator;
    }

    public void setPrimaryKeyGenerator(String primaryKeyGenerator) {
        this.primaryKeyGenerator = primaryKeyGenerator;
    }


    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
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

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }


    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }


    public String getUpdateable() {
        return updateable;
    }

    public void setUpdateable(String updateable) {
        this.updateable = updateable;
    }


    public String getPersist() {
        return persist;
    }

    public void setPersist(String persist) {
        this.persist = persist;
    }


    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }


    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }


    public String getSummaryExpression() {
        return summaryExpression;
    }

    public void setSummaryExpression(String summaryExpression) {
        this.summaryExpression = summaryExpression;
    }


    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }


    public String getGroupI18nCode() {
        return groupI18nCode;
    }

    public void setGroupI18nCode(String groupI18nCode) {
        this.groupI18nCode = groupI18nCode;
    }


    public String getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(String subGroup) {
        this.subGroup = subGroup;
    }


    public String getSubGroupI18nCode() {
        return subGroupI18nCode;
    }

    public void setSubGroupI18nCode(String subGroupI18nCode) {
        this.subGroupI18nCode = subGroupI18nCode;
    }


    public Integer getLimitedLength() {
        return limitedLength;
    }

    public void setLimitedLength(Integer limitedLength) {
        this.limitedLength = limitedLength;
    }


    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }


    public String getReadonly() {
        return readonly;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }


    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }


    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }


    public String getMaskFormat() {
        return maskFormat;
    }

    public void setMaskFormat(String maskFormat) {
        this.maskFormat = maskFormat;
    }

    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public String getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }


    public String getEditStyle() {
        return editStyle;
    }

    public void setEditStyle(String editStyle) {
        this.editStyle = editStyle;
    }


    public String getDictCodeMode() {
        return dictCodeMode;
    }

    public void setDictCodeMode(String dictCodeMode) {
        this.dictCodeMode = dictCodeMode;
    }


    public String getDictCodeExpr() {
        return dictCodeExpr;
    }

    public void setDictCodeExpr(String dictCodeExpr) {
        this.dictCodeExpr = dictCodeExpr;
    }

    public String getDictCodeLazy() {
        return dictCodeLazy;
    }

    public void setDictCodeLazy(String dictCodeLazy) {
        this.dictCodeLazy = dictCodeLazy;
    }

    public String getDictCodeTreeLeafOnly() {
        return dictCodeTreeLeafOnly;
    }

    public void setDictCodeTreeLeafOnly(String dictCodeTreeLeafOnly) {
        this.dictCodeTreeLeafOnly = dictCodeTreeLeafOnly;
    }

    public String getDictCodeTreeFull() {
        return dictCodeTreeFull;
    }

    public void setDictCodeTreeFull(String dictCodeTreeFull) {
        this.dictCodeTreeFull = dictCodeTreeFull;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }


    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }


    public String getTipsI18nCode() {
        return tipsI18nCode;
    }

    public void setTipsI18nCode(String tipsI18nCode) {
        this.tipsI18nCode = tipsI18nCode;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public String getNoteI18nCode() {
        return noteI18nCode;
    }

    public void setNoteI18nCode(String noteI18nCode) {
        this.noteI18nCode = noteI18nCode;
    }


    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }


    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }


    public String getMediaQuery() {
        return mediaQuery;
    }

    public void setMediaQuery(String mediaQuery) {
        this.mediaQuery = mediaQuery;
    }


    public String getHtmlStyle() {
        return htmlStyle;
    }

    public void setHtmlStyle(String htmlStyle) {
        this.htmlStyle = htmlStyle;
    }


    public long getColspan() {
        return colspan;
    }

    public void setColspan(long colspan) {
        this.colspan = colspan;
    }


    public String getEventExpr() {
        return eventExpr;
    }

    public void setEventExpr(String eventExpr) {
        this.eventExpr = eventExpr;
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

    public String getSortable() {
        return sortable;
    }

    public void setSortable(String sortable) {
        this.sortable = sortable;
    }
}
