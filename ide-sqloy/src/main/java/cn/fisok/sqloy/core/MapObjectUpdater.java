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
import cn.fisok.sqloy.converter.NameConverter;
import cn.fisok.sqloy.converter.impl.UnderlinedNameConverter;
import cn.fisok.sqloy.exception.OptimisticLockException;
import cn.fisok.sqloy.kit.MapObjectSQLKit;
import cn.fisok.sqloy.listener.DeleteListener;
import cn.fisok.sqloy.listener.InsertListener;
import cn.fisok.sqloy.listener.UpdateListener;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.raw.support.BeanSelfAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 对MapObject的修改处理
 */
public class MapObjectUpdater extends AbstractUpdater implements BeanSelfAware{
    private MapObjectQuery mapObjectQuery;

    private boolean emptyStringAsNull = true;       //空字串转为空对象
    private String optimisticLockProperty = null;   //乐观锁属性

    public MapObjectQuery getMapObjectQuery() {
        return mapObjectQuery;
    }

    public void setMapObjectQuery(MapObjectQuery mapObjectQuery) {
        this.mapObjectQuery = mapObjectQuery;
    }

    public boolean isEmptyStringAsNull() {
        return emptyStringAsNull;
    }

    public void setEmptyStringAsNull(boolean emptyStringAsNull) {
        this.emptyStringAsNull = emptyStringAsNull;
    }

    public String getOptimisticLockProperty() {
        return optimisticLockProperty;
    }

    public void setOptimisticLockProperty(String optimisticLockProperty) {
        this.optimisticLockProperty = optimisticLockProperty;
    }

    public NameConverter getNameConverter() {
        NameConverter nameConverter = super.getNameConverter();
        if (nameConverter == null)
            nameConverter = new UnderlinedNameConverter();
        return nameConverter;
    }

    /**
     * 获取代理对象自己，用于方法内部自我调用，无法AOP问题
     */
    @Resource(name ="mapObjectUpdater")
    private MapObjectUpdater self;

    public void setSelf(BeanSelfAware self) {
        if(self instanceof MapObjectUpdater) {
            this.self = (MapObjectUpdater) self;
        }
    }
    public MapObjectUpdater self() {
        return self;
    }

    public int insert(String table, MapObject entity, KeyHolder keyholder, InsertListener<MapObject> listener){
        if(listener != null) listener.before(entity);
        if(emptyStringAsNull) MapObjectSQLKit.emptyStringAsNull(entity);  //空串转为空（NULL)

        String sql = MapObjectSQLKit.genInsertSql(table,entity,optimisticLockProperty,getNameConverter());
        SqlParameterSource ps=new MapSqlParameterSource(entity);

        //执行
        SQLKit.logSQLStart(logger,"Map数据插入",prettySqlFormat(sql),entity);
        long startTime = System.currentTimeMillis();
        int r = jdbcTemplate.update(sql,ps,keyholder);
        long endTime = System.currentTimeMillis();
        SQLKit.logSQLEnd(logger,"Map数据插入",r,endTime-startTime);

        if(listener != null) listener.after(entity);
        return r;
    }

    public int insert(String table, MapObject entity, InsertListener<MapObject> listener){
        return insert(table,entity,new GeneratedKeyHolder(),listener);
    }

    public int insert(String table, MapObject entity){
        return insert(table,entity,new GeneratedKeyHolder(),null);
    }

