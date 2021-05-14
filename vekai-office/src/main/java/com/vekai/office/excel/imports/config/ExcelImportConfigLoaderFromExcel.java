package com.vekai.office.excel.imports.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.vekai.office.excel.utils.ExcelUtils;
import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;





/**
 * 从Excel配置文件中加载配置数据
 * @author yangsong
 * @since 2014/04/02
 *
 */
public class ExcelImportConfigLoaderFromExcel implements ExcelImportConfigLoader{
	public static final int CATALOG_SHEET_INDEX = 0;
	public static final int CATALOG_SHEET_START_ROW = 3;
	private Map<String,ExcelImportConfig> configMap = new HashMap<String, ExcelImportConfig>();
	
	public void load(InputStream is) throws ConfigException {
		ValidateKit.notNull(is);
		
	    configMap.clear();
	    Workbook workBook = null;
		try {
		    workBook = ExcelUtils.openWorkbook(is);
		} catch (IOException e) {
			 throw new ConfigException("读取流IO异常",e);
		}
		loadAllSheet(workBook);
	}
	public void load(String... args)
			throws ConfigException {
		if(args==null||args.length<1)throw new NullPointerException("参数args为空");
		String fullFileName = args[0];
		InputStream fis = null;
		configMap.clear();
		try {
    		if(fullFileName.startsWith("classpath:")){
    		    String path = fullFileName.substring(10);
    		    fis = this.getClass().getClassLoader().getResourceAsStream(path);
    		}else{
    		    File file= new File(fullFileName);
    		    fis = new FileInputStream(file);
    		}
			load(fis);
		} catch (FileNotFoundException e) {
			throw new ConfigException(MessageFormat.format("文件[{0}]不存在", fullFileName));
		} catch (ConfigException e){
			throw new ConfigException(MessageFormat.format("文件[{0}]配置错误", fullFileName),e);
		}finally{
		    IOKit.close(fis);
		}
	}
	
	
	protected void loadAllSheet(Workbook workBook) throws ConfigException{
		Sheet sheet = workBook.getSheetAt(CATALOG_SHEET_INDEX);
		for(int i=CATALOG_SHEET_START_ROW;i<=sheet.getLastRowNum();i++){
			Row row = sheet.getRow(i);
			Cell nameCell = row.getCell(2);
			Cell statusCell = row.getCell(4);
			if(nameCell==null||statusCell==null)continue;
			
			String status = StringKit.trim(statusCell.getStringCellValue());
			if("已完成".equals(status)){
				String catalogId = getCellStringValue(nameCell,"目录名称",4);
				String sheetName = catalogId;
				Hyperlink hyperlink = nameCell.getHyperlink();
				if(hyperlink != null) sheetName = hyperlink.getLabel();
				//如果目标sheet名称为空，则忽略，下一个
				if(sheetName==null||sheetName.length()==0)continue;
				//如果有超连接，则从连接上取连接到的目标sheet
				loadSheetItem(workBook,sheetName,catalogId);
			}
		}
	}
	
	/**
	 * 加载配置项了
	 * @param workBook
	 */
	public void loadSheetItem(Workbook workBook,String sheetName,String catalogId) throws ConfigException{
		int sheetIndex = workBook.getSheetIndex(sheetName);
		if(sheetIndex >=0 ){
		    Sheet sheet = workBook.getSheetAt(sheetIndex);
			ExcelImportConfig configItem = new ExcelImportConfig();
			
			//至少保证不少于7行，才有能列对照项的存在
			if(sheet.getLastRowNum()>=6){
				fillConfigItem(configItem,sheet);
			}else{
				throw new ConfigException(MessageFormat.format("配置数据不得少于7行，请检查配置", sheetName));
			}
			configMap.put(catalogId, configItem);
		}else{
			throw new ConfigException(MessageFormat.format("找不到名称为[{0}]的sheet页存在", sheetName));
		}
	}
	
