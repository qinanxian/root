package com.vekai.showcase.controller;

import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
import cn.fisok.raw.kit.DateKit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/showcase/RestControllerDemo")
public class RestControllerDemo {

    @RequestMapping("/now")
    public String helloWord(){
        return DateKit.format(DateKit.now());
    }

    @RequestMapping("/sessionUser")
    public User sessionUser(){
        return AuthHolder.getUser();
    }

}
