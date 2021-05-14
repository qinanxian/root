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
package cn.fisok.sqloy.core;

import cn.fisok.sqloy.core.rowmapper.JpaBeanPropertyRowMapper;
import cn.fisok.raw.kit.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 使用JDBC查询数据，并且映射到JavaBean
 */
public class BeanQuery extends AbstractQuery {
    public BeanQuery() {
    }

    public BeanQuery(NamedParameterJdbcTemplate jdbcOperations) {
        this.jdbcTemplate = jdbcOperations;
    }


    protected <T> RowMapper getRowMapper(Class<T> classType){
        RowMapper rowMapper = null;
        if(Number.class.isAssignableFrom(classType)||CharSequence.class.isAssignableFrom(classType)){
            rowMapper = (ResultSet rs, int rowNum)->{
                    if(classType.isAssignableFrom(Integer.class)){
                        return rs.getInt(1);
                    }else if(classType.isAssignableFrom(Long.class)){
                        return rs.getLong(1);
                    }else if(classType.isAssignableFrom(Double.class)){
                        return rs.getDouble(1);
                    }else if(classType.isAssignableFrom(BigDecimal.class)){
                        return rs.getBigDecimal(1);
                    }else{
                        return rs.getString(1);
                    }
                };
        }else{
            rowMapper = super.getRowMapper();
            if(rowMapper==null) rowMapper = new JpaBeanPropertyRowMapper<T>(classType);
        }
        return rowMapper;
    }

    public <T> List<T> selectList(Class<T> classType, String sql, Map<String, ?> parameter){
        SQLKit.logSQLStart(logger,"查询Bean列表",prettySqlFormat(sql),parameter);
        long startTime = System.currentTimeMillis();
        List<T> dataList = jdbcTemplate.query(sql,parameter, getRowMapper(classType));
        long endTime = System.currentTimeMillis();
        SQLKit.logSQLEnd(logger,"查询Bean列表",dataList.size(), endTime - startTime);

        return dataList;
    }

    public <T> List<T> selectList(Class<T> classType, String sql){
        return selectList(classType,sql,new HashMap<String, Object>());
    }

    public <T> List<T> selectList(Class<T> classType, String sql,String k1,Object v1){
        return selectList(classType,sql, MapKit.mapOf(k1,v1));
    }

    public <T> List<T> selectList(Class<T> classType, String sql,String k1,Object v1,String k2,Object v2){
        return selectList(classType,sql,MapKit.mapOf(k1,v1,k2,v2));
    }

    public <T> List<T> selectList(Class<T> classType, String sql,String k1,Object v1,String k2,Object v2,String k3,Object v3){
        return selectList(classType,sql,MapKit.mapOf(k1,v1,k2,v2,k3,v3));
    }

    public <T> T selectOne(Class<T> classType, String sql, Map<String, ?> parameter) {
        List<T> dataList = selectList(classType,sql,parameter);
        if(dataList!=null&&dataList.size()>0)return dataList.get(0);
        return null;
    }



    public <T> T selectOneById(Class<T> classType, Map<String,?> paraMap){
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT * FROM ").append(JpaKit.getTableName(classType));
        sqlBuffer.append(" WHERE ");
        sqlBuffer.append(JpaKit.getIdWhere(classType));
        String sql = sqlBuffer.toString();

        return selectOne(classType,sql,paraMap);
    }

    private <T,V> Map<String,Object> buildIdValuesToMap(Class<T> classType,V... values){
        Map<String,String> columnPropertyMap = JpaKit.getIdMappedFields(classType);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        int i=0;
        for(Map.Entry<String,String> entry : columnPropertyMap.entrySet()){
            Object v = null;
            if(i<values.length)v = values[i++];
            paramMap.put(entry.getValue(),v);
        }
        return paramMap;
    }

    public <T,V> T selectOneById(Class<T> classType, V... values){
        Map<String,Object> paramMap = buildIdValuesToMap(classType,values);
        return selectOneById(classType,paramMap);
    }

    public <T> boolean selectExistsById(Class<T> classType, Map<String,?> paraMap){
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT count(1) FROM ").append(JpaKit.getTableName(classType));
        sqlBuffer.append(" WHERE ");
        sqlBuffer.append(JpaKit.getIdWhere(classType));
        String sql = sqlBuffer.toString();

        SQLKit.logSQLStart(logger,"根据ID查询是否存在",prettySqlFormat(sql),paraMap);
        long startTime = System.currentTimeMillis();
        int r = jdbcTemplate.queryForObject(sql, paraMap, new RowMapper<Integer>() {
            public Integer mapRow(ResultSet rs, int index) throws SQLException {
                return rs.getInt(1);
            }
        });
        long endTime = System.currentTimeMillis();
        SQLKit.logSQLEnd(logger,"根据ID查询是否存在",1, endTime-startTime);

        return r > 0;
    }

    public <T,V> boolean selectExistsById(Class<T> classType, V... values){
        Map<String,Object> paramMap = buildIdValuesToMap(classType,values);
        return selectExistsById(classType,paramMap);
    }


    public <T> T selectOne(Class<T> classType, String sql) {
        return selectOne(classType,sql,new HashMap<String, Object>());
    }

    public <T> T selectOne(Class<T> classType, String sql,String k1,Object v1) {
        return selectOne(classType,sql,MapKit.mapOf(k1,v1));
    }

    public <T> T selectOne(Class<T> classType, String sql,String k1,Object v1,String k2,Object v2) {
        return selectOne(classType,sql,MapKit.mapOf(k1,v1,k2,v2));
    }

    public <T> T selectOne(Class<T> classType, String sql,String k1,Object v1,String k2,Object v2,String k3,Object v3) {
        return selectOne(classType,sql,MapKit.mapOf(k1,v1,k2,v2,k3,v3));
    }

    public <T> PaginResult<T> selectListPagination(Class<T> classType, PaginQuery query){
        return selectListPagination(query, getRowMapper(classType));
    }

    public <T> PaginResult<T> selectListPagination(Class<T> classType, String sql, Map<String,?> paramMap, int index, int size){
        if(paramMap == null) paramMap = MapKit.newHashMap();

        Map<String,Object> queryParamMap = new HashMap<String,Object>();
        queryParamMap.putAll(paramMap);

        PaginQuery query = new PaginQuery();
        query.setQuery(sql);
        query.setParameterMap(queryParamMap);
        query.setIndex(index);
        query.setSize(size);

        return selectListPagination(classType,query);
    }

}
