package com.vekai.auth.controller;

import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.auth.service.AuthService;
import cn.fisok.raw.kit.NumberKit;
import cn.fisok.raw.kit.ValidateKit;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "RoberApi-Auth", description = "用户授权")
@RestController
@RequestMapping("/auth/public")
public class AuthPublicController {
    @Autowired
    private AuthService authService;

    @GetMapping("/session-user")
    public User getSessionUser(){
        return AuthHolder.getUser();
    }
    @PostMapping("/register")
    public User register(@ModelAttribute User user, HttpServletRequest request, HttpServletResponse response){
        //检查用户是否存在
        User existsUser = authService.getUserByCode(user.getCode());
        ValidateKit.isTrue(existsUser==null,"用户[{0}]已经存在",user.getCode());

        //注册
        user.setId(NumberKit.nanoTime36());
        authService.saveUser(user);
        authService.updatePassword(user.getId(), null, user.getPassword(), false);

        //注册成功后，根据传入的URL，作重定向处理
        LoginAndOffRestController.setRedirectLocation(request,response);

        return user;
    }
}
