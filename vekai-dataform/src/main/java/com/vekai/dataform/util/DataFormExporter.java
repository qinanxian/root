package com.vekai.dataform.util;

import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormCombiner;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.types.ElementDataEditStyle;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.core.PaginResult;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * 把DataForm数据导出为
 */
public class DataFormExporter {
    private DataFormCombiner<?> combiner;
    private int sheetIndex = 0;
    private int headerOffset = 0;
    private int dataOffset = 1;

    private Cell headerTemplate = null;
    private Cell textTemplate = null;
    private Cell[] currencyTemplate = null;
    private Cell[] dateTemplate = null;

    public DataFormExporter(DataFormCombiner combiner) {
        this.combiner = combiner;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }


    /**
     * 打开EXCEL文件获取工作薄对象
     *
     * @param inputStream inputStream
     * @return Workbook
     * @throws IOException IOException
     */
    public static Workbook openWorkbook(InputStream inputStream) throws IOException {
        Workbook workBook = null;
        InputStream newIs = inputStream;
        try {
            try {
                newIs = IOKit.convertToByteArrayInputStream(newIs);
                workBook = new XSSFWorkbook(OPCPackage.open(newIs));
            } catch (NotOfficeXmlFileException e) {
                newIs.reset();
                workBook = new HSSFWorkbook(newIs);
            }
        } catch (IOException e) {
            throw e;
        } catch (OpenXML4JException e) {
            throw new IOException("读取流2007+格式异常", e);
        } finally {
            IOKit.close(newIs);
        }
        return workBook;
    }
    /**
     * 填充数据
     * @param templateResource 数据输入模板
     * @param outputStream 数据输出流
     */
    public void exportListToExcel(InputStream templateResource,OutputStream outputStream) throws IOException {
        Workbook workbook = LiteExcelUtils.openWorkbook(templateResource);
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        fillHeader(sheet);
        fillDataList(sheet);
        LiteExcelUtils.autoSizeColumn(sheet);
        workbook.write(outputStream);
    }

    private void fillHeader(Sheet sheet){
        DataForm dataForm = combiner.getMeta();

        Row row = sheet.getRow(headerOffset);
        if(row == null){
            throw new NullPointerException(StringKit.format("row [{0}] does not exist",headerOffset));
        }
        if(row.getLastCellNum()<=0){
            throw new NullPointerException(StringKit.format("cell [{0},{1}] does not exist",headerOffset,0));
        }
        int offset = 0;
        Cell startCell = row.getCell(offset);

        List<DataFormElement> elementList = dataForm.getElements();
        for(DataFormElement element:elementList){
            DataFormElement.FormElementUIHint uiHint = element.getElementUIHint();
            //不可见的元素，忽略
            if(element.getEnabled() == false ||uiHint.getVisible() == false){
                continue;
            }

            Cell cell = null;
            if(offset==0){
                cell = startCell;
            }else{
                cell = row.createCell(offset);
                LiteExcelUtils.copyCell(startCell,cell,false);
            }
            cell.setCellValue(element.getName());

            offset ++;

        }

    }
    private void fillDataList(Sheet sheet){
        DataForm dataForm = combiner.getMeta();

        Row startRow = sheet.getRow(dataOffset);
        if(startRow == null){
            startRow = sheet.createRow(dataOffset);
        }
        if(startRow.getLastCellNum()<=0){
            startRow.createCell(0);
        }
        Cell startCell = startRow.getCell(0);

        Object bodyWrapper = combiner.getBody();
        //不是分列列表数据，则不要在这里处理了
        if(!(bodyWrapper instanceof PaginResult))return;
        PaginResult<?> body = (PaginResult<?>)bodyWrapper;
        List<?> dataList = body.getDataList();

        for(int i=0;i<dataList.size();i++){
            Object rowData = dataList.get(i);
            Row excelRow = null;
            if(i==0){
                excelRow = startRow;
            }else{
                excelRow = sheet.createRow(i+dataOffset);
            }
            //根据表头填充数据
            List<DataFormElement> elementList = dataForm.getElements();
            int offset = 0;
            for(DataFormElement element:elementList){
                DataFormElement.FormElementUIHint uiHint = element.getElementUIHint();
                //不可见的元素，忽略
                if(element.getEnabled() == false ||uiHint.getVisible() == false){
                    continue;
                }
                Object value = BeanKit.getPropertyValue(rowData,element.getCode());

                Cell cell = null;
                if(offset == 0 && i==0){
                    cell = startCell;
                }else{
                    cell = excelRow.createCell(offset);
                }

                LiteExcelUtils.copyCell(startCell,cell,false);
                //币种处理
                if(uiHint.getEditStyle() == ElementDataEditStyle.Currency){
                    Integer digits = element.getDecimalDigits();    //小数位数
                    StringBuffer suffix = new StringBuffer();
                    for(int x=0;x<digits;x++){
                        suffix.append("0");
                    }
                    String formatStr = "###,###,###,###,###,##0";
                    if(suffix.length()>0){
                        formatStr += ("."+suffix.toString());
                    }

                    LiteExcelUtils.setNumberCellFormat(cell,formatStr);

                }else if(uiHint.getEditStyle() == ElementDataEditStyle.DatePicker || value instanceof Date){
                    LiteExcelUtils.setNumberCellFormat(cell, DateKit.DATE_FORMAT);
                }else if(uiHint.getEditStyle() == ElementDataEditStyle.DateTimePicker){
                    LiteExcelUtils.setNumberCellFormat(cell, DateKit.DATE_TIME_FORMAT);
                }

                LiteExcelUtils.setCellValue(cell,value);
                offset ++;

            }


        }


    }
}
