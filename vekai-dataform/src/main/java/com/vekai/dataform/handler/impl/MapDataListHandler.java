package com.vekai.dataform.handler.impl;

import cn.fisok.sqloy.core.*;
import com.vekai.dataform.validator.ValidateResult;
import com.vekai.dataform.handler.DataListHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.util.DataFormUtils;
import com.vekai.dataform.util.DataFormValidateUtils;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.raw.lang.ValueObject;
import cn.fisok.sqloy.kit.SQLInjector;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Map列表数据处理
 */
public class MapDataListHandler extends MapDataHandler implements DataListHandler<MapObject> {

    public void initDataForm(DataForm dataForm) {

    }

    private Map<String,String> propToColForSortMap(DataForm dataForm,Map<String, ?> sortMap){
        Map<String,String> sortRules = new LinkedCaseInsensitiveMap<String>();

        Iterator<String> iterator = sortMap.keySet().iterator();
        while(iterator.hasNext()){
            String propCode = iterator.next();
            DataFormElement element = dataForm.getElement(propCode);
            if(element == null)continue;
            sortRules.put(element.getColumn(),ValueObject.valueOf(sortMap.get(propCode)).strValue());
        }

        return sortRules;
    }

    protected ValueObject getFilterValue(DataForm dataForm,Map<String, ?> filterData,String code){
        return FilterWhereProcessor.getFilterValue(dataForm,filterData,code);
    }

    public PaginResult<MapObject> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        ValidateKit.notNull(dataForm);
        Map<String,?> sqlParameter = queryParameters;

        //处理筛选器以及快速搜索器
        Map<String,Object> quickParameter = MapKit.newHashMap();
        FilterWhereProcessor.parseFilter(dataForm,filterParameters,quickParameter,sqlParameter);

        String sql = dataForm.getQuery().buildQuerySql(false);
        ValidateKit.notBlank(sql,"dataform={0}.{1}的query属性值为空",dataForm.getPack(),dataForm.getId());

        //如果有排序数据，则把排序注入SQL查询中去
        if(sortMap.size()>0){
            Map<String,String> sortRules = propToColForSortMap(dataForm,sortMap);//把传入的属性转为字段名
            DBType dbType = mapObjectCruder.getDBType();
            sql = SQLInjector.injectSQLOrder(dbType,sql,sortRules);
        }
        //分页查询参数处理
        PaginQuery query = DataFormUtils.buildPaginationQuery(dataForm,sql,pageIndex,pageSize);
        query.getParameterMap().putAll(queryParameters);
        query.getParameterMap().putAll(quickParameter);

//        RowMapper<MapData> rowMapper = getDataFormRowMapper(dataForm);
//        PaginationData<MapData> ret = mapObjectCruder.getMapObjectQuery()
//                .selectListPagination(query);
        MapObjectQuery dataQuery = mapObjectCruder.getMapObjectQuery();
        PaginResult<MapObject> ret = dataQuery.exec(getRowMapper(dataForm),()->{
            return dataQuery.selectListPagination(query);
        });

        //由于数据库查询后，字段名全部变大写了，这里使用列名重新塑造下
        List<MapObject> dataList = ret.getDataList();
        ret.setDataList(remoldMapDataList(dataForm,dataList));

        DataFormUtils.fixSummaryCodeName(dataForm,ret);

        return ret;
    }

    public int insert(DataForm dataForm, List<MapObject> dataList) {
        validateDataForm(dataForm);
        String table = dataForm.getDataModel();

        List<MapObject> dbDataList = getDbMapDataList(dataForm,dataList);

        for(MapObject row : dbDataList){
            togglePrimaryKey(dataForm,row);
        }

//        return mapObjectCruder.getMapObjectUpdater()
//                .setNameConverter(getNameConverter(dataForm))
//                .insert(table,dbDataList);

        MapObjectUpdater updater = mapObjectCruder.getMapObjectUpdater();
        //在模板类中定义操作，这样，可以把其他需要统一处理的东西放到被调用的方法中处理
        return updater.exec(getNameConverter(dataForm), ()->{
            return updater.insert(table,dbDataList);
        });
    }

    public int update(DataForm dataForm, List<MapObject> dataList) {
        validateDataForm(dataForm);
        String table = dataForm.getDataModel();

        List<MapObject> dbDataList = getDbMapDataList(dataForm,dataList);
        List<MapObject> dbPkDataList = getDbPkMapDataList(dataForm,dataList);

//        return mapObjectCruder.getMapObjectUpdater()
//                .setNameConverter(getNameConverter(dataForm))
//                .update(table,dbDataList,dbPkDataList);
        MapObjectUpdater updater = mapObjectCruder.getMapObjectUpdater();
        //在模板类中定义操作，这样，可以把其他需要统一处理的东西放到被调用的方法中处理
        return updater.exec(getNameConverter(dataForm), ()->{
            return updater.update(table,dbDataList,dbPkDataList);
        });
    }


    public int save(DataForm dataForm, List<MapObject> dataList) {
        validateDataForm(dataForm);
        String table = dataForm.getDataModel();
        ValidateKit.notBlank(table,"当模型为MapData时，请填写显示模板[数据实体:DATA_MODEL]字段");

        List<MapObject> dbDataList = getDbMapDataList(dataForm,dataList);
        List<MapObject> dbPkDataList = getDbPkMapDataList(dataForm,dataList);

        for(MapObject row : dbDataList){
            togglePrimaryKey(dataForm,row);
        }

//        return mapObjectCruder.getMapObjectUpdater()
//                .setNameConverter(getNameConverter(dataForm))
//                .save(table,dbDataList,dbPkDataList);
        MapObjectUpdater updater = mapObjectCruder.getMapObjectUpdater();
        //在模板类中定义操作，这样，可以把其他需要统一处理的东西放到被调用的方法中处理
        return updater.exec(getNameConverter(dataForm), ()->{
            return updater.save(table,dbDataList,dbPkDataList);
        });
    }

    public Integer delete(DataForm dataForm, List<MapObject> dataList) {
        validateDataForm(dataForm);
        String table = dataForm.getDataModel();
        ValidateKit.notBlank(table,"显示模板{0}对应的主数据表没有配置，无法删除数据",dataForm.getId());
        ValidateKit.notEmpty(dataList,"删除的数据对象不存在");

        List<MapObject> dbPkDataList = getDbPkMapDataList(dataForm,dataList);
//        return mapObjectCruder.getMapObjectUpdater()
//                .setNameConverter(getNameConverter(dataForm))
//                .delete(table,dbPkDataList);
        MapObjectUpdater updater = mapObjectCruder.getMapObjectUpdater();
        //在模板类中定义操作，这样，可以把其他需要统一处理的东西放到被调用的方法中处理
        return updater.exec(getNameConverter(dataForm), ()->{
            return updater.delete(table,dbPkDataList);
        });
    }

    public List<ValidateResult> validate(DataForm dataForm, List<MapObject> dataList) {
        return DataFormValidateUtils.validateList(dataForm,this,dataList);
    }
}
