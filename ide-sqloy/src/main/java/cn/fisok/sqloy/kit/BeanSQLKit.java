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
package cn.fisok.sqloy.kit;

import cn.fisok.raw.kit.JpaKit;
import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : Bean模式数据对象的SQL处理
 */
public abstract class BeanSQLKit {

//    public final static String createdTimeColName = "CREATED_TIME";
//    public final static String createdByColName = "CREATED_BY";

    /**
     * 取JavaBean主键，KEY=数据库字段，VALUE=JavaBean属性名
     *
     * @param classType classType
     * @return map
     */
    public Map<String,String> getKeyMaps(Class<?> classType){
        Map<String,String> keyMap = new HashMap<String,String>();
        Map<String,String> mappedFields = JpaKit.getMappedFields(classType);
        
        List<Field> idFields = JpaKit.getIdFields(classType);
        ValidateKit.notEmpty(idFields,"类{0}不存在@Id注解",classType.getName());
        for(Field field : idFields){
//            keyList.add(field.getName()+"=:"+field.getName());
        }

        return keyMap;
    }

    public static String getExistsSqlByKey(Class<?> classType) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" SELECT count(1) FROM ");
        sqlBuilder.append(JpaKit.getTableName(classType));
        sqlBuilder.append(" WHERE ");
        List<Field> idFields = JpaKit.getIdFields(classType);
        ValidateKit.notEmpty(idFields,"类{0}不存在@Id注解",classType.getName());
        List<String> whereItems = new ArrayList<String>();
        for(Field field : idFields){
            whereItems.add(field.getName()+"=:"+field.getName());
        }
        sqlBuilder.append(StringKit.join(whereItems," AND "));
        String sql = sqlBuilder.toString();
        return sql;
    }

    public static String getDeleteSqlByKey(Class<?> classType) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" DELETE FROM ");
        sqlBuilder.append(JpaKit.getTableName(classType));
        sqlBuilder.append(" WHERE ");
        sqlBuilder.append(JpaKit.getIdWhere(classType));
        String sql = sqlBuilder.toString();
        return sql;
    }

    public static String getDeleteAllSql(Class<?> classType) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" DELETE FROM ");
        sqlBuilder.append(JpaKit.getTableName(classType));
        return sqlBuilder.toString();
    }

    public static String getInsertSql(Class<?> classType){
        Map<String,String> mappedFields = JpaKit.getMappedFields(classType);
        List<String> columns = ListKit.newArrayList();
        List<String> fields = ListKit.newArrayList();
        for(Map.Entry<String,String> entry : mappedFields.entrySet()){
            columns.add(entry.getKey());
            fields.add(":"+entry.getValue());
        }

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" INSERT INTO ");
        sqlBuilder.append(JpaKit.getTableName(classType));
        sqlBuilder.append("(");
        sqlBuilder.append(StringKit.join(columns,","));
        sqlBuilder.append(") ");
        sqlBuilder.append(" VALUES");
        sqlBuilder.append("(");
        sqlBuilder.append(StringKit.join(fields,","));
        sqlBuilder.append(")");

        return sqlBuilder.toString();
    }

    public static String getUpdateSql(Class<?> classType){
        Map<String,String> mappedFields = JpaKit.getMappedFields(classType);
        Map<String,String> idMap = JpaKit.getIdMappedFields(classType);
        ValidateKit.notEmpty(idMap,"类[{0}]不存在@Id注解",classType.getName());

        List<String> setItems = ListKit.newArrayList();
        List<String> whereItems = ListKit.newArrayList();
        for(Map.Entry<String,String> entry : mappedFields.entrySet()){
//            if (DataSQLKit.createdTimeColName.equals(entry.getKey())
//                    || DataSQLKit.createdByColName.equals(entry.getKey()))
//                continue;
            setItems.add(entry.getKey()+"=:"+entry.getValue());
        }
        for(Map.Entry<String,String> entry : idMap.entrySet()){
            whereItems.add(entry.getKey()+"=:"+entry.getValue());
        }

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" UPDATE ");
        sqlBuilder.append(JpaKit.getTableName(classType));
        sqlBuilder.append(" SET ");
        sqlBuilder.append(StringKit.join(setItems,","));
        sqlBuilder.append(" WHERE ");
        sqlBuilder.append(StringKit.join(whereItems," AND "));

        return sqlBuilder.toString();
    }
}
