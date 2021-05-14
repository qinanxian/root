package com.vekai.auth.shiro;

import com.vekai.auth.entity.User;
import org.apache.shiro.authc.AuthenticationException;


public interface UserAccountVerifier {
    /**
     * the message of AccountException will be sent to front end
     * @param user
     * @throws AuthenticationException
     */
    void verifyAccount(User user) throws AuthenticationException;
}
