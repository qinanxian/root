package com.vekai.dataform.handler.impl;

import cn.fisok.raw.kit.JpaKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.core.DBType;
import com.vekai.dataform.exception.DataFormException;
import com.vekai.dataform.handler.DataListHandler;
import com.vekai.dataform.validator.ValidateResult;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.util.DataFormUtils;
import com.vekai.dataform.util.DataFormValidateUtils;
import cn.fisok.raw.lang.ValueObject;
import cn.fisok.sqloy.core.BeanQuery;
import cn.fisok.sqloy.core.PaginResult;
import cn.fisok.sqloy.core.PaginQuery;
import cn.fisok.sqloy.kit.SQLInjector;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.*;

public class BeanDataListHandler<T> extends BeanDataHandler<T> implements DataListHandler<T> {


    public void initDataForm(DataForm dataForm) {

    }

    private Map<String,String> propToColForSortMap(Class<T> clazz,DataForm dataForm,Map<String, ?> sortMap){
        Map<String,String> sortRules = new LinkedCaseInsensitiveMap<String>();

        Iterator<String> iterator = sortMap.keySet().iterator();
        while(iterator.hasNext()){
            String propCode = iterator.next();
            DataFormElement element = dataForm.getElement(propCode);
            if(element == null)continue;
            String fieldName = StringKit.nvl(element.getColumn(),element.getCode());
            String column = JpaKit.getColumn(clazz,fieldName);
            if(StringKit.isBlank(column)){
                column = fieldName;
            }
            if(StringKit.isBlank(column)){
                column = JpaKit.getColumn(clazz,propCode);
            }
            sortRules.put(column,ValueObject.valueOf(sortMap.get(propCode)).strValue());
        }

        return sortRules;
    }

    protected ValueObject getFilterValue(DataForm dataForm,Map<String, ?> filterData,String code){
        return FilterWhereProcessor.getFilterValue(dataForm,filterData,code);
    }

    public PaginResult<T> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        Map<String,?> sqlParameter = queryParameters;
        //???????????????????????????
        //????????????????????????????????????
        Map<String,Object> quickParameter = MapKit.newHashMap();
        FilterWhereProcessor.parseFilter(dataForm,filterParameters,quickParameter,sqlParameter);

        Class<T> clazz = getFormClass(dataForm);
        String sql = dataForm.getQuery().buildQuerySql(false);
        //??????????????????????????????????????????SQL????????????
        if(sortMap!=null&&sortMap.size()>0){
            Map<String,String> sortRules = propToColForSortMap(clazz,dataForm,sortMap);//?????????????????????????????????
            DBType dbType = beanCruder.getDBType();//beanCruder.getDataQuery().getSqlDialectType();
            try{
                sql = SQLInjector.injectSQLOrder(dbType,sql,sortRules);
            }catch(Exception e){
                throw new DataFormException(e,"SQL????????????,SQL[{0}]",sql);
            }
        }



        //????????????????????????
        PaginQuery query = DataFormUtils.buildPaginationQuery(dataForm,sql,pageIndex,pageSize);
        query.getParameterMap().putAll(queryParameters);
        query.getParameterMap().putAll(quickParameter);

//        beanCruder.getDataQuery().setRowMapper(getRowMapper(dataForm,clazz));
//        PaginationData<T> ret = beanCruder.selectListPagination(clazz,query);
        BeanQuery dataQuery = beanCruder.getBeanQuery();
        PaginResult<T> ret = dataQuery.exec(getRowMapper(dataForm,clazz),()->{
            return dataQuery.selectListPagination(clazz,query);
        });
        DataFormUtils.fixSummaryCodeName(dataForm,ret);

        return ret;
    }

    public int insert(DataForm dataForm, List<T> dataList) {
        return beanCruder.insert(dataList);
    }

    public int update(DataForm dataForm, List<T> dataList) {
        return beanCruder.update(dataList);
    }

    public int save(DataForm dataForm, List<T> dataList) {
        return beanCruder.save(dataList);
    }

    public Integer delete(DataForm dataForm, List<T> dataList) {
        return beanCruder.delete(dataList);
    }


    public List<ValidateResult> validate(DataForm dataForm, List<T> dataList) {
        return DataFormValidateUtils.validateList(dataForm,this,dataList);
    }

}
