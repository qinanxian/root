package com.vekai.auth.controller;

import cn.fisok.raw.autoconfigure.RawProperties;
import cn.fisok.web.holder.WebHolder;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.web.kit.HttpKit;
import com.vekai.auth.AuthConsts;
import com.vekai.auth.autoconfigure.AuthProperties;
import com.vekai.auth.entity.User;
import com.vekai.auth.model.LoginResponse;
import com.vekai.auth.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Api(description = "用户登录退出")
@Controller
public class LoginAndOffRestController {
    @Autowired
    private AuthService authService;

    @Autowired
    private RawProperties rawProperties;

    @Autowired
    private AuthProperties authProperties;



    @ApiOperation(value = "登录",response = String.class)
    @RequestMapping(path="/public/logon",method = RequestMethod.POST,headers = "X-Requested-With=XMLHttpRequest") //#{@environment['shiro.loginUrl']}
    @ResponseBody
    public Object login(HttpServletRequest request) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            User currentUser = (User) ((WebSubject) subject).getServletRequest().getAttribute(AuthConsts.SESSION_USER);
            Serializable sessionToken = authProperties.isEnableJwtReplaceSession() ?
                    (String) request.getAttribute(AuthConsts.JWT_ATTRIBUTE) : subject.getSession().getId();
            return new LoginResponse(LoginResponse.getSUCCESS(), sessionToken, currentUser);
        } else {
            return LoginResponse.fail((String)request.getAttribute(AuthConsts.LOGIN_FAIL_MSG_ATTRIBUTE_NAME));
        }
    }

    @ApiOperation(value = "登录",response = String.class)
    @RequestMapping(path="/public/logon",method = RequestMethod.POST)
    public Object formLogin(HttpServletRequest request) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return retrieveRealView(LoginResponse.getSUCCESS().getRedirectUrl());
        } else {
            LoginResponse failResponse = LoginResponse.fail((String)request.getAttribute(AuthConsts.LOGIN_FAIL_MSG_ATTRIBUTE_NAME));
            request.setAttribute("errorMessage", failResponse);
            return retrieveRealView(failResponse.getRedirectUrl());
        }
    }

    @RequestMapping(path = "/public/logon", method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return new ModelAndView(retrieveRealView(LoginResponse.getSUCCESS().getRedirectUrl()));
        } else {
            return new ModelAndView("redirect:" + LoginResponse.logonRealUrl);
        }
    }

    private String retrieveRealView(String url) {
//        return HttpKit.retrieveRealView(rawProperties.getStaticResourceProxyUrl(), url);
        return "";
    }


    @ApiOperation(value = "注销", response = Void.class)
    @RequestMapping(path="/auth/logoff",method = RequestMethod.GET)
    public void logoff(HttpServletRequest request, HttpServletResponse response){
        Subject subject = SecurityUtils.getSubject();
        if(subject!=null)subject.logout();
        ThreadContext.unbindSubject();//退出时请解除绑定Subject到线程 否则对下次测试造成影响
        setRedirectLocation(request,response);
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public void logout() {
        //do nothing
    }

    public static void setRedirectLocation(HttpServletRequest request,HttpServletResponse response){
        //根据传入的URL，作重定向处理
        String redirectLocation = request.getParameter(AuthConsts.REDIRECT_LOCATION_PARAM);
        if(StringKit.isNotBlank(redirectLocation)){
            //如果地址不是以HTTP或HTTPS开始的，说明是当前ContextPath项下
            if(!AuthConsts.HTTP_PATTERN.matcher(redirectLocation).find()){
                redirectLocation = WebHolder.getServletContext().getContextPath()+redirectLocation;
            }
            HttpKit.sendRedirect(response,redirectLocation);

        }
    }
}
