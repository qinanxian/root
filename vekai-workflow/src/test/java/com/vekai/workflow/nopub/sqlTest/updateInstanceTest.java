package com.vekai.workflow.nopub.sqlTest;

import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.workflow.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: qyyao
 * @Description:
 * @Date: Created in 16:38 10/04/2018
 */
public class updateInstanceTest extends BaseTest {

    @Autowired
    private BeanCruder beanCruder;



    @Test
    public void testUpdateActInst() {
        String id = "Simple:100:3050040";
        String key = "Simple";
        int version = 100;

        String partSql = "select id_ from ACT_RE_PROCDEF where key_ =:key and version_ <:version";
        beanCruder
            .execute("update ACT_HI_ACTINST set PROC_DEF_ID_ =:id where PROC_DEF_ID_ in (" + partSql
                    + ") and END_TIME_ is null",
                MapKit.mapOf("id", id, "key", key, "version", version));

    }
}
