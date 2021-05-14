package com.vekai.auth.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

public class Md5HashCredentialsMatcher implements CredentialsMatcher {
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        Object holdCredentials = token.getCredentials();    //登录时持有使用的凭证（密码）
        Object storedCredentials = info.getCredentials();    //用于被比较的凭证（一般是数据库中的密码）

        String holdPass = md5Hash(getString(holdCredentials));
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
    private String md5Hash(String text){
        return new Md5Hash(text).toHex();
    }
}
