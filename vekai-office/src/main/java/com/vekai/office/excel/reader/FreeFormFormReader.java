package com.vekai.office.excel.reader;

import java.util.ArrayList;
import java.util.List;

import com.vekai.office.excel.imports.config.ExcelImportConfig;
import com.vekai.office.excel.imports.intercept.InterceptException;
import com.vekai.office.excel.imports.intercept.InterceptHelper;
import com.vekai.office.excel.utils.ExcelAddressConvert;
import com.vekai.office.excel.utils.ExcelCellHelper;
import cn.fisok.raw.lang.ValueObject;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 自由表格数据读取
 *
 * @author EX-YANGSONG001
 */
public class FreeFormFormReader extends ExcelReaderAbstract<ExcelRowData> {
	protected Logger logger = LoggerFactory.getLogger(getClass());



    public FreeFormFormReader() {
    }


    public ExcelRowData readSheet(Sheet sheet, ExcelImportConfig config)
            throws ReaderException {
        ExcelRowData data = new ExcelRowData(config);
        List<ExcelImportConfig.ColumnItem> items = config.getAllColumnItems();
        //校验是址是否有问题
        for (ExcelImportConfig.ColumnItem item : items) {
            String cellAddr = item.getFetchValue();
            int rowIdx = ExcelAddressConvert.getRowIndex(cellAddr);
            int colIdx = ExcelAddressConvert.getColumnIndex(cellAddr);
            rowIdx = Math.min(rowIdx, sheet.getLastRowNum());
            if (rowIdx > sheet.getLastRowNum())
                logger.warn("地址[" + cellAddr + "]超出了数据的最大行数[" + ExcelAddressConvert.getRowName(sheet.getLastRowNum()) + "]");
            Row row = sheet.getRow(rowIdx);
            colIdx = Math.min(colIdx, row.getLastCellNum());
            if (colIdx > row.getLastCellNum())
                logger.warn("地址[" + cellAddr + "]超出了数据的最大列数[" + ExcelAddressConvert.getColumnName(row.getLastCellNum()) + "]");
        }
        //================
        //读拦截器-before
        beforeReadRow(data, config);

        List<String> nameList = data.getNameList();
        boolean fillRow = false;
        for (int n = 0; n < nameList.size(); n++) {
            //查找当前要素对应的配置项
            String filedName = nameList.get(n);
            ValueObject fieldValue = data.getDataElement(filedName);
            ExcelImportConfig.ColumnItem columnItem = config.getColumnItem(filedName);

            String expr = columnItem.getFetchValue();
            //如果配置的是Excel单元格地址，则从Excel中取值
            if (ExcelAddressConvert.isCellAddress(expr)) {
                int rowIdx = ExcelAddressConvert.getRowIndex(expr);
                int colIdx = ExcelAddressConvert.getColumnIndex(expr);
                boolean b = ExcelCellHelper.fillElementWithCell(sheet, rowIdx, colIdx, fieldValue);
                if (b) {
                    fillRow = true;
                    fieldValue.setProperty("addr", expr);
                }
            } else {
                try {
                    fieldValue.setValue(getExprValue(expr, data, config));
                } catch (Exception e) {
                    throw new ReaderException(e);
                }
            }
        }
        if (fillRow) {
            if (afterReadRow(data, config)) return null;
        }
        //写拦截器-complete
        List<ExcelRowData> list = new ArrayList<ExcelRowData>();
        list.add(data);
        readComplete(list, config);
        return data;
    }

    private void beforeReadRow(ExcelRowData rowData, ExcelImportConfig config) throws ReaderException {
        if (config.getIntercepts().size() == 0) return;
        //拦截器beforeReadRow方法
        try {
            InterceptHelper.beforeReadRow(config.getIntercepts(), rowData, config);
        } catch (InterceptException e) {
            throw new ReaderException(e);
        }
    }

    private void readComplete(List<ExcelRowData> data, ExcelImportConfig config) throws ReaderException {
        if (config.getIntercepts().size() == 0) return;
        //拦截器beforeReadRow方法
        try {
            InterceptHelper.readComplete(config.getIntercepts(), data, config);
        } catch (InterceptException e) {
            throw new ReaderException(e);
        }
    }

}
