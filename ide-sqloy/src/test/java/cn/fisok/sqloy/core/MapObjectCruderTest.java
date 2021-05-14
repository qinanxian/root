package cn.fisok.sqloy.core;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.BaseTest;
import cn.fisok.sqloy.entity.TestData;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.MapKit;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class MapObjectCruderTest extends BaseTest {
    @Autowired
    protected MapObjectCruder mapObjectCruder;

        @Test
    public void selectOneTest(){
        MapObject person = mapObjectCruder.selectOne("select * from DEMO_PERSON where CODE=:code","code","P1001");
        Assert.assertNotNull(person);
        System.out.println(JSONKit.toJsonString(person));
        Assert.assertEquals("艾伦",person.getValue("name").strValue());
    }

        @Test
    public void selectListTest(){
        List<MapObject> personList = mapObjectCruder.selectList("select * from DEMO_PERSON where CODE>:code","code","P1001");
        Assert.assertNotNull(personList);
        System.out.println(JSONKit.toJsonString(personList));
    }

    @Test
    public void selectPaginationListTest(){
        Map<String,?> paramMap = MapKit.mapOf("code","P1001");
        PaginResult result = mapObjectCruder.selectListPagination("select * from DEMO_PERSON where CODE>:code",paramMap,2,15);
        System.out.println(JSONKit.toJsonString(result));
    }

    @Test
    public void testInsert(){
        mapObjectCruder.execute("delete from DEMO_PERSON where ID in ('701','702','703')");
        MapObject dataObject = TestData.dataList.get(0);
        mapObjectCruder.insert("DEMO_PERSON",dataObject);
        MapObject row = mapObjectCruder.selectOne("SELECT * FROM DEMO_PERSON where ID=:id",MapKit.mapOf("id",dataObject.getValue("id").intValue()));

        System.out.println(row.toJsonString());

        mapObjectCruder.execute("delete from DEMO_PERSON where ID=:id",MapKit.mapOf("id",dataObject.getValue("id").strValue()));

    }

    @Test
    public void testSave(){
        mapObjectCruder.save("DEMO_PERSON", TestData.dataList, TestData.keyList);
    }

    @Test
    public void testSaveDate(){
        MapObject object = new MapObject();
        object.put("id","PX1001");
        object.put("code","PX1001");
        object.put("name","日期测试");
        object.put("birth", DateKit.now());

        MapObject key = new MapObject();
        key.put("id","PX1001");

        mapObjectCruder.save("DEMO_PERSON",object,key);
    }
}
