package com.vekai.base;

import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.FisokException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ExceptionTestController {
    @RequestMapping(path = "/RuntimeEx")
    public void runtimeEx() {
        throw new RuntimeException();
    }

    @RequestMapping(path = "/FisokException")
    public void vekaiEx() {
        throw new FisokException();
    }

    @RequestMapping(path = "/BizException")
    public void bizEx() {
        throw new BizException();
    }

    @RequestMapping(path = "/IOException")
    public void ioEx() throws IOException{
        throw new IOException();
    }
}
