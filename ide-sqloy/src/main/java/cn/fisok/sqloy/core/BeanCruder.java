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

import cn.fisok.sqloy.dialect.SqlDialect;
import cn.fisok.sqloy.listener.DeleteListener;
import cn.fisok.sqloy.listener.InsertListener;
import cn.fisok.sqloy.listener.UpdateListener;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : JavaBean数据访问器
 */
public class BeanCruder {
    protected BeanQuery beanQuery;
    protected BeanUpdater beanUpdater;

    public BeanCruder(BeanQuery beanQuery, BeanUpdater beanUpdater) {
        this.beanQuery = beanQuery;
        this.beanUpdater = beanUpdater;
    }

    public BeanUpdater getBeanUpdater() {
        return beanUpdater;
    }

    public void setBeanUpdater(BeanUpdater beanUpdater) {
        this.beanUpdater = beanUpdater;
    }

    public BeanQuery getBeanQuery() {
        return beanQuery;
    }

    public void setBeanQuery(BeanQuery beanQuery) {
        this.beanQuery = beanQuery;
    }

    public DBType getDBType(){
        return beanQuery.getDBType();
    }

    public <T> List<T> selectList(Class<T> classType, String sql, Map<String, ?> parameter) {
        return beanQuery.selectList(classType, sql, parameter);
    }

    public <T> List<T> selectList(Class<T> classType, String sql) {
        return beanQuery.selectList(classType, sql);
    }

    public <T> List<T> selectList(Class<T> classType, String sql, String k1, Object v1) {
        return beanQuery.selectList(classType, sql, k1, v1);
    }

    public <T> List<T> selectList(Class<T> classType, String sql, String k1, Object v1, String k2, Object v2) {
        return beanQuery.selectList(classType, sql, k1, v1, k2, v2);
    }

    public <T> List<T> selectList(Class<T> classType, String sql, String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        return beanQuery.selectList(classType, sql, k1, v1, k2, v2, k3, v3);
    }

    public <T> T selectOne(Class<T> classType, String sql, Map<String, ?> parameter) {
        return beanQuery.selectOne(classType, sql, parameter);
    }

    public <T> T selectOneById(Class<T> classType, Map<String, ?> paraMap) {
        return beanQuery.selectOneById(classType, paraMap);
    }

    public <T, V> T selectOneById(Class<T> classType, V... values) {
        return beanQuery.selectOneById(classType, values);
    }

    public <T> boolean selectExistsById(Class<T> classType, Map<String, ?> paraMap) {
        return beanQuery.selectExistsById(classType, paraMap);
    }

    public <T, V> boolean selectExistsById(Class<T> classType, V... values) {
        return beanQuery.selectExistsById(classType, values);
    }

    public <T> T selectOne(Class<T> classType, String sql) {
        return beanQuery.selectOne(classType, sql);
    }

    public <T> T selectOne(Class<T> classType, String sql, String k1, Object v1) {
        return beanQuery.selectOne(classType, sql, k1, v1);
    }

    public <T> T selectOne(Class<T> classType, String sql, String k1, Object v1, String k2, Object v2) {
        return beanQuery.selectOne(classType, sql, k1, v1, k2, v2);
    }

    public <T> T selectOne(Class<T> classType, String sql, String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        return beanQuery.selectOne(classType, sql, k1, v1, k2, v2, k3, v3);
    }

    public <T> PaginResult<T> selectListPagination(Class<T> classType, PaginQuery query) {
        return beanQuery.selectListPagination(classType, query);
    }

    public <T> PaginResult<T> selectListPagination(Class<T> classType, String sql, Map<String, ?> paramMap, int index, int size) {
        return beanQuery.selectListPagination(classType, sql, paramMap, index, size);
    }

    public <T> PaginResult<T> selectListPagination(PaginQuery query, RowMapper<T> rowMapper) {
        return beanQuery.selectListPagination(query, rowMapper);
    }

    public Integer selectCount(String sql, Map<String, ?> paramMap) {
        return beanQuery.selectCount(sql, paramMap);
    }

    public SqlDialect getSqlDialect() {
        return beanQuery.getSqlDialect();
    }

    public Integer selectCount(String sql, String k1, Object v1) {
        return beanQuery.selectCount(sql, k1, v1);
    }

    public Integer selectCount(String sql, String k1, Object v1, String k2, Object v2) {
        return beanQuery.selectCount(sql, k1, v1, k2, v2);
    }

    public Integer selectCount(String sql, String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        return beanQuery.selectCount(sql, k1, v1, k2, v2, k3, v3);
    }

    public <T> int insert(T object, KeyHolder keyholder, InsertListener<T> listener) {
        return beanUpdater.insert(object, keyholder, listener);
    }

    public <T> int insert(T object, InsertListener<T> listener) {
        return beanUpdater.insert(object, listener);
    }

    public <T> int insert(T object) {
        return beanUpdater.insert(object);
    }

    public <T> int insert(List<T> objects, InsertListener<List<T>> listener) {
        return beanUpdater.insert(objects, listener);
    }

    public <T> int insert(List<T> objects) {
        return beanUpdater.insert(objects);
    }

    public <T> int update(T object, UpdateListener<T> listener) {
        return beanUpdater.update(object, listener);
    }

    public <T> int update(T object) {
        return beanUpdater.update(object);
    }

    public <T> int update(List<T> objects, UpdateListener<List<T>> listener) {
        return beanUpdater.update(objects, listener);
    }

    public <T> int update(List<T> objects) {
        return beanUpdater.update(objects);
    }

    public <T> int save(T object, InsertListener<T> insertListener, UpdateListener<T> updateListener) {
        return beanUpdater.save(object, insertListener, updateListener);
    }

    public <T> int save(T object) {
        return beanUpdater.save(object);
    }

    public <T> int save(List<T> objects, InsertListener<List<T>> insertListener, UpdateListener<List<T>> updateListener) {
        return beanUpdater.save(objects, insertListener, updateListener);
    }

    public <T> int save(List<T> objects) {
        return beanUpdater.save(objects);
    }

    public <T> int delete(T object, DeleteListener<T> listener) {
        return beanUpdater.delete(object, listener);
    }

    public <T> int delete(T object) {
        return beanUpdater.delete(object);
    }

    public <T> int delete(List<T> objects, DeleteListener<List<T>> listener) {
        return beanUpdater.delete(objects, listener);
    }

    public <T> int delete(List<T> objects) {
        return beanUpdater.delete(objects);
    }

    public <T> int delete(Class<T> tClass) {
        return beanUpdater.delete(tClass);
    }

    public int execute(String sql) {
        return beanUpdater.execute(sql);
    }

    public int execute(String sql, Map<String, ?> paramMap) {
        return beanUpdater.execute(sql, paramMap);
    }
}
