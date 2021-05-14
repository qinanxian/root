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

import cn.fisok.sqloy.kit.BeanSQLKit;
import cn.fisok.sqloy.listener.DeleteListener;
import cn.fisok.sqloy.listener.InsertListener;
import cn.fisok.sqloy.listener.UpdateListener;
import cn.fisok.sqloy.serialnum.finder.SerialNumberGeneratorFinder;
import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;
import cn.fisok.raw.kit.*;
import cn.fisok.raw.support.BeanSelfAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Resource;
import javax.persistence.GeneratedValue;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 使用JDBC更新数据，并且使用JavaBean中的映射关系
 */
public class BeanUpdater extends AbstractUpdater implements BeanSelfAware  {
    private BeanQuery beanQuery;
    private SerialNumberGeneratorFinder serialNumberGeneratorFinder;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取代理对象自己，用于方法内部自我调用，无法AOP问题
     */
    @Lazy
    @Resource(name ="beanUpdater")
    private BeanUpdater self;


    public void setSelf(BeanSelfAware self) {
        if(self instanceof BeanUpdater) {
            this.self = (BeanUpdater) self;
        }
    }
    public BeanSelfAware self() {
        return self;
    }

    public BeanQuery setBeanQuery() {
        return beanQuery;
    }

    public void setBeanQuery(BeanQuery beanQuery) {
        this.beanQuery = beanQuery;
    }

    public void setSerialNumberGeneratorFinder(SerialNumberGeneratorFinder finder) {
        this.serialNumberGeneratorFinder = finder;
    }

    public void fillSerialNumberValue(Object object) {
//        List<Field> fields = JpaKit.getGeneratedValueFields(object.getClass());
        List<Field> fields = JpaKit.getIdFields(object.getClass());
        for (Field field : fields) {
            Object fieldValue = BeanKit.getPropertyValue(object, field.getName());
            if (fieldValue == null || StringKit.isBlank(fieldValue.toString())) {
                String generatorId = object.getClass().getName() + "." + field.getName();    //使用默认规则计算出的流水号生成器;

                GeneratedValue gv = field.getAnnotation(GeneratedValue.class);
                if(gv != null){
                    String annoGenerator = gv.generator();                                          //注解上的流水号生成器
                    generatorId = StringKit.nvl(annoGenerator,generatorId);
                }

                SerialNumberGenerator<Object> snGenerator = serialNumberGeneratorFinder.find(generatorId);
                ValidateKit.notNull(snGenerator,"没有找到流水号生成器:[{0}]",generatorId);

                Object snValue = snGenerator.next(generatorId, object);
                BeanKit.setPropertyValue(object, field.getName(), snValue);
            }
        }
    }

    public <T> void fillSerialNumberValues(List<T> objects) {
        Object firstObject = objects.get(0);
        List<Field> fields = JpaKit.getGeneratedValueFields(firstObject.getClass());
        Map<String, String[]> serialNoMap = new HashMap<>();

        for (Field field : fields) {
            //查出需要自动生成序列号的记录位置
            int genCount = 0;
            for(int i=0;i<objects.size();i++){
                Object object = objects.get(i);
                Object fieldValue = BeanKit.getPropertyValue(object, field.getName());
                if (fieldValue != null && StringKit.isNotBlank(fieldValue.toString())) continue;
                genCount ++;        //数量加一下
            }
            if(genCount == 0)continue;

            //把需要的流水号生成出来，放到队列中去
            GeneratedValue gv = field.getAnnotation(GeneratedValue.class);
            String annoGenerator = gv.generator();                                              //注解上的流水号生成器
            String classGenerator = firstObject.getClass().getName() + "." + field.getName();   //使用默认规则计算出的流水号生成器
            String generatorId = StringKit.nvl(annoGenerator,classGenerator);

            SerialNumberGenerator<Object> snGenerator = serialNumberGeneratorFinder.find(generatorId);
            ValidateKit.notNull(snGenerator,"没有找到流水号生成器:[{0}]",generatorId);

            Object[] serialNumberValueArray = snGenerator.nextBatch(generatorId, genCount);
            ArrayDeque<Object> serialNumberQueue = new ArrayDeque<>();
            serialNumberQueue.addAll(ListKit.listOf(serialNumberValueArray));

            //从队列中取出生成的流水号，填写到需要的位置上去
            for(int i=0;i<objects.size();i++){
                Object object = objects.get(i);
                Object fieldValue = BeanKit.getPropertyValue(object, field.getName());
                if (fieldValue != null && StringKit.isNotBlank(fieldValue.toString())) continue;
                BeanKit.setPropertyValue(object, field.getName(),serialNumberQueue.remove());
            }
        }
    }

