package com.vekai.dataform.handler.impl;

import com.vekai.dataform.exception.DataFormException;
import com.vekai.dataform.DataFormConsts;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.DataFormFilter;
import com.vekai.dataform.model.types.ElementDataType;
import com.vekai.dataform.model.types.FormDataModelType;
import cn.fisok.raw.kit.JpaKit;
import cn.fisok.raw.kit.SQLKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 过滤器上对WHERE条件的处理
 */
public class FilterWhereProcessor {
    protected static Logger logger = LoggerFactory.getLogger(FilterWhereProcessor.class);

    protected static Class<?> getFormClass(DataForm dataForm){
        Class<?> clazz = null;
        try {
            clazz = (Class<?>) Class.forName(dataForm.getDataModel());
        } catch (ClassNotFoundException e) {
            throw new DataFormException("",e);
        }
        return clazz;
    }

    public static ValueObject getFilterValue(DataForm dataForm, Map<String, ?> filterData, String code){
        DataFormFilter filter = dataForm.getFilter(code);
        if(filter==null)return null;
        ValueObject value = ValueObject.valueOf(filterData.get(code));
        return value;
    }

    private static Object getValueByElement(DataFormElement element, ValueObject value){
        if(element.getDataType()== ElementDataType.Double){
            return value.doubleValue();
        }else if(element.getDataType()== ElementDataType.Integer){
            return value.intValue();
        }else if(element.getDataType()== ElementDataType.Date){
            return value.dateValue();
        }else{
            return value.strValue();
        }
    }

    private static Object[] getValuesByElement(DataFormElement element,ValueObject value){
        if(element.getDataType()== ElementDataType.Double){
            return value.doubleArray();
        }else if(element.getDataType()== ElementDataType.Integer){
            return value.intArray();
        }else if(element.getDataType()== ElementDataType.Date){
            return value.dateArray();
        }else{
            return value.strArray();
        }
    }
    /**
     * 拼装单项查询子句，并且重新组装参数
     * @param filter
     * @param element
     * @param column
     * @param code
     * @param value
     * @param sqlParameter
     * @return
     */
    private static String buildItemSQLAndFillParam(DataFormFilter filter,DataFormElement element,String column,String code,ValueObject value,Map<String,?> sqlParameter){
        Map<String,Object> param = (Map<String, Object>) sqlParameter;
        StringBuffer filterBuffer = new StringBuffer();
        switch (filter.getComparePattern()){
            case StartWith:
                filterBuffer.append(column).append(" LIKE ").append(":").append(code);
                param.put(code,value.strValue()+"%");
                break;
            case EndWith:
                filterBuffer.append(column).append(" LIKE ").append(":").append(code);
                param.put(code,"%"+value.strValue());
                break;
            case Contain:
                filterBuffer.append(column).append(" like ").append(":").append(code);
                param.put(code,"%"+value.strValue()+"%");
                break;
            case Equal:
                filterBuffer.append(column).append(" = ").append(":").append(code);
                param.put(code,getValueByElement(element,value));
                break;
            case NotEqual:
                filterBuffer.append(column).append(" <> ").append(":").append(code);
                param.put(code,getValueByElement(element,value));
                break;
            case Range:
                filterBuffer.append(column)
                        .append(" BETWEEN ")
                        .append(":").append(code).append("1")
                        .append(" AND ")
                        .append(":").append(code).append("2");
                Object[] values = getValuesByElement(element,value);
                if(values!=null&&values.length>1){
                    param.put(code+"1",values[0]);
                    param.put(code+"2",values[1]);
                }
                break;
            case GreaterThan:
                filterBuffer.append(column).append(" > ").append(":").append(code);
                param.put(code,getValueByElement(element,value));
                break;
            case LessThan:
                filterBuffer.append(column).append(" < ").append(":").append(code);
                param.put(code,getValueByElement(element,value));
                break;
        }
        return filterBuffer.toString();
    }

    private static String getColumn(DataForm dataForm,DataFormElement element){
        String column = "";
        if(dataForm.getDataModelType() == FormDataModelType.JavaBean){
            Class<?> clazz = getFormClass(dataForm);
            column = JpaKit.getColumn(clazz,element.getCode());
            if(StringKit.isBlank(column)){
                column = element.getColumn();
            }
        }else{
            column = element.getColumn();
            if(StringKit.isNotBlank(element.getTable())){
                column = element.getTable()+"."+element.getColumn();
            }
        }
        return column;
    }

