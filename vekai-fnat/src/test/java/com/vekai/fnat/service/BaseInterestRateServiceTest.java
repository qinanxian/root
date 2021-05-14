package com.vekai.fnat.service;

import com.vekai.fnat.model.BaseInterestRate;
import com.vekai.fnat.BaseTest;
import cn.fisok.sqloy.core.BeanCruder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseInterestRateServiceTest extends BaseTest{

    @Autowired
    protected BaseInterestRateService service;

    @Autowired
    protected BeanCruder beanCruder;

    @Test
    public void testNotNull(){
        Assert.assertNotNull(service);
    }

    @Test
    public void testDataRow(){
        List<BaseInterestRate> dataList = service.getBaseInterestRateList("PBOCLoan");
        Assert.assertNotNull(dataList);
        Assert.assertTrue(dataList.size()>0);
    }
}
