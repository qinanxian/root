package com.vekai.auth.exception;

/**
 * Created by tisir<yangsong158@qq.com> on 2017-05-14
 */
public class UserNotExistsException extends AuthException {
    public UserNotExistsException() {
        super();
    }

    public UserNotExistsException(String message) {
        super(message);
    }

    public UserNotExistsException(String messageFormat, Object... objects) {
        super(messageFormat, objects);
    }

    public UserNotExistsException(Throwable cause, String messageFormat, Object... objects) {
        super(cause, messageFormat, objects);
    }

    public UserNotExistsException(Throwable cause, String message) {
        super(cause, message);
    }

    public UserNotExistsException(Throwable cause) {
        super(cause);
    }

    public UserNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
