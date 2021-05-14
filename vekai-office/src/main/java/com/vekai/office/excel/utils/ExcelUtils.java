package com.vekai.office.excel.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.StringKit;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * EXCEL操作快捷工具类
 *
 * @author 杨松<syang@amarsoft.com>
 * @date 2017年3月21日
 */
public class ExcelUtils {

    /**
     * 打开EXCEL文件获取工作薄对象
     *
     * @param inputStream
     * @return
     * @throws IOException
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

    public static Row createRow(Sheet sheet, int rowIndex, Row referRow) {
        Row row = sheet.createRow(rowIndex);
        for (int i = 0; i < referRow.getLastCellNum(); i++) {
            Cell cell = row.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
            Cell referCell = referRow.getCell(i);

            cell.setCellStyle(referCell.getCellStyle());
        }
        return row;
    }

    public static Cell getCell(Sheet sheet, int rowIndex, int colIndex) {
        if (rowIndex <= sheet.getLastRowNum()) {
            Row row = sheet.getRow(rowIndex);
            if (colIndex <= row.getLastCellNum()) {
                return row.getCell(colIndex);
            }
        }
        return null;
    }

    public static void clearRow(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            row.getCell(i).setCellValue("");
        }
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

    /**
     * 复制一个单元格样式到目的单元格样式
     *
     * @param fromStyle
     * @param toStyle
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

    /**
     * 行复制功能
     *
     * @param fromRow
     * @param toRow
     */
    public static void copyRow(Row fromRow, Row toRow, boolean copyValueFlag) {
        for (int i = 0; i < fromRow.getLastCellNum(); i++) {
            Cell fromCell = fromRow.getCell(i);
            Cell toCell = toRow.createCell(fromCell.getColumnIndex());
            copyCell(fromCell, toCell, copyValueFlag);
        }
    }

