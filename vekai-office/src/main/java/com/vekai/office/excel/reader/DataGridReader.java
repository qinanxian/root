package com.vekai.office.excel.reader;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.vekai.office.excel.utils.ExcelAddressConvert;
import com.vekai.office.excel.utils.ExcelCellHelper;
import com.vekai.office.excel.imports.config.ExcelImportConfig;
import com.vekai.office.excel.imports.config.ExcelImportConfig.ColumnItem;
import com.vekai.office.excel.imports.ImportExecutor;
import com.vekai.office.excel.imports.intercept.InterceptException;
import com.vekai.office.excel.imports.intercept.InterceptHelper;
import cn.fisok.raw.lang.ValueObject;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * 表格类数据读取实现
 *
 * @author syang
 * @since 2014/04/03
 */
public class DataGridReader extends ExcelReaderAbstract<List<ExcelRowData>> {
    @SuppressWarnings("rawtypes")
    private ImportExecutor importExecutor;


    public DataGridReader() {
    }

    /**
     * 获取运行容器对象
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public ImportExecutor getImportExecutor() {
        return importExecutor;
    }

    /**
     * 设置运行容器对象
     *
     * @param runnerContext
     */
    @SuppressWarnings("rawtypes")
    public void setImportExecutor(ImportExecutor runnerContext) {
        this.importExecutor = runnerContext;
    }


    public List<ExcelRowData> readSheet(Sheet sheet,
                                        ExcelImportConfig config) throws ReaderException {
        //从配置中取地址范围
        final int startRowIdx = ExcelAddressConvert.getRowIndex(config.getStartAddr());
        final int startColIdx = ExcelAddressConvert.getColumnIndex(config.getStartAddr());
        final int endRowIdx = ExcelAddressConvert.getRowIndex(config.getEndAddr());
        final int endColIdx = ExcelAddressConvert.getColumnIndex(config.getEndAddr());

        int realEndRowIdx = Math.min(endRowIdx, sheet.getLastRowNum());

        List<ExcelRowData> gridList = new ArrayList<ExcelRowData>();
        int rowNo = 1;
        ExcelRowData rowData = null;
        for (int i = startRowIdx; i <= realEndRowIdx; i++) {
            rowData = new ExcelRowData(config);//构建行级数据容器
            rowData.setRowNo(rowNo++);
            Row row = sheet.getRow(i);
            if (row == null) continue;
            int realEndColIdx = Math.min(endColIdx, row.getLastCellNum());

            //读拦截器-before
            beforeReadRow(rowData, config);

            List<String> nameList = rowData.getNameList();
            boolean fillRow = false;
            for (int n = 0; n < nameList.size(); n++) {
                //查找当前要素对应的配置项
                String fielName = nameList.get(n);
                ValueObject element = rowData.getDataElement(fielName);
                ColumnItem columnItem = config.getColumnItem(fielName);
                if (columnItem == null) {
                    throw new ReaderException(MessageFormat.format("配置数据异常，找不到字段[{0}]对应的配置项", fielName));
                }

                String expr = columnItem.getFetchValue();
                //如果配置的是Excel单元格地址，则从Excel中取值
                if (ExcelAddressConvert.isCellAddress(expr)) {
//					expr += ExcelAddressConvert.getRowName(startRowIdx);
                    int colIdx = ExcelAddressConvert.getColumnIndex(expr);
                    //保证列地址的范围在指定范围中
                    if (colIdx >= startColIdx && colIdx <= realEndColIdx) {
                        boolean b = ExcelCellHelper.fillElementWithCell(sheet, i, colIdx, element);
                        if (b) {
                            fillRow = true;
                            element.setProperty("addr", ExcelAddressConvert.getCellAddress(colIdx, i));
                        }
                    }
                } else {
                    try {
                        element.setValue(getExprValue(expr, rowData, config));
                    } catch (Exception e) {
                        throw new ReaderException(e);
                    }
                }
            }
            if (fillRow) {
                if (afterReadRow(rowData, config)) continue;
                gridList.add(rowData);
            }
        }
        //写拦截器-complete
        readComplete(gridList, config);

        return gridList;
    }


    private void beforeReadRow(ExcelRowData rowData, ExcelImportConfig config) throws ReaderException {
        if (importExecutor == null || config.getIntercepts().size() == 0) return;
        //拦截器beforeReadRow方法
        try {
            InterceptHelper.beforeReadRow(config.getIntercepts(), rowData, config);
        } catch (InterceptException e) {
            throw new ReaderException(e);
        }
    }

    private void readComplete(List<ExcelRowData> data, ExcelImportConfig config) throws ReaderException {
        if (importExecutor == null || config.getIntercepts().size() == 0) return;
        //拦截器beforeReadRow方法
        try {
            InterceptHelper.readComplete(config.getIntercepts(), data, config);
        } catch (InterceptException e) {
            throw new ReaderException(e);
        }
    }

}
