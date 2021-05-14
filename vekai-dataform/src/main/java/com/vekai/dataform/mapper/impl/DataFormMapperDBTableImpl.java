//package com.vekai.dataform.mapper.impl;
//
//import com.vekai.dataform.DataFormConsts;
//import com.vekai.dataform.exception.DataFormException;
//import com.vekai.dataform.mapper.*;
//import com.vekai.dataform.mapper.impl.po.DataFormElementPO;
//import com.vekai.dataform.mapper.impl.po.DataFormFilterPO;
//import com.vekai.dataform.mapper.impl.po.DataFormPO;
//import com.vekai.dataform.mapper.impl.po.FormElementValidatorPO;
//import com.vekai.dataform.model.DataForm;
//import com.vekai.dataform.model.DataFormElement;
//import com.vekai.dataform.model.DataFormFilter;
//import com.vekai.dataform.util.DataFormUtils;
//import cn.fisok.raw.kit.BeanKit;
//import cn.fisok.raw.kit.ListKit;
//import cn.fisok.raw.kit.MapKit;
//import cn.fisok.raw.kit.StringKit;
//import cn.fisok.sqloy.core.BeanCruder;
//import cn.fisok.sqloy.core.BeanQuery;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * 使用XML文件存储DataForm实体
// * Created by tisir yangsong158@qq.com on 2017-05-22
// */
//public class DataFormMapperDBTableImpl implements DataFormMapper {
//
//
//    public static final String ELEMENTS_SQL = "SELECT * FROM FOWK_DATAFORM_ELEMENT where DATAFORM_ID=:dataFormId ORDER BY GROUP_ ASC,SORT_CODE ASC";
//
//    public static final String DATAFORM_SQL = "SELECT * from FOWK_DATAFORM ";
//
//    public static final String FILTER_SQL = "SELECT * from FOWK_DATAFORM_FILTER where DATAFORM_ID=:dataFormId";
//
//    public static final String ELEMENT_DELETE_SQL = "DELETE FROM FOWK_DATAFORM_ELEMENT where DATAFORM_ID=:dataFormId";
//
//    public static final String FILTER_DELETE_SQL = "DELETE FROM FOWK_DATAFORM_FILTER where DATAFORM_ID=:dataFormId";
//
//    public static final String VALIDATOR_DELETE_SQL = "DELETE FROM FOWK_DATAFORM_VALIDATOR where DATAFORM_ID=:dataFormId";
//
//    public static final String VALIDATOR_SELECT_SQL = "SELECT * FROM FOWK_DATAFORM_VALIDATOR where DATAFORM_ID=:dataFormId and ELEMENT_CODE=:elementCode order by CODE ASC";
//
//    @Autowired
//    protected BeanCruder beanCruder;
//    @Autowired
//    private DataFormDBRowMapper dataFormDBRowMapper;
//    @Autowired
//    private DataFormElementDBRowMapper dataFormElementDBRowMapper;
//    @Autowired
//    private DataFormFilterDBRowMapper dataFormFilterDBRowMapper;
//    @Autowired
//    private DataFormValidatorRowMapper dataFormValidatorRowMapper;
//
//    @Autowired
//    private DataFormMapperDBTableImpl _this;
//
//    /**
//     * 清空缓存
//     */
//    @CacheEvict(value= DataFormConsts.CACHE_KEY,allEntries=true,beforeInvocation=true)
//    public void clearCacheAll(){
//
//    }
//
//    /**
//     * 清空缓存
//     */
//    @CacheEvict(value=DataFormConsts.CACHE_KEY,key="#formId",beforeInvocation=true)
//    public void clearCacheItem(String formId){
//
//    }
//
//    @Override
//    public boolean exists(String id) {
//        return beanCruder.selectExistsById(DataForm.class, id);
//    }
//
//    @Cacheable(value=DataFormConsts.CACHE_KEY,key="#id")
//    public DataForm getDataForm(String id) {
//        String dataFormSQL = DATAFORM_SQL + "where ID=:id";
//        //dataform自定义映射器
////        beanCruder.getDataQuery().setRowMapper(dataFormDBRowMapper);
////        DataForm dataForm = beanCruder
////                .selectOne(DataForm.class, dataFormSQL,"id",id);
//        BeanQuery dataQuery = beanCruder.getDataQuery();
//        DataForm dataForm = dataQuery.exec(dataFormDBRowMapper,()->{
//            return dataQuery.selectOne(DataForm.class, dataFormSQL,"id",id);
//        });
//
//        if(dataForm==null)throw new DataFormException("DataForm={0} not exists",id);
//
//        fillDataFormFilters(dataForm);
//        fillDataFormElements(dataForm);
//
//        return dataForm;
//    }
//
//    private void fillDataFormElements(DataForm dataForm){
////        beanCruder.getDataQuery().setRowMapper(dataFormElementDBRowMapper);
////        List<DataFormElement> elementPOList = beanCruder
////                .selectList(DataFormElement.class,ELEMENTS_SQL,"dataFormId", dataForm.getId());
//        BeanQuery dataQuery = beanCruder.getDataQuery();
//        List<DataFormElement> elementPOList = dataQuery.exec(dataFormElementDBRowMapper,()->{
//            return dataQuery.selectList(DataFormElement.class,ELEMENTS_SQL,"dataFormId", dataForm.getId());
//        });
//
//        for(DataFormElement element : elementPOList){
//            dataForm.addElement(element);
//            fillElementValidators(element);
//        }
//    }
//    private void fillDataFormFilters(DataForm dataForm){
//        //filter自定义映射器
////        beanCruder.getDataQuery().setRowMapper(dataFormFilterDBRowMapper);
////        List<DataFormFilter> filters = beanCruder
////                .selectList(DataFormFilter.class,FILTER_SQL,"dataFormId", dataForm.getId());
//
//        BeanQuery dataQuery = beanCruder.getDataQuery();
//        List<DataFormFilter> filters = dataQuery.exec(dataFormFilterDBRowMapper,()->{
//            return dataQuery.selectList(DataFormFilter.class,FILTER_SQL,"dataFormId", dataForm.getId());
//        });
//
//        for(DataFormFilter filter : filters){
//            dataForm.addFilter(filter);
//        }
//    }
//
//    public void fillElementValidators(DataFormElement element){
//        Map<String,Object> param = MapKit.mapOf("dataFormId",element.getDataformId()
//                ,"elementCode",element.getCode());
////        beanCruder.getDataQuery().setRowMapper(dataFormValidatorRowMapper);
////        List<DataFormElement.FormElementValidator> validators = beanCruder.selectList(DataFormElement.FormElementValidator.class,
////                VALIDATOR_SELECT_SQL,param);
//
//        BeanQuery dataQuery = beanCruder.getDataQuery();
//        List<DataFormElement.FormElementValidator> validators = dataQuery.exec(dataFormValidatorRowMapper,()->{
//            return dataQuery.selectList(DataFormElement.FormElementValidator.class,VALIDATOR_SELECT_SQL,param);
//        });
//
//        element.setValidatorList(validators);
//    }
//
//
//    public DataForm getDataForm(String pack, String code) {
//        String dataFormId = StringKit.format("{0}-{1}", pack, code);
//        return _this.getDataForm(dataFormId);
//    }
//
//    public List<DataForm> getDataFormsByPack(String pack) {
//        StringBuilder dataFormSqlBuilder = new StringBuilder(DATAFORM_SQL);
////        beanCruder.getDataQuery().setRowMapper(dataFormDBRowMapper);
////        List<DataForm> dataFormList = null;
////        if (!StringUtils.isEmpty(pack)) {
////            dataFormSqlBuilder.append("where PACK = :pack");
////            dataFormList = beanCruder.selectList(DataForm.class, dataFormSqlBuilder.toString(),
////                    MapKit.mapOf("pack", pack));
////        } else {
////            dataFormList = beanCruder.selectList(DataForm.class, dataFormSqlBuilder.toString());
////        }
//
//        BeanQuery dataQuery = beanCruder.getDataQuery();
//        List<DataForm> dataFormList = dataQuery.exec(dataFormDBRowMapper,()->{
//            if (!StringUtils.isEmpty(pack)) {
//                dataFormSqlBuilder.append("where PACK = :pack");
//                return dataQuery.selectList(DataForm.class, dataFormSqlBuilder.toString(),
//                        MapKit.mapOf("pack", pack));
//            } else {
//                return dataQuery.selectList(DataForm.class, dataFormSqlBuilder.toString());
//            }
//        });
//
//
//        dataFormList.forEach(dataForm -> {
//            fillDataFormFilters(dataForm);
//        });
//        dataFormList.forEach(dataForm -> {
//            fillDataFormElements(dataForm);
//        });
//        return dataFormList;
//    }
//
//
//
//    public List<DataForm> getAllDataForms() {
//        return getDataFormsByPack(null);
//    }
//
//    @Transactional
//    public int save(DataForm dataForm) {
//        _this.clearCacheItem(dataForm.getId());
//
//        DataFormPO dataFormPO = new DataFormPO();
//        BeanKit.copyProperties(dataForm, dataFormPO);
//        BeanKit.copyProperties(dataForm.getFormUIHint(), dataFormPO);
//        if (dataForm.getQuery() != null) {
//            BeanKit.copyProperties(dataForm.getQuery(), dataFormPO);
//        }
//
//        String dataFormId = StringKit.format("{0}-{1}", dataForm.getPack(), dataForm.getCode());
//        dataFormPO.setId(dataFormId);
//
//        List<DataFormElementPO> elementPOList = ListKit.newArrayList();
//        List<FormElementValidatorPO> validatorPOList = new ArrayList<FormElementValidatorPO>();
//
//        for (DataFormElement element : dataForm.getElements()) {
//
//            DataFormElementPO elementPO = DataFormUtils.convertElementPO(element);
//            elementPO.setDataformId(dataFormPO.getId());
//
//            List<FormElementValidatorPO> validatorPOs = DataFormUtils.convertValidatorPO(
//                element,dataFormPO.getId());
//
//            validatorPOList.addAll(validatorPOs);
//            elementPOList.add(elementPO);
//        }
//
//        List<DataFormFilterPO> dataFormFilterPOList = ListKit.newArrayList();
//        for (DataFormFilter filter : dataForm.getFilters()) {
//            DataFormFilterPO filterPO = new DataFormFilterPO();
//            BeanKit.copyProperties(filter, filterPO);
//            filterPO.setDataformId(dataFormPO.getId());
//            filterPO.setEnabled(filter.getEnabled() ? "Y" : "N");
//            filterPO.setQuick(filter.getQuick() ? "Y" : "N");
//            dataFormFilterPOList.add(filterPO);
//        }
//
//        beanCruder.execute(FILTER_DELETE_SQL,MapKit.mapOf("dataFormId",dataFormId));
//        beanCruder.execute(ELEMENT_DELETE_SQL,MapKit.mapOf("dataFormId",dataFormId));
//        beanCruder.execute(VALIDATOR_DELETE_SQL,MapKit.mapOf("dataFormId",dataFormId));
//
//        int r = 0;
//        r += beanCruder.save(dataFormPO);
//
//        r += beanCruder.insert(dataFormFilterPOList);
//        r += beanCruder.insert(elementPOList);
//        r += beanCruder.insert(validatorPOList);
//        return r;
//    }
//
//    @Transactional
//    public int saveDataFormElement(DataFormElement element){
//        int r = 0;
//        String dataformId = element.getDataformId();
//        _this.clearCacheItem(dataformId);
//
//        DataFormElementPO elementPO = DataFormUtils.convertElementPO(element);
//        elementPO.setDataformId(dataformId);
//        r += beanCruder.save(elementPO);
//
//        //先删除所有的校验规则
//        String deleteValidators = "DELETE FROM FOWK_DATAFORM_VALIDATOR where DATAFORM_ID=:dataFormId and ELEMENT_CODE=:elementCode";
//        beanCruder.execute(deleteValidators,
//                MapKit.mapOf("dataFormId",dataformId
//                        ,"elementCode",element.getCode())
//                );
//        //再保存所有的校验规则
//        List<FormElementValidatorPO> validatorPOs = DataFormUtils
//                .convertValidatorPO(element,dataformId);
//        r += beanCruder.save(validatorPOs);
//        return r;
//
//    }
//
//    public int delete(String id) {
//        _this.clearCacheItem(id);
//
//        String dataFormDelete = "DELETE FROM FOWK_DATAFORM where ID=:id";
//        int r = 0;
//        r += beanCruder.execute(dataFormDelete,MapKit.mapOf("id",id));
//        r += beanCruder.execute(ELEMENT_DELETE_SQL,MapKit.mapOf("dataFormId",id));
//        r += beanCruder.execute(FILTER_DELETE_SQL,MapKit.mapOf("dataFormId",id));
//        r += beanCruder.execute(VALIDATOR_DELETE_SQL,MapKit.mapOf("dataFormId",id));
//        return r;
//    }
//
//    @Override
//    public int deleteAll() {
//        _this.clearCacheAll();
//        String dataFormDelete = "DELETE FROM FOWK_DATAFORM";
//        String ELEMENT_DELETE_SQL = "DELETE FROM FOWK_DATAFORM_ELEMENT";
//        String FILTER_DELETE_SQL = "DELETE FROM FOWK_DATAFORM_FILTER";
//        String VALIDATOR_DELETE_SQL = "DELETE FROM FOWK_DATAFORM_VALIDATOR";
//        int r = beanCruder.execute(dataFormDelete);
//        beanCruder.execute(ELEMENT_DELETE_SQL);
//        beanCruder.execute(FILTER_DELETE_SQL);
//        beanCruder.execute(VALIDATOR_DELETE_SQL);
//        return r;
//    }
//
//    public int delete(String pack, String code) {
//        String dataFormId = StringKit.format("{0}-{1}", pack, code);
//        return delete(dataFormId);
//    }
//}
