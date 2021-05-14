package com.vekai.auth.shiro;

import com.vekai.auth.service.AuthService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 明文密码比较器
 */
public class ClearTextCredentialsMatcher implements CredentialsMatcher {

    @Autowired
    private AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        Object holdCredentials = token.getCredentials();    //登录时持有使用的凭证（密码）
        Object storedCredentials = info.getCredentials();    //用于被比较的凭证（一般是数据库中的密码）

        String holdPass = getString(holdCredentials);
        String storedPass = getString(storedCredentials);
        if(holdPass==storedPass)return true;

        return holdPass.equals(storedPass);
    }
    private String getString(Object credential){
        if(credential==null)return "";
        if(credential instanceof String)return (String)credential;
        if(credential.getClass().isArray()){
            return new String((char[])credential);
        }
        return credential.toString();
    }

}
