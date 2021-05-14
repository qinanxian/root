//package com.vekai.office.excel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//
//import com.amarsoft.are.lang.DataElement;
//import com.amarsoft.rax.office.excel.utils.ExcelCellHelper;
//import com.amarsoft.rax.office.excel.utils.ExcelUtils;
//import com.amarsoft.rax.office.word.parameter.EmbedTable;
//import com.amarsoft.rax.office.word.parameter.EmbedTableCell;
//import com.amarsoft.rax.office.word.parameter.EmbedTableRow;
//import com.amarsoft.rax.office.word.parameter.NumberParameter;
//import com.amarsoft.rax.office.word.parameter.StringParameter;
//import com.amarsoft.rax.office.word.parameter.TableParameter;
//import com.amarsoft.rax.office.word.parameter.WordParameter;
//import com.amarsoft.rax.office.word.parameter.WordParameterSet;
//
//public class ExcelReplace {
//	private Sheet sheet = null;
//
//	public ExcelReplace(Sheet sheet){
//		this.sheet = sheet;
//	}
//
//	public void replace(WordParameterSet parameter){
//		List<String> keyNames = parameter.getAllNames();
//		for (int j=0;j<keyNames.size();j++) {
//			String key = keyNames.get(j);
//			WordParameter<?> value = parameter.getParameter(key);
//			List<Cell> cellList = lookupCell(key);
//			execReplace(value,cellList.toArray(new Cell[cellList.size()]));
//		}
//	}
//
//	protected void execReplace(WordParameter<?> value,Cell... cell){
//		//字串或数字
//		if ((value instanceof StringParameter)||(value instanceof NumberParameter)
//				) {
//			for(Cell c:cell){
//				c.setCellValue(value.getStringValue());
//			}
//		}else if(value instanceof TableParameter){
//			for(Cell c:cell){
//				fillLoopTable(c,(TableParameter)value);
//			}
//		}
//	}
//
//	private void fillLoopTable(Cell startCell,TableParameter tableObject){
//		Row baseRow = startCell.getRow();
//		Sheet sheet = startCell.getSheet();
//		EmbedTable table = tableObject.getValue();
//		int startRowIndex = startCell.getRowIndex();
//		int startCellIndex = startCell.getColumnIndex();
////     sheet.shiftRows(3, sheet.getLastRowNum(), 2, true, false);
//		for(int i=0;i<table.getRows().size();i++){
//			EmbedTableRow  tableRow = table.getRows().get(i);
//			Row row = null;
//			if(i==0){
//				row = baseRow;
//			}else{
//				row = sheet.createRow(i+startRowIndex);
//			}
//			for(int j=0;j<tableRow.getCells().size();j++){
//				EmbedTableCell tableCell = tableRow.getCells().get(j);
//				Cell cell = row.getCell(j+startCellIndex);
//				if(cell==null)cell = row.createCell(j+startCellIndex);
//				cell.setCellValue(tableCell.getText());
//			}
//			if(i!=0)ExcelUtils.copyRowFormat(baseRow, row);
//		}
//	}
//
//	/**
//	 * 根据EXCEL单元格中的值，查找EXCEL单元格
//	 * @param cellValue
//	 * @return
//	 */
//	public List<Cell> lookupCell(String cellValue){
//		List<Cell> cellList = new ArrayList<Cell>();
//		for(int i=0;i<sheet.getLastRowNum();i++){
//			Row row = sheet.getRow(i);
//			if(row==null)continue;
//			for(int j=0;j<row.getLastCellNum();j++){
//				Cell cell = row.getCell(j);
//				if(cell==null)continue;
//				DataElement element = ExcelCellHelper.getCellValueAsDataElement(cell);
//				if(cellValue.equals(element.getString())){
//					cellList.add(cell);
//				}
//			}
//		}
//		return cellList;
//	}
//	/**
//	 * 根据EXCEL单元格中的值，查找EXCEL单元格
//	 * @param cellValue
//	 * @return
//	 */
//	public Cell lookupSingleCell(String cellValue){
//		for(int i=0;i<sheet.getLastRowNum();i++){
//			Row row = sheet.getRow(i);
//			for(int j=0;j<row.getLastCellNum();j++){
//				Cell cell = row.getCell(j);
//				DataElement element = ExcelCellHelper.getCellValueAsDataElement(cell);
//				if(cellValue.equals(element.getString())){
//					return cell;
//				}
//			}
//		}
//		return null;
//	}
//}
