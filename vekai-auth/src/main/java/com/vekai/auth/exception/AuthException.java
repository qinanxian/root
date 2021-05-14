package com.vekai.auth.exception;

import cn.fisok.raw.lang.FisokException;

/**
 * Created by tisir<yangsong158@qq.com> on 2017-05-14
 */
public class AuthException extends FisokException {
    public AuthException() {
        super();
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String messageFormat, Object... objects) {
        super(messageFormat, objects);
    }

    public AuthException(Throwable cause, String messageFormat, Object... objects) {
        super(cause, messageFormat, objects);
    }

    public AuthException(Throwable cause, String message) {
        super(cause, message);
    }

    public AuthException(Throwable cause) {
        super(cause);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
