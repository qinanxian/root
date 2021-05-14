package com.vekai.office.excel.imports.intercept;

/**
 * 数据校验异常
 * @author yangsong
 * @since 2014/04/03
 *
 */
public class ExcelValidateException extends InterceptException {

	private static final long serialVersionUID = -1362920107336802L;

	public ExcelValidateException() {
		super();
	}

	public ExcelValidateException(String message, Throwable e) {
		super(message, e);
	}

	public ExcelValidateException(String message) {
		super(message);
	}

	public ExcelValidateException(Throwable e) {
		super(e);
	}

}
