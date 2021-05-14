package com.vekai.office.excel.imports.config;

import com.vekai.office.excel.imports.intercept.DataProcessIntercept;
import com.vekai.office.excel.utils.ExcelAddressConvert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Excel数据导入配置数据
 * @author yangsong
 * @since 2014/04/02
 *
 */
public class ExcelImportConfig implements Serializable{

	private static final long serialVersionUID = 1981629926715132549L;
	/**
	 * 数据表格的导入模式
	 *
	 */
	public enum ImpModel {Grid,FreeForm};
	/**
	 * 异常处理方式
	 */
	public enum ExceptionProcess{Warn,Ignore,Break}
	/**
	 * 数据类型
	 *
	 */
	public enum DataType{String,Double,Integer,Long,Date,Boolean}
	/**
	 * 冲突数据解决模式
	 *
	 */
	public enum ConflictSolveModel{Insert,Update,Skip,Erase}

	private String header;
	private String id;
	private String table;
	private String keyColumn;
	private ImpModel impModel;
	private ConflictSolveModel conflictSolveModel;
	private String startAddr;
	private String endAddr;
	private String fixAddr;
	private int commitNumber;
	private ExceptionProcess exceptionProcess;
	private Map<String,ColumnItem> columnItemsMap = new LinkedHashMap<String,ColumnItem>();
	
	//配置的拦截器类，实例化后的对象，会存放到interceptList的最后一位
	private String intercept;
	//拦截器列表，配置对象上的所有拦截器，有可能通过外部类注入
	protected List<DataProcessIntercept> interceptList;
	//存放扩展属性，比如用于存放当前用户，当前机构，当前日期等
	private Map<String, Object> properties = null;
	
