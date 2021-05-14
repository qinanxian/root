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
package cn.fisok.raw.holder;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/1/22
 * @desc : 字符集保持器，方便调用者调用时，直接使用，不需要把配置项直接传过去
 */
public class CharsetHolder {
    private static final ThreadLocal<Charset> charsetThreadLocal = new ThreadLocal<Charset>();
    public static Charset getCharset (){
        Charset charset = charsetThreadLocal.get();
        if(charset == null) charset = Charset.defaultCharset();
        return charset;
    }
    public static String getCharsetName(){
        return getCharset().name();
    }
    public static void setCharset(Charset charset){
        charsetThreadLocal.set(charset);
    }

    public static void clear() {
        charsetThreadLocal.remove();
    }
}
