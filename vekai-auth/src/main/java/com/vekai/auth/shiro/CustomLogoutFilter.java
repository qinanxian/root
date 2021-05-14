package com.vekai.auth.shiro;


import cn.fisok.web.kit.HttpKit;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.springframework.http.HttpMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class CustomLogoutFilter extends LogoutFilter{
    public CustomLogoutFilter() {
        setPostOnlyLogout(true);
    }

    @Override
    protected void issueRedirect(ServletRequest request, ServletResponse response, String redirectUrl) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        boolean isAjax = HttpKit.isAjaxRequest(httpRequest);
        if (!isAjax)
            super.issueRedirect(request, response, redirectUrl);
    }

    @Override
    protected boolean onLogoutRequestNotAPost(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpMethod httpMethod = HttpMethod.valueOf(httpRequest.getMethod());
        return HttpMethod.OPTIONS == httpMethod || super.onLogoutRequestNotAPost(request, response);
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        super.preHandle(request, response);
        return true;
    }
}