	protected ExcelImportConfig(){
		interceptList = new ArrayList<DataProcessIntercept>();
		properties = new HashMap<String, Object>();
	}
	/**
	 * 获取标题头信息
	 * @return
	 */
	public String getHeader() {
		return header;
	}
	/**
	 * 设置标题头信息
	 * @param header
	 */
	public void setHeader(String header) {
		this.header = header;
	}
	/**
	 * 获取ID
	 * @return
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置ID
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取目标表
	 * @return
	 */
	public String getTable() {
		return table;
	}
	/**
	 * 设置目标表
	 * @param table
	 */
	public void setTable(String table) {
		this.table = table;
	}
	/**
	 * 获取主键列
	 * @return
	 */
	public String getKeyColumn() {
		return keyColumn;
	}
	/**
	 * 设置主键列
	 * @param keyColumn
	 */
	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}
	/**
	 * 获取导入类型
	 * @return
	 */
	public ImpModel getImpModel() {
		return impModel;
	}
	/**
	 * 设置导入类型
	 * @param impModel
	 */
	public void setImpModel(ImpModel impModel) {
		this.impModel = impModel;
	}
	/**
	 * 获取存在数据冲突解决方式
	 * @return
	 */
	public ConflictSolveModel getConflictSolveModel() {
		return conflictSolveModel;
	}
	/**
	 * 设置获取存在数据冲突解决方式
	 * @param conflictSolveModel
	 */
	public void setConflictSolveModel(ConflictSolveModel conflictSolveModel) {
		this.conflictSolveModel = conflictSolveModel;
	}
	/**
	 * 获取开始地址
	 * @return
	 */
	public String getStartAddr() {
		return startAddr;
	}
	/**
	 * 设置开始地址
	 * @param startAddr
	 */
	public void setStartAddr(String startAddr) {
		this.startAddr = startAddr;
	}
	/**
	 * 获取结束地址
	 * @return
	 */
	public String getEndAddr() {
		return endAddr;
	}
	/**
	 * 设置结束地址
	 * @param endAddr
	 */
	public void setEndAddr(String endAddr) {
		this.endAddr = endAddr;
	}
	/**
	 * 获取固定地址
	 * @return
	 */
	public String getFixAddr() {
		return fixAddr;
	}
	/**
	 * 设置固定地址
	 * @param fixAddr
	 */
	public void setFixAddr(String fixAddr) {
		this.fixAddr = fixAddr;
	}
	/**
	 * 获取最大行数。如果为列表类型，则返回结束行号-开始行号的差值，如果是自由表格，返回1
	 * @return
	 */
	public int getMaxRow() {
		if(getImpModel()==ImpModel.Grid){
			int startRowIdx = ExcelAddressConvert.getRowIndex(startAddr);
			int endRowIdx = ExcelAddressConvert.getRowIndex(endAddr);
			return endRowIdx-startRowIdx+1;
		}else{
			return 1;
		}
	}
	/**
	 * 获取批量提交行数
	 * @return
	 */
	public int getCommitNumber() {
		return commitNumber;
	}
	/**
	 * 设置批量提交行数
	 * @param commitNumber
	 */
	public void setCommitNumber(int commitNumber) {
		this.commitNumber = commitNumber;
	}
	/**
	 * 获取异常处理方式
	 * @return
	 */
	public ExceptionProcess getExceptionProcess() {
		return exceptionProcess;
	}
	/**
	 * 设置异常处理方式
	 * @param exceptionProcess
	 */
	public void setExceptionProcess(ExceptionProcess exceptionProcess) {
		this.exceptionProcess = exceptionProcess;
	}
	/**
	 * 获取拦截器
	 * @return
	 */
	public String getIntercept() {
		return intercept;
	}
	/**
	 * 设置拦截器
	 * @param intercept
	 */
	public void setIntercept(String intercept) {
		this.intercept = intercept;
	}
	/**
	 * 创建对照项
	 * @return
	 */
	public ColumnItem createColumnItem(){
		return new ColumnItem();
	}
	/**
	 * 获取一个列对照项
	 * @param name
	 * @return
	 */
	public ColumnItem getColumnItem(String name){
		return columnItemsMap.get(name.toUpperCase());
	}
	/**
	 * 添加列对照项
	 * @param columnItem
	 */
	public void addColumnItem(ColumnItem columnItem){
		columnItemsMap.put(columnItem.getName().toUpperCase(), columnItem);
	}
	/**
	 * 移除一个列对照项
	 * @param columnItem
	 */
	public void removeColumnItem(ColumnItem columnItem){
		columnItemsMap.remove(columnItem.getName().toUpperCase());
	}
	/**
	 * 获取所有的列对照项
	 * @return
	 */
	public List<ColumnItem> getAllColumnItems(){
		List<ColumnItem> items = new ArrayList<ColumnItem>();
		items.addAll(columnItemsMap.values());
		return items;
	}
	
	/**
	 * 获取所有的拦截器列表
	 * @return
	 */
	public List<DataProcessIntercept> getIntercepts(){
		return interceptList;
	}	
	
	/**
	 * 设置属性值
	 * @param name
	 * @param value
	 */
	public void setProperty(String name,Object value){
		properties.put(name, value);
	}
	
	/**
	 * 获取属性值
	 * @param name
	 * @return
	 */
	public Object getProperty(String name){
		return properties.get(name);
	}
	
	/**
	 * 列对照项
	 */
	public class ColumnItem implements Serializable{
		private static final long serialVersionUID = -2272198474497922910L;
		
		private int rowNo;
		private String name;
		private DataType dataType;
		private String comment;
		private String fetchValue;
		private boolean required;
		private String formatValidate;
		private String valueConvert;
		private String specialProcess;
		private String remark;
		
		private ColumnItem(){
		}

		/**
		 * 获取行号
		 * @return
		 */
		public int getRowNo() {
			return rowNo;
		}
		/**
		 * 设置行号
		 * @param rowNo
		 */
		public void setRowNo(int rowNo) {
			this.rowNo = rowNo;
		}
		/**
		 * 获取名称
		 * @return
		 */
		public String getName() {
			return name;
		}
		/**
		 * 设置名称
		 * @param name
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * 获取数据类型类型
		 * @return
		 */
		public DataType getDataType() {
			return dataType;
		}
		/**
		 * 设置数据类型
		 * @param dataType
		 */
		public void setDataType(DataType dataType) {
			this.dataType = dataType;
		}
		/**
		 * 获取列说明
		 * @return
		 */
		public String getComment() {
			return comment;
		}
		/**
		 * 设置列说明
		 * @param comment
		 */
		public void setComment(String comment) {
			this.comment = comment;
		}
		/**
		 * 获取取值方式
		 * @return
		 */
		public String getFetchValue() {
			return fetchValue;
		}
		/**
		 * 设置取值方式
		 * @param fetchValue
		 */
		public void setFetchValue(String fetchValue) {
			this.fetchValue = fetchValue;
		}
		/**
		 * 是否必需
		 * @return
		 */
		public boolean isRequired() {
			return required;
		}
		/**
		 * 设置是否必需
		 * @param required
		 */
		public void setRequired(boolean required) {
			this.required = required;
		}
		/**
		 * 获取格式校验
		 * @return
		 */
		public String getFormatValidate() {
			return formatValidate;
		}
		/**
		 * 设置格式校验
		 * @param formatValidate
		 */
		public void setFormatValidate(String formatValidate) {
			this.formatValidate = formatValidate;
		}
		/**
		 * 获取值转换
		 * @return
		 */
		public String getValueConvert() {
			return valueConvert;
		}
		/**
		 * 设置值转换
		 * @param valueConvert
		 */
		public void setValueConvert(String valueConvert) {
			this.valueConvert = valueConvert;
		}
		/**
		 * 获取特殊处理
		 * @return
		 */
		public String getSpecialProcess() {
			return specialProcess;
		}
		/**
		 * 设置特殊处理
		 * @param specialProcess
		 */
		public void setSpecialProcess(String specialProcess) {
			this.specialProcess = specialProcess;
		}
		/**
		 * 获取备注
		 * @return
		 */
		public String getRemark() {
			return remark;
		}
		/**
		 * 设置备注
		 * @param remark
		 */
		public void setRemark(String remark) {
			this.remark = remark;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("ColumnItem [comment=").append(comment).append(
					", dataType=").append(dataType).append(", fetchValue=")
					.append(fetchValue).append(", formatValidate=").append(
							formatValidate).append(", name=").append(name)
					.append(", remark=").append(remark).append(", required=")
					.append(required).append(", rowNo=").append(rowNo).append(
							", specialProcess=").append(specialProcess).append(
							", valueConvert=").append(valueConvert).append("]");
			return builder.toString();
		}
		
	}
}
