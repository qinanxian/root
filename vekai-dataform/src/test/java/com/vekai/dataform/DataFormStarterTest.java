package com.vekai.dataform;

import com.vekai.dataform.mapper.DataFormMapper;
import cn.fisok.sqloy.core.BeanCruder;
import org.junit.Assert;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class DataFormStarterTest extends BaseTest {
    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    protected BeanCruder beanCruder;

    @Test
    public void test(){
        Assert.assertNotNull(beanCruder);
    }

    @Test
    public void baseAutoConfigTest(){
        Assert.assertNotNull(sqlSessionTemplate);
    }

    @Autowired
    DataFormMapper dataFormMapper;

    @Test
    public void testExist() {
        boolean res = dataFormMapper.exists("workflow-WorkflowParamsList");
        Assert.assertTrue(res);
        res = dataFormMapper.exists("non-exist");
        Assert.assertFalse(res);
    }
}
