package com.vekai.auth.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.vekai.auth.session")
public class SessionProperties {
    private String activeSessionsCacheName = "session";

    private boolean sessionIdCookieEnabled = true;
    private long globalSessionTimeout = 1800000L; //10000L;//
    private boolean deleteInvalidSessions = true;
    private boolean sessionValidationSchedulerEnabled = true;
    private long sessionValidationInterval = 1800000L;
    private String sessionIdCookieName = "SRSSION";
    private String sessionIdCookieDomain = "127.0.0.1";

    public String getActiveSessionsCacheName() {
        return activeSessionsCacheName;
    }

    public void setActiveSessionsCacheName(String activeSessionsCacheName) {
        this.activeSessionsCacheName = activeSessionsCacheName;
    }

    public boolean isSessionIdCookieEnabled() {
        return sessionIdCookieEnabled;
    }

    public void setSessionIdCookieEnabled(boolean sessionIdCookieEnabled) {
        this.sessionIdCookieEnabled = sessionIdCookieEnabled;
    }

    public long getGlobalSessionTimeout() {
        return globalSessionTimeout;
    }

    public void setGlobalSessionTimeout(long globalSessionTimeout) {
        this.globalSessionTimeout = globalSessionTimeout;
    }

    public boolean isDeleteInvalidSessions() {
        return deleteInvalidSessions;
    }

    public void setDeleteInvalidSessions(boolean deleteInvalidSessions) {
        this.deleteInvalidSessions = deleteInvalidSessions;
    }

    public boolean isSessionValidationSchedulerEnabled() {
        return sessionValidationSchedulerEnabled;
    }

    public void setSessionValidationSchedulerEnabled(boolean sessionValidationSchedulerEnabled) {
        this.sessionValidationSchedulerEnabled = sessionValidationSchedulerEnabled;
    }

    public long getSessionValidationInterval() {
        return sessionValidationInterval;
    }

    public void setSessionValidationInterval(long sessionValidationInterval) {
        this.sessionValidationInterval = sessionValidationInterval;
    }

    public String getSessionIdCookieName() {
        return sessionIdCookieName;
    }

    public void setSessionIdCookieName(String sessionIdCookieName) {
        this.sessionIdCookieName = sessionIdCookieName;
    }

    public String getSessionIdCookieDomain() {
        return sessionIdCookieDomain;
    }

    public void setSessionIdCookieDomain(String sessionIdCookieDomain) {
        this.sessionIdCookieDomain = sessionIdCookieDomain;
    }
}
