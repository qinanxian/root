package com.vekai.office.excel.reader;

import com.vekai.office.excel.imports.exception.ExcelImportException;

/**
 * Excel读取异常
 * @author yangsong
 * @since 2014/04/02
 *
 */
public class ReaderException extends ExcelImportException {

	private static final long serialVersionUID = -3424801566873750048L;

	public ReaderException() {
		super();
	}

	public ReaderException(String message, Throwable e) {
		super(message, e);
	}

	public ReaderException(String message) {
		super(message);
	}

	public ReaderException(Throwable e) {
		super(e);
	}

}
