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
package cn.fisok.sqloy.converter.impl;

import cn.fisok.sqloy.converter.NameConverter;
import cn.fisok.raw.kit.ClassKit;
import cn.fisok.raw.kit.JpaKit;
import cn.fisok.raw.kit.StringKit;
import org.springframework.beans.BeanUtils;

import javax.persistence.Transient;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : JPA转换器，使用JPA注解进行转换处理
 */
public class JPANameConverter<T> implements NameConverter {

    private Map<String, String> columnToFieldMap = new HashMap<String, String>();

    public JPANameConverter(Class<T> mappedClass){
        initialize(mappedClass);
    }

    protected void initialize(Class<T> mappedClass){
        this.columnToFieldMap = new HashMap<String, String>();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
        for (PropertyDescriptor pd : pds) {
            Method writeMethod = pd.getWriteMethod();
            if (writeMethod != null) {
                String pdName = pd.getName();
//                String upperUnderscore = StringKit.upperUnderscore(pdName);
                String upperUnderscore = StringKit.camelToUnderline(pdName);
                Field field = ClassKit.getField(mappedClass, pdName);
                //如果数据Field不存在，写方法或Field被票房为Transient,则说明此字段需要忽略
                if (field == null) continue;
                if (writeMethod.getAnnotation(Transient.class) != null) continue;
                if (field.getAnnotation(Transient.class) != null) continue;

                String jpaColumn = JpaKit.getColumn(mappedClass, field);
                if (StringKit.isBlank(jpaColumn)) {
                    jpaColumn = upperUnderscore;
                }
                columnToFieldMap.put(jpaColumn, pd.getName());
            }
        }

    }

    public String getPropertyName(String columnName) {
        return columnToFieldMap.get(columnName);
    }

    public String getColumnName(String propertyName) {
        if(propertyName==null)return propertyName;
//        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, propertyName);
        return StringKit.camelToUnderline(propertyName);
    }

    public String getClassName(String tableName) {
        if(tableName==null)return tableName;
//        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName);
        return StringKit.underlineToCamel(tableName);
    }

    public String getTableName(Class<?> clazz) {
        if(clazz==null)return null;
        String name = clazz.getSimpleName();
//        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name);
        return StringKit.camelToUnderline(name);
    }
}
