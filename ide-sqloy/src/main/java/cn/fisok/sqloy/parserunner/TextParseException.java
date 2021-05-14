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
package cn.fisok.sqloy.parserunner;

import cn.fisok.sqloy.exception.SqloyException;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 定义文本解析异常类
 */
public class TextParseException extends SqloyException {

	private static final long serialVersionUID = 82734950866419781L;

	public TextParseException() {
		super();
	}

	public TextParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public TextParseException(String message) {
		super(message);
	}

	public TextParseException(Throwable cause) {
		super(cause);
	}
}