	/**
	 * 一个sheet页为一个配置项
	 * @param configItem
	 * @param sheet
	 */
	private void fillConfigItem(ExcelImportConfig configItem,Sheet sheet) throws ConfigException{
		String sheetName = sheet.getSheetName()+"!";
		//标题头
		String header    =  getCellStringValue(sheet.getRow(0).getCell(1),sheetName+"标题头",1);
		//目标表设置
		String table     =  getCellStringValue(sheet.getRow(1).getCell(1),sheetName+"目标表",1);
		String keyColumn =  getCellStringValue(sheet.getRow(1).getCell(3),sheetName+"逻辑主键",1);
		String impModel  =  getCellStringValue(sheet.getRow(1).getCell(5),sheetName+"导入类型",1);
		String onflict   =  getCellStringValue(sheet.getRow(1).getCell(8),sheetName+"数据冲突解决方式",1);
		//地址设置
		String startAddr =  getCellStringValue(sheet.getRow(2).getCell(1),sheetName+"开始地址",1);
		String endAddr   =  getCellStringValue(sheet.getRow(2).getCell(3),sheetName+"结束地址",1);
		String fixAddr   =  getCellStringValue(sheet.getRow(2).getCell(5),sheetName+"固定地址",1);
		//范围及处理方式
		int commitNumber =  getCellIntegerValue(sheet.getRow(3).getCell(1),sheetName+"批量提交");
		String excepModel=   getCellStringValue(sheet.getRow(3).getCell(3),sheetName+"非法数据",1);
		
		//拦截器
		String intercept =  getCellStringValue(sheet.getRow(4).getCell(1),sheetName+"辅助类",4);
		
		//根据配置值，初始化模型
		configItem.setId(sheet.getSheetName());
		configItem.setHeader(header);
		configItem.setTable(table);
		configItem.setKeyColumn(keyColumn);
		if("数据列表".endsWith(impModel)) configItem.setImpModel(ExcelImportConfig.ImpModel.Grid);
		else if("自由表格".endsWith(impModel)) configItem.setImpModel(ExcelImportConfig.ImpModel.Grid);
		else throw new ConfigException("配置项[导入类型]值错误，该项值只能包括[数据列表,自由表格]中的一项");
		configItem.setStartAddr(startAddr);
		configItem.setEndAddr(endAddr);
		configItem.setFixAddr(fixAddr);
//		configItem.setMaxRow(maxRow);
		configItem.setCommitNumber(commitNumber);
		if("警告".equals(excepModel))configItem.setExceptionProcess(ExcelImportConfig.ExceptionProcess.Warn);
		else if("忽略".equals(excepModel))configItem.setExceptionProcess(ExcelImportConfig.ExceptionProcess.Ignore);
		else if("中断".equals(excepModel))configItem.setExceptionProcess(ExcelImportConfig.ExceptionProcess.Break);
		else throw new ConfigException("配置项[非法数据]值错误，该项值只能包括[警告,忽略,中断]中的一项");

		if("插入".equals(onflict))configItem.setConflictSolveModel(ExcelImportConfig.ConflictSolveModel.Insert);
		else if("更新".equals(onflict))configItem.setConflictSolveModel(ExcelImportConfig.ConflictSolveModel.Update);
		else if("跳过".equals(onflict))configItem.setConflictSolveModel(ExcelImportConfig.ConflictSolveModel.Skip);
		else if("全部重写".equals(onflict))configItem.setConflictSolveModel(ExcelImportConfig.ConflictSolveModel.Erase);
		else throw new ConfigException("配置项[数据冲突解决方式]值错误，该项值只能包括[插入,更新,全部重写]中的一项");
		
		configItem.setIntercept(intercept);
		
		fillConfigColumnItems(configItem,sheet);
	}
	
	/**
	 * 填充对照项
	 */
	private void fillConfigColumnItems(ExcelImportConfig configItem,Sheet sheet) throws ConfigException{
		String sheetName = sheet.getSheetName()+"!";
		for(int i=6;i<=sheet.getLastRowNum();i++){
			String rowText = sheetName+"第"+(i+1)+"行，";
			Row row = sheet.getRow(i);
			
			ExcelImportConfig.ColumnItem item = configItem.createColumnItem();
			
			int    rowNo          = getCellIntegerValue(row.getCell(0),rowText+"编号");
			String name           = getCellStringValue(row.getCell(1),rowText+"字段名",4);
			String dataType       = getCellStringValue(row.getCell(2),rowText+"数据类型",4);
			String comment        = getCellStringValue(row.getCell(3),rowText+"字段说明",4);
			String fetchValue     = getCellStringValue(row.getCell(4),rowText+"取值",4);
			String required       = getCellStringValue(row.getCell(5),rowText+"必需",4);
			String formatValidate = getCellStringValue(row.getCell(6),rowText+"格式校验",4);
			String valueConvert   = getCellStringValue(row.getCell(7),rowText+"值转换",4);
			String specialProcess = getCellStringValue(row.getCell(8),rowText+"特殊处理",4);
			String remark         = String.valueOf(getCellValue(row.getCell(9)));
			
			if(name==null||name.length()==0)continue;
			
			item.setRowNo(rowNo);
			item.setName(name);
			
			if("String".equals(dataType))item.setDataType(ExcelImportConfig.DataType.String);
			else if("Double".equals(dataType))item.setDataType(ExcelImportConfig.DataType.Double);
			else if("Date".equals(dataType))item.setDataType(ExcelImportConfig.DataType.Date);
			else if("Integer".equals(dataType))item.setDataType(ExcelImportConfig.DataType.Integer);
			else if("Long".equals(dataType))item.setDataType(ExcelImportConfig.DataType.Long);
			else if("Boolean".equals(dataType))item.setDataType(ExcelImportConfig.DataType.Boolean);
			else throw new ConfigException(MessageFormat.format("配置项[{0}]值错误，该项值只能包括[String,Double,Date]中的一项",rowText+"[数据类型]字段"));

			item.setComment(comment);
			item.setFetchValue(fetchValue);
			
			if("是".equals(required))item.setRequired(true);
			else if("否".equals(required))item.setRequired(false);
			else throw new ConfigException(MessageFormat.format("配置项[{0}]值错误，该项值只能包括[是,否]中的一项",rowText+"[必需]字段"));

			item.setFormatValidate(formatValidate);
			item.setValueConvert(valueConvert);
			item.setSpecialProcess(specialProcess);
			item.setRemark(remark);
			
			configItem.addColumnItem(item);
		}
	}
	
