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
package cn.fisok.sqloy.converter;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 名称转换器,字段名和属性名,表名和类名之间相互转换
 */
public interface NameConverter {
    /**
     * 把数据表列名转换为字段属性名
     *
     * @param columnName columnName
     * @return String
     */
    String getPropertyName(String columnName);

    /**
     * 把属性名转换为数据表字段名
     *
     * @param propertyName propertyName
     * @return String
     */
    String getColumnName(String propertyName);

    /**
     * 根据表名转换为类名
     *
     * @param tableName tableName
     * @return String
     */
    String getClassName(String tableName);

    /**
     * 把类转换为表名
     *
     * @param clazz clazz
     * @return String
     */
    String getTableName(Class<?> clazz);

}
