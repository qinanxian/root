package cn.fisok.raw.kit;

import org.junit.Test;

public class ClassKitTest {
    @Test
    public void test01(){
        String expr = "com.vekai.runtime.entity.Person.chnName";
        int lastDotIdx = expr.lastIndexOf(".");
        String className = expr.substring(0,lastDotIdx);
        String propertyName = expr.substring(lastDotIdx+1);
        System.out.println(className);
        System.out.println(propertyName);
    }
}
