/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.fisok.sqloy.exception;

import cn.fisok.raw.lang.FisokException;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : SQL模块异常
 */
public class SqloyException extends FisokException {
    public SqloyException() {
    }

    public SqloyException(String message) {
        super(message);
    }

    public SqloyException(String messageFormat, Object... objects) {
        super(messageFormat, objects);
    }

    public SqloyException(Throwable cause, String messageFormat, Object... objects) {
        super(cause, messageFormat, objects);
    }

    public SqloyException(Throwable cause, String message) {
        super(cause, message);
    }

    public SqloyException(Throwable cause) {
        super(cause);
    }

    public SqloyException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqloyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