	/**
	 * 把字串转为数字，转换不成功，抛出相关域的异常
	 * @param s 字串值
	 * @param name 域名称
	 * @return
	 * @throws ConfigException
	 */
	protected int parseInt(String s,String name) throws ConfigException{
		Integer num = 0;
		try{
			num = Integer.parseInt(s);
		}catch(NumberFormatException e){
			throw new ConfigException(MessageFormat.format("配置项[{0}]值应该为整数型",name));
		}
		return num;
	}
	
	private double getCellDoubleValue(Cell cell,String name) throws ConfigException{
		double value = 0;
		try{
			value = cell.getNumericCellValue();
		}catch(IllegalStateException e){
			throw new ConfigException(MessageFormat.format("配置项[{0}]值应该为数字类型",name));
		}catch(Exception e2){
			System.out.println("name:"+name);
			throw new ConfigException(MessageFormat.format("空",name));
		}
		return value;
	}
	private int getCellIntegerValue(Cell cell,String name) throws ConfigException{
		return (int)getCellDoubleValue(cell,name);
	}
	/**
	 * 获取单元格的字串值
	 * @param cell 单元格
	 * @param name 单元格中文名称
	 * @param spaceProcessType 空格处理方式 <br/>
	 * 0 不处理<br/>
	 * 1 删除所有空格<br/>
	 * 2 删除左部空格<br/>
	 * 3 删除右部空格<br/>
	 * 4 删除两端空格<br/>
	 * @return
	 * @throws ConfigException
	 */
	private String getCellStringValue(Cell cell,String name,int spaceProcessType) throws ConfigException{
		String value = null;
		try{
		    if(cell == null)return value;
			value = cell.getStringCellValue();
		}catch(IllegalStateException e){
			throw new ConfigException(MessageFormat.format("配置项[{0}]值应该为字串类型",name),e);
		}catch(Exception e){
		    throw new ConfigException(MessageFormat.format("配置项[{0}]取值错误",name),e);
		}
		if(spaceProcessType==1)value = StringKit.clearSpace(value);
		else if(spaceProcessType==2)value = StringKit.ltrim(value);
		else if(spaceProcessType==3)value = StringKit.rtrim(value);
		else if(spaceProcessType==4)value = StringKit.trim(value);
		return value;		
	}
	
	/**
	 * 根据Cell的类型，取值
	 * @param cell
	 * @return
	 */
	@SuppressWarnings("deprecation")
    public Object getCellValue(Cell cell) {
		if(cell==null)return null;
		int type=cell.getCellType();
		Object value=null;
		switch(type){
		case Cell.CELL_TYPE_STRING:
			value=cell.getStringCellValue();break;
		case Cell.CELL_TYPE_NUMERIC:
			value=cell.getNumericCellValue();break;
		case Cell.CELL_TYPE_FORMULA:
			value=cell.getStringCellValue();break;
		case Cell.CELL_TYPE_ERROR:
			value=cell.getErrorCellValue();break;
		case Cell.CELL_TYPE_BOOLEAN:
			value=cell.getBooleanCellValue();break;
		case Cell.CELL_TYPE_BLANK:
			value=null;break;
		default:
			value=null;break;
		}
		return value;
	}	
	
	public ExcelImportConfig getExcelImportConfig(String name) {
		return configMap.get(name);
	}


}
