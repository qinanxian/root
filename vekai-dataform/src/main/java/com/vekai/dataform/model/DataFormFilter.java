package com.vekai.dataform.model;

import com.vekai.dataform.model.types.ElementFilterComparePattern;

import java.io.Serializable;

/**
 * DataForm的过滤器
 * Created by tisir yangsong158@qq.com on 2017-05-23
 */
public class DataFormFilter implements DataFormStamp, Serializable,Cloneable{

    protected String dataformId;
    protected String code;
    protected String name;
    protected String bindFor;
    protected Boolean enabled = true;
    protected Boolean quick = false;
    protected String sortCode;
    protected ElementFilterComparePattern comparePattern = ElementFilterComparePattern.Equal;

    @Override
    public String getDataformId() {
        return dataformId;
    }

    public void setDataformId(String dataformId) {
        this.dataformId = dataformId;
    }

    public String getCode() {
        return code;
    }

    public DataFormFilter setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataFormFilter setName(String name) {
        this.name = name;
        return this;
    }

    public String getBindFor() {
        return bindFor;
    }

    public DataFormFilter setBindFor(String bindFor) {
        this.bindFor = bindFor;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public DataFormFilter setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Boolean getQuick() {
        return quick;
    }

    public DataFormFilter setQuick(Boolean quick) {
        this.quick = quick;
        return this;
    }

    public ElementFilterComparePattern getComparePattern() {
        return comparePattern;
    }

    public DataFormFilter setComparePattern(ElementFilterComparePattern comparePattern) {
        this.comparePattern = comparePattern;
        return this;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }
}
