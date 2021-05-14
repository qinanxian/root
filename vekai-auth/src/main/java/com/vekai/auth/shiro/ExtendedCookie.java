package com.vekai.auth.shiro;


import cn.fisok.raw.kit.StringKit;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// even not cookie not enable by configuration, this class(named cookie -_-!) is still the best to customize shiro
public class ExtendedCookie extends SimpleCookie{

    public static final String DEFAULT_CUSTOM_HEADER_NAME = "X-SESSION-TOKEN";
    protected volatile boolean cookieEnable = false;

    public ExtendedCookie() {
        super();
    }

    public ExtendedCookie(String name) {
        super(name);
    }

    public ExtendedCookie(Cookie cookie) {
        super(cookie);
        if (cookie instanceof ExtendedCookie) {
            ExtendedCookie that = (ExtendedCookie)cookie;
            this.cookieEnable = that.cookieEnable;
        }
    }


    public boolean isCookieEnable() {
        return cookieEnable;
    }

    public void setCookieEnable(boolean cookieEnable) {
        this.cookieEnable = cookieEnable;
    }

    @Override
    public void saveTo(HttpServletRequest request, HttpServletResponse response) {
        if (cookieEnable)
            super.saveTo(request, response);
        response.addHeader(DEFAULT_CUSTOM_HEADER_NAME, getValue());
    }

    @Override
    public void removeFrom(HttpServletRequest request, HttpServletResponse response) {
        if (cookieEnable)
            super.removeFrom(request, response);
        response.addHeader(DEFAULT_CUSTOM_HEADER_NAME, DELETED_COOKIE_VALUE);
    }

    @Override
    public String readValue(HttpServletRequest request, HttpServletResponse response) {
        String resultValue = null;
        if (cookieEnable)
            resultValue = super.readValue(request, response);
        if (!StringKit.isEmpty(resultValue))
            return resultValue;
        resultValue = request.getHeader(DEFAULT_CUSTOM_HEADER_NAME);
        if (!StringKit.isEmpty(resultValue))
            return resultValue;
        return request.getParameter(DEFAULT_CUSTOM_HEADER_NAME);
    }
}
