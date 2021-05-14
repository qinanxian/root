package com.vekai.office.excel.utils;

import cn.fisok.raw.lang.ValueObject;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;

/**
 * Excel单元格工具类
 * @author yangsong
 * @since 2014/04/02
 *
 */
public class ExcelCellHelper {
	
	/**
	 * 
	 * 根据指定的单元格地址位置，从Excel地址中取出数据并且把数据填充到DataElement中
	 * @param sheet 页对象
	 * @param rowIdx 行索引
	 * @param colIdx 列索引
	 * @param valueObject 元素
	 * @return 如果有值填充了，返回true
	 */
	public static boolean fillElementWithCell(Sheet sheet,int rowIdx,int colIdx,ValueObject valueObject){
		Row row = sheet.getRow(rowIdx);
		if(row==null)return false;
		Cell cell = row.getCell(colIdx);
		if(cell==null)return false;
		fillElementWithCell(cell,valueObject);
		return true;
	}
	
	/**
	 * 使用Cell中的值填充DataElement
	 * @param cell
	 * @param valueObject
	 */
	public static void fillElementWithCell(Cell cell,ValueObject valueObject){
		Object cellValue = getCellValue(cell);
		if(cellValue!=null){
			valueObject.setValue(cellValue);
		}else{
			valueObject.setNull();
		}
	}
	
	/**
	 * 获取单元格的值
	 * @param cell
	 * @return
	 */
    @SuppressWarnings("deprecation")
    public static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum();
        Object ret = null;

        switch (cellType) {
        case STRING:
            ret = cell.getStringCellValue();
            break;
        case NUMERIC:
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                ret = cell.getDateCellValue();
            } else if (cell.getCellStyle().getDataFormat() == 58) {// 处理自定义日期格式
                ret = DateUtil.getJavaDate(cell.getNumericCellValue());
            } else {
                ret = cell.getNumericCellValue();
            }
            break;
        case FORMULA:
            try {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    ret = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                } else {
                    ret = String.valueOf(cell.getNumericCellValue());
                }
            } catch (IllegalStateException e) {
                try {
                    ret = String.valueOf(cell.getRichStringCellValue());
                } catch (IllegalStateException e1) {
                    ret = "#VALUE!";
                }
            }
            break;
        case ERROR:
            ret = cell.getErrorCellValue();
            break;
        case BOOLEAN:
            ret = cell.getBooleanCellValue();
            break;
        default:
            ret = cell.getStringCellValue();
            break;
        }
        return ret;
    }
	
	/**
	 * 获取单元格的值，并且使用DataElement包装
	 * @param cell
	 * @return
	 */
	public static ValueObject getCellValueAsDataElement(Cell cell){
		ValueObject value = new ValueObject();
		fillElementWithCell(cell,value);
		return value;
	}
}
