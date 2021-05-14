package com.vekai.auth;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

public class ShiroPingTest extends BaseTest {

    @Test
    public void test01(){
        Subject subject = SecurityUtils.getSubject();
        Assert.assertNotNull(subject);
    }
}