    public <T> int insert(T object, KeyHolder keyholder, InsertListener<T> listener){
        if(listener != null) listener.before(object);

        String sql = BeanSQLKit.getInsertSql(object.getClass());
        fillSerialNumberValue(object);
        SqlParameterSource sps = new BeanPropertySqlParameterSource(object);

        //执行
        SQLKit.logSQLStart(logger,"Bean插入",prettySqlFormat(sql),object);
        long startTime = System.currentTimeMillis();
        int r = jdbcTemplate.update(sql,sps,keyholder);
        long endTime = System.currentTimeMillis();
        SQLKit.logSQLEnd(logger,"Bean插入",1, endTime - startTime);

        if(listener != null)listener.after(object);

        return r;
    }
    public <T> int insert(T object, InsertListener<T> listener){
        return insert(object,new GeneratedKeyHolder(),listener);
    }
    public <T> int insert(T object){
        return insert(object,null);
    }

    public <T> int insert(List<T> objects, InsertListener<List<T>> listener){
        if(objects==null||objects.size()==0)return 0;
//        ValidateKit.notEmpty(objects,"要插入的数据列表不能为空");
        if(listener != null) listener.before(objects);

        int ret = 0;

        if(objects.size()==1){
            insert(objects.get(0));
        }else{
            String sql = BeanSQLKit.getInsertSql(objects.get(0).getClass());
            fillSerialNumberValues(objects);
            SqlParameterSource[] spsList = new SqlParameterSource[objects.size()];
            for(int i=0;i<objects.size();i++){
                spsList[i] = new BeanPropertySqlParameterSource(objects.get(i));
            }
            //执行
            SQLKit.logSQLStart(logger,"Bean批量插入",prettySqlFormat(sql),objects);
            long startTime = System.currentTimeMillis();
            int[] r = jdbcTemplate.batchUpdate(sql,spsList);
            long endTime = System.currentTimeMillis();
            SQLKit.logSQLEnd(logger,"Bean批量插入",ret,endTime-startTime);

            for(int i=0;i<r.length;i++){
                ret += r[i];
            }
        }

        if(listener != null)listener.after(objects);
        return ret;
    }
    public <T> int insert(List<T> objects){
        return insert(objects,null);
    }

    public <T> int update(T object,UpdateListener<T> listener){
        if(listener != null )listener.before(object);

        String sql = BeanSQLKit.getUpdateSql(object.getClass());
        SqlParameterSource sps = new BeanPropertySqlParameterSource(object);

        SQLKit.logSQLStart(logger,"Bean更新",prettySqlFormat(sql),object);
        long startTime = System.currentTimeMillis();
        int ret = jdbcTemplate.update(sql,sps);
        long endTime = System.currentTimeMillis();
        SQLKit.logSQLEnd(logger,"Bean更新",ret,endTime-startTime);

        if(listener != null) listener.after(object);

        return ret;
    }

    public <T> int update(T object){
        return update(object,null);
    }
    public <T> int update(List<T> objects, UpdateListener<List<T>> listener){
        if(objects==null||objects.size()==0)return 0;
        if(listener != null) listener.before(objects);

        int ret = 0;

        if(objects.size()==1){
            update(objects.get(0));
        }else{
            String sql = BeanSQLKit.getUpdateSql(objects.get(0).getClass());

            SqlParameterSource[] spsList = new SqlParameterSource[objects.size()];
            for(int i=0;i<objects.size();i++){
                spsList[i] = new BeanPropertySqlParameterSource(objects.get(i));
            }
            //执行
            SQLKit.logSQLStart(logger,"Bean批量更新",prettySqlFormat(sql),objects);
            long startTime = System.currentTimeMillis();
            int[] r = jdbcTemplate.batchUpdate(sql,spsList);
            long endTime = System.currentTimeMillis();
            SQLKit.logSQLEnd(logger,"Bean批量更新",ret,endTime-startTime);

            for(int i=0;i<r.length;i++){
                ret += r[i];
            }

        }

        if(listener != null)listener.after(objects);
        return ret;
    }
    public <T> int update(List<T> objects){
        return update(objects,null);
    }

