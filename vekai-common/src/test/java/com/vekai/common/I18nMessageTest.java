package com.vekai.common;

import cn.fisok.raw.autoconfigure.ResourceBundleMessageAutoConfiguration;
import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.kit.JSONKit;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class I18nMessageTest extends BaseTest {

    @Autowired
    private ResourceBundleMessageAutoConfiguration resourceBundleMessage;
    @Autowired
    private MessageSource messageSource;

    @Test
    public void test01(){
        LocaleContextHolder.setLocale(new Locale("zh","CN"));
//        LocaleContextHolder.setLocale(new Locale("en","US"));
//        LocaleContextHolder.setLocale(Locale.US);
//        System.out.println(messageSource.getClass().getName());
        Assert.assertEquals(messageSource,resourceBundleMessage);
        System.out.println(JSONKit.toJsonString(resourceBundleMessage.getProperties()));
        System.out.println(messageSource.getMessage("common.message1",new Object[]{"A","B"},LocaleContextHolder.getLocale()));
        System.out.println(ApplicationContextHolder.getContext().getMessage("common.message1",new Object[]{"A","B"},LocaleContextHolder.getLocale()));
        System.out.println("Holder0:"+ MessageHolder.getMessage(null,"common.message1","POS1",2));
        System.out.println("Holder1:"+ MessageHolder.getMessage("参数1:{0},参数:{1}","common.message9","POS1",2));
        System.out.println("Holder1:"+ MessageHolder.getMessage("参数1:{0},参数:{1}","common.message9"));
//        Assert.assertEquals(messageSource.getMessage("common.message1",new Object[]{"A","B"},LocaleContextHolder.getLocale())
//                ,"这是一段由[Runtime]模块输出配置的国际化消息,参数1:A,参数:B");
    }
}
