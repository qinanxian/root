package com.vekai.office.excel.imports.exception;

/**
 * 定义导入Excel异常基础类
 * @author yangsong
 * @since 2014/04/02
 *
 */
public class ExcelImportException extends Exception {

	private static final long serialVersionUID = -6346559431520139811L;

	public ExcelImportException() {
	}

	public ExcelImportException(String message) {
		super(message);
	}

	public ExcelImportException(Throwable e) {
		super(e);
	}

	public ExcelImportException(String message, Throwable e) {
		super(message, e);
	}

}