    public int insert(String table, List<MapObject> entityList, InsertListener<List<MapObject>> listener){
        if(listener != null) listener.before(entityList);
        ValidateKit.notEmpty(entityList,"要更新的数据列表不能为空");

        int ret = 0;

        if(entityList.size()==1){
            insert(table,entityList.get(0));
        }else{
            String sql = null;
            SqlParameterSource[] batchArgs = new SqlParameterSource[entityList.size()];
            for(int i=0;i<entityList.size() ;i++){
                MapObject entity = entityList.get(i);
                if(sql==null)sql = MapObjectSQLKit.genInsertSql(table,entity,optimisticLockProperty,getNameConverter());
                if(emptyStringAsNull) MapObjectSQLKit.emptyStringAsNull(entity);  //空串转为空（NULL)
                batchArgs[i]=new MapSqlParameterSource(entity);
            }
            //执行
            SQLKit.logSQLStart(logger,"Map数据批量插入",prettySqlFormat(sql),entityList);
            long startTime = System.currentTimeMillis();
            int[] r = jdbcTemplate.batchUpdate(sql,batchArgs);
            for(int i=0;i<r.length;i++){
                ret += r[i];
            }
            long endTime = System.currentTimeMillis();
            SQLKit.logSQLEnd(logger,"Map数据批量插入",ret,endTime-startTime);
        }

        if(listener != null) listener.after(entityList);
        return ret;
    }

    public int insert(String table,List<MapObject> entityList){
        return insert(table,entityList,null);
    }

    public int update(String table, MapObject entity, MapObject keyAttributes, UpdateListener<MapObject> listener){
        if(listener != null) listener.before(entity);
        if(emptyStringAsNull) MapObjectSQLKit.emptyStringAsNull(entity);  //空串转为空（NULL)

        String sql = MapObjectSQLKit.genUpdateSql(table,entity,keyAttributes,optimisticLockProperty,getNameConverter());
        MapSqlParameterSource ps=new MapSqlParameterSource(entity);
        ps.addValues(keyAttributes);
        //执行
        SQLKit.logSQLStart(logger,"Map数据更新",prettySqlFormat(sql),entity);
        long startTime = System.currentTimeMillis();
        int ret = jdbcTemplate.update(sql,ps);
        //使用了乐观锁，但是更新不到数据的情况下
        if(StringKit.isNotBlank(optimisticLockProperty)&& ret==0){
            throw new OptimisticLockException("SQL[{0}],Parameter:{1}",sql, JSONKit.toJsonString(entity));
        }
        long endTime = System.currentTimeMillis();
        SQLKit.logSQLEnd(logger,"Map数据更新",ret,endTime-startTime);

        if(listener != null) listener.after(entity);
        return ret;
    }
    public int update(String table, MapObject entity, MapObject keyAttributes){
        return update(table,entity,keyAttributes,null);
    }

    public int update(String table, List<MapObject> entityList, List<MapObject> keyAttributesList, UpdateListener<List<MapObject>> listener){
        if(listener != null) listener.before(entityList);
         ValidateKit.notEmpty(entityList,"update data list cannot be empty");
        ValidateKit.notEmpty(keyAttributesList,"update key attributes cannot be empty");

        int ret = 0;

        if(entityList.size()==1){
            update(table,entityList.get(0),keyAttributesList.get(0));
        }else{
            String sql = null;
            SqlParameterSource[] batchArgs = new SqlParameterSource[entityList.size()];
            for(int i=0;i<entityList.size() ;i++){
                Map<String,Object> entity = entityList.get(i);
                if(sql==null)sql = MapObjectSQLKit.genUpdateSql(table,entity,keyAttributesList.get(i),optimisticLockProperty,getNameConverter());
                if(emptyStringAsNull) MapObjectSQLKit.emptyStringAsNull(entity);  //空串转为空（NULL)
                batchArgs[i]=new MapSqlParameterSource(entity);
            }

            SQLKit.logSQLStart(logger,"Map数据更新",prettySqlFormat(sql),entityList);
            long startTime = System.currentTimeMillis();
            int[] r = jdbcTemplate.batchUpdate(sql,batchArgs);
            for(int i=0;i<r.length;i++){
                ret += r[i];
                if(r[i]==0){
                    throw new OptimisticLockException("SQL[{0}],Parameter:{1}",sql, JSONKit.toJsonString(entityList.get(i)));
                }
            }
            long endTime = System.currentTimeMillis();
            SQLKit.logSQLEnd(logger,"Map数据更新",ret,endTime-startTime);
//            logSQL("Batch Update",sql,entityList,entityList.size(),ret,endTime-startTime);
        }

        if(listener != null) listener.after(entityList);
        return ret;
    }

