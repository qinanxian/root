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
import cn.fisok.raw.kit.StringKit;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 下划线和驼峰法的相互转换
 */
public class UnderlinedNameConverter implements NameConverter {

    public String getPropertyName(String columnName) {
        if(columnName==null)return columnName;
//        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
        return StringKit.underlineToCamel(columnName);
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