    public static void parseFilterFillParam(DataForm dataForm,Map<String, ?> filterParameters,Map<String,?> sqlParameter){
        if(filterParameters!=null&&filterParameters.size()>0){
            StringBuffer subQueryBuffer = new StringBuffer();

            Iterator<String> iterator = filterParameters.keySet().iterator();
            while(iterator.hasNext()){
                /**
                 * 1.从参数中取代码，
                 * 2.根据代码找到过滤器，
                 * 3.根据过滤器绑定参数，找到业务要素字段，
                 * 4.根据业务要素代码，
                 * 5.根据业务要素代码数据表字段
                 * 6.根据过滤器设置，拼装相应的SQL
                 */

                String code = iterator.next();
                ValueObject value = ValueObject.valueOf(filterParameters.get(code));
                DataFormFilter filter = dataForm.getFilter(code);
                if(filter == null)continue;
                String bindFor = filter.getBindFor();
                if(StringKit.isBlank(bindFor))continue;
                DataFormElement element = dataForm.getElement(bindFor);
                if(element == null) {
                    logger.warn("过滤器["+code+"],找不到绑定的字段元素");
                    continue;
                }

                String column = getColumn(dataForm,element);
                //把WHERE查询子句拼起来
                String condItem = buildItemSQLAndFillParam(filter,element,column,code,value,sqlParameter);
                subQueryBuffer.append(" AND ").append(condItem);
            }

            if(subQueryBuffer.length()>0){
                String where = dataForm.getQuery().getWhere();
                if(StringKit.isBlank(where)){
                    where  = subQueryBuffer.substring(5);  //去掉第一个AND
                }else{
                    where  += " AND ("+subQueryBuffer.substring(5)+")";  //去掉第一个AND
                }
                dataForm.getQuery().setWhere(where);
            }
        }
    }

    public static void parseQuickFilterFillParam(DataForm dataForm,Map<String, ?> filterParameters,Map<String,?> sqlParameter){
//        ValueObject quickValue = ValueObject.valueOf(filterParameters.get(DataFormConsts.QUICK_QUERY_PARAM_NAME));
        List<DataFormFilter> filters = dataForm.getFilters();

        StringBuffer quickWhere = new StringBuffer();
        for(DataFormFilter filter : filters){
            if(filter == null)continue;
            if(!filter.getQuick())continue;//只要设置了快速搜索的

            String bindFor = filter.getBindFor();
            if(StringKit.isBlank(bindFor))continue;
            DataFormElement element = dataForm.getElement(bindFor);
            if(element == null) {
                logger.warn("过滤器["+filter.getCode()+"],找不到绑定的字段元素");
                continue;
            }

            String column = getColumn(dataForm,element);
            if(quickWhere.length()>0){
                quickWhere.append("OR");
            }
            quickWhere.append(" ").append(column).append(" LIKE :").append(DataFormConsts.QUICK_QUERY_PARAM_NAME).append(" ");
        }

        if(quickWhere.length()>0){
            String where = dataForm.getQuery().getWhere();
            StringBuffer sbWhere = new StringBuffer("");
            if(StringKit.isNotBlank(where)){
                sbWhere.append(where);
                sbWhere.append(" AND (").append(quickWhere).append(")");
            }else{
                sbWhere.append(quickWhere);
            }
            dataForm.getQuery().setWhere(sbWhere.toString());
        }
    }

    public static void parseFilter(DataForm dataForm,Map<String, ?> filterParameters,Map<String,Object> quickParameters,Map<String,?> sqlParameter){
        if(filterParameters.containsKey(DataFormConsts.QUICK_QUERY_PARAM_NAME)){
            ValueObject quickValue = ValueObject.valueOf(filterParameters.get(DataFormConsts.QUICK_QUERY_PARAM_NAME));
            FilterWhereProcessor.parseQuickFilterFillParam(dataForm,filterParameters,sqlParameter);
            String value = quickValue.strValue();
            value = SQLKit.replaceSQLInjector(value);
            value = "%"+quickValue.strValue()+"%";
            quickParameters.put(DataFormConsts.QUICK_QUERY_PARAM_NAME,value);
        }else{
            FilterWhereProcessor.parseFilterFillParam(dataForm,filterParameters,sqlParameter);
        }
    }
}
