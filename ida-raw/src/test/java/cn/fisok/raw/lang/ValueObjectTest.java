package cn.fisok.raw.lang;

import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.JSONKit;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by tisir<yangsong158@qq.com> on 2017-06-01
 */
public class ValueObjectTest {

    @Test
    public void testSimple(){
        ValueObject[] vos = new ValueObject[]{
               new ValueObject("300380"),
                new ValueObject("2017-6-1"),
                new ValueObject(250),
                new ValueObject(3003.81),
        };

//        System.out.println(vos[0]);
//        System.out.println(vos[1]);
//        System.out.println(vos[2]);
//        System.out.println(vos[3]);


        vos[0].intValue();
        vos[0].dateValue();

        Assert.assertEquals(vos[0].value(),"300380");
        Assert.assertEquals(vos[1].value(),"2017-6-1");
        Assert.assertEquals((int)vos[2].value(),250,0);
        Assert.assertEquals((double)vos[3].value(),3003.81,0.001);

        Assert.assertEquals(vos[0].getValueType(), ValueObject.ValueType.String);
        Assert.assertEquals(vos[1].getValueType(), ValueObject.ValueType.String);
        Assert.assertEquals(vos[2].getValueType(), ValueObject.ValueType.Number);
        Assert.assertEquals(vos[3].getValueType(), ValueObject.ValueType.Number);

        Assert.assertEquals(vos[0].intValue(),300380,0);
        Assert.assertEquals(vos[1].dateValue(), DateKit.xparse("2017-6-1"));
        Assert.assertEquals(vos[2].strValue(), "250");
        Assert.assertEquals(vos[3].strValue(), "3003.81");
        Assert.assertEquals(vos[3].intValue(), 3003,0);
        Assert.assertEquals(vos[3].doubleValue(), 3003.81,0.001);

        System.out.println(JSONKit.toJsonString(vos[3]));
    }

    @Test
    public void testArray(){
        ValueObject[] vos = new ValueObject[]{
                new ValueObject("start"),
                new ValueObject(new String[]{"300380","201505"}),
                new ValueObject(new String[]{"2015-05-01","2016-08-31"}),
                new ValueObject(new Number[]{28,32,520.338}),
                new ValueObject(new Date[]{DateKit.parse("2008-6-4"), DateKit.parse("2009-7-15")}),
        };

        System.out.println(vos[0]);
        System.out.println(vos[1]);
        System.out.println(vos[2]);
        System.out.println(vos[3]);
        System.out.println(vos[4]);

        Assert.assertEquals(vos[1].getValueType(), ValueObject.ValueType.Array);
        Assert.assertEquals(vos[1].strArray()[1],"201505");
        Assert.assertEquals(vos[1].intArray()[1],201505,0);
        Assert.assertEquals(vos[2].dateArray()[0], DateKit.parse("2015-05-01"));
        Assert.assertEquals(vos[3].doubleArray()[2],520.338,0000.1);
        Assert.assertEquals(vos[4].strArray()[0],"2008-06-04 00:00:00.000");
    }
}
