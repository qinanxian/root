package cn.fisok.raw.lang;

import cn.fisok.raw.entity.Person;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.JSONKit;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tisir<yangsong158@qq.com> on 2017-05-19
 */
public class MapObjectTest {
    static MapObject dx = new MapObject();
    static{
        dx.putValue("user.id",1001L);
        dx.putValue("user.elName","Anne");
        dx.putValue("user.chnName","安妮");
        dx.putValue("user.gender","F");
        dx.putValue("user.age","18");
        dx.putValue("user.phone",null);
        dx.putValue("user.viewTimes",8L);
        dx.putValue("user.revision",1);
        dx.putValue("user.birth", DateKit.parse("1999-08-08"));
        dx.putValue("user.favor", new String[]{"football","swim"});
        dx.putValue("user.receiveAddress", new ArrayList<String>());
        dx.putValue("user.receiveAddress[1]", "上海浦东新区世纪大道8号");
        dx.putValue("user.receiveAddress[2]", "重庆渝中区");
        dx.putValue("user.receiveAddress[3]", "江苏苏州市高新区");
        List<MapObject> addressList = new ArrayList<MapObject>();
        addressList.add(new MapObject());
        addressList.add(new MapObject());
        addressList.add(new MapObject());
        dx.putValue("user.addresses",addressList);
        dx.putValue("user.addresses[1].id", 1L);
        dx.putValue("user.addresses[1].name", "重庆");
        dx.putValue("user.addresses[1].provinceCode", "CQ");
        dx.putValue("user.addresses[1].cityCode", "CQ");
        dx.putValue("user.addresses[1].fullAddress", "重庆渝中区");
        dx.putValue("user.addresses[1].revision", 0L);
        dx.putValue("user.addresses[2].id", 2L);
        dx.putValue("user.addresses[2].name", "上海");
        dx.putValue("user.addresses[2].provinceCode", "SH");
        dx.putValue("user.addresses[2].cityCode", "SH");
        dx.putValue("user.addresses[2].fullAddress", "上海浦东新区世纪大道8号");
        dx.putValue("user.addresses[2].revision", 0L);
        dx.putValue("user.addresses[3].id", 3L);
        dx.putValue("user.addresses[3].name", "苏州");
        dx.putValue("user.addresses[3].provinceCode", "JS");
        dx.putValue("user.addresses[3].cityCode", "SZ");
        dx.putValue("user.addresses[3].fullAddress", "江苏苏州市高新区");
        dx.putValue("user.addresses[3].revision", 0L);
    }

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void testToJson(){
        System.out.println(JSONKit.toJsonString(dx,true));
    }

    @Test
    public void xgetTest(){

        List<String> addressList = dx.getValue("user.receiveAddress").objectVal(ArrayList.class);

        System.out.println(dx.toJsonString());

        Assert.assertEquals("安妮",dx.getValue("user.chnName").toString());
        Assert.assertEquals("安妮",dx.getValue("user.chnName").strValue());
        Assert.assertEquals("安妮",dx.getValue("user.chnName").value());
        Assert.assertEquals("安妮",dx.getValue("user/chnName").value());
        Assert.assertNull(dx.getValue("user.chnName1").value());
        Assert.assertEquals("18",dx.getValue("user.age").strValue());
        Assert.assertEquals(18,dx.getValue("user.age").intValue(1),0);
        Assert.assertEquals(3,addressList.size(),0);
        Assert.assertTrue(dx.getValue("user.age1").isNull());
        Assert.assertFalse(dx.getValue("user.receiveAddress").isEmpty());
        Assert.assertFalse(dx.getValue("user.receiveAddress").isEmpty());

    }

    @Test
    public void strictModeTest(){
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("expression:user.chnName1");

        Assert.assertNull(dx.get("user.chnName1",true).value());
    }

    @Test
    public void strictModeTest1(){
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("No value for xpath: user/chnName1/a01");

        Assert.assertNull(dx.get("user.chnName1.a01",true).value());
    }

    @Test
    public void testToBeanTransfer(){
        MapObject subObject = dx.getSubDataObject("user");
        Person person = subObject.toBean(Person.class);
        Assert.assertNotNull(person);

        Assert.assertEquals("安妮",person.getChnName());
        Assert.assertEquals("F",person.getGender());
        Assert.assertEquals(8L,person.getViewTimes(),0);
        Assert.assertEquals(1,person.getRevision(),0);
//        Map转Bean多层暂时不支持
//        Assert.assertEquals(3,person.getAddresses().size(),0);
//        System.out.println(person.getAddresses().get(1));
//        Assert.assertEquals("上海浦东新区世纪大道8号",person.getAddresses().get(1).getFullAddress());
    }

    @Test
    public void testFromBean(){
        Person person = new Person();
        person.setId(701L);
        person.setName("测试1");
        person.setChnName("测试1");
        person.setEngName("test1");
        person.setRevision(1);
        person.setHeight(182.3);
        person.setViewTimes(3L);
        person.setBirth(DateKit.parse("1999-8-8"));
        MapObject dx = new MapObject();
        dx.putFromBean(person);

        Assert.assertEquals(dx.getValue("id").longValue(),701L,0);
    }

    @Test
    public void testGetNumberNotExists(){
        System.out.println(dx.getValue("111"));
    }


    @Test
    public void replaceKeyOrValueTest(){
        MapObject mapObject = dx.deepClone();
        System.out.println("---------原始值--------");
        System.out.println(mapObject.toJsonString(true));

        System.out.println("---------替换KEY--------");
        mapObject.replaceKey("user.id","user.key");
        mapObject.replaceKey("user.elName","user.engName");
        System.out.println(mapObject.toJsonString(true));

        System.out.println("---------替换VALUE--------");
        mapObject.replaceValue("user.viewTimes",9,10);
        mapObject.replaceValue("user.revision",1,2);
        System.out.println(mapObject.toJsonString(true));

    }


//    @Test
//    public void existsTest(){
//        dx.exists("user.phone");
//    }
}