    /**
     * 根据业务主键保存，自动判别是插入还是更新
     *
     * @param object object
     * @param insertListener insertListener
     * @param updateListener updateListener
     * @param <T> T
     * @return T
     */
    public <T> int save(T object, InsertListener<T> insertListener, UpdateListener<T> updateListener){
        Map<String,Object> idMap = JpaKit.getIdMap(object);
        ValidateKit.notEmpty(idMap,"类[{0}]不存在@Id注解",object.getClass().getName());
        boolean exists = beanQuery.selectExistsById(object.getClass(),idMap);
        if(!exists){
            return self.insert(object,insertListener);
//            return insert(object,insertListener);
        }else{
            return self.update(object,updateListener);
//            return update(object,updateListener);
        }
    }
    public <T> int save(T object){
        return save(object,null,null);
    }

    /**
     * 根据业务主键保存，自动判别是插入还是更新
     *
     * @param objects objects
     * @param insertListener insertListener
     * @param updateListener updateListener
     * @param <T> T
     * @return int
     */
    public <T> int save(List<T> objects,InsertListener<List<T>> insertListener, UpdateListener<List<T>> updateListener){
        List<T> insertDataList = ListKit.newArrayList();
        List<T> updateDataList = ListKit.newArrayList();

        for(T object : objects){
            Map<String,Object> idMap = JpaKit.getIdMap(object);
            boolean exists = beanQuery.selectExistsById(object.getClass(),idMap);
            if(exists){
                updateDataList.add(object);
            }else{
                insertDataList.add(object);
            }
        }

        int ret = 0;
        if(insertDataList.size()>0){
            ret += self.insert(insertDataList,insertListener);
        }
        if(updateDataList.size()>0){
            ret += self.update(updateDataList,updateListener);
        }

        return ret;
    }

    public <T> int save(List<T> objects){
        return save(objects,null,null);
    }

    public <T> int delete(T object, DeleteListener<T> listener){
        if(listener != null )listener.before(object);

        String sql = BeanSQLKit.getDeleteSqlByKey(object.getClass());
        SqlParameterSource sps = new BeanPropertySqlParameterSource(object);

        SQLKit.logSQLStart(logger,"Bean删除",prettySqlFormat(sql),object);
        long startTime = System.currentTimeMillis();
        int ret = jdbcTemplate.update(sql,sps);
        long endTime = System.currentTimeMillis();
        SQLKit.logSQLEnd(logger,"Bean删除",ret,endTime-startTime);

        if(listener != null) listener.after(object);

        return ret;
    }

    public <T> int delete(T object){
        return delete(object,null);
    }

    public <T> int delete(List<T> objects,DeleteListener<List<T>> listener){
        if(listener != null )listener.before(objects);

        String sql = BeanSQLKit.getDeleteSqlByKey(objects.get(0).getClass());

        SqlParameterSource[] spsList = new SqlParameterSource[objects.size()];
        for(int i=0;i<objects.size();i++){
            spsList[i] = new BeanPropertySqlParameterSource(objects.get(i));
        }
        //执行
        int ret = 0;

        SQLKit.logSQLStart(logger,"Bean批量删除",prettySqlFormat(sql),objects);
        long startTime = System.currentTimeMillis();
        int[] r = jdbcTemplate.batchUpdate(sql,spsList);
        long endTime = System.currentTimeMillis();
        SQLKit.logSQLEnd(logger,"Bean批量删除",ret,endTime-startTime);

        for(int i=0;i<r.length;i++){
            ret += r[i];
        }

        if(listener != null) listener.after(objects);

        return ret;
    }

    public <T> int delete(List<T> objects){
        return delete(objects,null);
    }

    public <T> int delete(Class<T> tClass) {
        String sql = BeanSQLKit.getDeleteAllSql(tClass);

        //执行
        int ret = 0;

        SQLKit.logSQLStart(logger,"Bean批量更新",prettySqlFormat(sql),MapKit.newEmptyMap());
        long startTime = System.currentTimeMillis();
        int[] r = jdbcTemplate.batchUpdate(sql, new SqlParameterSource[0]);
        long endTime = System.currentTimeMillis();
        SQLKit.logSQLEnd(logger,"Bean批量更新",ret,endTime-startTime);
        for(int i=0;i<r.length;i++){
            ret += r[i];
        }
        return ret;
    }

}
