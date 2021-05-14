package com.vekai.auth.shiro;


import com.vekai.auth.entity.User;
import cn.fisok.raw.kit.JSONKit;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.subject.WebSubjectContext;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomSubjectFactory extends DefaultWebSubjectFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomSubjectFactory.class);
    private final Cookie cookie;
    private final String realName;
    private final JwtParser jwtParser;

    public CustomSubjectFactory(Cookie cookie, String realName, JwtParser jwtParser) {
        this.cookie = new ExtendedCookie(cookie);
        this.realName = realName;
        this.jwtParser = jwtParser;
    }

    public Subject createSubject(SubjectContext context) {
        if (!(context instanceof WebSubjectContext) || context.isSessionCreationEnabled() ||
                null != context.getAuthenticationInfo()) {
            return super.createSubject(context);
        }
        WebSubjectContext wsc = (WebSubjectContext) context;
        SecurityManager securityManager = wsc.resolveSecurityManager();
        HttpServletRequest request = (HttpServletRequest) wsc.resolveServletRequest();
        HttpServletResponse response = (HttpServletResponse) wsc.resolveServletResponse();
        User user = restoreSessionFromJwt(readJwt(request, response));
        boolean authenticated = null != user;
        String host = wsc.resolveHost();
        PrincipalCollection principals = null;
        if (authenticated)
            principals = new SimplePrincipalCollection(user, realName);
        return new WebDelegatingSubject(principals, authenticated, host, null, false,
                request, response, securityManager);
    }

    private String readJwt(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        return cookie.readValue(httpRequest, httpResponse);
    }

    //todo jwt expire
    private User restoreSessionFromJwt(String strJwt) {
        if (null == strJwt || strJwt.isEmpty() || strJwt.equals("null"))
            return null;
        try {
            Jwt jwt = jwtParser.parse(strJwt);
            Claims claims = (Claims) jwt.getBody();
            String subject = claims.getSubject();
            User user = JSONKit.jsonToBean(subject, User.class);
//            SimpleSession session = new SimpleSession();
//            session.setId(strJwt);
//            session.setAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY, Boolean.TRUE);
            return user;
        } catch (Exception ex) {
            LOGGER.trace(ex.getMessage());
        }
        return null;
    }
}
