package com.vekai.fiscal.book.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vekai.fiscal.entity.FiscBookPO;
import cn.fisok.raw.kit.ListKit;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 账套
 */
public class FiscalBook extends FiscBookPO implements Serializable,Cloneable {
    @Transient
    @JsonIgnore
    private Map<String,FiscalBookEntry> entryMap = new LinkedHashMap<>();
    @Transient
    @JsonIgnore
    private Map<Integer,FiscalBookPeriod> periodMap = new LinkedHashMap<>();

    public FiscalBookEntry getBookEntry(String entryCode){
        return entryMap.get(entryCode);
    }

    public FiscalBookPeriod getBookPeriod(String periodCode){
        return null;
    }

    public Map<String, FiscalBookEntry> getEntryMap() {
        return entryMap;
    }

    public void setEntryMap(Map<String, FiscalBookEntry> entryMap) {
        this.entryMap = entryMap;
    }

    public Map<Integer, FiscalBookPeriod> getPeriodMap() {
        return periodMap;
    }

    public void setPeriodMap(Map<Integer, FiscalBookPeriod> periodMap) {
        this.periodMap = periodMap;
    }

    /**
     * 获取当前正在启用的会计期间
     * @return
     */
    public FiscalBookPeriod getActivePeriod(){
        return periodMap.get(getCurrentTerm());
    }

    public List<FiscalBookEntry> getEntries(){
        List<FiscalBookEntry> retList = ListKit.newArrayList();
        retList.addAll(entryMap.values());
        return retList;
    }
    public List<FiscalBookPeriod> getPeriods(){
        List<FiscalBookPeriod> retList = ListKit.newArrayList();
        retList.addAll(periodMap.values());
        return retList;

    }

}
