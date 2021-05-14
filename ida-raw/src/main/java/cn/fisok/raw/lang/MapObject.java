/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.fisok.raw.lang;


import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.ValidateKit;
import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.apache.commons.jxpath.Pointer;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.Locale;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 22:29
 * @desc :
 */
public class MapObject extends LinkedCaseInsensitiveMap<Object> {

    private static final long serialVersionUID = -8545051570671246562L;

    JXPathContext context = null;

    public MapObject() {
    }
    public MapObject(Map<String,?> value) {
        this.putAll(value);
    }

    public static <V> MapObject valueOf(String jsonContent){
        return new MapObject().merge(jsonContent);
    }

    public static <V> MapObject valueOf(Map<String,?> value){
        return new MapObject(value);
    }

    /**
     * 把字串json中的内容合并至对象中
     * @param jsonContent
     * @return
     */
    public MapObject merge(String jsonContent){
        Map<String,Object> data = JSONKit.jsonToMap(jsonContent);
        this.putAll(data);
        return this;
    }

    public static <V> MapObject valueOf(String k1, V v1){
        MapObject object = new MapObject();
        object.putAll(MapKit.mapOf(k1,v1));
        return object;
    }

    public static <V> MapObject valueOf(String k1, V v1, String k2, V v2){
        MapObject object = new MapObject();
        object.putAll(MapKit.mapOf(k1,v1,k2,v2));
        return object;
    }
    public static <V> MapObject valueOf(String k1, V v1, String k2, V v2, String k3, V v3){
        MapObject object = new MapObject();
        object.putAll(MapKit.mapOf(k1,v1,k2,v2,k3,v3));
        return object;
    }
    public static <V> MapObject valueOf(String k1, V v1, String k2, V v2, String k3, V v3, String k4, V v4){
        MapObject object = new MapObject();
        object.putAll(MapKit.mapOf(k1,v1,k2,v2,k3,v3,k4,v4));
        return object;
    }

    public MapObject(Locale locale) {
        super(locale);
    }

    public MapObject(int initialCapacity) {
        super(initialCapacity);
    }

    public MapObject(int initialCapacity, Locale locale) {
        super(initialCapacity, locale);
    }

    public static MapObject build(Map<String,?> map){
        MapObject dataObject = new MapObject();
        dataObject.putAll(map);
        return dataObject;
    }
    public static MapObject buildFromBean(Object bean){
        MapObject dataObject = new MapObject();
        dataObject.putFromBean(bean);
        return dataObject;
    }

    protected void toggleInit(){
        if(context==null){
            context = JXPathContext.newContext(this);
            //xpath路径中,自动创建对象使用的factory
            context.setFactory(getCreateObjectFactory());
        }
    }

    /**
     * 把JavaBean转为DataBox
     *
     * @param bean bean
     * @return map
     */
    public MapObject putFromBean(Object bean) {
        Map<String, Object> data = BeanKit.bean2Map(bean);
        this.putAll(data);
        return this;
    }

    /**
     * 将DataBox转换为JavaBean
     *
     * @param classType classType
     * @param <T> T
     * @return T
     */
    public <T> T toBean(Class<T> classType){
        return BeanKit.map2Bean(this,classType);
    }

    public Map<String,Object> toMap(){
        return (Map<String,Object>)this;
    }

    protected AbstractFactory getCreateObjectFactory(){
        return new AbstractFactory(){
            public boolean createObject(JXPathContext context, Pointer pointer,
                                        Object parent, String name, int index){
                if (parent instanceof MapObject){
                    MapObject dtx = (MapObject)parent;
                    dtx.put(name,new MapObject());
                    return true;
                }else{
                    return false;
                }
            }
        };
    }

