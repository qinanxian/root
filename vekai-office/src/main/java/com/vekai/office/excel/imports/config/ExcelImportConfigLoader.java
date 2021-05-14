package com.vekai.office.excel.imports.config;

import java.io.InputStream;

/**
 * 配置数据加载器
 * @author yangsong
 * @since 2014/04/02
 *
 */
public interface ExcelImportConfigLoader {

	public void load(InputStream is) throws ConfigException;
	public void load(String... args) throws ConfigException;
	public ExcelImportConfig getExcelImportConfig(String name);
}
