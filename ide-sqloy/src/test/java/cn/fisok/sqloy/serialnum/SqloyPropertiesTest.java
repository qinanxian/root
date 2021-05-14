package cn.fisok.sqloy.serialnum;

import cn.fisok.sqloy.BaseTest;
import cn.fisok.sqloy.autoconfigure.SqloyProperties;
import cn.fisok.sqloy.serialnum.consts.CursorRecordType;
import cn.fisok.sqloy.serialnum.consts.GeneratorType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class SqloyPropertiesTest extends BaseTest {
    @Autowired
    protected SqloyProperties properties;

    @Test
    public void test01() {
        Assert.assertNotNull(properties);
//        Assert.assertEquals(properties.getSqlDialectType(), DBType.MYSQL);
        Assert.assertEquals(properties.getSerialNumber().getSnCursorRecordType(), CursorRecordType.DB_TABLE);
        //        Assert.assertEquals(properties.getSerialNo().getCursorRecord(), "Redis");
        Assert.assertEquals(properties.getSerialNumber().getGeneratorMap().get("demo.Person.personId"),
            GeneratorType.AUTO_INCREMENT);
        Assert.assertEquals(properties.getSerialNumber().getTemplateMap().get("demo.Person.personId"),
            "AS0000");

    }
}
