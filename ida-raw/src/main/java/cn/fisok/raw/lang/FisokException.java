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
package cn.fisok.raw.lang;

import java.text.MessageFormat;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 22:26
 * @desc :
 */
public class FisokException extends RuntimeException{

    private static final long serialVersionUID = -2049467256019982005L;
    private String code = "0";


    public FisokException() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public FisokException(String message) {
        super(message);
    }

    public FisokException(String messageFormat, Object ...objects) {
        this(MessageFormat.format(messageFormat, objects));
    }
    public FisokException(Throwable cause, String messageFormat, Object ...objects) {
        this(MessageFormat.format(messageFormat, objects),cause);
    }

    public FisokException(Throwable cause, String message) {
        super(message, cause);
    }

    public FisokException(Throwable cause) {
        super(cause);
    }


    public FisokException(String message, Throwable cause) {
        super(message, cause);
    }

    public FisokException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
