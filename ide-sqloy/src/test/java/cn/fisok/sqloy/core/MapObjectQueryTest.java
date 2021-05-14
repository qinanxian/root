package cn.fisok.sqloy.core;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.BaseTest;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.MapKit;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class MapObjectQueryTest extends BaseTest {
    @Autowired
    protected MapObjectQuery dataQuery;

//    @Test
    public void selectOneTest(){
        MapObject person = dataQuery.selectOne("select * from DEMO_PERSON where CODE=:code","code","P1001");
        Assert.assertNotNull(person);
        System.out.println(JSONKit.toJsonString(person));
        Assert.assertEquals("艾伦",person.getValue("name").strValue());
    }

//    @Test
    public void selectListTest(){
        List<MapObject> personList = dataQuery.selectList("select * from DEMO_PERSON where CODE>:code","code","P1001");
        Assert.assertNotNull(personList);
        System.out.println(JSONKit.toJsonString(personList));
    }

    @Test
    public void selectPaginationListTest(){
        Map<String,?> paramMap = MapKit.mapOf("code","P1001");
        PaginResult result = dataQuery.selectListPagination("select * from DEMO_PERSON where CODE>:code",paramMap,2,15);
        System.out.println(JSONKit.toJsonString(result));
    }
}
