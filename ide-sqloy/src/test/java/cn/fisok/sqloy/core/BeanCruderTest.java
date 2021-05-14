package cn.fisok.sqloy.core;

import cn.fisok.raw.kit.*;
import cn.fisok.sqloy.BaseTest;
import cn.fisok.sqloy.entity.DemoPerson;
import cn.fisok.sqloy.entity.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class BeanCruderTest extends BaseTest {
    @Autowired
    protected BeanCruder beanCruder;

    @Test
    public void selectOneTest(){
//        DemoPerson DemoPerson = beanCruder.selectOne(DemoPerson.class,"select * from DEMO_PERSON where CODE=:code","code","P1001");
        DemoPerson DemoPerson = beanCruder.selectOne(DemoPerson.class,"select * from DEMO_PERSON where CODE LIKE :code ","code","%P1001");
        Assert.assertNotNull(DemoPerson);
        Assert.assertEquals("艾伦",DemoPerson.getName());
    }

    @Test
    public void selectByIdTest(){
        DemoPerson DemoPerson = beanCruder.selectOneById(DemoPerson.class,1);
        Assert.assertNotNull(DemoPerson);
        Assert.assertEquals("艾伦",DemoPerson.getName());

        DemoPerson = beanCruder.selectOneById(DemoPerson.class, MapKit.mapOf("id",1));
        Assert.assertNotNull(DemoPerson);
        Assert.assertEquals("艾伦",DemoPerson.getName());

        Assert.assertTrue(beanCruder.selectExistsById(DemoPerson.class,1));
        Assert.assertTrue(beanCruder.selectExistsById(DemoPerson.class,MapKit.mapOf("id",1)));
    }

    @Test
    public void testPaginationQuery(){
        Assert.assertNotNull(beanCruder);
        Map<String,?> paramMap = MapKit.mapOf("code","P1002");
        PaginResult result = beanCruder.selectListPagination(DemoPerson.class,"select * from DEMO_PERSON where CODE > :code",paramMap,3,15);
        Assert.assertTrue(result.getRowCount()>0);
        System.out.println(JSONKit.toJsonString(result,true));
    }

    @Test
    public void testSelectCount(){
        Map<String,?> paramMap = MapKit.mapOf("code","P1002");
        int count = beanCruder.selectCount("select * from DEMO_PERSON where CODE=:code",paramMap);
        Assert.assertEquals(1,count);
    }

    //    @Test
    public void insertTest(){
//        updater.execute("delete from DEMO_PERSON where ID in (701,702,703)");
        DemoPerson demoPerson = new DemoPerson();
        demoPerson.setId(NumberKit.nanoTime());
        demoPerson.setCode("jt");
        demoPerson.setName("测试1");
        demoPerson.setChnName("测试1");
        demoPerson.setEngName("test1");
        demoPerson.setHeight(182);
        demoPerson.setViewTimes(3L);
        demoPerson.setBirth(DateKit.parse("1999-8-8"));

        beanCruder.insert(demoPerson);

        boolean exists = beanCruder.selectExistsById(DemoPerson.class,demoPerson.getId());
        Assert.assertTrue(exists);

        demoPerson = beanCruder.selectOneById(DemoPerson.class,demoPerson.getId());
        Assert.assertEquals("测试1",demoPerson.getName());

        beanCruder.delete(demoPerson);
//        updater.execute("delete * from DEMO_PERSON where ID=:id",MapKit.mapOf("id",DemoPerson.getId());

        exists = beanCruder.selectExistsById(DemoPerson.class,MapKit.mapOf("id",demoPerson.getId()));
        Assert.assertFalse(exists);
    }

    @Test
    public void testBatch(){

        beanCruder.execute("delete from DEMO_PERSON where ID in ('701','702','703')");
        beanCruder.insert(TestData.personList);

        List<DemoPerson> DemoPersonList = beanCruder.selectList(DemoPerson.class,"select * from DEMO_PERSON");
        System.out.println(JSONKit.toJsonString(DemoPersonList));

        beanCruder.delete(TestData.personList);

    }

    @Test
    public void testSave(){

        beanCruder.execute("delete from DEMO_PERSON where ID in ('701','702','703')");
        beanCruder.save(TestData.personList);

        List<DemoPerson> DemoPersonList = beanCruder.selectList(DemoPerson.class,"select * from DEMO_PERSON");
        System.out.println(JSONKit.toJsonString(DemoPersonList));

        beanCruder.delete(TestData.personList);

    }

    @Test
    public void testSelectInteger(){
        List<Integer> dataList = beanCruder.selectList(Integer.class,"select 1 from DEMO_PERSON");
        System.out.println(dataList);
        System.out.println(beanCruder.selectOne(Integer.class,"select 1 from DEMO_PERSON"));
        System.out.println(beanCruder.selectOne(String.class,"select 'A123' from DEMO_PERSON"));
    }
}
