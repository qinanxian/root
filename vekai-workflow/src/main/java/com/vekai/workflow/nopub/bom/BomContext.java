package com.vekai.workflow.nopub.bom;

import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.lang.MapObject;

import java.util.List;
import java.util.Map;

public class BomContext extends MapObject {

    public BomContext appendObject(String name,Object object){
        this.put(name, MapObject.buildFromBean(object));
        this.put(name,new MapObject().toBean(object.getClass()));
        return this;
    }
    public BomContext appendBom(BomObject bomObject){
        List<BomAttribute> attributes = bomObject.getAttributes();
        for(BomAttribute attribute : attributes){
            this.putValue(attribute.getFullName(), attribute.getExpression());
        }
        return this;
    }

    public void putBean(String name,Object bean){
        Map<String,Object> map = BeanKit.bean2Map(bean);
        put(name, map);
    }

    public void putBean(Object bean){
        Map<String,Object> map = BeanKit.bean2Map(bean);
        this.putAll(map);
    }
}
