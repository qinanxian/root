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

import cn.fisok.sqloy.converter.NameConverter;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : MapObject模式数据对象的SQL处理
 */
public abstract class MapObjectSQLKit {


    /**
     * 根据主键属性检查记录是否存在
     *
     * @param jdbcTemplate jdbcTemplate
     * @param table table
     * @param keyColumns keyColumns
     * @param nameConverter nameConverter
     * @return boolean
     */
    public static boolean exists(NamedParameterJdbcTemplate jdbcTemplate,String table, Map<String,Object> keyColumns, NameConverter nameConverter){
        ValidateKit.notEmpty(keyColumns,"check exists, key attributes cannot be empty");
        StringBuilder chkExistsSql = new StringBuilder();
        chkExistsSql.append(" SELECT count(1) FROM ");
        chkExistsSql.append(table);
        chkExistsSql.append(" WHERE ");
        chkExistsSql.append(genWhereClause(keyColumns,nameConverter));
        String sql = chkExistsSql.toString();

        Integer count = jdbcTemplate.queryForObject(sql, keyColumns, new RowMapper<Integer>() {
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt(1);
            }
        });
        return count > 0;
    }

    /**
     * 根据属性生成WHERE子句(不含WHERE)
     *
     * @param keyRow keyRow
     * @param nameConverter nameConverter
     * @return String
     */
    public static String genWhereClause(Map<String, Object> keyRow, NameConverter nameConverter){
        StringBuilder whereClause = new StringBuilder();
        Iterator<String> iterator = keyRow.keySet().iterator();
        while (iterator.hasNext()){
            String property = iterator.next();
            String column = nameConverter.getColumnName(property);
            Object value = keyRow.get(property);
            if(StringKit.isBlank(property))continue;
//            if(value == null|| StringKit.isBlank(value.toString()))continue;

            if(whereClause.length()>0)whereClause.append(" AND ");
            whereClause.append(" ").append(column).append("=:").append(property);
        }
        return whereClause.toString();
    }


    public static String genUpdateSql(String table,Map<String,Object> dataRow,Map<String,Object> keyRow,String optimisticLockProperty, NameConverter nameConverter){
        ValidateKit.notBlank(table,"the updated destination table cannot be empty");
        ValidateKit.notEmpty(dataRow,"when updating, the where condition is not allowed to be empty");


        StringBuilder sql = new StringBuilder();
        StringBuilder updateClause = new StringBuilder();
        boolean optimisticLockValueExists = false;

        Iterator<String> iterator = dataRow.keySet().iterator();
        while (iterator.hasNext()){
            String property = iterator.next();
            if(StringKit.isBlank(property))continue;
            if(keyRow.containsKey(property))continue;   //把主键排队掉
            String column = nameConverter.getColumnName(property);
            Object value = dataRow;

            if(property.equals(optimisticLockProperty)){
                if(value == null|| StringKit.isBlank(value.toString())) {//有乐观锁,但是没值
                    continue;
                }else{
                    updateClause.append(",").append(column).append("=:").append(property);
                    updateClause.append(" +1 ");
                    optimisticLockValueExists = true;
                }
            }else{
                updateClause.append(",").append(column).append("=:").append(property);
            }

        }
        if(updateClause.length()>0)updateClause.deleteCharAt(0);

        sql.append("UPDATE ").append(table).append(" ");
        sql.append("SET ");
        sql.append(updateClause);
        sql.append(" WHERE ");
        sql.append(genWhereClause(keyRow,nameConverter));
        //有乐观锁时,并且乐观锁字段存在时,使用乐观锁控制
        if(optimisticLockProperty != null && dataRow.containsKey(optimisticLockProperty) && optimisticLockValueExists){
            sql.append(" AND ").append(nameConverter.getColumnName(optimisticLockProperty));
            sql.append("=:").append(optimisticLockProperty);
        }

        return sql.toString();
    }

    /**
     * 生成插入语句
     *
     * @param table 目标表
     * @param dataRow 数据集
     * @param optimisticLockProperty 乐观锁
     * @param nameConverter 名称转换
     * @return String
     */
    public static String genInsertSql(String table,Map<String,?> dataRow,String optimisticLockProperty, NameConverter nameConverter){
        ValidateKit.notBlank(table,"the inserted destination table cannot be empty");

        Iterator<String> iterator = dataRow.keySet().iterator();

        StringBuilder insertClause = new StringBuilder();
        StringBuilder insertColumnClause = new StringBuilder();
        StringBuilder insertValueClause = new StringBuilder();

        while (iterator.hasNext()){
            String property = iterator.next();
            if(StringKit.isBlank(property))continue;
            String column = nameConverter.getColumnName(property);
            Object value = dataRow.get(property);
            if(property.equals(optimisticLockProperty)){
                if(value == null||StringKit.isBlank(value.toString())) {
                    continue;
                }
            }
            insertColumnClause.append(",").append(column);
            insertValueClause.append(",:").append(property);
        }
        if(insertColumnClause.length()>0)insertColumnClause.deleteCharAt(0);
        if(insertValueClause.length()>0)insertValueClause.deleteCharAt(0);

        insertClause.append("INSERT INTO ").append(table).append("");
        insertClause.append("(");
        insertClause.append(insertColumnClause);
        insertClause.append(")");
        insertClause.append(" VALUES");
        insertClause.append("(");
        insertClause.append(insertValueClause);
        insertClause.append(")");

        return insertClause.toString();
    }

    public static String genDeleteSql(String table,Map<String,?> dataRow,String optimisticLockProperty, NameConverter nameConverter){
        ValidateKit.notEmpty(dataRow);
        StringBuffer deleteClause = new StringBuffer();
        deleteClause.append("DELETE FROM ").append(table);
        deleteClause.append(" WHERE ");
        Iterator<String> iterator = dataRow.keySet().iterator();
        while (iterator.hasNext()){
            String property = iterator.next();
            if(StringKit.isBlank(property))continue;
            String column = nameConverter.getColumnName(property);
            Object value = dataRow.get(property);
            if(property.equals(optimisticLockProperty)){
                if(value == null||StringKit.isBlank(value.toString())) {
                    continue;
                }
            }
            deleteClause.append(column).append("=:").append(property).append(" AND ");
        }
        return deleteClause.substring(0,deleteClause.length()-5);//把最后的" AND "删除掉
    }

    /**
     * 把空字串处理成NULL,否则插入时,数字类型的,插入空串会报错
     *
     * @param parameters parameters
     */
    public static void emptyStringAsNull(Map<String,?> parameters){
        Iterator<String> iterator = parameters.keySet().iterator();
        while(iterator.hasNext()){
            String column = iterator.next();
            Object value = parameters.get(column);
            if(value!=null&& StringKit.isBlank(value.toString())){
                parameters.put(column,null);
            }
        }
    }
}
