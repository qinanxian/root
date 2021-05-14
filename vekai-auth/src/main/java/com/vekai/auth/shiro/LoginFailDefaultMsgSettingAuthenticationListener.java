package com.vekai.auth.shiro;

import cn.fisok.raw.kit.StringKit;
import com.vekai.auth.AuthConsts;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.web.context.request.RequestContextHolder;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

public class LoginFailDefaultMsgSettingAuthenticationListener implements AuthenticationListener {
   private final static String DEFAULT_MSG = "登录失败";

    @Override
    public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {

    }

    @Override
    public void onFailure(AuthenticationToken token, AuthenticationException ae) {
        String msg = (String) RequestContextHolder.getRequestAttributes().getAttribute(AuthConsts.LOGIN_FAIL_MSG_ATTRIBUTE_NAME, SCOPE_REQUEST);
        if (StringKit.isEmpty(msg))
            RequestContextHolder.getRequestAttributes().setAttribute(AuthConsts.LOGIN_FAIL_MSG_ATTRIBUTE_NAME, DEFAULT_MSG, SCOPE_REQUEST);
    }

    @Override
    public void onLogout(PrincipalCollection principals) {

    }
}
