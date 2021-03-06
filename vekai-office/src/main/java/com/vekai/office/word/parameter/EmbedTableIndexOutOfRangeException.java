package com.vekai.office.word.parameter;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 表格相关索引超出边界时，抛出此异常
 *
 * @author yangsong
 * @since 2014年8月1日
 */
public class EmbedTableIndexOutOfRangeException extends RuntimeException {

	private static final long serialVersionUID = -2962957437964476214L;

	
	public EmbedTableIndexOutOfRangeException() {
		super();
	}


	public EmbedTableIndexOutOfRangeException(String message, Throwable cause) {
		super(message, cause);
	}


	public EmbedTableIndexOutOfRangeException(String message) {
		super(message);
	}


	public EmbedTableIndexOutOfRangeException(Throwable cause) {
		super(cause);
	}


	public String getMessage() {
		return super.getMessage();
	}

	
	public String getLocalizedMessage() {
		return super.getLocalizedMessage();
	}

	
	public Throwable getCause() {
		return super.getCause();
	}

	
	public synchronized Throwable initCause(Throwable cause) {
		return super.initCause(cause);
	}

	
	public String toString() {
		return super.toString();
	}

	
	public void printStackTrace() {
		super.printStackTrace();
	}

	
	public void printStackTrace(PrintStream s) {
		super.printStackTrace(s);
	}

	
	public void printStackTrace(PrintWriter s) {
		super.printStackTrace(s);
	}

	
	public synchronized Throwable fillInStackTrace() {
		return super.fillInStackTrace();
	}

	
	public StackTraceElement[] getStackTrace() {
		return super.getStackTrace();
	}

	
	public void setStackTrace(StackTraceElement[] stackTrace) {
		super.setStackTrace(stackTrace);
	}

}
