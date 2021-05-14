package com.vekai.workflow.exception;

import java.text.MessageFormat;

/**
 * 定义统一流程引擎异常
 * 
 * @author 杨松<syang@amarsoft.com>
 * @date 2017年1月10日
 */
public class WorkflowException extends RuntimeException {
    
    public WorkflowException(Throwable throwable) {
        super(throwable);
    }
    public WorkflowException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public WorkflowException(String s) {
		super(s);
	}
	public WorkflowException(String messageFormat,Object ...objects) {
        this(MessageFormat.format(messageFormat, objects));
    }
	public WorkflowException(Throwable throwable,String messageFormat,Object ...objects) {
	    this(MessageFormat.format(messageFormat, objects),throwable);
	}

}
