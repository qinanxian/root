package com.vekai.dataform.util;

import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.ClassKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.raw.lang.PairBond;
import cn.fisok.raw.lang.ValueObject;
import cn.fisok.sqloy.core.PaginQuery;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.dataform.exception.DataFormException;
import com.vekai.dataform.mapper.impl.po.DataFormElementPO;
import com.vekai.dataform.mapper.impl.po.FormElementValidatorPO;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.types.ElementDataType;
import com.vekai.dataform.model.types.FormDataModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by luyu on 2017/12/26.
 */
public abstract class DataFormUtils {
    protected static Logger logger = LoggerFactory.getLogger(DataFormUtils.class);

    public static DataFormElementPO convertElementPO(DataFormElement dataFormElement) {
        DataFormElement.FormElementUIHint hint = dataFormElement.getElementUIHint();

        DataFormElementPO elementPO = new DataFormElementPO();
        BeanKit.copyProperties(dataFormElement, elementPO);
        BeanKit.copyProperties(dataFormElement.getElementUIHint(), elementPO);

        elementPO.setPrimaryKey(dataFormElement.getPrimaryKey() ? "Y" : "N");
        elementPO.setUpdateable(dataFormElement.getUpdateable() ? "Y" : "N");
        elementPO.setPersist(dataFormElement.getPersist() ? "Y" : "N");
        elementPO.setSortable(dataFormElement.getSortable() ? "Y" : "N");
        elementPO.setEnabled(dataFormElement.getEnabled() ? "Y" : "N");

        elementPO.setReadonly(hint.getReadonly() ? "Y" : "N");
        elementPO.setRequired(hint.getRequired() ? "Y" : "N");
        elementPO.setVisible(hint.getVisible() ? "Y" : "N");
        elementPO.setDictCodeLazy(hint.getDictCodeLazy() ? "Y" : "N");
        elementPO.setDictCodeTreeLeafOnly(hint.getDictCodeTreeLeafOnly() ? "Y" : "N");
        elementPO.setDictCodeTreeFull(hint.getDictCodeTreeFull() ? "Y" : "N");


        return elementPO;
    }

    public static List<FormElementValidatorPO> convertValidatorPO(DataFormElement element, String dataformId) {
        List<FormElementValidatorPO> validatorPOs = element.getValidatorList().stream()
                .map(formElementValidator -> getFormElementValidatorPO(formElementValidator, element.getCode(), dataformId))
                .collect(Collectors.toList());
        return validatorPOs;
    }

    private static FormElementValidatorPO getFormElementValidatorPO(
            DataFormElement.FormElementValidator formElementValidator, String code, String dataformId) {
        FormElementValidatorPO validatorPO = new FormElementValidatorPO();
        BeanKit.copyProperties(formElementValidator, validatorPO);
        validatorPO.setElementCode(code);
        validatorPO.setDataformId(dataformId);
        validatorPO.setMessage(formElementValidator.getDefaultMessage());
        validatorPO.setMessageI18nCode(formElementValidator.getDefaultMessageI18nCode());
        return validatorPO;
    }

    public static Class<?> getDataFormClass(DataForm dataForm) {
        FormDataModelType dataModelType = dataForm.getDataModelType();
        String id = dataForm.getId();

        Class<?> clazz = null;
        try {
            clazz = Class.forName(dataForm.getDataModel());
        } catch (ClassNotFoundException e) {
            String error = "DataModel配置错误，formId={0},DataModelType={1},模型类:{2}不存在";
            throw new DataFormException(error, id, dataModelType, dataForm.getDataModel());
        }
        return clazz;
    }

    public static List<Object> mapDataListToBeanDataList(DataForm dataForm, List<MapObject> dataList) {
        Class<?> clazz = getDataFormClass(dataForm);
        //根据模板绑定的模型，把数据填充进去
        List<Object> objectList = new ArrayList<>();
        for (MapObject mapData : dataList) {
            Object inst = BeanKit.map2Bean(mapData, clazz);
            objectList.add(inst);
        }

        return objectList;
    }

    public static Object mapDataToBeanData(DataForm dataForm, MapObject mapData) {
        Class<?> clazz = getDataFormClass(dataForm);
        //根据模板绑定的模型，把数据填充进去
        return BeanKit.map2Bean(mapData, clazz);
    }


    /**
     * 根据显示模板类型类型，取MAP相应元素的值
     *
     * @param element element
     * @param entity entity
     * @return  Object
     */
    public static Object getMapValue(DataFormElement element, MapObject entity) {
        String elementCode = element.getCode();
        ValueObject v = entity.get(elementCode, false);
        return fitValueByValueObject(element,v);
    }

    /**
     * 根据显示模板配置的数据类型，对数值作相应的修正
     *
     * @param element element
     * @param value value
     * @return Object
     */
    public static Object fitValue(DataFormElement element, Object value) {
        ValueObject v = ValueObject.valueOf(value);
        return fitValueByValueObject(element,v);
    }

    private static Object fitValueByValueObject(DataFormElement element,ValueObject v){
        switch (element.getDataType()) {
            case Date:
                return v.dateValue();
            case Double:
                return v.doubleValue();
            case Integer:
                return v.intValue();
            default:
                return v.strValue("");
        }
    }

