package com.vekai.base;

import com.vekai.base.entity.Person;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RuntimeAOPTest extends BaseTest {
    @Autowired
    protected BeanCruder beanCruder;

    @Test
    public void testSave(){
        Person person = new Person();
        person.setId("701");
        person.setCode("jt");
        person.setName("测试1");
        person.setChnName("测试1");
        person.setEngName("test1");
//        person.setRevision(1);
        person.setHeight(182.3);
        person.setViewTimes(3L);
        person.setBirth(DateKit.parse("1999-8-8"));

        beanCruder.save(person);
    }
}
