package com.vekai.auth.autoconfigure;


import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "com.vekai.auth", ignoreUnknownFields = true)
public class AuthProperties {
    private String authTagName = "auth";
    private Map<String,List<String>> accessControlStrategy = new LinkedHashMap<String,List<String>>();

    private String shiroTagName = "shiro";

    private String userInitPassword = "000000";

    /**
     * 安全审计项的处理
     */
    private AuditProperties audit = new AuditProperties();


    private int cookieMaxAge = 180000;

    @Value("#{ @environment['shiro.successUrl'] ?: '/' }")
    protected String successUrl;

    private String loginStaticPath = "/public/login.html";

    private String[] pwdResetPath;

    //
    private boolean enableHashPwd = true;
    private String pwdHashAlgorithmName = Sha256Hash.ALGORITHM_NAME;
    private boolean hashSalted = false;
    private int hashIterations = 1;
    private boolean storedCredentialsHexEncoded = false;
    //

    //
    private boolean enableJwtReplaceSession = false;
    private String jwtSecretKey = "vekaiinner";
    private String jwtIssuer = "Amarsoft";
    //


    private boolean authzFilterEnabled = true;
    private boolean authorizationCachingEnabled = false;
    private boolean authenticationCachingEnabled = false;

    public String getAuthTagName() {
        return authTagName;
    }

    public void setAuthTagName(String authTagName) {
        this.authTagName = authTagName;
    }

    public Map<String, List<String>> getAccessControlStrategy() {
        return accessControlStrategy;
    }

    public void setAccessControlStrategy(Map<String, List<String>> accessControlStrategy) {
        this.accessControlStrategy = accessControlStrategy;
    }

    public String getShiroTagName() {
        return shiroTagName;
    }

    public String getUserInitPassword() {
        return userInitPassword;
    }

    public void setUserInitPassword(String userInitPassword) {
        this.userInitPassword = userInitPassword;
    }

    public void setShiroTagName(String shiroTagName) {
        this.shiroTagName = shiroTagName;
    }

    public AuditProperties getAudit() {
        return audit;
    }

    public void setAudit(AuditProperties audit) {
        this.audit = audit;
    }

    public int getCookieMaxAge() {
        return cookieMaxAge;
    }

    public void setCookieMaxAge(int cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getLoginStaticPath() {
        return loginStaticPath;
    }

    public void setLoginStaticPath(String loginStaticPath) {
        this.loginStaticPath = loginStaticPath;
    }

    public boolean isEnableHashPwd() {
        return enableHashPwd;
    }

    public void setEnableHashPwd(boolean enableHashPwd) {
        this.enableHashPwd = enableHashPwd;
    }

    public String getPwdHashAlgorithmName() {
        return pwdHashAlgorithmName;
    }

    public void setPwdHashAlgorithmName(String pwdHashAlgorithmName) {
        this.pwdHashAlgorithmName = pwdHashAlgorithmName;
    }

    public boolean isHashSalted() {
        return hashSalted;
    }

    public void setHashSalted(boolean hashSalted) {
        this.hashSalted = hashSalted;
    }

    public int getHashIterations() {
        return hashIterations;
    }

    public boolean isStoredCredentialsHexEncoded() {
        return storedCredentialsHexEncoded;
    }

    public void setStoredCredentialsHexEncoded(boolean storedCredentialsHexEncoded) {
        this.storedCredentialsHexEncoded = storedCredentialsHexEncoded;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public String getJwtSecretKey() {
        return jwtSecretKey;
    }

    public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    public String getJwtIssuer() {
        return jwtIssuer;
    }

    public void setJwtIssuer(String jwtIssuer) {
        this.jwtIssuer = jwtIssuer;
    }

    public boolean isAuthorizationCachingEnabled() {
        return authorizationCachingEnabled;
    }

    public void setAuthorizationCachingEnabled(boolean authorizationCachingEnabled) {
        this.authorizationCachingEnabled = authorizationCachingEnabled;
    }

    public boolean isAuthenticationCachingEnabled() {
        return authenticationCachingEnabled;
    }

    public void setAuthenticationCachingEnabled(boolean authenticationCachingEnabled) {
        this.authenticationCachingEnabled = authenticationCachingEnabled;
    }

    public boolean isAuthzFilterEnabled() {
        return authzFilterEnabled;
    }

    public void setAuthzFilterEnabled(boolean authzFilterEnabled) {
        this.authzFilterEnabled = authzFilterEnabled;
    }

    public boolean isEnableJwtReplaceSession() {
        return enableJwtReplaceSession;
    }

    public void setEnableJwtReplaceSession(boolean enableJwtReplaceSession) {
        this.enableJwtReplaceSession = enableJwtReplaceSession;
    }

    public String[] getPwdResetPath() {
        return pwdResetPath;
    }

    public void setPwdResetPath(String[] pwdResetPath) {
        this.pwdResetPath = pwdResetPath;
    }
}
