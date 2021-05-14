package cn.fisok.sqloy.core;

import cn.fisok.sqloy.BaseTest;
import cn.fisok.sqloy.entity.DemoPerson;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.MapKit;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class BeanQueryTest extends BaseTest {

    @Autowired
    protected BeanQuery sqlQuery;

    @Test
    public void selectOneTest(){
        DemoPerson person = sqlQuery.selectOne(DemoPerson.class,"select * from DEMO_PERSON where CODE=:code","code","P1001");
        Assert.assertNotNull(person);
        System.out.println(JSONKit.toJsonString(person));
        Assert.assertEquals("艾伦",person.getName());
    }

    @Test
    public void selectByIdTest(){
        DemoPerson person = sqlQuery.selectOneById(DemoPerson.class,1);
        Assert.assertNotNull(person);
        Assert.assertEquals("艾伦",person.getName());

        person = sqlQuery.selectOneById(DemoPerson.class,MapKit.mapOf("id",1));
        Assert.assertNotNull(person);
        Assert.assertEquals("艾伦",person.getName());

        Assert.assertTrue(sqlQuery.selectExistsById(DemoPerson.class,1));
        Assert.assertTrue(sqlQuery.selectExistsById(DemoPerson.class,MapKit.mapOf("id",1)));
    }

    @Test
    public void testPaginationQuery(){
        Assert.assertNotNull(sqlQuery);
        Map<String,?> paramMap = MapKit.mapOf("code","P1002");
        PaginResult result = sqlQuery.selectListPagination(DemoPerson.class,"select * from DEMO_PERSON where CODE > :code",paramMap,3,15);
        Assert.assertTrue(result.getRowCount()>0);
        System.out.println(JSONKit.toJsonString(result));
    }

    @Test
    public void testSelectCount(){
        Map<String,?> paramMap = MapKit.mapOf("code","P1002");
        int count = sqlQuery.selectCount("select * from DEMO_PERSON where CODE=:code",paramMap);
        Assert.assertEquals(1,count);
    }
}
