package com.vekai.auth.shiro;


public interface SessionTimeoutStrategy {

    /**
     *
     * @return 0 or negative number indicates no handling, positive number indicates effective value
     */
    long getTimeout();
}
