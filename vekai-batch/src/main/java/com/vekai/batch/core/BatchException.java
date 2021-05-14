package com.vekai.batch.core;

import cn.fisok.raw.lang.FisokException;

public class BatchException extends FisokException {
    public BatchException() {
    }

    public BatchException(String message) {
        super(message);
    }

    public BatchException(String messageFormat, Object... objects) {
        super(messageFormat, objects);
    }

    public BatchException(Throwable cause, String messageFormat, Object... objects) {
        super(cause, messageFormat, objects);
    }

    public BatchException(Throwable cause, String message) {
        super(cause, message);
    }

    public BatchException(Throwable cause) {
        super(cause);
    }

    public BatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public BatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
