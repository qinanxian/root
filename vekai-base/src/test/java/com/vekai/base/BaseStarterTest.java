package com.vekai.base;

import cn.fisok.sqloy.core.BeanCruder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;

public class BaseStarterTest extends BaseTest{
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected BeanCruder beanCruder;
    @Autowired
    protected CacheManager cacheManager;

    @Test
    public void runtimeTest(){
        Assert.assertNotNull(jdbcTemplate);
        Assert.assertNotNull(beanCruder);
    }

    @Test
    public void testBase(){
        Assert.assertNotNull(cacheManager);
    }
}
