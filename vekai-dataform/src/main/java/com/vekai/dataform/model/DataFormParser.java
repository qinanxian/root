package com.vekai.dataform.model;


import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DataForm解析转换工具
 * Created by tisir yangsong158@qq.com on 2017-05-29
 */
public class DataFormParser {
    protected DataForm dataForm;
    protected String querySql;
    protected Map<Integer,String> fieldsMap = new LinkedHashMap<Integer,String>();
    protected Map<Integer,String> summaryFieldsMap = new LinkedHashMap<Integer,String>();
//    protected NameConverter nameConverter;
//    protected NameConverter summaryNameConverter ;
    protected Map<String,String> summaryExpression = new LinkedHashMap<String,String>();

    public DataFormParser(DataForm dataForm) {
        this.dataForm = dataForm;
    }
    public void parse(){
        ValidateKit.notNull(dataForm,"解析DataForm对象上的SQL出错,DataForm对象为空");
        ValidateKit.notNull(dataForm.getQuery(),"解析DataForm对象上的SQL出错,DataForm.query对象为空");

//        final Map<Integer,String> nameConvertMap = MapKit.newHashMap();
//        final Map<Integer,String> summaryNameConvertMap = MapKit.newHashMap();
        //第一个是总数列,PaginationQuery的查询规范是这样制订的
        summaryFieldsMap.put(1,"totalCount");
        int summaryIndex = 2;
        StringBuilder columnsClause = new StringBuilder();

        List<DataFormElement> elements = dataForm.getElements();
        for(int i=0;i<elements.size();i++){
            DataFormElement element = elements.get(i);
            String column = element.getColumn();
            String code = element.getCode();
            Boolean persist = element.getPersist();

            ValidateKit.notBlank(column,"数据异常,属性column不能为空,dataForm=%s.%s",dataForm.getPack(),dataForm.getId());
            ValidateKit.notBlank(code,"数据异常,属性code不能为空,dataForm=%s.%s",dataForm.getPack(),dataForm.getId());
//            columnsClause.append(",").append(column).append(" AS ").append(code);
            columnsClause.append(",").append(column);
            fieldsMap.put(i+1,code);
            if(!StringKit.isBlank(element.getSummaryExpression())){
                summaryExpression.put(column,element.getSummaryExpression());
                summaryFieldsMap.put(summaryIndex,code);
                summaryIndex ++ ;
            }
        }
        if(columnsClause.length()>0)columnsClause.deleteCharAt(0);

        //处理掉以前的select * 部分
        String fromClause = dataForm.getQuery().buildQuerySql();
        fromClause = fromClause.replaceAll("(?u)^\\s*select\\s+\\*\\s+", "");
        StringBuffer sbSQL = new StringBuffer("SELECT ");
        sbSQL.append(columnsClause).append(" ").append(fromClause);

        querySql = sbSQL.toString();
//        nameConverter = new NameConverter() {
//            public String getPropertyName(String columnName) {
//                return null;
//            }
//            public String getPropertyName(int column) {
//                return nameConvertMap.get(column);
//            }
//            public String getColumnName(String propertyName) {
//                return null;
//            }
//            public String getClassName(String tableName) {
//                return null;
//            }
//            public String getTableName(Class<?> clazz) {
//                return null;
//            }
//        };
//        summaryNameConverter = new NameConverter() {
//            public String getPropertyName(String columnName) {
//                return null;
//            }
//            public String getPropertyName(int column) {
//                if(column == 1)return "totalCount";
//                return summaryNameConvertMap.get(column);
//            }
//            public String getColumnName(String propertyName) {
//                return null;
//            }
//            public String getClassName(String tableName) {
//                return null;
//            }
//            public String getTableName(Class<?> clazz) {
//                return null;
//            }
//        };

    }

    public String getQuerySql() {
        return querySql;
    }

//    public NameConverter getNameConverter() {
//        return nameConverter;
//    }
//
//    public NameConverter getSummaryNameConverter() {
//        return summaryNameConverter;
//    }

    public Map<String, String> getSummaryExpression() {
        return summaryExpression;
    }

    public Map<Integer, String> getFieldsMap() {
        return fieldsMap;
    }

    public Map<Integer, String> getSummaryFieldsMap() {
        return summaryFieldsMap;
    }
}
