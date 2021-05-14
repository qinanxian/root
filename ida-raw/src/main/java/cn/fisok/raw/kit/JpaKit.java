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

import groovy.lang.GroovyObject;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 23:02
 * @desc :
 */
public abstract class JpaKit {
    /**
     * 取JPA注解对应的字段名
     *
     * @param classType classType
     * @param fieldName fieldName
     * @return string
     */
    public static String getColumn(Class<?> classType,String fieldName){
        Field field = ClassKit.getField(classType,fieldName);
        return getColumn(classType,field);
    }
    public static String getColumn(Class<?> classType,Field field){
        if(field==null)return null;
        Column column=field.getAnnotation(Column.class);
        if(column != null) return column.name();
        return null;
    }

    /**
     * 把类名转换为表名，如果使用JPA的Table，则JPA注解优先
     *
     * @param classType classType
     * @return String
     */
    public static String getTableName(Class<?> classType){
//        String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, classType.getSimpleName());
        String tableName = StringKit.camelToUnderline(classType.getSimpleName());
//        Table table = classType.getAnnotation(Table.class);
        Table table = getAnnotationTable(classType);
        if(table!=null&& StringKit.isNotBlank(table.name())){
            tableName = table.name();
        }
        return tableName;
    }

    public static Table getAnnotationTable(Class<?> classType){
        Table table = classType.getAnnotation(Table.class);
        if(table == null){
            Class<?> superclass = classType.getSuperclass();
            if(superclass != null && superclass != Object.class){
                table = getAnnotationTable(superclass);
            }
        }
        return table;
    }

    public static List<Field> getGeneratedValueFields(Class<?> classType){
        return ClassKit.getAnnotationFields(classType, GeneratedValue.class);
    }
    /**
     * 取标记为ID的域
     *
     * @param classType classType
     * @return List
     */
    public static List<Field> getIdFields(Class<?> classType){
        return ClassKit.getAnnotationFields(classType, Id.class);
    }

    public static <T> Map<String,Object> getIdMap(T object){
        Map<String,Object> idMap = MapKit.newLinkedHashMap();
        List<Field> idFields = JpaKit.getIdFields(object.getClass());
        for(Field field : idFields){
            idMap.put(field.getName(),BeanKit.getPropertyValue(object,field.getName()));
        }
        return idMap;
    }

    /**
     * 根据类名，取得字段名和属性名的映射关系
     *
     * @param classType classType
     * @return map
     */
    public static Map<String,String> getMappedFields(Class<?> classType){
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(classType);
        Map<String,String> mappedFields = MapKit.newLinkedHashMap();

        for (PropertyDescriptor pd : pds){
            Field field = ClassKit.getField(classType,pd.getName());
            Method readMethod = pd.getReadMethod();
            if(field == null)continue;
            if(readMethod == null)continue;
            if(field.getAnnotation(Transient.class) != null)continue;
            if(readMethod.getAnnotation(Transient.class) != null)continue;

            //Groovy需要忽略metaClass属性
            if(GroovyObject.class.isAssignableFrom(classType)&&"metaClass".equals(field.getName())){
                continue;
            }

//            String columnName = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, field.getName());
            String columnName = StringKit.camelToUnderline(field.getName());
            Column column = field.getAnnotation(Column.class);
            if(column != null && StringKit.isNotBlank(column.name())){
                columnName = column.name();
            }
            mappedFields.put(columnName,field.getName());
        }

        return mappedFields;
    }

    /**
     * 根据类名，取得字段名和属性名的映射关系
     *
     * @param classType classType
     * @return map
     */
    public static Map<String,String> getIdMappedFields(Class<?> classType){
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(classType);
        Map<String,String> mappedFields = MapKit.newLinkedHashMap();

        for (PropertyDescriptor pd : pds){
            Field field = ClassKit.getField(classType,pd.getName());
            Method readMethod = pd.getReadMethod();
            if(field == null)continue;
            if(readMethod == null)continue;
            if(field.getAnnotation(Transient.class) != null)continue;
            if(readMethod.getAnnotation(Transient.class) != null)continue;
            if(readMethod.getAnnotation(Id.class) == null && field.getAnnotation(Id.class) == null)continue;

//            String columnName = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, field.getName());
            String columnName = StringKit.camelToUnderline(field.getName());
            Column column = field.getAnnotation(Column.class);
            if(column != null && StringKit.isNotBlank(column.name())){
                columnName = column.name();
            }
            mappedFields.put(columnName,field.getName());
        }

        return mappedFields;
    }

    public static <T> String getIdWhere(Class<T> classType){
        Map<String,String> columnPropertyMap = JpaKit.getIdMappedFields(classType);
        ValidateKit.notEmpty(columnPropertyMap,"类{0}不存在@Id注解",classType.getName());
        List<String> whereItems = new ArrayList<>();
        for(Map.Entry<String,String> entry : columnPropertyMap.entrySet()){
            whereItems.add(StringKit.format("{0}=:{1}",entry.getKey(),entry.getValue()));
        }
        return StringKit.join(whereItems," AND ");
    }
}
