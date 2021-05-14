package com.vekai.lact.exception;

import cn.fisok.raw.lang.BizException;

public class LactException extends BizException {
    public LactException() {
        super();
    }

    public LactException(String message) {
        super(message);
    }

    public LactException(String messageFormat, Object... objects) {
        super(messageFormat, objects);
    }

    public LactException(Throwable cause, String messageFormat, Object... objects) {
        super(cause, messageFormat, objects);
    }

    public LactException(Throwable cause, String message) {
        super(cause, message);
    }

    public LactException(Throwable cause) {
        super(cause);
    }

    public LactException(String message, Throwable cause) {
        super(message, cause);
    }

    public LactException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
