package com.vekai.dataform.handler.impl;

import com.vekai.dataform.handler.DataOneHandler;
import com.vekai.dataform.validator.ValidateResult;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.util.DataFormValidateUtils;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.ClassKit;
import cn.fisok.raw.kit.JpaKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanQuery;
import cn.fisok.sqloy.core.MapObjectUpdater;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.Map;

/**
 * 关于JavaBean，如果字段没有配置，要不要查出来的问题，存在一个逻辑悖论，只能暂时放弃
 * 1.如果查询时，从JavaBean上自动补全没有配置的的字段，多表查询会导致字段不明确问题
 * 2.如果更新时，按显示模板的更新，会出现显示模板没有配置的字段，通过JavaBean修改后，无法被保存进去
 * 3.基于第2步，如果保留有值的属性，又会导致，清空的值，无法被保存问题
 * @param <T>
 */
public class BeanDataOneWithValueFilterHandler<T> extends BeanDataHandler<T> implements DataOneHandler<T> {
    @Autowired
    protected MapObjectUpdater mapDataUpdater;

    public void initDataForm(DataForm dataForm) {

    }

    public T createDataObject(DataForm dataForm) {
        return (T) ClassKit.newInstance(dataForm.getDataModel());
    }

    public T query(DataForm dataForm, Map<String, ?> queryParameters) {
        String sql = dataForm.getQuery().buildQuerySql(false);

        Class<T> clazz = getFormClass(dataForm);
//        beanCruder.getDataQuery().setRowMapper(getRowMapper(dataForm,clazz));
//        return beanCruder.selectOne(clazz, sql, queryParameters);
        BeanQuery dataQuery = beanCruder.getBeanQuery();
        T ret = dataQuery.exec(getRowMapper(dataForm,clazz),()->{
            return dataQuery.selectOne(clazz, sql, queryParameters);
        });
        return ret;
    }

    public int insert(DataForm dataForm, T object) {
        //1. 生成主键
        beanCruder.getBeanUpdater().fillSerialNumberValue(object);

        //2. 转成Map并保存
        String table = JpaKit.getTableName(object.getClass());
        Map<String,Object> dataOne = BeanKit.bean2Map(object);

        //3. 从显示模板读取需要保存的数据，并保存
        MapObject storeDataRow = new MapObject();
        dataForm.getElements().forEach(element->{
            //不是主键，又设置了不持久化
            if (!element.getPrimaryKey() && !element.getPersist()) {
                return;
            }
            String code = StringKit.nvl(element.getCode(),StringKit.underlineToCamel(element.getColumn()));
            String column = StringKit.nvl(element.getColumn(),StringKit.camelToUnderline(element.getCode()));

            //只有显示模板配置的字段才做插入操作
            if(dataOne.containsKey(code)){
                storeDataRow.put(column,dataOne.get(code));
            }
        });
        if(storeDataRow.size()>0){
            return mapDataUpdater.insert(table,storeDataRow);
        }else{
            return 0;
        }
    }

    public int update(DataForm dataForm, T object) {
        String table = JpaKit.getTableName(object.getClass());
        MapObject keyMapRow = MapObject.valueOf(JpaKit.getIdMap(object));
        MapObject mapRow = MapObject.valueOf(BeanKit.bean2Map(object));
        if(keyMapRow==null||keyMapRow.size()==0)return 0;

        MapObject storeDataRow = makeStoreDataRow(dataForm,keyMapRow,mapRow);

        if(storeDataRow != null && storeDataRow.size() > 0){
            return mapDataUpdater.update(table,storeDataRow,keyMapRow);
        }else{
            return 0;
        }
    }

    protected MapObject makeStoreDataRow(DataForm dataForm, MapObject keyMapRow, MapObject mapRow){
        final MapObject storeDataRow = new MapObject();

        //1. 把主键数据清理出去
        Iterator<String> keyIterator = keyMapRow.keySet().iterator();
        while(keyIterator.hasNext()){
            String keyName = keyIterator.next();
            mapRow.remove(keyName);
        }
        //2. 根据显示模板重做数据
        dataForm.getElements().forEach(element->{
            //不是主键，又设置了不持久化
            if (!element.getPrimaryKey() && (!element.getPersist()||element.getUpdateable())) {
                return;
            }
            String code = StringKit.nvl(element.getCode(),StringKit.underlineToCamel(element.getColumn()));
            String column = StringKit.nvl(element.getColumn(),StringKit.camelToUnderline(element.getCode()));

            //只有显示模板配置的字段才做插入操作
            if(mapRow.containsKey(code)){
                storeDataRow.put(column,mapRow.get(code));
            }
        });
        //3. 如果值不为空

        return storeDataRow;
    }

    public int save(DataForm dataForm, T object) {
        //1. 生成主键
        beanCruder.getBeanUpdater().fillSerialNumberValue(object);

        String table = JpaKit.getTableName(object.getClass());
        MapObject keyMapRow = MapObject.valueOf(JpaKit.getIdMap(object));
        MapObject mapRow = MapObject.valueOf(BeanKit.bean2Map(object));
        if(keyMapRow==null||keyMapRow.size()==0)return 0;

        MapObject storeDataRow = makeStoreDataRow(dataForm,keyMapRow,mapRow);

        if(storeDataRow != null && storeDataRow.size() > 0){
            return mapDataUpdater.save(table,storeDataRow,keyMapRow);
        }else{
            return 0;
        }
    }

    public int delete(DataForm dataForm, T object) {
        return beanCruder.delete(object);
    }

    public ValidateResult validate(DataForm dataForm, T object) {
        return DataFormValidateUtils.validateOne(dataForm,this,object);
    }

}
