package com.vekai.office.excel.imports.intercept;


import com.vekai.office.excel.imports.exception.ExcelImportException;

public class InterceptException extends ExcelImportException {

	private static final long serialVersionUID = 311865118206838152L;

	public InterceptException() {
		super();
	}

	public InterceptException(String message, Throwable e) {
		super(message, e);
	}

	public InterceptException(String message) {
		super(message);
	}

	public InterceptException(Throwable e) {
		super(e);
	}

}
