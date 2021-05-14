//package com.vekai.dataform.service.impl;
//
//import com.vekai.dataform.dto.CloneDataFormBean;
//import com.vekai.dataform.exception.DataFormException;
//import com.vekai.dataform.mapper.DataFormDBRowMapper;
//import com.vekai.dataform.mapper.DataFormMapper;
//import com.vekai.dataform.model.DataForm;
//import com.vekai.dataform.model.DataFormElement;
//import com.vekai.dataform.service.DataFormAdminServiceAbstract;
//import cn.fisok.raw.kit.BeanKit;
//import cn.fisok.raw.kit.ListKit;
//import cn.fisok.raw.kit.MapKit;
//import cn.fisok.raw.kit.StringKit;
//import cn.fisok.raw.lang.ValueObject;
//import cn.fisok.sqloy.core.BeanQuery;
//import cn.fisok.sqloy.core.PaginResult;
//import cn.fisok.sqloy.core.PaginQuery;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by luyu on 2017/12/25.
// */
//public class DataFormAdminServiceDBImpl extends DataFormAdminServiceAbstract {
//
//    @Autowired
//    private BeanQuery dataQuery;
//    @Autowired
//    private DataFormDBRowMapper dataFormDBRowMapper;
//    @Autowired
//    private DataFormMapper dataFormMapper;
//
//
//    public List<DataForm> getDataForms() {
//        List<DataForm> dataForms = dataQuery.exec(dataFormDBRowMapper,()->{
//            return dataQuery
//                    .selectList(DataForm.class, "select * from FOWK_DATAFORM ORDER BY SORT_CODE ASC,PACK ASC,CODE ASC");
//        });
//
//        return dataForms;
//    }
//
//
//
//
//
//    public DataForm getDataForm(String id) {
//        DataForm dataForm = dataFormMapper.getDataForm(id);
//        return dataForm;
//    }
//
//    @Transactional
//    public DataForm saveDataForm(DataForm dataForm, String oldDataFormId) {
//        String newDataFormId = StringKit.format("{0}-{1}", dataForm.getPack(), dataForm.getCode());
//        dataFormMapper.save(dataForm);
//        if (!StringUtils.isEmpty(oldDataFormId) && !newDataFormId.equals(oldDataFormId)) {
//            dataFormMapper.delete(oldDataFormId);
//        }
//        return dataForm;
//    }
//
//    @Transactional
//    public DataForm cloneDataForm(CloneDataFormBean cloneDataFormBean) {
//        String[] dataFormInfo = cloneDataFormBean.getNewDataFormId().split("-");
//        if (dataFormInfo.length != 2) {
//            throw new DataFormException("dataForm format error");
//        }
//        String dataFormSQL = DataFormMapperDBTableImpl.DATAFORM_SQL + "where ID=:id";
//        DataForm newDataForm = dataQuery.exec(dataFormDBRowMapper,()->{
//            return dataQuery.selectOne(DataForm.class, dataFormSQL, "id", cloneDataFormBean.getNewDataFormId());
//        });
//
//        if (newDataForm != null) {
//            throw new DataFormException("new dataFormId has existed");
//        }
//
//        DataForm oldDataForm = dataFormMapper.getDataForm(cloneDataFormBean.getOldDataFormId());
//        DataForm cloneDataForm = BeanKit.deepClone(oldDataForm);
//        cloneDataForm.setId(cloneDataFormBean.getNewDataFormId());
//        cloneDataForm.setName("copyof" + oldDataForm.getName());
//        dataFormMapper.save(cloneDataForm);
//        return cloneDataForm;
//    }
//
//    public DataFormElement getDataFormElementDetail(String dataformId, String code) {
//        DataForm dataForm = getDataForm(dataformId);
//        return dataForm.getElement(code);
//    }
//
//    @Transactional
//    public DataFormElement saveDataFormElement(DataFormElement element, String dataFormId) {
//        dataFormMapper.saveDataFormElement(element);
//        return element;
//    }
//
//    public PaginResult<DataForm> getPageDataForms(Integer size, Integer index,
//                                                  Map<String, ?> filterMap, Map<String, Object> sortMap) {
//        StringBuffer sqlBuffer = new StringBuffer("select * from FOWK_DATAFORM");
//        Map<String, ?> queryParameters = MapKit.newHashMap();
//        String filterSql = this.parseFilterFillParam(queryParameters, filterMap);
//        sqlBuffer.append(filterSql);
//        String orderSql = this.parseOrderBySql(sortMap);
//        sqlBuffer.append(orderSql);
//
//        PaginQuery query = new PaginQuery();
//        query.setQuery(sqlBuffer.toString());
//        query.setIndex(index);
//        query.setSize(size);
//        query.getParameterMap().putAll(queryParameters);
//
//        return dataQuery.exec(dataFormDBRowMapper,()->{
//            return dataQuery.selectListPagination(DataForm.class, query);
//        });
//    }
//
//    private String parseOrderBySql(Map<String, Object> sortMap) {
//        StringBuffer sqlBuffer = new StringBuffer();
//        List<String> orderBySqlList = ListKit.newArrayList();
//        if (sortMap != null && sortMap.size() > 0) {
//            sqlBuffer.append(" order by ");
//            Iterator<String> iterator = sortMap.keySet().iterator();
//            while (iterator.hasNext()) {
//                String code = iterator.next();
//                ValueObject value = ValueObject.valueOf(sortMap.get(code));
//                String condItem = buildOrderBySQL(code, value);
//                orderBySqlList.add(condItem);
//            }
//        }
//        return sqlBuffer.append(orderBySqlList.stream().reduce((s1, s2) -> s1 + "," + s2).get()).toString();
//    }
//
//    private String buildOrderBySQL(String code, ValueObject value) {
//        StringBuffer filterBuffer = new StringBuffer();
//        filterBuffer.append(underLining(code)).append(" " + value.strValue());
//        return filterBuffer.toString();
//    }
//
//    private String parseFilterFillParam(Map<String, ?> param, Map<String, ?> filterParameters) {
//        StringBuffer filterBuffer = new StringBuffer();
//        filterParameters.remove("_");
//        if (filterParameters != null && filterParameters.size() > 0) {
//            filterBuffer.append(" Where ");
//            Iterator<String> iterator = filterParameters.keySet().iterator();
//            List<String> condItems = ListKit.newArrayList();
//            while (iterator.hasNext()) {
//                String code = iterator.next();
//                ValueObject value = ValueObject.valueOf(filterParameters.get(code));
//                String condItem = buildFilterSQL(code, value, param);
//                condItems.add(condItem);
//            }
//            String FilterSql = condItems.stream().reduce((s1, s2) -> s1 + " and " + s2).get();
//            filterBuffer.append(FilterSql);
//        }
//        return filterBuffer.toString();
//    }
//
//    private String buildFilterSQL(String code, ValueObject value, Map<String, ?> param) {
//        Map<String, Object> paramMap = (Map<String, Object>) param;
//        StringBuffer filterBuffer = new StringBuffer();
//        filterBuffer.append(underLining(code)).append(" LIKE ").append(":").append(code);
//        paramMap.put(code, "%" + value.strValue() + "%");
//        return filterBuffer.toString();
//    }
//
//    public void deleteDataForm(String dataFormId) {
//        dataFormMapper.delete(dataFormId);
//    }
//
//
//    public void deleteAll() {
//        dataFormMapper.deleteAll();
//    }
//
//
//    private String underLining(String str) {
//        if (StringUtils.isEmpty(str)) {
//            return "";
//        }
//        String returnValue = str.substring(0, 1).toLowerCase() + str.substring(1);
//
//        String[] ss = str.split("(?<!^)(?=[A-Z])");
//        if (ss.length > 1) {
//            List<String> list = Arrays.asList(ss);
//            returnValue = list.stream()
//                    .reduce((s1, s2) -> lowerCaseFirstLetter(s1) + "_" + lowerCaseFirstLetter(s2))
//                    .get();
//        }
//        return returnValue;
//    }
//
//    public static String lowerCaseFirstLetter(String name) {
//        if (StringUtils.isEmpty(name)) {
//            return "";
//        }
//        return name.substring(0, 1).toLowerCase() + name.substring(1);
//    }
//}
