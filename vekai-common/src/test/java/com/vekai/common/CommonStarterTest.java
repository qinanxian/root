package com.vekai.common;

import com.vekai.common.fileman.service.FileManService;
import org.junit.Assert;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

public class CommonStarterTest extends BaseTest {
    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    protected FileManService fileManService;

    @Autowired
    protected DataSource dataSource;

    @Test
    public void test(){
        Assert.assertNotNull(dataSource);
        Assert.assertNotNull(fileManService);
    }

    @Test
    public void baseAutoConfigTest(){
        Assert.assertNotNull(sqlSessionTemplate);
    }
}
