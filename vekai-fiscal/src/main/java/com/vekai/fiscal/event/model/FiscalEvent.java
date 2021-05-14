package com.vekai.fiscal.event.model;

import com.vekai.fiscal.event.entity.FiscEventPO;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * 财务事件实例
 * Created by 炮弹侠<yangsong158@qq.com> on 2018-06-10
 */
public class FiscalEvent extends FiscEventPO implements Serializable,Cloneable{
    @Transient
    protected List<FiscalEventParam> params;
    @Transient
    protected List<FiscalEventEntry> entries;
    @Transient
    protected String exchangeRateType;
    @Transient
    protected Double exchangeRate;

    public List<FiscalEventParam> getParams() {
        return params;
    }

    public void setParams(List<FiscalEventParam> params) {
        this.params = params;
    }

    public List<FiscalEventEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<FiscalEventEntry> entries) {
        this.entries = entries;
    }

    public String getExchangeRateType() {
        return exchangeRateType;
    }

    public void setExchangeRateType(String exchangeRateType) {
        this.exchangeRateType = exchangeRateType;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
