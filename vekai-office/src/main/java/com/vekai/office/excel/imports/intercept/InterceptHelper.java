package com.vekai.office.excel.imports.intercept;

import com.vekai.office.excel.reader.ExcelRowData;
import com.vekai.office.excel.imports.config.ExcelImportConfig;

import java.util.List;


/**
 * 拦截器协助处理类
 * 
 * @author yangsong
 * @since 2014/04/03
 * 
 */
public class InterceptHelper {
	/**
	 * 调用拦截器列表中的所有beforeReadRow方法
	 * @param interceptList 拦截器列表
	 * @param rowData 行数据
	 * @param config 配置对象
	 * @throws InterceptException
	 */
	public static void beforeReadRow(List<DataProcessIntercept> interceptList,
                                     ExcelRowData rowData, ExcelImportConfig config)
			throws InterceptException {
		if(interceptList==null||interceptList.size()==0)return;
		for(int i=0;i<interceptList.size();i++){
			interceptList.get(i).beforeReadRow(rowData, config);
		}
	}
	/**
	 * 调用拦截器列表中的所有afterReadRow方法
	 * @param interceptList 拦截器列表
	 * @param rowData 行数据
	 * @param config 配置对象
	 * @throws InterceptException
	 */
	public static void afterReadRow(List<DataProcessIntercept> interceptList,
			ExcelRowData rowData, ExcelImportConfig config)
	throws InterceptException {
		if(interceptList==null||interceptList.size()==0)return;
		for(int i=0;i<interceptList.size();i++){
			interceptList.get(i).afterReadRow(rowData, config);
		}
	}
	
	/**
	 * 调用拦截器列表中的所有readComplete方法
	 * @param interceptList 拦截器列表
	 * @param data 所有数据对象
	 * @param config 配置对象
	 * @throws InterceptException
	 */
	public static void readComplete(List<DataProcessIntercept> interceptList,
			List<ExcelRowData> data,ExcelImportConfig config)
	throws InterceptException {
		if(interceptList==null||interceptList.size()==0)return;
		for(int i=0;i<interceptList.size();i++){
			interceptList.get(i).readComplete(data, config);
		}
	}
	
	/**
	 * 调用拦截器列表中的所有beforeWriteRow方法
	 * @param interceptList 拦截器列表
	 * @param rowData 行数据
	 * @param config 配置对象
	 * @throws InterceptException
	 */
	public static void beforeWriteRow(List<DataProcessIntercept> interceptList,
			ExcelRowData rowData, ExcelImportConfig config)
	throws InterceptException {
		if(interceptList==null||interceptList.size()==0)return;
		for(int i=0;i<interceptList.size();i++){
			interceptList.get(i).beforeWriteRow(rowData, config);
		}
	}	
	/**
	 * 调用拦截器列表中的所有afterWriteRow方法
	 * @param interceptList 拦截器列表
	 * @param rowData 行数据
	 * @param config 配置对象
	 * @throws InterceptException
	 */
	public static void afterWriteRow(List<DataProcessIntercept> interceptList,
			ExcelRowData rowData, ExcelImportConfig config)
	throws InterceptException {
		if(interceptList==null||interceptList.size()==0)return;
		for(int i=0;i<interceptList.size();i++){
			interceptList.get(i).afterWriteRow(rowData, config);
		}
	}	
	/**
	 * 调用拦截器列表中的所有writeComplete方法
	 * @param interceptList 拦截器列表
	 * @param data 所有数据对象
	 * @param config 配置对象
	 * @throws InterceptException
	 */
	public static void writeComplete(List<DataProcessIntercept> interceptList,
			List<ExcelRowData> data, ExcelImportConfig config)
	throws InterceptException {
		if(interceptList==null||interceptList.size()==0)return;
		for(int i=0;i<interceptList.size();i++){
			interceptList.get(i).writeComplete(data, config);
		}
	}	
}
