package com.vekai.auth;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

public class Md5HashTest {

    @Test
    public void test1(){
        String text = "000000als";
        System.out.println(new Md5Hash(text).toHex());
    }
}
