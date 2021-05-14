package com.vekai.auth.shiro;


import cn.fisok.web.kit.HttpKit;
import com.vekai.auth.AuthConsts;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomFormAuthenticationFilter extends FormAuthenticationFilter{


    private final static Logger LOGGER = LoggerFactory.getLogger(CustomFormAuthenticationFilter.class);

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        boolean isAjax = HttpKit.isAjaxRequest(httpRequest);
        if (!isAjax)
            return super.onLoginSuccess(token, subject, request, response);
        else {
            // 由对应的controller处理
            return true;
        }
    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpMethod httpMethod = HttpMethod.valueOf(httpRequest.getMethod());
        if (HttpMethod.POST == httpMethod && isLoginRequest(request, response)) {
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated())
                subject.logout();
        }
        return super.onPreHandle(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) (request);
        HttpMethod httpMethod = HttpMethod.valueOf(httpRequest.getMethod());
        if (isLoginRequest(request, response) ) //|| httpMethod == HttpMethod.GET
            return super.onAccessDenied(request, response);

        if (httpMethod == HttpMethod.OPTIONS)
            return true;


        HttpServletResponse httpResponse = (HttpServletResponse) (response);
        httpResponse.setStatus(AuthConsts.AUTH_FAIL_STATUS_CODE);
        httpResponse.addHeader(AuthConsts.AUTH_FAIL_HEADER, getAuthFailReason());
        request.getRequestDispatcher(AuthConsts.NOP_HANDLER).forward(request, response);
        return false;
    }

    protected String getAuthFailReason() {
        return AuthConsts.SESSION_INVALID;
    }

}
