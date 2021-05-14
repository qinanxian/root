package cn.fisok.sqloy.core;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.BaseTest;
import cn.fisok.sqloy.entity.TestData;
import cn.fisok.raw.kit.MapKit;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MapObjectUpdaterTest extends BaseTest {
    @Autowired
    protected MapObjectUpdater dataUpdater;
    @Autowired
    protected MapObjectQuery dataQuery;

    @Test
    public void testInsert(){
        dataUpdater.execute("delete from DEMO_PERSON where ID in (701,702,703)");
        MapObject dataObject = TestData.dataList.get(0);
        dataUpdater.insert("DEMO_PERSON",dataObject);
        MapObject row = dataQuery.selectOne("SELECT * FROM DEMO_PERSON where ID=:id",MapKit.mapOf("id",dataObject.getValue("id").intValue()));

        System.out.println(row.toJsonString());

        dataUpdater.delete("DEMO_PERSON", MapObject.valueOf("id",dataObject.getValue("id").intValue()));
        dataUpdater.execute("delete from DEMO_PERSON where ID=:id",MapKit.mapOf("id",dataObject.getValue("id").intValue()));
    }

    @Test
    public void testSave(){
        dataUpdater.save("DEMO_PERSON", TestData.dataList, TestData.keyList);
    }

}
