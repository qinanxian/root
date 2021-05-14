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

import cn.fisok.raw.kit.SQLKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.rowmapper.MapObjectRowMapper;
import cn.fisok.sqloy.core.rowmapper.MapObjectCamelRowMapper;
import cn.fisok.raw.kit.MapKit;
import org.springframework.jdbc.core.RowMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 使用JDBC更新数据，针对MapObject的数据查询处理
 */
public class MapObjectQuery extends AbstractQuery{
    private static MapObjectRowMapper defaultRowMapper = new MapObjectCamelRowMapper();

    public MapObjectQuery() {
    }

    public List<MapObject> selectList(String sql, Map<String, ?> parameter){
        SQLKit.logSQLStart(logger,"查询Map列表",prettySqlFormat(sql),parameter);
        long startTime = System.currentTimeMillis();
        List<MapObject> dataList = jdbcTemplate.query(sql,parameter,getRowMapper());
        long endTime = System.currentTimeMillis();
        SQLKit.logSQLEnd(logger,"查询Map列表",dataList.size(), endTime - startTime);

        return dataList;
    }
    public List<MapObject> selectList(String sql){
        return selectList(sql,new HashMap<String, Object>());
    }

    public List<MapObject> selectList(String sql, String k1, Object v1){
        return selectList(sql, MapKit.mapOf(k1,v1));
    }

    public List<MapObject> selectList(String sql, String k1, Object v1, String k2, Object v2){
        return selectList(sql,MapKit.mapOf(k1,v1,k2,v2));
    }

    public List<MapObject> selectList(String sql, String k1, Object v1, String k2, Object v2, String k3, Object v3){
        return selectList(sql,MapKit.mapOf(k1,v1,k2,v2,k3,v3));
    }

    public MapObject selectOne(String sql, Map<String, ?> parameter) {
        List<MapObject> dataList = selectList(sql,parameter);
        if(dataList!=null&&dataList.size()>0)return dataList.get(0);
        return null;
    }

    public MapObject selectOne(String sql) {
        return selectOne(sql,new HashMap<String, Object>());
    }

    public MapObject selectOne(String sql, String k1, Object v1) {
        return selectOne(sql,MapKit.mapOf(k1,v1));
    }

    public MapObject selectOne(String sql, String k1, Object v1, String k2, Object v2) {
        return selectOne(sql,MapKit.mapOf(k1,v1,k2,v2));
    }

    public MapObject selectOne(String sql, String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        return selectOne(sql,MapKit.mapOf(k1,v1,k2,v2,k3,v3));
    }

    public PaginResult<MapObject> selectListPagination(PaginQuery query){
        return selectListPagination(query,getRowMapper());
    }

    public PaginResult<MapObject> selectListPagination(String sql, Map<String,?> paramMap, int index, int size){
        Map<String,Object> queryParamMap = new HashMap<String,Object>();
        queryParamMap.putAll(paramMap);

        PaginQuery query = new PaginQuery();
        query.setQuery(sql);
        query.setParameterMap(queryParamMap);
        query.setIndex(index);
        query.setSize(size);

        return selectListPagination(query);
    }

    public RowMapper getRowMapper() {
        RowMapper rowMapper = super.getRowMapper();
        if(rowMapper==null)rowMapper = defaultRowMapper;
        return rowMapper;
    }

}
