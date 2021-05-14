package com.vekai.auth.model;



import com.fasterxml.jackson.annotation.JsonProperty;
import com.vekai.auth.entity.User;
import com.vekai.auth.shiro.ExtendedCookie;

import java.io.Serializable;

public class LoginResponse {
    private static volatile LoginResponse SUCCESS;
    private static volatile LoginResponse FAIL;
    private static volatile LoginResponse PWD_RESET;
    public static volatile transient String logonRealUrl;
    private final LoginResCode code;
    private final String redirectUrl;
    private final String message;
    private final Serializable srSession;
    private final User user;

    public LoginResponse(LoginResCode code, String redirectUrl, String message) {
        this.code = code;
        this.redirectUrl = redirectUrl;
        this.message = message;
        this.srSession = null;
        this.user = null;
    }

    public LoginResponse(LoginResponse that, Serializable srSessionId, User user) {
        this.code = that.code;
        this.message = that.message;
        this.redirectUrl = that.redirectUrl;
        this.srSession = srSessionId;
        this.user = user;
    }

    public LoginResponse(LoginResponse that, String message) {
        this.code = that.code;
        this.redirectUrl = that.redirectUrl;
        this.srSession = that.srSession;
        this.user = that.user;
        this.message = message;
    }

    public LoginResponse(LoginResponse that, LoginResCode code) {
        this.code = code;
        this.redirectUrl = that.redirectUrl;
        this.srSession = that.srSession;
        this.user = that.user;
        this.message = that.message;
    }

    public LoginResCode getCode() {
        return code;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getMessage() {
        return message;
    }

    @JsonProperty(ExtendedCookie.DEFAULT_CUSTOM_HEADER_NAME)
    public Serializable getSrSession() {
        return srSession;
    }

    public User getUser() {
        return user;
    }

    public static LoginResponse getSUCCESS() {
        return SUCCESS;
    }

    public static LoginResponse getPwdReset() {
        return PWD_RESET;
    }

    public static void setSUCCESS(LoginResponse SUCCESS) {
        LoginResponse.SUCCESS = SUCCESS;
    }

    public static LoginResponse getFAIL() {
        return FAIL;
    }

    public static LoginResponse fail(String reason) {
       return new LoginResponse(FAIL, reason);
    }

    public static void setFAIL(LoginResponse FAIL) {
        LoginResponse.FAIL = FAIL;
    }

    public static void setPWDRESET(LoginResponse pwdReset) {
        PWD_RESET = pwdReset;
    }

    public enum LoginResCode {
        Success, Fail, PasswordNeedReset
    }
}
