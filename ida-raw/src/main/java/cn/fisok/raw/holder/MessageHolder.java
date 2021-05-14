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

import cn.fisok.raw.kit.ValidateKit;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 22:52
 * @desc : 国际化消息处理快速API提供
 */
public abstract class MessageHolder {
    public static MessageSource getMessageSource(){
        MessageSource messageSource = ApplicationContextHolder.getBean(MessageSource.class);
        ValidateKit.notNull(messageSource);
        return messageSource;
    }


    /**
     *
     * @param code 对应messages配置的key.
     * @param args 数组参数.
     * @param defaultMessage 没有设置key的时候的默认值.
     * @return string
     */
    public static String getMessage(String defaultMessage,String code,Object... args){
        //这里使用比较方便的方法，不依赖request.
        Locale locale = LocaleContextHolder.getLocale();
        return getMessageSource().getMessage(code, args, defaultMessage, locale);
    }
}
