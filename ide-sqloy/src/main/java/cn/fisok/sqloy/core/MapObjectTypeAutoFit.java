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
import cn.fisok.sqloy.exception.SqloyException;
import cn.fisok.sqloy.parser.SqlInsertParser;
import cn.fisok.sqloy.parser.SqlUpdateParser;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.PairBond;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 对MapObject的查询以及修改处理，由于无数据类型关联，例如日期类型，在会传入时，可能会被修改为long类型，因此，在插入之前，要进行一些处理
 */
public class MapObjectTypeAutoFit {
    public interface FixDataItem{
        void exec(String valueName);
    }

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private JdbcOperations jdbcOperations;
    private DBType sqlDialectType;

    public MapObjectTypeAutoFit(JdbcOperations jdbcOperations, DBType sqlDialectType) {
        this.jdbcOperations = jdbcOperations;
        this.sqlDialectType = sqlDialectType;
    }

    public SqlRowSetMetaData getMetaData(String table){
        String query = StringKit.format("select * from {0} where 1=2");
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(query, MapKit.newEmptyMap());
        SqlRowSetMetaData meta = rowSet.getMetaData();
        return meta;
    }

    public List<String> getDateColumns(SqlRowSetMetaData meta){
        List<String> fitColumns = new ArrayList<String>();
        for(int i=1;i<=meta.getColumnCount();i++){
            String column = meta.getColumnName(i);
//            String className = meta.getColumnClassName(i);
            int type = meta.getColumnType(i);

            if(type == Types.DATE || type == Types.TIMESTAMP){
                fitColumns.add(column);
            }
        }
        return fitColumns;
    }

    public void fitForUpdate(String sql,FixDataItem fixDataItem){
        SqlUpdateParser parser = null;
        try{
            parser = new SqlUpdateParser(sql,sqlDialectType);
        }catch(Exception e){
            logger.warn("解析SQL出错。SQL："+sql,e);
            return ;
        }
        SqlRowSetMetaData meta = getMetaData(parser.getTable());

        List<String> fitColumns = getDateColumns(meta);
        if(fitColumns.size()==0)return;


        //把日期类型的字段，做个类型转换修正
        List<PairBond<String, String[]>> fields = parser.getFields();
        for(String column : fitColumns){
            for(PairBond<String, String[]> field : fields){
                if(field.getLeft().equalsIgnoreCase(column)){
                    String[] values = field.getRight();
                    if(values==null)continue;
                    for(String v : values){
                        fixDataItem.exec(v);
                    }
                }
            }
        }
    }

    /**
     * 根据数据表，根据数据表的源数据数据类型，更正相应字段的数据类型，典型的如日期被转为了long类型时，需要自动转回
     * @param sql sql
     * @param entity entity
     */
    public void fitForUpdate(String sql,final MapObject entity){
        fitForUpdate(sql, new FixDataItem() {
            public void exec(String valueName) {
                fitDataItem(valueName,entity);
            }
        });
    }

    /**
     * 根据数据表，根据数据表的源数据数据类型，更正相应字段的数据类型，典型的如日期被转为了long类型时，需要自动转回
     * @param sql sql
     * @param entityList entityList
     */
    public void fitForUpdate(String sql,final List<MapObject> entityList){
        fitForUpdate(sql, new FixDataItem() {
            public void exec(String valueName) {
                for(MapObject entity : entityList){
                    fitDataItem(valueName,entity);
                }
            }
        });
    }

    private void fitDataItem(String valueName, MapObject entity){
        if(StringKit.isBlank(valueName))return;
        String vn = valueName.replaceAll("^:","");
        if(StringKit.isBlank(vn))return;
        entity.put(vn,entity.getValue(vn).dateValue());
    }

    public void fitForInsert(String sql,FixDataItem fixDataItem){
        SqlInsertParser parser = null;
        try{
            parser = new SqlInsertParser(sql,sqlDialectType);
        }catch(Exception e){
            throw new SqloyException("解析SQL出错。SQL："+sql,e);
        }
        SqlRowSetMetaData meta = getMetaData(parser.getTable());
        List<String> fitColumns = getDateColumns(meta);

        if(fitColumns.size()==0)return;

        //把日期类型的字段，做个类型转换修正
        List<PairBond<String, String>> fields = parser.getFields();
        for(String column : fitColumns){
            for(PairBond<String, String> field : fields){
                if(field.getLeft().equalsIgnoreCase(column)){
                    String value = field.getRight();
                    fixDataItem.exec(value);
                }
            }
        }
    }

    /**
     * 根据数据表，根据数据表的源数据数据类型，更正相应字段的数据类型，典型的如日期被转为了long类型时，需要自动转回
     * @param sql sql
     * @param entity entity
     */
    public void fitForInsert(String sql,final MapObject entity){
        fitForInsert(sql, new FixDataItem() {
            public void exec(String valueName) {
                fitDataItem(valueName,entity);
            }
        });
    }

    /**
     * 根据数据表，根据数据表的源数据数据类型，更正相应字段的数据类型，典型的如日期被转为了long类型时，需要自动转回
     *
     * @param sql sql
     * @param entityList entityList
     */
    public void fitForInsert(String sql,final List<MapObject> entityList){
        fitForInsert(sql, new FixDataItem() {
            public void exec(String valueName) {
                for(MapObject entity : entityList){
                    fitDataItem(valueName,entity);
                }
            }
        });
    }


}
