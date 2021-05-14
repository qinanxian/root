package com.vekai.base.detector;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.raw.lang.ValueObject;

import java.io.Serializable;
import java.util.Map;

/**
 * 检查器上下文对象
 */
public class DetectorContext implements Serializable{
    protected MapObject params = new MapObject();

    /**
     * 设置参数对象
     *
     * @param xpath xpath
     * @param value value
     * @return DetectorContext
     */
    protected DetectorContext setParam(String xpath,Object value){
        this.params.putValue(xpath,value);
        return this;
    }

    protected DetectorContext setParam(Map<String,Object> param){
        params.putAll(param);
        return this;
    }

    public ValueObject getParam(String xpath){
        return this.params.getValue(xpath);
    }
    public <T> T getParam(String xpath,Class<T> classType){
        return this.params.getValue(xpath).objectVal(classType);
    }
}
