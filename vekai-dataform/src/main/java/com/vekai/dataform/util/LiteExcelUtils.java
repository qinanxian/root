package com.vekai.dataform.util;


import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.StringKit;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * EXCEL操作快捷工具类,仅仅提供给DataForm模块使用，断掉dataform模块对office模块的依赖
 *
 * @author 杨松 syang@amarsoft.com
 */
class LiteExcelUtils {

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

    public static void autoSizeColumn(Sheet sheet){
        if(sheet.getLastRowNum()>0){
            Row row = sheet.getRow(0);
            int cols = row.getLastCellNum();
            for(int i=0;i<cols;i++){
                sheet.autoSizeColumn((short)i); //调整第一列宽度
            }
        }
    }

    public static void setCellValue(Cell cell, Object value) {
//    	System.out.println(cell.getAddress()+"="+value);
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof RichTextString) {
            cell.setCellValue((RichTextString) value);
        } else {
            cell.setCellValue(StringKit.nvl(value,""));
        }
    }

    private static CellStyle touchCellStyle(Cell cell) {
        CellStyle cellStyle = cell.getCellStyle();
        if (cellStyle == null) {
            cellStyle = cell.getSheet().getWorkbook().createCellStyle();
        }
        return cellStyle;
    }

    public static void setDateCellFormat(Cell cell, String format) {
        CellStyle cellStyle = touchCellStyle(cell);

        DataFormat dataFormat = cell.getSheet().getWorkbook().createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat(format));
    }

    public static void setNumberCellFormat(Cell cell, String format) {
        CellStyle cellStyle = touchCellStyle(cell);
        DataFormat dataFormat = cell.getSheet().getWorkbook().createDataFormat();

        cellStyle.setDataFormat(dataFormat.getFormat(format));
    }


    /**
     * 复制单元格
     *
     * @param srcCell srcCell
     * @param distCell distCell
     * @param copyValue copyValue
     */
    @SuppressWarnings("deprecation")
    public static void copyCell(Cell srcCell, Cell distCell, boolean copyValue) {
        Workbook wb = srcCell.getSheet().getWorkbook();
        CellStyle newstyle = wb.createCellStyle();
        copyCellStyle(srcCell.getCellStyle(), newstyle);
        //样式
        distCell.setCellStyle(newstyle);
        //评论
        if (srcCell.getCellComment() != null) {
            distCell.setCellComment(srcCell.getCellComment());
        }
        // 不同数据类型处理
        int srcCellType = srcCell.getCellType();
        distCell.setCellType(srcCellType);
        if (!copyValue) return;

        if (srcCellType == Cell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
                distCell.setCellValue(srcCell.getDateCellValue());
            } else {
                distCell.setCellValue(srcCell.getNumericCellValue());
            }
        } else if (srcCellType == Cell.CELL_TYPE_STRING) {
            distCell.setCellValue(srcCell.getRichStringCellValue());
        } else if (srcCellType == Cell.CELL_TYPE_BLANK) {
        } else if (srcCellType == Cell.CELL_TYPE_BOOLEAN) {
            distCell.setCellValue(srcCell.getBooleanCellValue());
        } else if (srcCellType == Cell.CELL_TYPE_ERROR) {
            distCell.setCellErrorValue(srcCell.getErrorCellValue());
        } else if (srcCellType == Cell.CELL_TYPE_FORMULA) {
            distCell.setCellFormula(srcCell.getCellFormula());
        } else { // nothing29
        }
    }

    /**
     * 复制一个单元格样式到目的单元格样式
     *
     * @param fromStyle fromStyle
     * @param toStyle toStyle
     */
    @SuppressWarnings("deprecation")
    public static void copyCellStyle(CellStyle fromStyle, CellStyle toStyle) {
        if(fromStyle==null)return;
        toStyle.setAlignment(fromStyle.getAlignment());
        //边框和边框颜色
        toStyle.setBorderBottom(fromStyle.getBorderBottom());
        toStyle.setBorderLeft(fromStyle.getBorderLeft());
        toStyle.setBorderRight(fromStyle.getBorderRight());
        toStyle.setBorderTop(fromStyle.getBorderTop());
        toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
        toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
        toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
        toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());

        //背景和前景
        toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
        toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());

        toStyle.setDataFormat(fromStyle.getDataFormat());
        toStyle.setFillPattern(fromStyle.getFillPattern());
//		toStyle.setFont(fromStyle.getFont());
        toStyle.setHidden(fromStyle.getHidden());
        toStyle.setIndention(fromStyle.getIndention());//首行缩进
        toStyle.setLocked(fromStyle.getLocked());
        toStyle.setRotation(fromStyle.getRotation());        //旋转
        toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
        toStyle.setWrapText(fromStyle.getWrapText());

        if(fromStyle instanceof XSSFCellStyle && toStyle instanceof XSSFCellStyle){
            XSSFCellStyle xssFromStyle = (XSSFCellStyle)fromStyle;
            XSSFCellStyle xssToStyle = (XSSFCellStyle)toStyle;
            //字体以及颜色
            xssToStyle.setFont(xssFromStyle.getFont());
            xssToStyle.setFillBackgroundColor(xssFromStyle.getFillBackgroundXSSFColor());
            xssToStyle.setFillForegroundColor(xssFromStyle.getFillForegroundXSSFColor());
        }

    }
}
