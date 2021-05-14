package com.vekai.showcase.webservices;

import javax.jws.WebService;

@WebService
public interface DemoService {
    public String sayHello(String user);
}