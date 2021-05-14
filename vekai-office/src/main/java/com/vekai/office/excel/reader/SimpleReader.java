package com.vekai.office.excel.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vekai.office.excel.utils.ExcelAddressConvert;
import com.vekai.office.excel.utils.ExcelCellHelper;
import cn.fisok.raw.kit.IOKit;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * 简单的数据读取
 * @author cytsir
 *
 */
public class SimpleReader {
	private InputStream fis = null;

	public SimpleReader(InputStream fis) {
		this.fis = fis;
	}
	
	public SheetData read(int sheetIndex){
		SheetData  sheetData = new SheetData();
		
		Workbook workBook=null;
		InputStream newIs = null;
		try {
			newIs = IOKit.convertToByteArrayInputStream(fis);
			try{
				newIs.reset();
				workBook=new HSSFWorkbook(newIs);
			}catch(OfficeXmlFileException e){
				newIs.reset();
				workBook=new XSSFWorkbook(OPCPackage.open(newIs));
			}
		} catch (IOException e) {
			throw new RuntimeException("读取异常",e);
		}catch (InvalidFormatException e){
			throw new RuntimeException("读取异常",e);
		}finally{
			try {
					if(newIs!=null) newIs.close();
				} catch (IOException e) {
					throw new RuntimeException("关闭异常",e);
				}
		}
		
		Sheet sheet = workBook.getSheetAt(sheetIndex);
		readData(sheet,sheetData);
		
		return sheetData;
	}
	
	private void readData(Sheet sheet,SheetData  sheetData){
		final int startRowIdx = ExcelAddressConvert.getRowIndex("A1");
		final int startColIdx = ExcelAddressConvert.getColumnIndex("A1");
		final int endRowIdx = ExcelAddressConvert.getRowIndex("H100");
		final int endColIdx = ExcelAddressConvert.getColumnIndex("H100");
		
		int realEndRowIdx = Math.min(endRowIdx, sheet.getLastRowNum());
		
		for(int i=startRowIdx;i<=realEndRowIdx;i++){
			Row row = sheet.getRow(i);
			Map<String,Object> rowData = new LinkedHashMap<String,Object>();
			int realEndColIdx = Math.min(endColIdx, row.getLastCellNum());
			for(int j=startColIdx;j<= realEndColIdx;j++){
				String rowName = ExcelAddressConvert.getRowName(i);
				String colName = ExcelAddressConvert.getColumnName(j);
				String addr = colName+rowName;
				Cell cell = row.getCell(j);
				Object cellValue = ExcelCellHelper.getCellValue(cell);
				sheetData.mapData.put(addr, cellValue);
				rowData.put(addr, cellValue);
			}
			sheetData.listData.add(rowData);
		}
	}
	
	
	public class SheetData{
		private Map<String,Object> mapData = new LinkedHashMap<String,Object>();
		private List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
		
		public List<Map<String,Object>> getListData(){
			return listData;
		}
		public Map<String,Object> getMapData(){
			return mapData;
		}
	}
}
