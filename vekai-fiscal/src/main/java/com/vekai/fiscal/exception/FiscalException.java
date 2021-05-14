package com.vekai.fiscal.exception;


import cn.fisok.raw.lang.FisokException;

/**
 * Created by tisir<yangsong158@qq.com> on 2018-06-13
 */
public class FiscalException extends FisokException {

    public FiscalException() {
        super();
    }

    public FiscalException(String message) {
        super(message);
    }

    public FiscalException(String messageFormat, Object... objects) {
        super(messageFormat, objects);
    }

    public FiscalException(Throwable cause, String messageFormat, Object... objects) {
        super(cause, messageFormat, objects);
    }

    public FiscalException(Throwable cause, String message) {
        super(cause, message);
    }

    public FiscalException(Throwable cause) {
        super(cause);
    }

    public FiscalException(String message, Throwable cause) {
        super(message, cause);
    }

    public FiscalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