    public int update(String table, List<MapObject> entity, List<MapObject> keyAttributes){
        return update(table,entity,keyAttributes,null);
    }

    public int save(String table, MapObject entity, MapObject keyAttributes, KeyHolder keyholder, InsertListener<MapObject> insertListener, UpdateListener<MapObject> updateListener){
        boolean exists = MapObjectSQLKit.exists(jdbcTemplate,table,keyAttributes,getNameConverter());
        if(exists){
            return self.update(table,entity,keyAttributes,updateListener);
        }else{
            return self.insert(table,entity,keyholder,insertListener);
        }
    }

    public int save(String table, MapObject entity, MapObject keyAttributes, KeyHolder keyHolder){
        return save(table,entity,keyAttributes,keyHolder,null,null);
    }

    public int save(String table, MapObject entity, MapObject keyAttributes){
        return save(table,entity,keyAttributes,new GeneratedKeyHolder());
    }

    public int save(String table, List<MapObject> entityList, List<MapObject> keyAttributesList, InsertListener<List<MapObject>> insertListener, UpdateListener<List<MapObject>> updateListener){
        ValidateKit.notEmpty(entityList,"update data list cannot be empty");
        ValidateKit.notEmpty(keyAttributesList,"update key attributes cannot be empty");
        ValidateKit.isTrue(entityList.size()==keyAttributesList.size(),"data list length must be equal to parameter list length");

        List<Integer> updatedIndexList = new ArrayList<Integer>();
        for(int i=0;i<keyAttributesList.size();i++){
            boolean exists = MapObjectSQLKit.exists(jdbcTemplate,table,keyAttributesList.get(i),getNameConverter());
            if(exists)updatedIndexList.add(i);
        }

        List<MapObject> insertDataList = new ArrayList<MapObject>();
        List<MapObject> updateDataList = new ArrayList<MapObject>();
        List<MapObject> updateKeyList = new ArrayList<MapObject>();

        //把需要插入的部分和更新的部分分出来
        for(int i=0;i<entityList.size();i++){
            if(updatedIndexList.indexOf(i)>=0){
                updateDataList.add(entityList.get(i));
                updateKeyList.add(keyAttributesList.get(i));
            }else{
                insertDataList.add(entityList.get(i));
            }
        }

        int ret = 0;
        if(insertDataList.size()>0){
            ret += self.insert(table,insertDataList,insertListener);
        }
        if(updateDataList.size()>0){
            ret += self.update(table,updateDataList,updateKeyList,updateListener);
        }

        return ret;
    }

    public int save(String table, List<MapObject> entityList, List<MapObject> keyAttributesList){
        return save(table,entityList,keyAttributesList,null,null);
    }

    public int delete(String table, MapObject keyAttributes){
        return delete(table,keyAttributes,null);
    }

    public int delete(String table, MapObject keyAttributes, DeleteListener<MapObject> listener){
        if(listener!=null)listener.before(keyAttributes);

        String sql = MapObjectSQLKit.genDeleteSql(table,keyAttributes,getOptimisticLockProperty(),getNameConverter());
        int r = execute(sql,keyAttributes);

        if(listener!=null)listener.after(keyAttributes);

        return r;
    }

    public int delete(String table,List<MapObject> keyAttributesList){
        return delete(table,keyAttributesList,null);
    }

    public int delete(String table, List<MapObject> keyAttributesList, DeleteListener<List<MapObject>> listener){
        if(listener!=null)listener.before(keyAttributesList);

        int r = 0;
        for(MapObject row : keyAttributesList){
            r += delete(table,row,null);
        }

        if(listener!=null)listener.after(keyAttributesList);

        return r;
    }

}
