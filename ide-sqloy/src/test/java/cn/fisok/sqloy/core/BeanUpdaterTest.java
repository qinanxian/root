package cn.fisok.sqloy.core;

import cn.fisok.raw.kit.NumberKit;
import cn.fisok.sqloy.BaseTest;
import cn.fisok.sqloy.entity.DemoPerson;
import cn.fisok.sqloy.entity.DemoPersonExt;
import cn.fisok.sqloy.entity.TestData;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.MapKit;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BeanUpdaterTest extends BaseTest {
    @Autowired
    protected BeanQuery query;
    @Autowired
    protected BeanUpdater updater;

//    @Test
    public void insertTest(){
//        updater.execute("delete from DEMO_PERSON where ID in (701,702,703)");
        DemoPerson person = new DemoPerson();
        person.setId(NumberKit.nanoTime());
        person.setCode("jt");
        person.setName("测试1");
        person.setChnName("测试1");
        person.setEngName("test1");
        person.setHeight(182);
        person.setViewTimes(3L);
        person.setBirth(DateKit.parse("1999-8-8"));

        updater.insert(person);

        boolean exists = query.selectExistsById(DemoPerson.class,person.getId());
        Assert.assertTrue(exists);

        person = query.selectOneById(DemoPerson.class,person.getId());
        Assert.assertEquals("测试1",person.getName());

        updater.delete(person);
//        updater.execute("delete * from DEMO_PERSON where ID=:id",MapKit.mapOf("id",person.getId());

        exists = query.selectExistsById(DemoPerson.class,MapKit.mapOf("id",person.getId()));
        Assert.assertFalse(exists);
    }

    @Test
    public void testBatch(){

        updater.execute("delete from DEMO_PERSON where ID in (701,702,703)");
        updater.insert(TestData.personList);

        List<DemoPerson> personList = query.selectList(DemoPerson.class,"select * from DEMO_PERSON");
        System.out.println(JSONKit.toJsonString(personList));

        updater.delete(TestData.personList);

    }

    @Test
    public void testSave(){

        updater.execute("delete from DEMO_PERSON where ID in (701,702,703)");
        updater.save(TestData.personList);

        List<DemoPerson> personList = query.selectList(DemoPerson.class,"select * from DEMO_PERSON");
        System.out.println(JSONKit.toJsonString(personList));

        updater.delete(TestData.personList);

    }

    @Test
    public void testSaveOne(){
        updater.execute("delete from DEMO_PERSON where ID in ('P01')");

        DemoPerson person = new DemoPerson();
        person.setId(101L);
        person.setCode("C01");
        person.setName("插入测试1");

        updater.save(person);

        updater.execute("delete from DEMO_PERSON where ID in ('P01')");
    }


    @Test
    public void testSaveOneExt(){
        updater.execute("delete from DEMO_PERSON where ID in ('P01')");

        DemoPersonExt person = new DemoPersonExt();
        person.setId(101L);
        person.setCode("C01");
        person.setName("插入测试1");

        updater.save(person);

        updater.execute("delete from DEMO_PERSON where ID in ('P01')");
    }

}
