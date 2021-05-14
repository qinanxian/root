package cn.fisok.sqloy.entity;

import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.lang.MapObject;

import java.util.ArrayList;
import java.util.List;

public class TestData {
    public static List<MapObject> dataList = new ArrayList<MapObject>();
    public static List<MapObject> keyList = new ArrayList<MapObject>();
    public static DemoPerson[] persons = null;
    public static List<DemoPerson> personList = new ArrayList<DemoPerson>();
        static {
            persons = new DemoPerson[]{new DemoPerson(), new DemoPerson(), new DemoPerson()};

            persons[0].setId(701L);
            persons[0].setCode("jt");
            persons[0].setName("测试1");
            persons[0].setChnName("测试1");
            persons[0].setEngName("test1");
//        persons[0].setRevision(1);
            persons[0].setHeight(182);
            persons[0].setViewTimes(3L);
            persons[0].setBirth(DateKit.parse("1999-8-8"));

            persons[1].setId(702L);
            persons[1].setCode("jt");
            persons[1].setName("测试2");
            persons[1].setChnName("测试2");
            persons[1].setEngName("test2");
//        persons[1].setRevision(1);
            persons[1].setHeight(182);
            persons[1].setViewTimes(3L);
            persons[1].setBirth(DateKit.parse("1999-8-8"));

            persons[2].setId(703L);
            persons[2].setCode("jt");
            persons[2].setName("测试3");
            persons[2].setChnName("测试3");
            persons[2].setEngName("test3");
//        persons[2].setRevision(1);
            persons[2].setHeight(182);
            persons[2].setViewTimes(3L);
            persons[2].setBirth(DateKit.parse("1999-8-8"));

            for (int i = 0; i < persons.length; i++) {
                DemoPerson p = persons[i];
                MapObject dataItem = new MapObject();
                dataItem.putFromBean(p);
                dataItem.remove("addresses");
                dataItem.remove("viewTimes");
                dataList.add(dataItem);

                MapObject keys = new MapObject();
                keys.put("id", p.getId());
                keyList.add(keys);
                personList.add(p);
            }

        }
    }
