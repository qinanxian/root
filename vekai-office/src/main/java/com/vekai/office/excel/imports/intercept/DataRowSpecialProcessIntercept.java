package com.vekai.office.excel.imports.intercept;

import com.vekai.office.excel.reader.ExcelRowData;
import com.vekai.office.excel.imports.config.ExcelImportConfig;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 特殊处理拦截器，主要完成以下两个功能<br/>
 * 1.根据配置，确定清除空格方式<br/>
 * 2.处理日期格式<br/>
 *
 * @author yangsong
 * @since 2014/04/07
 */
public class DataRowSpecialProcessIntercept implements DataProcessIntercept {

    public void afterReadRow(ExcelRowData rowData, ExcelImportConfig config)
            throws InterceptException {
        List<String> nameList = rowData.getNameList();
        for (int i = 0; i < nameList.size(); i++) {
            String fieldName = nameList.get(i);
            ValueObject fieldValue = rowData.getDataElement(fieldName);
            ExcelImportConfig.ColumnItem configItem = config.getColumnItem(fieldName);
            if (configItem == null) continue;
            String expr = configItem.getSpecialProcess();
            dealStrWithSpace(fieldValue, expr, configItem);
        }

    }

    public void afterWriteRow(ExcelRowData rowData, ExcelImportConfig config)
            throws InterceptException {

    }

    public void beforeReadRow(ExcelRowData rowData, ExcelImportConfig config)
            throws InterceptException {

    }

    public void beforeWriteRow(ExcelRowData rowData, ExcelImportConfig config)
            throws InterceptException {
        List<String> nameList = rowData.getNameList();
        for (int i = 0; i < nameList.size(); i++) {
            String fieldName = nameList.get(i);
            ValueObject fieldValue = rowData.getDataElement(fieldName);
            ExcelImportConfig.ColumnItem configItem = config.getColumnItem(fieldName);
            if (configItem == null) continue;

            String expr = configItem.getSpecialProcess();
            //处理日期格式
            if (configItem.getDataType() == ExcelImportConfig.DataType.Date
                    && !fieldValue.isBlank()
                    && expr.startsWith("日期格式:")) {
                String dataFormatText = expr.replaceFirst("日期格式:", "");
                DateFormat dateFormat = new SimpleDateFormat(dataFormatText);
                fieldValue.setValue(dateFormat.format(fieldValue.dateValue()));
            }

            dealStrWithSpace(fieldValue, expr, configItem);
        }

    }

    public void readComplete(List<ExcelRowData> data, ExcelImportConfig config)
            throws InterceptException {

    }

    public void writeComplete(List<ExcelRowData> data, ExcelImportConfig config)
            throws InterceptException {

    }

    private void dealStrWithSpace(ValueObject element, String expr, ExcelImportConfig.ColumnItem configItem) {
        //字串特殊处理
        if (!element.isBlank()) {
            String stringValue = element.strValue();
            if ("清除前端空格".endsWith(expr)) {
                stringValue = StringKit.ltrim(stringValue);
            } else if ("清除后端空格".endsWith(expr)) {
                stringValue = StringKit.rtrim(stringValue);
            } else if ("清除两端空格".endsWith(expr)) {
                stringValue = StringKit.trim(stringValue);
            } else if ("清除所有空格".endsWith(expr)) {
                stringValue = StringKit.clearSpace(stringValue);
            }
            if (configItem.getDataType() == ExcelImportConfig.DataType.String) {
                element.setValue(stringValue);
            } else if (configItem.getDataType() == ExcelImportConfig.DataType.Double) {
                if (stringValue.indexOf(" ") != -1) {
                    stringValue = stringValue.replaceAll(" ", "");
                }
                Double d = Double.parseDouble(stringValue);
                element.setValue(d);
            }
        }
    }
}
