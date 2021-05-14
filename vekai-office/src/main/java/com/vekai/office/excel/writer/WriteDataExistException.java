package com.vekai.office.excel.writer;


/**
 * 数据已存在异常
 * @author yangsong
 * @since 2014/04/02
 *
 */
public class WriteDataExistException extends WriterException {
	private static final long serialVersionUID = -7755740030056430013L;

	public WriteDataExistException() {
		super();
	}

	public WriteDataExistException(String message, Throwable e) {
		super(message, e);
	}

	public WriteDataExistException(String message) {
		super(message);
	}

	public WriteDataExistException(Throwable e) {
		super(e);
	}

}
