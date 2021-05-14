package com.vekai.dataform.exception;


import cn.fisok.raw.lang.FisokException;

/**
 * Created by tisir yangsong158@qq.com on 2017-05-22
 */
public class ValidatorException extends FisokException {

    public ValidatorException() {
        super();
    }

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(String messageFormat, Object... objects) {
        super(messageFormat, objects);
    }

    public ValidatorException(Throwable cause, String messageFormat, Object... objects) {
        super(cause, messageFormat, objects);
    }

    public ValidatorException(Throwable cause, String message) {
        super(cause, message);
    }

    public ValidatorException(Throwable cause) {
        super(cause);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
