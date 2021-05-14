package com.vekai.office.excel.imports.intercept;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vekai.office.excel.reader.ExcelRowData;
import com.vekai.office.excel.imports.config.ExcelImportConfig;
import com.vekai.office.excel.imports.config.ExcelImportConfig.ColumnItem;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;


/**
 * 数据转换拦截器，完成代码转换，空格清理等操作
 *
 * @author yangsong
 * @since 2014/04/03
 */
public class DataRowConvertIntercept implements DataProcessIntercept {
    private Map<String, Map<String, String>> codeConvert = new HashMap<String, Map<String, String>>();

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DataRowConvertIntercept() {
    }

    public DataRowConvertIntercept(JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    public void beforeReadRow(ExcelRowData rowData, ExcelImportConfig config)
            throws InterceptException {

    }

    public void afterReadRow(ExcelRowData rowData, ExcelImportConfig config)
            throws InterceptException {
        initConvertMap(config);

        List<String> nameList = rowData.getNameList();
        for (int i = 0; i < nameList.size(); i++) {
            String fieldName = nameList.get(i);
            ValueObject fieldValue = rowData.getDataElement(fieldName);
            ColumnItem configItem = config.getColumnItem(fieldName);
            if (configItem == null) {
                continue;
            }
            if (StringKit.isEmpty(configItem.getValueConvert())) continue;
            //值转换
            convertValue(configItem, fieldValue);
        }
    }

    public void readComplete(List<ExcelRowData> data, ExcelImportConfig config)
            throws InterceptException {

    }

    public void beforeWriteRow(ExcelRowData rowData, ExcelImportConfig config)
            throws InterceptException {
//	    initConvertMap(config);
//		List<DataElement> elements = rowData.getAllDataElements();
//		for(int i=0;i<elements.size();i++){
//			DataElement element = elements.get(i);
//			ColumnItem configItem = config.getColumnItem(element.getName());
//			if(configItem==null){
//				continue;
//			}
//			if(StringX.isEmpty(configItem.getValueConvert()))continue;
//			//值转换
//			convertValue(configItem,element);
//		}		

    }


    private void convertValue(ColumnItem configItem, ValueObject valueObject) {
        if (valueObject.isEmpty()) return;
        String fieldName = configItem.getName();
        Map<String, String> codeItem = codeConvert.get(fieldName);
        if (codeItem == null || codeItem.size() == 0) return;
        String strValue = valueObject.strValue();
        if(StringKit.isBlank(strValue))return;
        strValue = strValue.replaceAll("\\s+","");  //删除所有空格

        //看是不是多值的情况
        String value = "";
        if(strValue.indexOf(",")>0){
            String[] values = strValue.split(",");
            StringBuffer sbValue = new StringBuffer();
            for(String v :values){
                sbValue.append(getDictItemCode(codeItem,v)).append(",");
            }
            if(sbValue.length()>0){
                sbValue.deleteCharAt(sbValue.length()-1);
            }
            value = sbValue.toString();
        }else{
            value = getDictItemCode(codeItem,strValue);
        }

        if(StringKit.isNotBlank(value)){
            valueObject.setValue(value);
        }

    }

    private String getDictItemCode(Map<String, String> codeItem,String name){
        String ret = name;
        if(name.lastIndexOf("/")>0){    //处理多级代码的问题
            String key = name.substring(name.lastIndexOf("/")+1) ;
            ret = codeItem.get(key);
        }else{
            ret = codeItem.get(name);
        }
        ret = StringKit.nvl(ret,name);
        return ret;
    }

    //控制对照表只初始化一次
    private boolean convertMapInited = false;

    private void initConvertMap(ExcelImportConfig config) throws InterceptException {
        if (convertMapInited) return;
        List<ColumnItem> items = config.getAllColumnItems();
        for (int i = 0; i < items.size(); i++) {
            ColumnItem item = items.get(i);
            String convertExpr = item.getValueConvert();
            if (StringKit.isEmpty(convertExpr)) continue;

            if (convertExpr.startsWith("Code:")) {
                String codeNo = convertExpr.replaceFirst("Code:", "");
                initCode(item.getName(), codeNo, config);
            } else if (convertExpr.startsWith("CodeTable:")) {
                String codeTable = convertExpr.replaceFirst("CodeTable:", "");
                initCodeTable(item.getName(), codeTable, config);
            } else if (convertExpr.startsWith("CodeSQL:")) {
                String sql = convertExpr.replaceFirst("CodeSQL:", "");
                initCodeSql(item.getName(), sql, config);
            }
        }
        convertMapInited = true;
    }

    /**
     * 使用代码表初始化代码对照表
     *
     * @param colName
     * @param codeNo
     * @param config
     * @throws InterceptException
     */
    private void initCode(String colName, String codeNo, ExcelImportConfig config) throws InterceptException {
//		CodeMapRestCtrl  codeMaprestCtrl = new CodeMapRestCtrl();
//		List<TreeNodeModel> codeMapList = codeMaprestCtrl.getCodeMap(codeNo, false);
//		Map<String, String> codeMap = new HashMap<String,String>();
//		for(int i=0;i<codeMapList.size();i++){
//			String itemName = codeMapList.get(i).getName();
//			String itemNo = codeMapList.get(i).getId();
//			codeMap.put(itemName, itemNo);
//		}
//		if(codeMap.size()>0){
//			codeConvert.put(colName, codeMap);
//		}
        String sql = "select NAME,CODE from FOWK_DICT_ITEM where DICT_CODE='"+codeNo+"' and STATUS in ('1','2')";
        initCodeSql(colName,sql,config);
    }

    /**
     * 使用SQL初始化代码对照表
     *
     * @param colName
     * @param sql
     * @param config
     * @throws InterceptException
     */
    private void initCodeSql(String colName, String sql, ExcelImportConfig config) throws InterceptException {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        Map<String, String> codeMap = new HashMap<String, String>();
        while (rs.next()) {
            String itemName = rs.getString(1);
            String itemNo = rs.getString(2);
            codeMap.put(itemName,itemNo);
        }
        if (codeMap.size() > 0) {
            codeConvert.put(colName, codeMap);
        }
    }

    /**
     * 使用CodeTable初始化对照表
     */
    private void initCodeTable(String colName, String codeTable, ExcelImportConfig config) throws InterceptException {
        codeTable = codeTable.replaceAll("\\{|\\}|\\s+", "");
        String[] codeItems = codeTable.split(",");
        Map<String, String> codeMap = new HashMap<String, String>();
        for (int i = 0; i < codeItems.length; i++) {
            String codeItem = codeItems[i];
            String[] kv = codeItem.split(":");
            if (kv.length != 2) return;

            String itemName = kv[0];
            String itemNo = kv[1];
            codeMap.put(itemName, itemNo);
        }

        if (codeMap.size() > 0) {
            codeConvert.put(colName, codeMap);
        }
    }

    public void afterWriteRow(ExcelRowData rowData, ExcelImportConfig config)
            throws InterceptException {

    }


    public void writeComplete(List<ExcelRowData> data, ExcelImportConfig config)
            throws InterceptException {

    }
}
