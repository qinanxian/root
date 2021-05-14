package com.vekai.dataform.model;

import com.vekai.dataform.model.types.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tisir yangsong158@qq.com on 2017-05-20
 */
public class DataFormElement implements DataFormStamp,Serializable,Cloneable{
    protected String dataformId;

    /** 字段要素代码(一般情况下为字段名,或别名) */
    protected String code;
    /** 该字段是否为主键 */
    protected Boolean primaryKey = Boolean.FALSE;
    /** 指定主键产生方式,可以是固定值,某个bean实例等.如:AutoIncrement,Default,bean:seq36Radix,class:xxx.xxx.XxxImpl */
    protected String primaryKeyGenerator;
    /** 排序代码 */
    protected String sortCode;
    /** 字段要素显示名称 */
    protected String name;
    /** 字段要素显示名称代码，国际化时，会根据这个值，查找相应的国际化KEY */
    protected String nameI18nCode;
    /** 数据域(一般为:数据表别名.字段名) */
    protected String column;
    /** 数据来源表,如果数据域填写了,则这个字段自动失效,在写入模式入,该字段控制写入的数据表,可以多个,用逗号隔开 */
    protected String table;
    /** 控制字段是否可以更新 */
    protected Boolean updateable = true;
    /** 控制字段是否可以排序 */
    protected Boolean sortable = false;
    /** 控制本元素是否做持久化,用于解决虚字段问题 */
    protected Boolean persist = true;
    /** 字段数据类型 */
    protected ElementDataType dataType = ElementDataType.String;
    /** 小数位数(-1表示不限制，默认保留19位小数） */
    protected Integer decimalDigits = -1;
    /** 默认值*/
    protected String defaultValue;
    /** 字段统计表达式,如SUM($COLUMN},AVG($COLUMN),MAX($COLUMN) */
    protected String summaryExpression;
    /** 字段是否可用 */
    protected Boolean enabled = true;
    /** 一级分组：字段分组表达式,如: 10:客户基本信息 或 基本信息 */
    protected String group;
    protected String groupI18nCode;
    /** 二级分组：字段分组表达式,如: 10:客户基本信息 或 基本信息 */
    protected String subGroup;
    protected String subGroupI18nCode;
    /** 限制长度 */
    protected Integer limitedLength=-1;
    /** 数字倍数,一般用在金额为万元或亿元时,实际数据还是在元为单位的情况 */
    protected Double multiplier=1d;
    /** UI界面展示设置 */
    protected FormElementUIHint elementUIHint = new FormElementUIHint();
    /** 校验器设置 */
    protected List<FormElementValidator> validatorList = new ArrayList<FormElementValidator>();
    /** 版本号,每修改一次,加一 */
    protected Integer revision = 0;
    /** 创建人 */
    protected String createdBy;
    /** 创建时间 */
    protected Date createdTime;
    /** 修改人 */
    protected String updatedBy;
    /** 修改时间 */
    protected Date updatedTime;

    public DataFormElement(){
    }

