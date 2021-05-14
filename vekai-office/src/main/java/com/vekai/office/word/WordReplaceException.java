package com.vekai.office.word;

/**
 * 定义Word替换异常类
 *
 * @author yangsong
 * @since 2014年7月30日
 */
public class WordReplaceException extends Exception{
	private static final long serialVersionUID = -3437997345312132740L;

	public WordReplaceException() {
		super();
	}

	public WordReplaceException(String message, Throwable cause) {
		super(message, cause);
	}

	public WordReplaceException(String message) {
		super(message);
	}

	public WordReplaceException(Throwable cause) {
		super(cause);
	}
}
