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
package cn.fisok.raw.kit;

import cn.fisok.raw.io.ByteOutputStream;
import cn.fisok.raw.lang.RawException;
import cn.fisok.raw.lang.ValueObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import groovy.lang.GroovyObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.persistence.Transient;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 22:44
 * @desc :
 */
public abstract class BeanKit extends BeanUtils {
    private static Logger logger = LoggerFactory.getLogger(BeanKit.class);

    /**
     * 设置调用：BeanUtils.populate(bean, map) 时，如果Date的值为空，则默认为空
     */
    static{
        ConvertUtils.register(new DateConverter(null), Date.class);
    }


    public static boolean propertyExists(Object object, String propertyName) {
        try {
            BeanUtils.getProperty(object, propertyName);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        } catch (IllegalAccessException e) {
            return false;
        } catch (InvocationTargetException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 复制javaBean属性
     *
     * @param source 数据源对象
     * @param target 复制对的目标对象
     */
    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, null, null);
    }

    private static void copyProperties(Object source, Object target, Class<?> editable, String... ignoreProperties)
            throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = org.springframework.beans.BeanUtils.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);
        if (source instanceof Map) {
            if (target instanceof Map) {
                ((Map) target).putAll((Map)source);
                return;
            } else {
                Map<String, Object> map = (Map<String, Object>) source;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    try {
                        if (null == ignoreList || !ignoreList.contains(key)) {
                            PropertyDescriptor targetPd = org.springframework.beans.BeanUtils.getPropertyDescriptor(target.getClass(), key);
                            if (null == targetPd) continue;
                            Method writeMethod = targetPd.getWriteMethod();
                            setPropertyImpl(entry.getValue(), writeMethod, target);
                        }
                    } catch (Throwable ex) {
                        throw new FatalBeanException(
                                "Could not copy property '" + key + "' from source to target", ex);
                    }
                }
                return;
            }
        }
        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = org.springframework.beans.BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null) {

                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            setPropertyImpl(value, writeMethod, target);
                        } catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }

                    }
                }
            }
        }
    }

    private static void setPropertyImpl(Object propertyValue, Method writeMethod, Object targetBean) throws IllegalAccessException, InvocationTargetException{
        Class<?> writeType = writeMethod.getParameterTypes()[0];
        if (null == propertyValue || ClassUtils.isAssignable(writeType, propertyValue.getClass())) {
            writeMethod.invoke(targetBean, propertyValue);
        } else {
            if (propertyValue.getClass().isEnum() || writeType.isEnum()) {
                if (propertyValue.getClass().isEnum() && writeType.isEnum()) {
                    Class<? extends Enum> en = (Class<? extends Enum>)writeType;
                    writeMethod.invoke(targetBean, Enum.valueOf(en, ((Enum)propertyValue).name()));
                } else if (writeType.isEnum()){
                    Class<? extends Enum> en = (Class<? extends Enum>)writeType;
                    writeMethod.invoke(targetBean, Enum.valueOf(en, propertyValue.toString()));
                } else {
                    writeMethod.invoke(targetBean, ((Enum)propertyValue).name());
                }
            } else {
                writeMethod.invoke(targetBean, BeanUtilsBean.getInstance().getConvertUtils().convert(propertyValue, writeType));
            }
        }
    }


    /**
     * 复制javaBean属性
     *
     * @param source 数据源对象
     * @param target 复制对的目标对象
     */
    public static void copyProperties(Object source, Object target, Map<String, Map<Object, Object>> mapConverter) {
        try {

            //个别字段的值需要作对照转换的，这里来处理
            if (mapConverter != null) {
                Iterator<String> iterator = mapConverter.keySet().iterator();
                while (iterator.hasNext()) {
                    String propName = iterator.next();
                    Object value = getPropertyValue(source, propName);
                    Map<Object, Object> kvMap = mapConverter.get(propName);
                    if (kvMap == null) continue;
                    Object mapValue = kvMap.get(value);
                    setPropertyValue(target, propName, mapValue);
                }
            }
            copyProperties(source, target);
        }  catch (IllegalArgumentException e) {
            logger.debug("copyProperties error ", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T castTo(Object orig, Class<T> requiredType) {
        if (orig == null) return null;
        try {
            Object object = requiredType.newInstance();
            copyProperties(orig, object);
            return (T) object;
        } catch (InstantiationException e) {
            logger.error("", e);
            throw new RuntimeException("", e);
        } catch (IllegalAccessException e) {
            logger.error("", e);
            throw new RuntimeException("", e);
        }
    }

    public static <T> List<T> castListTo(List<?> origList, Class<T> requiredType) {
        if (origList == null) return null;
        List<T> objectList = new ArrayList<T>();
        for (Object object : origList) {
            objectList.add(castTo(object, requiredType));
        }
        return objectList;
    }

    public static final <T> List<Map<String, Object>> bean2MapList(List<T> objects){
        ValidateKit.notEmpty(objects);

        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>(objects.size());
        objects.forEach(o -> {
            mapList.add(bean2Map(o));
        });
        return mapList;
    }
    /**
     * JavaBean转为Map
     *
     * @param bean bean
     * @return map
     * @throws IntrospectionException IntrospectionException
     * @throws IllegalAccessException IllegalAccessException
     * @throws InvocationTargetException InvocationTargetException
     */
    public static final Map<String, Object> bean2Map(Object bean) {
        Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if ("class".equals(propertyName)) {
                    continue;
                }
                //Groovy要忽略getMetaClass
                if (bean instanceof GroovyObject && "metaClass".equals(propertyName)) {
                    continue;
                }


                Method readMethod = descriptor.getReadMethod();
                if (readMethod.isAnnotationPresent(Transient.class)) continue;//忽略掉不需要持久化的
//                Transient transient_ = readMethod.getAnnotation(Transient.class);
//                if(transient_!=null)continue;
                Object result = readMethod.invoke(bean, new Object[0]);
                returnMap.put(propertyName, result);
//                if (result != null) {
//                    returnMap.put(propertyName, result);
//                } else {
//                    returnMap.put(propertyName, null);
//                }
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return returnMap;
    }

    public static <T> T map2Bean(Map<String, Object> map, Class<T> clazz) {
        try {
            String json = JSONKit.OBJECT_MAPPER.writeValueAsString(map);
            return (T) JSONKit.OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T mapFillBean(Map<String, Object> map,T object){
        T bean = object;
        try {
            BeanUtils.populate(bean, map);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return bean;
    }

    public static <T> List<T> map2BeanList(List<Map<String, Object>> mapList, Class<T> clazz) {
        List<T> beanList = new ArrayList<T>();

        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> jsonItem = (Map<String, Object>) mapList.get(i);
            T beanItem = map2Bean(jsonItem, clazz);
            beanList.add(beanItem);
        }

        return beanList;
    }

    public static void setPropertyValue(final Object object, final String name, final Object value){

        if(object instanceof Map){
            Map<String,Object> mapObject =  ((Map<String,Object>)object);
            mapObject.put(name,value);
        }

        if(BeanKit.propertyExists(object,name)){
            try {
                BeanKit.setProperty(object, name, value);
            } catch (IllegalAccessException e) {
                logger.warn("", e);
            } catch (InvocationTargetException e) {
                logger.warn("", e);
            }
        }
    }

    /**
     * 取Bean的属性值，如果Bean为Map，则取map的属性值
     *
     * @param bean bean
     * @param name name
     * @return object
     */
    public static Object getPropertyValue(final Object bean, final String name) {
        if(bean instanceof Map){
            return ((Map)bean).get(name);
        }
        try {
            return BeanUtilsBean.getInstance().getPropertyUtils().getNestedProperty(bean,name);
        } catch (IllegalAccessException e) {
            throw new RawException(e);
        } catch (InvocationTargetException e) {
            throw new RawException(e);
        } catch (NoSuchMethodException e) {
//            throw new FisokException(e);
            return null;
        }
    }

    /**
     * 把getPropertyValue的返回值，用ValueObject封装
     *
     * @param bean bean
     * @param name name
     * @return valueObject
     */
    public static ValueObject getPropertyValueObject(final Object bean, final String name){
        return new ValueObject(getPropertyValue(bean,name));
    }

    public static <T> T deepClone(T object){
        ByteOutputStream outputStream = new ByteOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(object);
            oos.close();
            ObjectInputStream ois = new ObjectInputStream(outputStream.createInputStream());
            T ret = (T) ois.readObject();
            IOKit.close(outputStream);
            IOKit.close(ois);
            return ret;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}