    public DataFormElement(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public DataFormElement(String code, String name, String group) {
        this.code = code;
        this.name = name;
        this.group = group;
    }

    public DataFormElement(String code, String column, String name, String group, ElementDataType dataType) {
        this.code = code;
        this.column = column;
        this.name = name;
        this.group = group;
        this.dataType = dataType;
    }
    public DataFormElement(String code, String column, String name, String group) {
        this.code = code;
        this.column = column;
        this.name = name;
        this.group = group;
    }

    @Override
    public String getDataformId() {
        return dataformId;
    }

    public DataFormElement setDataformId(String dataformId) {
        this.dataformId = dataformId;
        return this;
    }

    public String getCode() {
        return code;
    }

    public DataFormElement setCode(String code) {
        this.code = code;
        return this;
    }

    public Boolean getPrimaryKey() {
        return primaryKey;
    }

    public DataFormElement setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    public String getPrimaryKeyGenerator() {
        return primaryKeyGenerator;
    }

    public DataFormElement setPrimaryKeyGenerator(String primaryKeyGenerator) {
        this.primaryKeyGenerator = primaryKeyGenerator;
        return this;
    }

    public String getSortCode() {
        return sortCode;
    }

    public DataFormElement setSortCode(String sortCode) {
        this.sortCode = sortCode;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataFormElement setName(String name) {
        this.name = name;
        return this;
    }

    public String getNameI18nCode() {
        return nameI18nCode;
    }

    public void setNameI18nCode(String nameI18nCode) {
        this.nameI18nCode = nameI18nCode;
    }

    public String getColumn() {
        return column;
    }

    public DataFormElement setColumn(String column) {
        this.column = column;
        return this;
    }

    public String getTable() {
        return table;
    }

    public DataFormElement setTable(String table) {
        this.table = table;
        return this;
    }

    public Boolean getUpdateable() {
        return updateable;
    }

    public DataFormElement setUpdateable(Boolean updateable) {
        this.updateable = updateable;
        return this;
    }

    public Boolean getSortable() {
        return sortable;
    }

    public DataFormElement setSortable(Boolean sortable) {
        this.sortable = sortable;
        return this;
    }

    public Boolean getPersist() {
        return persist;
    }

    public DataFormElement setPersist(Boolean persist) {
        this.persist = persist;
        return this;
    }

    public ElementDataType getDataType() {
        return dataType;
    }

    public DataFormElement setDataType(ElementDataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public DataFormElement setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public String getSummaryExpression() {
        return summaryExpression;
    }

    public DataFormElement setSummaryExpression(String summaryExpression) {
        this.summaryExpression = summaryExpression;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public DataFormElement setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public DataFormElement setGroup(String group) {
        this.group = group;
        return this;
    }

    public String getGroupI18nCode() {
        return groupI18nCode;
    }

    public DataFormElement setGroupI18nCode(String groupI18nCode) {
        this.groupI18nCode = groupI18nCode;
        return this;
    }

    public String getSubGroup() {
        return subGroup;
    }

    public DataFormElement setSubGroup(String subGroup) {
        this.subGroup = subGroup;
        return this;
    }

    public String getSubGroupI18nCode() {
        return subGroupI18nCode;
    }

    public DataFormElement setSubGroupI18nCode(String subGroupI18nCode) {
        this.subGroupI18nCode = subGroupI18nCode;
        return this;
    }

    public Integer getLimitedLength() {
        return limitedLength;
    }

    public DataFormElement setLimitedLength(Integer limitedLength) {
        this.limitedLength = limitedLength;
        return this;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public DataFormElement setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
        return this;
    }

    public FormElementUIHint getElementUIHint() {
        return elementUIHint;
    }

    public DataFormElement setElementUIHint(FormElementUIHint elementUIHint) {
        this.elementUIHint = elementUIHint;
        return this;
    }

    public List<FormElementValidator> getValidatorList() {
        return validatorList;
    }

    public DataFormElement setValidatorList(List<FormElementValidator> validatorList) {
        this.validatorList = validatorList;
        return this;
    }

    public Integer getRevision() {
        return revision;
    }

    public DataFormElement setRevision(Integer revision) {
        this.revision = revision;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public DataFormElement setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public DataFormElement setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public DataFormElement setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public DataFormElement setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }

    /** 把UI界面暗示指导部分单独组织 */
    public static class FormElementUIHint implements Serializable,Cloneable{
        protected String placeholder = "";
        protected Boolean readonly = false;
        protected Boolean reading = false;
        protected Boolean required = false;
        protected ElementDataFormat dataFormat = ElementDataFormat.String;
        protected String maskFormat;
        protected ElementDataTextAlign textAlign = ElementDataTextAlign.Left;
        protected ElementDataEditStyle editStyle = ElementDataEditStyle.Text;
        protected ElementDataDictCodeMode dictCodeMode;
        protected String dictCodeExpr;
        protected Boolean dictCodeLazy = false;
        protected Boolean dictCodeTreeLeafOnly = false;
        protected Boolean dictCodeTreeFull = false;
        protected String prefix;
        protected String suffix;
        protected String tips;
        protected String tipsI18nCode;
        protected String note;
        protected String noteI18nCode;
        protected Boolean visible = true;
        protected Integer rank = 0;  //层级权重,用于ListItem或ListCard根据此层级权重自动组织前端页面
        protected String mediaQuery = "xs";//媒体查询(允许显示的终端尺寸),用于指导不同终端,是否作展示,
        protected String htmlStyle;
        protected Integer colspan=1;
        protected String eventExpr;

        public String getPlaceholder() {
            return placeholder;
        }

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }

        public Boolean getReadonly() {
            return readonly;
        }

        public FormElementUIHint setReadonly(Boolean readonly) {
            this.readonly = readonly;
            return this;
        }

        public Boolean getReading() {
            return reading;
        }

        public FormElementUIHint setReading(Boolean reading) {
            this.reading = reading;
            return this;
        }

        public Boolean getRequired() {
            return required;
        }

        public FormElementUIHint setRequired(Boolean required) {
            this.required = required;
            return this;
        }

        public ElementDataFormat getDataFormat() {
            return dataFormat;
        }

        public FormElementUIHint setDataFormat(ElementDataFormat dataFormat) {
            this.dataFormat = dataFormat;
            return this;
        }

        public String getMaskFormat() {
            return maskFormat;
        }

        public FormElementUIHint setMaskFormat(String maskFormat) {
            this.maskFormat = maskFormat;
            return this;
        }

        public ElementDataTextAlign getTextAlign() {
            return textAlign;
        }

        public FormElementUIHint setTextAlign(ElementDataTextAlign textAlign) {
            this.textAlign = textAlign;
            return this;
        }

        public ElementDataEditStyle getEditStyle() {
            return editStyle;
        }

        public FormElementUIHint setEditStyle(ElementDataEditStyle editStyle) {
            this.editStyle = editStyle;
            return this;
        }

        public ElementDataDictCodeMode getDictCodeMode() {
            return dictCodeMode;
        }

        public FormElementUIHint setDictCodeMode(ElementDataDictCodeMode dictCodeMode) {
            this.dictCodeMode = dictCodeMode;
            return this;
        }

        public String getDictCodeExpr() {
            return dictCodeExpr;
        }

        public FormElementUIHint setDictCodeExpr(String dictCodeExpr) {
            this.dictCodeExpr = dictCodeExpr;
            return this;
        }

        public Boolean getDictCodeLazy() {
            return dictCodeLazy;
        }

        public void setDictCodeLazy(boolean dictCodeLazy) {
            this.dictCodeLazy = dictCodeLazy;
        }

        public Boolean getDictCodeTreeLeafOnly() {
            return dictCodeTreeLeafOnly;
        }

        public void setDictCodeTreeLeafOnly(Boolean dictCodeTreeLeafOnly) {
            this.dictCodeTreeLeafOnly = dictCodeTreeLeafOnly;
        }

        public Boolean getDictCodeTreeFull() {
            return dictCodeTreeFull;
        }

        public void setDictCodeTreeFull(Boolean dictCodeTreeFull) {
            this.dictCodeTreeFull = dictCodeTreeFull;
        }

        public String getPrefix() {
            return prefix;
        }

        public FormElementUIHint setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public String getSuffix() {
            return suffix;
        }

        public FormElementUIHint setSuffix(String suffix) {
            this.suffix = suffix;
            return this;
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

        public Boolean getVisible() {
            return visible;
        }

        public FormElementUIHint setVisible(Boolean visible) {
            this.visible = visible;
            return this;
        }

        /**
         * 层级权重,用于ListItem或ListCard等,根据层级权重自动组织前端页面
         * @return
         */
        public Integer getRank() {
            return rank;
        }

        /**
         * 层级权重,用于ListItem或ListCard等,根据层级权重自动组织前端页面
         * @param rank
         * @return
         */
        public FormElementUIHint setRank(Integer rank) {
            this.rank = rank;
            return this;
        }

        /**
         * <dt>媒体查询,指导限制最小屏幕的最小尺寸,目前允许的值有:</dt>
         * <dd>xs=[特别小的设备(手机竖屏)]Extra small devices (portrait phones, less than 576px)</dd>
         * <dd>sm=[小的设备(手机横屏)]Small devices (landscape phones, less than 768px)</dd>
         * <dd>md=[中型设备(平板设备)]Medium devices (tablets, less than 992px)</dd>
         * <dd>lg=[大的设备(普通桌面机)]Large devices (desktops, less than 1200px)</dd>
         * <dd>xl=[特别大的设备(宽屏桌面机)]Extra large devices (large desktops)</dd>
         * @return
         */
        public String getMediaQuery() {
            return mediaQuery;
        }

        /**
         * <dt>媒体查询,指导限制最小屏幕的最小尺寸,目前允许的值有:</dt>
         * <dd>xs=[特别小的设备(手机竖屏)]Extra small devices (portrait phones, less than 576px)</dd>
         * <dd>sm=[小的设备(手机横屏)]Small devices (landscape phones, less than 768px)</dd>
         * <dd>md=[中型设备(平板设备)]Medium devices (tablets, less than 992px)</dd>
         * <dd>lg=[大的设备(普通桌面机)]Large devices (desktops, less than 1200px)</dd>
         * <dd>xl=[特别大的设备(宽屏桌面机)]Extra large devices (large desktops)</dd>
         * @param mediaQuery
         * @return
         */
        public FormElementUIHint setMediaQuery(String mediaQuery) {
            this.mediaQuery = mediaQuery;
            return this;
        }

        public String getHtmlStyle() {
            return htmlStyle;
        }

        public FormElementUIHint setHtmlStyle(String htmlStyle) {
            this.htmlStyle = htmlStyle;
            return this;
        }

        public Integer getColspan() {
            return colspan;
        }

        public FormElementUIHint setColspan(Integer colspan) {
            this.colspan = colspan;
            return this;
        }

        public String getEventExpr() {
            return eventExpr;
        }

        public FormElementUIHint setEventExpr(String eventExpr) {
            this.eventExpr = eventExpr;
            return this;
        }
    }

    public static class FormElementValidator implements Serializable,Cloneable {
        protected String code;
        protected ElementValidatorRunAt runAt;
        protected ElementValidatorMode mode;
        protected String expr;
        protected String triggerEvent;
        protected String defaultMessage;
        protected String defaultMessageI18nCode;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public ElementValidatorRunAt getRunAt() {
            return runAt;
        }

        public void setRunAt(ElementValidatorRunAt runAt) {
            this.runAt = runAt;
        }

        public ElementValidatorMode getMode() {
            return mode;
        }

        public void setMode(ElementValidatorMode mode) {
            this.mode = mode;
        }

        public String getExpr() {
            return expr;
        }

        public void setExpr(String expr) {
            this.expr = expr;
        }

        public String getTriggerEvent() {
            return triggerEvent;
        }

        public void setTriggerEvent(String triggerEvent) {
            this.triggerEvent = triggerEvent;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }

        public void setDefaultMessage(String defaultMessage) {
            this.defaultMessage = defaultMessage;
        }

        public String getDefaultMessageI18nCode() {
            return defaultMessageI18nCode;
        }

        public void setDefaultMessageI18nCode(String defaultMessageI18nCode) {
            this.defaultMessageI18nCode = defaultMessageI18nCode;
        }
    }

}
