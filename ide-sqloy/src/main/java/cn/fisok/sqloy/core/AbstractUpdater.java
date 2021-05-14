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

import cn.fisok.sqloy.converter.NameConverter;
import cn.fisok.raw.kit.SQLKit;
import cn.fisok.sqloy.kit.DBTypeKit;
import com.alibaba.druid.sql.SQLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 顶层数据更新器，抽象化
 */
public class AbstractUpdater {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected NamedParameterJdbcTemplate jdbcTemplate;
//    protected DBType sqlDialectType = DBType.MYSQL;
    private ThreadLocal<NameConverter> nameConverterThreadLocal = new ThreadLocal<NameConverter>();

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DBType getDBType() {
        return DBTypeKit.getDBType(jdbcTemplate);
    }

    protected String prettySqlFormat(String sql) {
        return SQLUtils.format(sql, getDBType().name().toLowerCase(), new SQLUtils.FormatOption(true, true));
    }
//    public DBType getSqlDialectType() {
//        return sqlDialectType;
//    }
//
//    public void setSqlDialectType(DBType sqlDialectType) {
//        this.sqlDialectType = sqlDialectType;
//    }

    public int execute(String sql){
        return execute(sql,new HashMap<String, Object>());
    }
    public int execute(String sql,Map<String,?> paramMap){
        SQLKit.logSQLStart(logger,"执行SQL",prettySqlFormat(sql),paramMap);
        long startTime = System.currentTimeMillis();
        int ret = jdbcTemplate.update(sql,paramMap);
        long endTime = System.currentTimeMillis();
        SQLKit.logSQLEnd(logger,"执行SQL",1, endTime - startTime);
        return ret;
    }

//    protected void logSQL(String summary,String sql,Object param,int paramSize,int result,long costTime){
//        SQLKit.logSQL(logger,summary,sql,param,paramSize,result,costTime);
//    }
//
//    protected NameConverter getNameConverter(){
//        NameConverter nameConverter = nameConverterThreadLocal.get();
//        if(nameConverter==null){
//            nameConverter = new UnderlinedNameConverter();
//        }else{
//            if(remove){
//                nameConverterThreadLocal.remove();
//            }
//        }
//        return nameConverter;
//    }

    protected NameConverter getNameConverter(){
        return nameConverterThreadLocal.get();
    }


    protected AbstractUpdater setNameConverter(NameConverter nameConverter){
        nameConverterThreadLocal.set(nameConverter);
        return this;
    }

    protected AbstractUpdater removeNameConverter(){
        NameConverter nameConverter = nameConverterThreadLocal.get();
        if(nameConverter!=null){
            nameConverterThreadLocal.remove();
        }
        return this;
    }

    public <T> T exec(NameConverter nameConverter, ExecutorFollow<T> executor){
        T r = null;
        try{
            setNameConverter(nameConverter);
            r = executor.impl();
        }catch(Exception e){
            throw e;
        }finally {
            removeNameConverter();
        }

        return r;
    }
}
