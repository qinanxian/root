package com.vekai.auth.shiro;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.subject.PrincipalCollection;

public class IncorrectCredentialsWithPrincipalException extends IncorrectCredentialsException {
    private final PrincipalCollection principalCollection;

    public IncorrectCredentialsWithPrincipalException(PrincipalCollection principalCollection) {
        this.principalCollection = principalCollection;
    }

    public IncorrectCredentialsWithPrincipalException(PrincipalCollection principalCollection, String msg) {
        super(msg);
        this.principalCollection = principalCollection;
    }

    public IncorrectCredentialsWithPrincipalException(PrincipalCollection principalCollection, String msg, Throwable cause) {
        super(msg, cause);
        this.principalCollection = principalCollection;
    }

    public PrincipalCollection getPrincipalCollection() {
        return principalCollection;
    }
}