    protected String xpath(String p){
        if(p==null)return null;
        return p.replaceAll("\\.","/");
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(String expression, boolean strict, Class<T> classType){
        Object object = getObject(expression,strict);
        if(object==null)return null;
        if(classType.isAssignableFrom(object.getClass())){
            T ret = (T)object;
            //如果获取一个结果是Map类型,但是目标类型是DataBox,则封装转换一下
            if(classType.isAssignableFrom(MapObject.class)&&!(ret instanceof MapObject)&&ret instanceof Map){
                Map<String,Object> mapObject = (Map<String, Object>)ret;
                MapObject dataBox = new MapObject();
                dataBox.putAll(mapObject);
                ret = (T)dataBox;
            }

            return ret;
        }else{
            throw new ClassCastException(object.getClass()+" can not cast to "+classType);
        }
    }

    public <T> T getObject(String expression, Class<T> classType){
        return this.getObject(expression,false,classType);
    }
    /**
     * 取一个子对象
     *
     * @param expression expression
     * @param strict strict
     * @return object
     */
    public Object getObject(String expression, boolean strict){
        Object value = null;
        if(expression.indexOf(".")<0 && expression.indexOf("/")<0){
            value = get(expression);
        }else{
            toggleInit();
            try{
                value = context.getValue(xpath(expression));
            }catch(JXPathNotFoundException e){  //如果表达式不存在,在严格模式下,也抛出错
                if(strict){
                    throw new NullPointerException(e.getMessage());
                }
            }
        }
        if(strict) ValidateKit.notNull(value,"expression:"+expression,",not exits in object:"+toJsonString());
        return value;
    }

    /**
     * 获取一个值对象
     *
     * @param expression xpath表达式
     * @param strict 是否使用严格模式
     * @return ValueObject
     */
    public ValueObject get(String expression, boolean strict){
        Object value = getObject(expression,strict);
        return new ValueObject(value);
    }


    public MapObject getSubDataObject(String expression, boolean strict){
        return getSubObject(expression,strict);
    }


    public MapObject getSubDataObject(String expression){
        return getSubDataObject(expression,false);
    }

    public MapObject getSubObject(String expression, boolean strict){
        Object value = getObject(expression,strict);
        if(value instanceof MapObject){
            return ((MapObject)value);
        }else if(value instanceof Map){
            Map ret = (Map)value;
            return MapObject.valueOf(ret);
        }else{
            return null;
        }
    }

    public MapObject getSubObject(String expression){
        return getSubObject(expression,false);
    }



    public ValueObject getValue(String expression){
        return get(expression,false);
    }

    /**
     * 使用新值替换老值
     * @param expression
     * @param oldValue
     * @param newValue
     */
    public void replaceValue(String expression,Object oldValue,Object newValue){
        toggleInit();
        Object value = this.getValue(expression).objectVal(oldValue.getClass());
        if(value == oldValue){
            this.putValue(expression,newValue);
        }
        if(value == null)return;
        if(value.equals(oldValue)){
            this.putValue(expression,newValue);
        }
    }

    /**
     * 替换KEY
     * @param oldExpression
     * @param newExpression
     */
    public void replaceKey(String oldExpression,String newExpression){
        toggleInit();
        Object value = getObject(xpath(oldExpression),false);
        context.removePath(xpath(oldExpression));
        putValue(newExpression,value);
    }

    public void putValue(String expression, Object value){
        toggleInit();
        context.createPathAndSetValue(xpath(expression),value);
    }

    /**
     * 放入值，如果这个值存在，则就不要放了
     * @param expression
     * @param value
     * @param existsIgnore
     */
    public void putValue(String expression, Object value,boolean existsIgnore){
        ValueObject v = getValue(expression);
        if(!v.isNull()  && existsIgnore){
            return;
        }
        putValue(expression,value);
    }

    public String toJsonString(){
        return toJsonString(false);
    }

    public String toJsonString(boolean prettify){
        return JSONKit.toJsonString(this,prettify);
    }

    public MapObject deepClone(){
        String strContent = this.toJsonString();
        Map<String,Object> mapData = JSONKit.jsonToMap(strContent);
        return MapObject.valueOf(mapData);
    }

}