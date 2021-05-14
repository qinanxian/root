package com.vekai.dataform.handler.impl;

import cn.fisok.sqloy.core.ExecutorFollow;
import com.vekai.dataform.handler.DataOneHandler;
import com.vekai.dataform.validator.ValidateResult;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.util.DataFormValidateUtils;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.MapObjectQuery;
import cn.fisok.sqloy.core.MapObjectUpdater;

import java.util.List;
import java.util.Map;

/**
 * Map单条数据处理
 */
public class MapDataOneHandler extends MapDataHandler implements DataOneHandler<MapObject> {

    protected MapObject params;

    public void initDataForm(DataForm dataForm) {

    }

    public MapObject getParams(DataForm dataForm) {
        return params;
    }

    public MapObject createDataObject(DataForm dataForm) {
        MapObject object = new MapObject();
        List<DataFormElement> elements = dataForm.getElements();
        for (DataFormElement element : elements) {
            object.put(element.getCode(), null);
        }
        return object;
    }

    public MapObject query(DataForm dataForm, Map<String, ?> queryParameters) {
        validateDataForm(dataForm);
        String query = dataForm.getQuery().buildQuerySql(false);

        MapObjectQuery dataQuery = mapObjectCruder.getMapObjectQuery();
//        MapData row = mapObjectCruder.getMapObjectQuery()
//                .selectOne(query, queryParameters);
        MapObject row = dataQuery.exec(getRowMapper(dataForm),()->{
            return dataQuery.selectOne(query, queryParameters);
        });

        MapObject ret = remoldMapData(dataForm, row);

        return ret;
    }


    public int insert(DataForm dataForm, MapObject object) {
        validateDataForm(dataForm);
        final MapObject dbMapdata = getDbMapData(dataForm, object);
        togglePrimaryKey(dataForm, dbMapdata);

        MapObjectUpdater updater = mapObjectCruder.getMapObjectUpdater();
        //在模板类中定义操作，这样，可以把其他需要统一处理的东西放到被调用的方法中处理
        return updater.exec(getNameConverter(dataForm), new ExecutorFollow<Integer>() {
            public Integer impl() {
                return updater.insert(dataForm.getDataModel(), dbMapdata);
            }
        });
    }


    public int update(DataForm dataForm, MapObject object) {
        validateDataForm(dataForm);

        String table = dataForm.getDataModel();
        final MapObject dbMapdata = getDbMapData(dataForm, object);
        final MapObject dbPkMapdata = getDbPkMapData(dataForm, object);

        MapObjectUpdater updater = mapObjectCruder.getMapObjectUpdater();
        //在模板类中定义操作，这样，可以把其他需要统一处理的东西放到被调用的方法中处理
        return updater.exec(getNameConverter(dataForm), new ExecutorFollow<Integer>() {
            public Integer impl() {
                return updater.update(table, dbMapdata, dbPkMapdata);
            }
        });
    }

    public int save(DataForm dataForm, MapObject object) {
        validateDataForm(dataForm);

        final String table = dataForm.getDataModel();
        final MapObject dbMapdata = getDbMapData(dataForm, object);
        final MapObject dbPkMapdata = getDbPkMapData(dataForm, object);
        ValidateKit.notBlank(table,"当模型为MapData时，请填写显示模板[数据实体:DATA_MODEL]字段");

        //如果有新手成的主键，则放回数据模型中去
        togglePrimaryKey(dataForm, dbMapdata);

        object.putAll(dbMapdata);

        MapObjectUpdater updater = mapObjectCruder.getMapObjectUpdater();
        //在模板类中定义操作，这样，可以把其他需要统一处理的东西放到被调用的方法中处理
        return updater.exec(getNameConverter(dataForm), new ExecutorFollow<Integer>() {
            public Integer impl() {
                return updater.save(table, dbMapdata, dbPkMapdata);
            }
        });
    }

    public int delete(DataForm dataForm, MapObject object) {
        validateDataForm(dataForm);

        final String table = dataForm.getDataModel();
        final MapObject dbPkMapdata = getDbPkMapData(dataForm, object);

        MapObjectUpdater updater = mapObjectCruder.getMapObjectUpdater();
        //在模板类中定义操作，这样，可以把其他需要统一处理的东西放到被调用的方法中处理
        return updater.exec(getNameConverter(dataForm), new ExecutorFollow<Integer>() {
            public Integer impl() {
                return updater.delete(table, dbPkMapdata);
            }
        });
    }

    public ValidateResult validate(DataForm dataForm, MapObject object) {
        return DataFormValidateUtils.validateOne(dataForm, this, object);
    }
}
