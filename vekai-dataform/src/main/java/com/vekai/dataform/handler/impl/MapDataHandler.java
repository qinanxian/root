package com.vekai.dataform.handler.impl;

import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.kit.JdbcKit;
import cn.fisok.raw.kit.SQLKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.raw.lang.ValueObject;
import cn.fisok.sqloy.autoconfigure.SqloyProperties;
import cn.fisok.sqloy.converter.NameConverter;
import cn.fisok.sqloy.core.MapObjectCruder;
import cn.fisok.sqloy.core.SqlQuery;
import cn.fisok.sqloy.serialnum.finder.SerialNumberGeneratorFinder;
import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.types.FormDataModelType;
import com.vekai.dataform.util.DataFormUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Map类型数据处理公共部分剥离
 */
public abstract class MapDataHandler {

    @Autowired
    protected MapObjectCruder mapObjectCruder;
    @Autowired
    protected SerialNumberGeneratorFinder serialNumberGeneratorFinder;
    @Autowired
    protected SqloyProperties sqloyProperties;

    public MapObjectCruder getMapObjectCruder() {
        return mapObjectCruder;
    }

    public void setMapObjectCruder(MapObjectCruder mapObjectCruder) {
        this.mapObjectCruder = mapObjectCruder;
    }

    /**
     * 建立列和属性的映射
     * @param dataForm
     * @return
     */
    protected Map<String,String> getColumn2PropertyMap(final DataForm dataForm){
        //
        final Map<String,String> columnPropertyMap = new HashMap<String,String>();
        List<DataFormElement> elements = dataForm.getElements();
        for(DataFormElement element : elements) {
            String colName = element.getColumn();
            String propName = StringKit.nvl(element.getCode(),colName);
            columnPropertyMap.put(colName,propName);
        }
        return columnPropertyMap;
    }

    /**
     * 建立列和属性的映射(仅主键）
     * @param dataForm
     * @return
     */
    protected Map<String,String> getPkColumn2PropertyMap(final DataForm dataForm){
        final Map<String,String> columnPropertyMap = new HashMap<String,String>();
        List<DataFormElement> elements = dataForm.getElements();
        for(DataFormElement element : elements) {
            if(!element.getPrimaryKey())continue;
            String colName = element.getColumn();
            String propName = StringKit.nvl(element.getCode(),colName);
            columnPropertyMap.put(colName,propName);
        }
        return columnPropertyMap;
    }
//    /**
//     * 根据DataForm的配置，设置数据表字段和属性的映射关系
//     * @param dataForm
//     * @return
//     */
//    protected RowMapper<MapData> getDataFormRowMapper(final DataForm dataForm){
//        final Map<String,String> columnPropertyMap = getColumn2PropertyMap(dataForm);
//
//        return new RowMapper<MapData>() {
//            public MapData mapRow(ResultSet rs, int rowNum) throws SQLException {
//                MapData row = new MapData();
//
//                ResultSetMetaData meta = rs.getMetaData();
//                int columnCount = meta.getColumnCount();
//                for(int i=1;i<=columnCount;i++){
//                    String columnName = meta.getColumnName(i);
//                    String propName = columnPropertyMap.get(columnName);
//                    //字段在显示模板中没有指定，则忽略
//                    if(StringKit.isBlank(propName))continue;
//                    Object value = JdbcUtils.getResultSetValue(rs,i);
//                    row.put(propName,value);
//                }
//
//                return row;
//            }
//        };
//    }

    /**
     * 参数mapData中的KEY是属性，需要把它映射转换成数据库的字段
     * @param dataForm
     * @param object
     * @return
     */
    protected MapObject getDbMapData(DataForm dataForm, MapObject object){
        Map<String,String> columnPropertyMap = getColumn2PropertyMap(dataForm);
        MapObject ret = convertDbMapData(dataForm,columnPropertyMap,object);
        return ret;
    }

