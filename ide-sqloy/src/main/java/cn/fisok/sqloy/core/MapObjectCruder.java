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

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.listener.InsertListener;
import cn.fisok.sqloy.listener.UpdateListener;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 使用JDBC更新数据，并且使用MapObject作为结果
 */
public class MapObjectCruder {
    protected MapObjectQuery mapObjectQuery;
    protected MapObjectUpdater mapObjectUpdater;

    public MapObjectCruder(MapObjectQuery MapObjectQuery, MapObjectUpdater MapObjectUpdater) {
        this.mapObjectQuery = MapObjectQuery;
        this.mapObjectUpdater = MapObjectUpdater;
    }

    public MapObjectQuery getMapObjectQuery() {
        return mapObjectQuery;
    }

    public MapObjectUpdater getMapObjectUpdater() {
        return mapObjectUpdater;
    }

    public DBType getDBType(){
        return mapObjectQuery.getDBType();
    }

    public List<MapObject> selectList(String sql, Map<String, ?> parameter) {
        return mapObjectQuery.selectList(sql, parameter);
    }

    public List<MapObject> selectList(String sql) {
        return mapObjectQuery.selectList(sql);
    }

    public List<MapObject> selectList(String sql, String k1, Object v1) {
        return mapObjectQuery.selectList(sql, k1, v1);
    }

    public List<MapObject> selectList(String sql, String k1, Object v1, String k2, Object v2) {
        return mapObjectQuery.selectList(sql, k1, v1, k2, v2);
    }

    public List<MapObject> selectList(String sql, String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        return mapObjectQuery.selectList(sql, k1, v1, k2, v2, k3, v3);
    }

    public MapObject selectOne(String sql, Map<String, ?> parameter) {
        return mapObjectQuery.selectOne(sql, parameter);
    }

    public MapObject selectOne(String sql) {
        return mapObjectQuery.selectOne(sql);
    }

    public MapObject selectOne(String sql, String k1, Object v1) {
        return mapObjectQuery.selectOne(sql, k1, v1);
    }

    public MapObject selectOne(String sql, String k1, Object v1, String k2, Object v2) {
        return mapObjectQuery.selectOne(sql, k1, v1, k2, v2);
    }

    public MapObject selectOne(String sql, String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        return mapObjectQuery.selectOne(sql, k1, v1, k2, v2, k3, v3);
    }

    public PaginResult<MapObject> selectListPagination(PaginQuery query) {
        return mapObjectQuery.selectListPagination(query);
    }

    public PaginResult<MapObject> selectListPagination(String sql, Map<String, ?> paramMap, int index, int size) {
        return mapObjectQuery.selectListPagination(sql, paramMap, index, size);
    }

    public <T> PaginResult<T> selectListPagination(PaginQuery query, RowMapper<T> rowMapper) {
        return mapObjectQuery.selectListPagination(query, rowMapper);
    }

    public Integer selectCount(String sql, Map<String, ?> paramMap) {
        return mapObjectQuery.selectCount(sql, paramMap);
    }

    public Integer selectCount(String sql, String k1, Object v1) {
        return mapObjectQuery.selectCount(sql, k1, v1);
    }

    public Integer selectCount(String sql, String k1, Object v1, String k2, Object v2) {
        return mapObjectQuery.selectCount(sql, k1, v1, k2, v2);
    }

    public Integer selectCount(String sql, String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        return mapObjectQuery.selectCount(sql, k1, v1, k2, v2, k3, v3);
    }

    public int insert(String table, MapObject entity, KeyHolder keyholder, InsertListener<MapObject> listener) {
        return mapObjectUpdater.insert(table, entity, keyholder, listener);
    }

    public int insert(String table, MapObject entity, InsertListener<MapObject> listener) {
        return mapObjectUpdater.insert(table, entity, listener);
    }

    public int insert(String table, MapObject entity) {
        return mapObjectUpdater.insert(table, entity);
    }

    public int insert(String table, List<MapObject> entityList, InsertListener<List<MapObject>> listener) {
        return mapObjectUpdater.insert(table, entityList, listener);
    }

    public int insert(String table, List<MapObject> entityList) {
        return mapObjectUpdater.insert(table, entityList);
    }

    public int update(String table, MapObject entity, MapObject keyAttributes, UpdateListener<MapObject> listener) {
        return mapObjectUpdater.update(table, entity, keyAttributes, listener);
    }

    public int update(String table, MapObject entity, MapObject keyAttributes) {
        return mapObjectUpdater.update(table, entity, keyAttributes);
    }

    public int update(String table, List<MapObject> entityList, List<MapObject> keyAttributesList, UpdateListener<List<MapObject>> listener) {
        return mapObjectUpdater.update(table, entityList, keyAttributesList, listener);
    }

    public int update(String table, List<MapObject> entity, List<MapObject> keyAttributes) {
        return mapObjectUpdater.update(table, entity, keyAttributes);
    }

    public int save(String table, MapObject entity, MapObject keyAttributes, KeyHolder keyholder, InsertListener<MapObject> insertListener, UpdateListener<MapObject> updateListener) {
        return mapObjectUpdater.save(table, entity, keyAttributes, keyholder, insertListener, updateListener);
    }

    public int save(String table, MapObject entity, MapObject keyAttributes, KeyHolder keyHolder) {
        return mapObjectUpdater.save(table, entity, keyAttributes, keyHolder);
    }

    public int save(String table, MapObject entity, MapObject keyAttributes) {
        return mapObjectUpdater.save(table, entity, keyAttributes);
    }

    public int save(String table, List<MapObject> entityList, List<MapObject> keyAttributesList, InsertListener<List<MapObject>> insertListener, UpdateListener<List<MapObject>> updateListener) {
        return mapObjectUpdater.save(table, entityList, keyAttributesList, insertListener, updateListener);
    }

    public int save(String table, List<MapObject> entityList, List<MapObject> keyAttributesList) {
        return mapObjectUpdater.save(table, entityList, keyAttributesList);
    }

    public int execute(String sql) {
        return mapObjectUpdater.execute(sql);
    }

    public int execute(String sql, Map<String, ?> paramMap) {
        return mapObjectUpdater.execute(sql, paramMap);
    }
}
