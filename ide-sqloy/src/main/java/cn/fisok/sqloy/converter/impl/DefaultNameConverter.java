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

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 默认转换器，实际上就是不做转换
 */
public class DefaultNameConverter implements NameConverter {
    public String getPropertyName(String columnName) {
        return columnName;
    }

    public String getColumnName(String propertyName) {
        return propertyName;
    }

    public String getClassName(String tableName) {
        return tableName;
    }

    public String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }
}
