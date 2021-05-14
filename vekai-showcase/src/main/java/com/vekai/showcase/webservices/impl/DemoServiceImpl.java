package com.vekai.showcase.webservices.impl;

import com.vekai.showcase.webservices.DemoService;

import java.util.Date;

public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String user) {
        return user+":hello"+"("+new Date()+")";
    }
}
