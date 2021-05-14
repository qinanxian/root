package com.vekai.base;

import com.vekai.base.entity.Person;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BeanCruderWhenFillTest extends BaseTest {
    @Autowired
    protected BeanCruder beanCruder;

    @Test
    public void testSaveBatch(){

        beanCruder.execute("delete from DEMO_PERSON where ID in (701,702,703)",MapKit.newEmptyMap());
        beanCruder.save(TestData.personList.get(0));
//        dataBeanAccessor.save(TestData.personList);

        List<Person> personList = beanCruder.selectList(Person.class,"select * from DEMO_PERSON",MapKit.newEmptyMap());
        System.out.println(JSONKit.toJsonString(personList));
//        beanCruder.update("delete from DEMO_PERSON where ID in (701,702,703)",MapKit.newEmptyMap());

    }
}
