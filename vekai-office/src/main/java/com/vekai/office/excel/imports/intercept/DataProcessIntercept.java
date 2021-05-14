package com.vekai.office.excel.imports.intercept;

import com.vekai.office.excel.reader.ExcelRowData;
import com.vekai.office.excel.imports.config.ExcelImportConfig;

import java.util.List;


/**
 * Excel数据处理拦截器
 * @author yangsong
 * @since 2014/04/03
 *
 */
public interface DataProcessIntercept {
	/**
	 * 当读取到一行数据之前，触发调用此方法
	 * @param rowData 当前记录（行）
	 * @param config 配置对象
	 * @throws InterceptException
	 */
	public void beforeReadRow(ExcelRowData rowData, ExcelImportConfig config) throws InterceptException;

	/**
	 * 当读取完一行数据时，触发调用此方法
	 * @param rowData 当前记录（行）
	 * @param config 配置对象
	 * @throws InterceptException
	 */
	public void afterReadRow(ExcelRowData rowData, ExcelImportConfig config) throws InterceptException;
	
	/**
	 * 当所有数据被读取完毕时，触发调用此方法
	 * @param data 所有记录
	 * @param config 配置对象
	 * @throws InterceptException
	 */
	public void readComplete(List<ExcelRowData> data, ExcelImportConfig config) throws InterceptException;

	/**
	 * 当写入一行数据之前，触发调用此方法
	 * @param rowData 当前记录（行）
	 * @param config 配置对象
	 * @throws InterceptException
	 */
	public void beforeWriteRow(ExcelRowData rowData, ExcelImportConfig config) throws InterceptException;

	/**
	 * 当写入一行数据之后，触发调用此方法
	 * @param rowData 当前记录（行）
	 * @param config 配置对象
	 * @throws InterceptException
	 */
	public void afterWriteRow(ExcelRowData rowData, ExcelImportConfig config) throws InterceptException;

	/**
	 * 当所有数据写入完毕时，调用此方法
	 * @param data 所有记录
	 * @param config 配置对象
	 * @throws InterceptException
	 */
	public void writeComplete(List<ExcelRowData> data, ExcelImportConfig config) throws InterceptException;

}
