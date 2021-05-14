package com.vekai.office.excel.reader;

import java.io.Serializable;
import java.util.*;

import com.vekai.office.excel.imports.config.ExcelImportConfig;
import com.vekai.office.excel.imports.config.ExcelImportConfig.ColumnItem;
import com.vekai.office.excel.imports.config.ExcelImportConfig.DataType;
import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.lang.ValueObject;
import cn.fisok.raw.lang.ValueObject.ValueType;

/**
 * Excel行数据
 *
 * @author yangsong
 * @since 2014/04/03
 */
public class ExcelRowData implements Serializable {

    private static final long serialVersionUID = 1355591120953917384L;
    private int rowNo = 0;
    private Map<String,ValueObject> rowData = new LinkedHashMap<String,ValueObject>();

    /**
     * 创建一个数据行对象
     *
     * @param config
     */
    public ExcelRowData(ExcelImportConfig config) {
        this.rowData = new LinkedHashMap<String,ValueObject>();
        List<ColumnItem> items = config.getAllColumnItems();
        //根据配置信息，初始化数据区域
        for (int i = 0; i < items.size(); i++) {
            ColumnItem item = items.get(i);
            String fieldName = item.getName();
            ValueObject value = new ValueObject();
            if (item.getDataType() == DataType.String) {
                value.setValueType(ValueType.String);
            } else if (item.getDataType() == DataType.Double) {
                value.setValueType(ValueType.Double);
            } else if (item.getDataType() == DataType.Integer) {
                value.setValueType(ValueType.Integer);
            } else if (item.getDataType() == DataType.Long) {
                value.setValueType(ValueType.Long);
            } else if (item.getDataType() == DataType.Date) {
                value.setValueType(ValueType.Date);
            } else if (item.getDataType() == DataType.Boolean) {
                value.setValueType(ValueType.Boolean);
            } else {
                value.setValueType(ValueType.String);
            }
//            rowData.put(fieldName.toUpperCase(), value);
            rowData.put(fieldName, value);
        }
    }

    public List<String> getNameList(){
        List<String> list = ListKit.newArrayList();
        Iterator<String> iterator = rowData.keySet().iterator();
        while(iterator.hasNext()){
            list.add(iterator.next());
        }
        return list;
    }

    public Map<String, ValueObject> getRowData() {
        return rowData;
    }

    public void setRowData(Map<String, ValueObject> rowData) {
        this.rowData = rowData;
    }

    /**
     * 设置数据行号
     */
    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    /**
     * 获取行号顺序号
     *
     * @return
     */
    public int getRowNo() {
        return rowNo;
    }

    /**
     * 获取该行对象的所有元素
     *
     * @return
     */
    public List<ValueObject> getAllDataElements() {
        List<ValueObject> elements = new ArrayList<ValueObject>();
        elements.addAll(rowData.values());
        return elements;
    }

    /**
     * 清除数据区的数据
     */
    public void resetData() {
        List<ValueObject> elements = getAllDataElements();
        for (int i = 0; i < elements.size(); i++) {
            ValueObject element = elements.get(i);
            element.setNull();
        }
    }

    /**
     * 取元素值
     *
     * @param name
     * @return
     */
    public ValueObject getDataElement(String name) {
        if (name == null) return null;
//        return rowData.get(name.toUpperCase());
        return rowData.get(name);
    }

    /**
     * 当前行对象元素个数
     *
     * @return
     */
    public int size() {
        return rowData.size();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(rowNo + "-").append(rowData).append("");
        return builder.toString();
    }
}
