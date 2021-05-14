package cn.fisok.raw;

import cn.fisok.raw.autoconfigure.ResourceBundleMessageAutoConfiguration;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.script.ScriptParser;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Locale;

public class RawStarterTest extends BaseTest {
    @Autowired
    private ScriptEngine scriptEngine;
    @Autowired
    ResourceBundleMessageAutoConfiguration messageResource;

    @Test
    public void testContext(){
        Assert.assertNotNull(scriptEngine);
    }
    @Test
    public void testI18n(){

        String refer1 = "这是一段由[RAW]模块输出配置的国际化消息,参数1:A1,参数:B2";
        String msg1 = MessageHolder.getMessage("默认","raw.message","A1","B2");
        Assert.assertEquals(refer1,msg1);

        //作语言切换
        Locale oldLocale = LocaleContextHolder.getLocale();
        LocaleContextHolder.setLocale(Locale.US);
        String refer2 = "This is an internationalized message that is configured by the [RAW] module, parameter 1:A1, parameter: B2";
        String msg2 = MessageHolder.getMessage("默认","raw.message","A1","B2");
        LocaleContextHolder.setLocale(oldLocale);
        Assert.assertEquals(refer2,msg2);
    }
    @Test
    public void testGroovy() throws ScriptException {
        ScriptParser<Integer> scriptParser = new ScriptParser<Integer>(scriptEngine);
        int ret = scriptParser.parse("1+2");
        Assert.assertEquals(3,ret);
    }

    @Test
    public void testGetI18nProperties(){
        System.out.println(messageResource.getProperties());
    }

}