    /**
     * 行复制功能
     *
     * @param fromRow
     * @param toRow
     */
    public static void copyRowFormat(Row fromRow, Row toRow) {
        for (int i = 0; i < fromRow.getLastCellNum(); i++) {
            Cell fromCell = fromRow.getCell(i);
            Cell toCell = toRow.getCell(i);
            if (fromCell == null) continue;
            if (toCell == null) continue;
            copyCell(fromCell, toCell, false);
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
     * @param srcCell
     * @param distCell
     * @param copyValue
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
     * 复制行
     *
     * @param sheet
     * @param startRow
     * @param endRow
     * @param pPosition
     */
    public static void copyRows(Sheet sheet, int startRow, int endRow, int pPosition) {
        int pStartRow = startRow - 1;
        int pEndRow = endRow - 1;
        int targetRowFrom;
        int targetRowTo;
        int columnCount;
        CellRangeAddress region = null;
        if (pStartRow == -1 || pEndRow == -1) return;
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            region = sheet.getMergedRegion(i);
            if ((region.getFirstRow() >= pStartRow)
                    && (region.getLastRow() <= pEndRow)) {
                targetRowFrom = region.getFirstRow() - pStartRow + pPosition;
                targetRowTo = region.getLastRow() - pStartRow + pPosition;
                CellRangeAddress newRegion = region.copy();
                newRegion.setFirstRow(targetRowFrom);
                newRegion.setFirstColumn(region.getFirstColumn());
                newRegion.setLastRow(targetRowTo);
                newRegion.setLastColumn(region.getLastColumn());
                sheet.addMergedRegion(newRegion);
            }
        }
        for (int i = pStartRow; i <= pEndRow; i++) {
            Row sourceRow = sheet.getRow(i);
            columnCount = sourceRow.getLastCellNum();
            if (sourceRow != null) {
                Row newRow = sheet.createRow(pPosition - pStartRow + i);
                newRow.setHeight(sourceRow.getHeight());
                for (int j = 0; j < columnCount; j++) {
                    Cell templateCell = sourceRow.getCell(j);
                    if (templateCell != null) {
                        Cell newCell = newRow.createCell(j);
                        copyCell(templateCell, newCell, true);
                    }
                }
            }
        }
    }

    /**
     * 插入新行
     *
     * @param sheet
     * @param startRow
     * @param rows
     */
    public static void insertRow(Sheet sheet, int startRow, int rows) {
        sheet.shiftRows(startRow, sheet.getLastRowNum(), rows, true, false);
//        startRow = startRow-1;
//        for (int i = 0; i < rows; i++) {
//            Row sourceRow = null;//原始位置
//            Row targetRow = null;	//移动后位置
//            Cell sourceCell = null;
//            Cell targetCell = null;
//            startRow = startRow+1;
//            sourceRow = sheet.getRow(startRow);
//            if(sourceRow==null)sourceRow = sheet.createRow(startRow);
//            targetRow = sheet.createRow(startRow + 1);
//            sourceRow.setHeight(targetRow.getHeight());
//
//            for(int m=sourceRow.getFirstCellNum();m<sourceRow.getLastCellNum();m++){
//        	sourceCell = sourceRow.createCell(m);
//        	targetCell = targetRow.getCell(m);
//              sourceCell.setCellStyle(targetCell.getCellStyle());
//              sourceCell.setCellType(targetCell.getCellType());            	
//            }
//            for (int m = targetRow.getFirstCellNum(); m < targetRow.getPhysicalNumberOfCells(); m++) {
//                sourceCell = sourceRow.createCell(m);
//                targetCell = targetRow.getCell(m);
//                sourceCell.setCellStyle(targetCell.getCellStyle());
//                sourceCell.setCellType(targetCell.getCellType());
//            }
//            startRow++;
//        }
    }

    //    public byte[] creatExcel(List<String> csvHeaderList,List<List<JSONObject>> listData, String sheetName,OWHandler bp) throws IOException{
//    	ByteArrayOutputStream os = new ByteArrayOutputStream();
//		XSSFWorkbook wb = new XSSFWorkbook();
//		Sheet sheet = wb.createSheet(sheetName);
//		XSSFCellStyle headStyle = wb.createCellStyle();
//		XSSFFont font = wb.createFont();
//		font.setBold(true);
//		headStyle.setFont(font);
//		Row headerRow = sheet.createRow(0);
//		for(int p=0;p<csvHeaderList.size();p++){
//			Cell headerCell = headerRow.createCell(p);
//			headerCell.setCellStyle(headStyle);
//			headerCell.setCellValue(csvHeaderList.get(p));
//		}
//		Map<String,XSSFCellStyle> map = this.getStyleMap(wb);
//		if(bp != null){//有些样式需要在设值之前设置
//			bp.setStyleBeforeExportExcel(map);
//		}
//		XSSFCellStyle cellStyle = null;
//		for(int i=0;i<listData.size();i++){
//			List<JSONObject> jsonList = listData.get(i);
//			Row excelRow = sheet.createRow(i+1);
//			//XSSFDataFormat xdf = wb.createDataFormat();
//			for(int j=0;j<listData.get(i).size();j++){
//				JSONObject json = jsonList.get(j);
//				//String key = json.getString("key");
//				//XSSFDataFormat xdf = wb.createDataFormat();
//				//XSSFCellStyle cellStyle = wb.createCellStyle();
//				String dataType = json.getString("dataType");
//				String checkFormat = json.getString("checkFormat");
//				Object value = null;
//				Cell cell = excelRow.createCell(j);
//				if(dataType.equalsIgnoreCase("Number")){
//					String v = json.getString("value");
//					v = StringUtils.isBlank(v)?"0":v;
//					if(!StringUtils.isBlank(checkFormat)){
//						if("Currency".equalsIgnoreCase(checkFormat)||"Float2".equalsIgnoreCase(checkFormat)){
//							//cellStyle.setDataFormat(xdf.getFormat("###,###,###,###,###,##0.00"));
//							cellStyle = map.get("Currency");
//							cell.setCellValue(Double.parseDouble(v));
//						}else if("Float4".equalsIgnoreCase(checkFormat)){
//							//cellStyle.setDataFormat(xdf.getFormat("###,###,###,###,###,##0.0000"));
//							cellStyle = map.get("Float4");
//							cell.setCellValue(Double.parseDouble(v));
//						}else if("Float6".equalsIgnoreCase(checkFormat)){
//							//cellStyle.setDataFormat(xdf.getFormat("###,###,###,###,###,##0.000000"));
//							cellStyle = map.get("Float6");
//							cell.setCellValue(Double.parseDouble(v));
//						}else if("Float10".equalsIgnoreCase(checkFormat)){
//							//cellStyle.setDataFormat(xdf.getFormat("###,###,###,###,###,##0.0000000000"));
//							cellStyle = map.get("Float10");
//							cell.setCellValue(Double.parseDouble(v));
//						}else if("Integer".equalsIgnoreCase(checkFormat)){
//							//cellStyle.setDataFormat(xdf.getFormat("###,###,###,###,###,##0"));
//							cellStyle = map.get("Integer");
//							cell.setCellValue(Integer.parseInt(v));
//						}else {
//							//cellStyle.setDataFormat(xdf.getFormat("###,###,###,###,###,##0.00"));
//							cellStyle = map.get("Default");
//							cell.setCellValue(Double.parseDouble(v));
//						}
//					}else{
//						//cellStyle.setDataFormat(xdf.getFormat("###,###,###,###,###,##0.00"));
//						cellStyle = map.get("Default");
//						cell.setCellValue(Double.parseDouble(v));
//					}
//					cell.setCellStyle(cellStyle);
//					sheet.setDefaultColumnStyle(i, cellStyle);
//				}else{
//					value = json.getString("value");
//					value = value==null?"":value;
//					//cellStyle.setDataFormat(xdf.getFormat("@"));
//					if("DateTime".equalsIgnoreCase(checkFormat)){
//						cellStyle = map.get("DateTime");
//						cell.setCellValue(value.toString());
//					}else if("Date".equalsIgnoreCase(checkFormat)){
//						cellStyle = map.get("Date");
//						cell.setCellValue(value.toString());
//					}else{
//						cellStyle = map.get("String");
//						cell.setCellValue((String)value);
//						cell.setCellType(CellType.STRING);
//					}
//					cell.setCellStyle(cellStyle);
//				}
//		   }
//		}
//		if(bp!=null){
//			bp.afterExportExcel(wb);
//		}
//		wb.write(os);
//		byte[] bytes = os.toByteArray();
//		os.close();
//		wb.close();
//		return bytes;
//    }
    private Map<String, XSSFCellStyle> getStyleMap(XSSFWorkbook wb) {
        Map<String, XSSFCellStyle> map = new HashMap<String, XSSFCellStyle>();
        XSSFDataFormat xdf = wb.createDataFormat();
        XSSFCellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setDataFormat(xdf.getFormat("###,###,###,###,###,##0.00"));
        map.put("Currency", cellStyle1);
        map.put("Default", cellStyle1);
        XSSFCellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setDataFormat(xdf.getFormat("###,###,###,###,###,##0.0000"));
        map.put("Float4", cellStyle2);
        XSSFCellStyle cellStyle3 = wb.createCellStyle();
        cellStyle3.setDataFormat(xdf.getFormat("###,###,###,###,###,##0.000000"));
        map.put("Float6", cellStyle3);
        XSSFCellStyle cellStyle4 = wb.createCellStyle();
        cellStyle4.setDataFormat(xdf.getFormat("###,###,###,###,###,##0.0000000000"));
        map.put("Float10", cellStyle4);
        XSSFCellStyle cellStyle5 = wb.createCellStyle();
        cellStyle5.setDataFormat(xdf.getFormat("###,###,###,###,###,##0"));
        map.put("Integer", cellStyle5);
        XSSFCellStyle cellStyle6 = wb.createCellStyle();
        cellStyle6.setDataFormat(xdf.getFormat("@"));
        map.put("String", cellStyle6);
        XSSFCellStyle cellStyle7 = wb.createCellStyle();
        cellStyle7.setDataFormat(xdf.getFormat("yyyy/mm/dd"));
        map.put("Date", cellStyle7);
        XSSFCellStyle cellStyle8 = wb.createCellStyle();
        cellStyle8.setDataFormat(xdf.getFormat("yyyy/mm/dd HH:MM:DD"));
        map.put("DateTime", cellStyle8);
        return map;
    }

}
