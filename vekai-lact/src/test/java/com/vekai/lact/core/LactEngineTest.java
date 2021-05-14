package com.vekai.lact.core;

import com.vekai.lact.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LactEngineTest extends BaseTest {

    @Autowired
    protected LactEngine engine;

    @Test
    public void test01(){
        Assert.assertNotNull(engine);
        Assert.assertNotNull(engine.getTransaction("LACTS10"));
    }


}
