package com.vekai.lact.exception;

/**
 * 核算交易定义异常
 */
public class LactTransactionDefinitionException extends LactException {
    public LactTransactionDefinitionException() {
        super();
    }

    public LactTransactionDefinitionException(String message) {
        super(message);
    }

    public LactTransactionDefinitionException(String messageFormat, Object... objects) {
        super(messageFormat, objects);
    }

    public LactTransactionDefinitionException(Throwable cause, String messageFormat, Object... objects) {
        super(cause, messageFormat, objects);
    }

    public LactTransactionDefinitionException(Throwable cause, String message) {
        super(cause, message);
    }

    public LactTransactionDefinitionException(Throwable cause) {
        super(cause);
    }

    public LactTransactionDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public LactTransactionDefinitionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
