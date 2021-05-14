package com.vekai.dataform.handler.impl;

import com.vekai.dataform.validator.ValidateResult;
import com.vekai.dataform.handler.DataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.util.DataFormValidateUtils;
import cn.fisok.raw.kit.ClassKit;
import cn.fisok.sqloy.core.BeanQuery;

import java.util.Map;

public class BeanDataOneHandler<T> extends BeanDataHandler<T> implements DataOneHandler<T> {

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
        return beanCruder.insert(object);
    }

    public int update(DataForm dataForm, T object) {
        return beanCruder.update(object);
    }

    public int save(DataForm dataForm, T object) {
        return beanCruder.save(object);
    }

    public int delete(DataForm dataForm, T object) {
        return beanCruder.delete(object);
    }

    public ValidateResult validate(DataForm dataForm, T object) {
        return DataFormValidateUtils.validateOne(dataForm,this,object);
    }

}
