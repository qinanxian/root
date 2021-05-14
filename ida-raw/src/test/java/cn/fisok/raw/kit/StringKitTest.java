package cn.fisok.raw.kit;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class StringKitTest {
    @Test
    public void test01(){
        System.out.println(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase("PersonName"),"_"));
        System.out.println(StringUtils.join(StringUtils.splitByCharacterType("PersonName"),"_"));
    }

    @Test
    public void test2(){
        System.out.println(StringKit.camelToUnderline("PersonName"));
        System.out.println(StringKit.camelToUnderline("nameI18nCode"));
        System.out.println(StringKit.camelToUnderline("subGroupI18nCode"));
        System.out.println(StringKit.camelToUnderline("PERSON_NAME"));
        System.out.println(StringKit.underlineToCamel("PERSON_NAME"));
    }
    @Test
    public void testDecodeKeyValue(){
        System.out.println(StringKit.decodeKeyValue("k1","k1","v1","s0"));
        System.out.println(StringKit.decodeKeyValue("k9","k1","v1","s0"));
        System.out.println(StringKit.decodeKeyValue("k2","k1","v1","k2","v2","s0"));
    }

    @Test
    public void testStringPad(){
        Assert.assertEquals("001",StringKit.leftPad("1",3,"0"));
        Assert.assertEquals("1234",StringKit.leftPad("1234",3,"0"));
        Assert.assertEquals("100",StringKit.rightPad("1",3,"0"));
    }
}
