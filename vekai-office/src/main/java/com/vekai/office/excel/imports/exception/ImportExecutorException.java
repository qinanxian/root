package com.vekai.office.excel.imports.exception;


/**
 * 定义运行容器异常
 * @author yangsong
 * @since 2014/04/03
 *
 */
public class ImportExecutorException extends ExcelImportException {
	private static final long serialVersionUID = -871562514408874464L;

	public ImportExecutorException() {
		super();
	}

	public ImportExecutorException(String message, Throwable e) {
		super(message, e);
	}

	public ImportExecutorException(String message) {
		super(message);
	}

	public ImportExecutorException(Throwable e) {
		super(e);
	}

}
