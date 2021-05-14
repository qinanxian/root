package com.vekai.auth.jwt.shiro;

import com.vekai.auth.AuthConsts;
import com.vekai.auth.jwt.JwtGenerator;
import com.vekai.auth.entity.User;
import com.vekai.auth.service.AuthService;
import cn.fisok.raw.kit.JSONKit;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// maybe the class name is not suitable
// there exists some historic reasons and don't want to make too many changes
@Component
public class JwtGenerationAuthenticationListener implements AuthenticationListener {

    @Autowired(required = false)
    JwtGenerator jwtGenerator;

    @Autowired
    AuthService authService;

    @Override
    public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
        User user = info.getPrincipals().oneByType(User.class);
        if (null == user.getOrg())
            user.setOrg(authService.getOrg(user.getOrgId()));
        Subject subject = SecurityUtils.getSubject();
        if (subject instanceof WebSubject) {
            WebSubject webSubject = (WebSubject) subject;
            webSubject.getServletRequest().setAttribute(AuthConsts.SESSION_USER, user);
            if (null != jwtGenerator) {
                String jwt = jwtGenerator.generate(user.getId(), -1, JSONKit.toJsonString(user));
                webSubject.getServletRequest().setAttribute(AuthConsts.JWT_ATTRIBUTE, jwt);
            }
        }
    }


    @Override
    public void onFailure(AuthenticationToken token, AuthenticationException ae) {

    }

    @Override
    public void onLogout(PrincipalCollection principals) {

    }
}
