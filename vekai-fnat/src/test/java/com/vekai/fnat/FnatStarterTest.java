package com.vekai.fnat;

import cn.fisok.sqloy.core.BeanCruder;
import org.junit.Assert;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class FnatStarterTest extends BaseTest {
    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    protected BeanCruder beanCruder;

    @Test
    public void test(){
        Assert.assertNotNull(beanCruder);
    }

}