    /**
     * 参数mapData中的KEY是属性，需要把它映射转换成数据库的字段
     * @param dataForm
     * @param dataList
     * @return
     */
    protected List<MapObject> getDbMapDataList(DataForm dataForm, List<MapObject> dataList){
        List<MapObject> retList = new ArrayList<MapObject>(dataList.size());
        for(MapObject row : dataList){
            retList.add(getDbMapData(dataForm,row));
        }
        return retList;
    }

    /**
     * 根据模板设置，把主键部分抽出来
     * @param dataForm
     * @param object
     * @return
     */
    protected MapObject getDbPkMapData(DataForm dataForm, MapObject object){
        Map<String,String> columnPropertyMap = getPkColumn2PropertyMap(dataForm);
        return convertDbMapData(dataForm,columnPropertyMap,object);
    }

    /**
     * 根据模板设置，把主键部分抽出来
     * @param dataForm
     * @param dataList
     * @return
     */
    protected List<MapObject> getDbPkMapDataList(DataForm dataForm, List<MapObject> dataList){
        List<MapObject> retList = new ArrayList<MapObject>(dataList.size());
        for(MapObject row : dataList){
            retList.add(getDbPkMapData(dataForm,row));
        }
        return retList;
    }

    protected MapObject convertDbMapData(DataForm dataForm, Map<String,String> columnPropertyMap, MapObject object){
        //KEY为数据库字段名
        MapObject dbMapData = new MapObject();

//        //根据显示模板设置，把显示模板上不存在的字段切割掉，同时，作数据类型转换
//        Iterator<String> iterator = object.keySet().iterator();
//        while (iterator.hasNext()){
//            String name = iterator.next();
//            DataFormElement element = dataForm.getElement(name);
//            if(element == null){
//                element = dataForm.getElement(StringKit.camelToUnderline(name));
//            }
//            if(element != null){
//                dbMapData.put(name,DataFormUtils.getMapValue(element,object));
//            }
//        }


        Set<Map.Entry<String, String>> itemSet = columnPropertyMap.entrySet();
        for(Map.Entry<String, String> entry : itemSet){
            String column = entry.getKey();
            String prop = entry.getValue();
//            Object value = object.get(prop);    //取出值

            DataFormElement element = dataForm.getElement(prop);
            //剔除列明为''或者非持久化的元素
//            if ("''".equals(column) || !element.getPersist()) {
//                continue;
//            }
            //不是主键，又设置了不可更新的时候，就不要了
            if (!element.getPrimaryKey() &&
                    (!element.getPersist() || !element.getUpdateable())) {
                continue;
            }
//            //常数字段，不要拼到SQL中去
//            if(SQLKit.isConstColumn(column)){
//                continue;
//            }

//            dbMapData.put(column,value);        //把KEY换成数据列
            //根据显示模板数据类型设置，把数据类型类型单独处理
            dbMapData.put(prop, DataFormUtils.getMapValue(element,object));
        }

        return dbMapData;
    }

    protected NameConverter getNameConverter(final DataForm dataForm){
        return new NameConverter(){
            public String getPropertyName(String columnName) {
                return columnName;
            }
            public String getColumnName(String propertyName) {
                DataFormElement element = dataForm.getElement(propertyName);
                if(element!=null){
                    return StringKit.nvl(element.getColumn(),StringKit.camelToUnderline(propertyName));
                }else{
                    return StringKit.camelToUnderline(propertyName);
                }
            }
            public String getClassName(String tableName) {
                return null;
            }
            public String getTableName(Class<?> clazz) {
                return null;
            }
        };
    }

