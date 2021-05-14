package com.vekai.fiscal.event.service;

import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.lang.MapObject;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 财务事件参数
 * Created by 炮弹侠<yangsong158@qq.com> on 2018-06-10
 */
public class EventData implements Serializable,Cloneable {

    /** 记账原币 */
    private String originalCurrency ;
    /** 原币金额 */
    private Double originalAmt ;
    /** 汇率类型 */
    private String exchangeRateType;
    /** 汇率值 */
    private Double exchangeRateValue;
    /** 记账币种 */
    private String currency ;
    /** 发生金额 */
    private Double occurAmt ;
    /** 业务发生日期 */
    private Date occurTime;
    /** 法人主体 */
    private String mainCorpId ;
    /** 业务对象类型 */
    private String objectType ;
    /** 业务对象号 */
    private String objectId ;

    private MapObject params = new MapObject();

    public EventData(String currency, Double occurAmt) {
        setCurrency(currency);
        setOccurAmt(occurAmt);
    }
    public EventData(String currency, Integer occurAmt) {
        this(currency,(double)occurAmt);
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
        params.put("currency",currency);
    }

    public Date getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(Date occurTime) {
        this.occurTime = occurTime;
        params.put("occurTime",occurTime);
    }

    public void setParams(MapObject params) {
        this.params = params;
    }

    public String getExchangeRateType() {
        return exchangeRateType;
    }

    public void setExchangeRateType(String exchangeRateType) {
        this.exchangeRateType = exchangeRateType;
        params.put("exchangeRateType",exchangeRateType);
    }

    public Double getExchangeRateValue() {
        return exchangeRateValue;
    }

    public void setExchangeRateValue(Double exchangeRateValue) {
        this.exchangeRateValue = exchangeRateValue;
        params.put("exchangeRateValue",exchangeRateValue);
    }

    public String getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(String originalCurrency) {
        this.originalCurrency = originalCurrency;
        params.put("originalCurrency",originalCurrency);
    }

    public Double getOriginalAmt() {
        return originalAmt;
    }

    public void setOriginalAmt(Double originalAmt) {
        this.originalAmt = originalAmt;
        params.put("originalAmt",originalAmt);
    }

    public Double getOccurAmt() {
        return occurAmt;
    }

    public void setOccurAmt(Double occurAmt) {
        this.occurAmt = occurAmt;
        params.put("occurAmt",occurAmt);
    }

    public String getMainCorpId() {
        return mainCorpId;
    }

    public void setMainCorpId(String mainCorpId) {
        this.mainCorpId = mainCorpId;
        params.put("mainCorpId",mainCorpId);
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
        params.put("objectType",objectType);
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
        params.put("objectId",objectId);
    }

    public MapObject getParams() {
        return params;
    }

    public EventData addParam(String key,Object value){
        params.put(key,value);
        return this;
    }

    /**
     * 把一个对象转为参数，添加到参数对象中去
     * @param object
     * @return
     */
    public EventData addParam(Object object){
        if(object instanceof Map){
            Map<String,Object> mapObject =  ((Map<String,Object>)object);
            params.putAll(mapObject);
        }else{
            Map<String, Object> beanMap = BeanKit.bean2Map(object);
            addParam(beanMap);
        }
        return this;
    }
}
