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

import cn.fisok.raw.lang.RawException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 22:46
 * @desc : 类反射处理类
 */

public abstract class ClassKit {
    protected static Logger logger = LoggerFactory.getLogger(ClassKit.class);
    public static Stack<ClassLoader> getClassLoaderStack(Class<?> startClass){
        Stack<ClassLoader> stack = new Stack<ClassLoader>();
        if(startClass != null){
            ClassLoader classLoader = startClass.getClassLoader();
            while(classLoader!=null){
                stack.push(classLoader);
                classLoader = classLoader.getParent();
            }
        }
        return stack;
    }

    public static List<Field> getAnnotationFields(Class<?> classType, Class annotation){
        List<Field> fieldList = new ArrayList<Field>(1);
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(classType);
        for (PropertyDescriptor pd : pds){
            Field field = ClassKit.getField(classType,pd.getName());
            if(field == null)continue;
            if(field.getAnnotation(annotation) != null){
                fieldList.add(field);
            }
        }
        return fieldList;
    }
    public static Method getMethod(Class<?> clazz, String methodName){
        Method[] methods = clazz.getMethods();
        for(Method method :methods){
            if(methodName.equals(method.getName())){
                return method;
            }
        }
        return null;
    }

    public static Field getField(Class<?> classType,String fieldName){
        Field field = null;
        try {
            field = classType.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            //如果直接反射找不到，则通过spring的接口，到父类上去找，如果父类也找不到，就说明真的没有了
            field = ReflectionUtils.findField(classType,fieldName);
            if(field == null&&!"class".equals(fieldName)){
                logger.warn(MessageFormat.format("{0}.{1}不存在,请检查",classType.getName(),fieldName));
            }
        }
        return field;
    }

    public static URL getClassURL(Class<?> clazz){
        String className = clazz.getName();
        className = className.replace('.', '/');
        String resource = "/" + className + ".class";
        URL url = clazz.getResource(resource);
        return url;
    }

    public static Class<?> forName(String name){
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RawException(e);
        }
    }

    public static Object newInstance(String className){
        try {
            return newInstance(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new RawException(e);
        }
    }

    public static Object newInstance(Class<?> clazz){
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RawException(e);
        } catch (IllegalAccessException e) {
            throw new RawException(e);
        }
    }

    public static Object newInstance(Class<?> clazz,Class<?>[] constructorParameterTypes,Object[] constructorArgs){
        try {
            Constructor<?> constructor = clazz.getConstructor(constructorParameterTypes);
            return constructor.newInstance(constructorArgs);
        } catch (InstantiationException e) {
            throw new RawException(e);
        } catch (IllegalAccessException e) {
            throw new RawException(e);
        } catch (NoSuchMethodException e) {
            throw new RawException(e);
        } catch (InvocationTargetException e) {
            throw new RawException(e);
        }
    }

}