    public static <T> PaginQuery buildPaginationQuery(DataForm dataForm, String sql, Integer pageIndex, Integer pageSize){
        PaginQuery query = new PaginQuery();
        query.setQuery(sql);
        query.setIndex(pageIndex);
        query.setSize(pageSize);

        List<DataFormElement> elements = dataForm.getElements();
        elements.forEach(element -> {
            String summaryExpression = element.getSummaryExpression();
            if(StringKit.isNotBlank(summaryExpression)){
                String name = StringKit.nvl(element.getCode(),element.getColumn());
//                if(StringKit.isBlank(name)){
//                    name = getColumnByCode(dataForm,element.getCode());
//                }
                PairBond<String,String> column = new PairBond<String,String>();

                String columnName = StringKit.nvl(element.getColumn(),StringKit.camelToUnderline(element.getCode()));
                column.setLeft(columnName);
                column.setRight(name);

                query.addSummaryExpression(column,summaryExpression);
            }
        });
        return query;
    }

    /**
     * 根据列名转换为代码
     *
     * @param dataForm dataForm
     * @param fieldCode fieldCode
     * @return String
     */
    public static String getColumnByCode(DataForm dataForm,String fieldCode){
        List<PairBond> pairBonds = dataForm.getQuery().getSelectItems();
        for(PairBond<String,String> pairBond :pairBonds){
            if(fieldCode.equals(pairBond.getLeft())){
                return pairBond.getRight();
            }
        }
        return null;
    }

    /**
     * 根据代码转换为列名
     *
     * @param dataForm dataForm
     * @param column column
     * @return String
     */
    public static String getCodeByColumn(DataForm dataForm,String column){
        List<PairBond> pairBonds = dataForm.getQuery().getSelectItems();
        for(PairBond<String,String> pairBond :pairBonds){
            if(column.equals(pairBond.getRight())){
                return pairBond.getLeft();
            }
        }
        return null;
    }

    /**
     * 根据DataForm的配置，重新处理小计以及合计部分的KEY值
     *
     * @param dataForm dataForm
     * @param <T> T
     */
    public static <T> void fixSummaryCodeName(DataForm dataForm, PaginResult<T> paginationData){
        if(paginationData==null
                ||paginationData.getSummarizes()==null||paginationData.getSummarizes().isEmpty()
                ||paginationData.getTotalSummarizes()==null||paginationData.getTotalSummarizes().isEmpty()
                ){
            return ;
        }

        Map<String,Object> summarizes = paginationData.getSummarizes();
        Map<String,Object> totalSummarizes = paginationData.getTotalSummarizes();

        paginationData.setSummarizes(newFixedSummary(dataForm,summarizes));
        paginationData.setTotalSummarizes(newFixedSummary(dataForm,totalSummarizes));

    }

    /**
     * 根据显示模板配置，重新修正统计结果
     *
     * @param dataForm dataForm
     * @param summaryData summaryData
     * @return map
     */
    private static Map<String,Object> newFixedSummary(DataForm dataForm,Map<String,Object> summaryData){
        Map<String,Object> retData = new LinkedHashMap<String,Object>();
        Iterator<String> iterator = summaryData.keySet().iterator();
        while (iterator.hasNext()){
            String name = iterator.next();
            Object value = summaryData.get(name);
            DataFormElement element = lookupDataFormElement(dataForm,name);
            if(element!=null){
                Object fitValue = fitValue(element,value);
                String fitName = StringKit.nvl(element.getCode(),element.getColumn());
                retData.put(fitName,fitValue);
            }
        }
        return retData;
    }

    /**
     * 根据字段名称，查找元素
     *
     * @param dataForm dataForm
     * @param name name
     * @return DataFormElement
     */
    public static DataFormElement lookupDataFormElement(DataForm dataForm,String name){
        List<DataFormElement> elements = dataForm.getElements();
        for(DataFormElement element:elements){
            if(name.equalsIgnoreCase(element.getCode())||name.equalsIgnoreCase(element.getColumn())){
                return element;
            }
        }
        return null;
    }

    /**
     * 把JavaBean上没有的字段，补到显示模板上去
     * @param clazz
     * @param column
     * @param alias
     * @param dataForm
     */
    public static void  addBeanPropertyToDataForm(Class<?> clazz,String column,String alias,DataForm dataForm){
        //根据字段补上显示模板元素
        Field field = ClassKit.getField(clazz,alias);
        ElementDataType dataType = getFieldDataType(field);

        DataFormElement patchElement = new DataFormElement(alias,column);
        DataFormElement.FormElementUIHint uiHint = patchElement.getElementUIHint();
        patchElement.setCreatedBy("$DATA_REPAIR");    //标识下是因为数据补全而添加的字段
        patchElement.setDataType(dataType);
        uiHint.setVisible(false);
        uiHint.setReading(true);

        dataForm.addElement(patchElement);
    }

    /**
     * 把JavaBean上没有的字段，从显示模板上拿下来
     * @param dataForm
     */
    public static void  removeBeanPropertyFromDataForm(DataForm dataForm){
        List<DataFormElement> elements = dataForm.getElements();
        List<DataFormElement> removeElements = elements.stream()
                .filter(element->"$DATA_REPAIR".equals(element.getCreatedBy()))
                .collect(Collectors.toList());
        removeElements.forEach(element->{
            dataForm.removeElement(element.getCode());
        });
    }

    /**
     * 根据字段的数据类型，映射元素的数据类型
     * @param field
     * @return
     */
    private static ElementDataType getFieldDataType(Field field){
        ElementDataType dataType = ElementDataType.String;
        if(field == null) return dataType;
        Class<?> typeClass = field.getType();
        if(Number.class.isAssignableFrom(typeClass)){
            if(Integer.class.isAssignableFrom(typeClass)||Long.class.isAssignableFrom(typeClass)){
                dataType = ElementDataType.Integer;
            }else{
                dataType = ElementDataType.Double;
            }
        }else if(Date.class.isAssignableFrom(typeClass)){
            dataType = ElementDataType.Date;
        }else {
            dataType = ElementDataType.String;
        }

        return dataType;
    }



}
