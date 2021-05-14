package com.vekai.office.excel.writer;


import com.vekai.office.excel.imports.exception.ExcelImportException;

/**
 * Excel数据写入异常定义
 * @author yangsong
 * @since 2014/04/02
 *
 */
public class WriterException extends ExcelImportException {
	private static final long serialVersionUID = -7755740030056430013L;

	public WriterException() {
		super();
	}

	public WriterException(String message, Throwable e) {
		super(message, e);
	}

	public WriterException(String message) {
		super(message);
	}

	public WriterException(Throwable e) {
		super(e);
	}

}
