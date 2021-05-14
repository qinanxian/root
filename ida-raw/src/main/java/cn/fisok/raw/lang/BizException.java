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
 * @date : 2020/7/22
 * @desc :
 */
public class BizException extends FisokException {

    private static final long serialVersionUID = -2049467256019982005L;
    private String code = "0";


    public BizException() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String messageFormat, Object ...objects) {
        this(MessageFormat.format(messageFormat, objects));
    }
    public BizException(Throwable cause, String messageFormat, Object ...objects) {
        this(MessageFormat.format(messageFormat, objects),cause);
    }

    public BizException(Throwable cause, String message) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }


    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