    protected RowMapper<MapObject> getRowMapper(final DataForm dataForm){
        Map<String,DataFormElement> columnMap = new HashMap<>();
        Set<String> scannedCols = new HashSet<String>();

        dataForm.getElements().forEach(element->{
            String column = element.getColumn();
            String columnFixed = column;
            String code = element.getCode();
            if(StringKit.isBlank(column)){
                column = StringKit.camelToUnderline(element.getCode());
            }
            if(StringKit.isBlank(column))return;

            if(SQLKit.isConstColumn(column) && StringKit.isNotBlank(code)){
                String name = StringKit.camelToUnderline(code);
                columnMap.put(name,element);
            }else if(scannedCols.contains(columnFixed) && StringKit.isNotBlank(code)){
                String name = StringKit.camelToUnderline(code);
                columnMap.put(name,element);
            }else if (SQLKit.isSubSelect(column) && StringKit.isNotBlank(code)) {
                String name = StringKit.camelToUnderline(code);
                columnMap.put(name,element);
            } else{
                columnMap.put(column,element);
            }
            scannedCols.add(columnFixed);
        });

        return new RowMapper<MapObject>(){
            public MapObject mapRow(ResultSet rs, int rowNum) throws SQLException {
                MapObject row = new MapObject(columnMap.size());

                ResultSetMetaData metaData = rs.getMetaData();
                int count = metaData.getColumnCount();
                for(int i=1;i<=count;i++){
                    String column = metaData.getColumnLabel(i);
                    if(StringKit.isBlank(column)){
                        column = metaData.getColumnName(i);
                    }
                    DataFormElement element = columnMap.get(column);
                    if(element == null)continue;

                    String code = StringKit.nvl(element.getCode(),StringKit.underlineToCamel(column));

                    Object v = JdbcKit.getResultSetValue(rs,i);
                    row.put(code,v);
                }

                return row;
            }
        };
    }

    /**
     * 没有流水号的情况下，根据配置的流水号生成器或者默认的流水号生成器，生成流水号
     * @param dataForm
     * @param row
     */
    protected void togglePrimaryKey(DataForm dataForm, MapObject row){
        List<DataFormElement> keyElements = dataForm.getPrimaryKeyElements();
        for(DataFormElement element : keyElements){
            String name = element.getCode();
            if(row.getValue(name).isBlank()){//如果主键为空
                String generatorId = element.getPrimaryKeyGenerator();
                SerialNumberGenerator generator = serialNumberGeneratorFinder.find(generatorId);
                if(generator == null){
                    String defaultGenerator = sqloyProperties.getSerialNumber().getDefaultGenerator();
                    generator = ApplicationContextHolder.getBean(defaultGenerator, SerialNumberGenerator.class);
                }
                ValidateKit.notNull(generator,"没有找到流水号生成器,默认流水号生成器也没有找到");
                String genKey = dataForm.getDataModel()+"."+element.getColumn();
                ValueObject value = ValueObject.valueOf(generator.next(genKey));


                row.put(name,value.value());
            }
        }
    }

    protected MapObject remoldMapData(DataForm dataForm, MapObject row){
        if(row==null)return null;
        MapObject ret = new MapObject();
        List<DataFormElement> elements = dataForm.getElements();
        for(DataFormElement element : elements){
            ret.put(element.getCode(),row.get(element.getCode()));
        }
        return ret;
    }

    protected List<MapObject> remoldMapDataList(DataForm dataForm, List<MapObject> rows){
        List<MapObject> dataList = new ArrayList<>(rows.size());
        for(MapObject row : rows){
            dataList.add(remoldMapData(dataForm,row));
        }
        return dataList;
    }

    protected void validateDataForm(DataForm dataForm){
        ValidateKit.notNull(dataForm);
        String table = dataForm.getDataModel();
        SqlQuery query = dataForm.getQuery();
        if (dataForm.getDataModelType() == FormDataModelType.JavaBean) {
            ValidateKit.notBlank(table,"dataform={0}.{1}的dataModel属性值为空",dataForm.getPack(),dataForm.getId());
        }
        ValidateKit.notNull(query,"dataform={0}.{1}的query属性值为空",dataForm.getPack(),dataForm.getId());
    }


}
