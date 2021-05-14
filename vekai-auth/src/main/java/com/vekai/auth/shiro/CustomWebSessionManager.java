package com.vekai.auth.shiro;


import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

public class CustomWebSessionManager extends DefaultWebSessionManager{

    @Autowired(required = false)
    List<SessionTimeoutStrategy> sessionTimeoutStrategies;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected void storeSessionId(Serializable currentId, HttpServletRequest request, HttpServletResponse response) {
        if (currentId == null) {
            String msg = "sessionId cannot be null when persisting for subsequent requests.";
            throw new IllegalArgumentException(msg);
        }
        Cookie template = getSessionIdCookie();
        Cookie cookie = new ExtendedCookie(template);
        String idString = currentId.toString();
        cookie.setValue(idString);
        cookie.saveTo(request, response);
        LOGGER.trace("Set session ID cookie for session with id {}", idString);
    }

    @Override
    protected void onStart(Session session, SessionContext context) {
        if (!WebUtils.isHttp(context)) {
            LOGGER.debug("SessionContext argument is not HTTP compatible or does not have an HTTP request/response " +
                    "pair. No session ID cookie will be set.");
            return;

        }
        HttpServletRequest request = WebUtils.getHttpRequest(context);
        HttpServletResponse response = WebUtils.getHttpResponse(context);

        if (isSessionIdCookieEnabled()) {
            Serializable sessionId = session.getId();
            storeSessionId(sessionId, request, response);
        } else {
            LOGGER.debug("Session ID cookie is disabled.  No cookie has been set for new session with id {}", session.getId());
        }

        request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, Boolean.TRUE);
    }

    @Override
    protected void applyGlobalSessionTimeout(Session session) {
        session.setTimeout(getTimeout(session));
        onChange(session);
    }

    protected long getTimeout(Session session) {
        if (null == sessionTimeoutStrategies || sessionTimeoutStrategies.isEmpty())
            return getGlobalSessionTimeout();
        for (SessionTimeoutStrategy strategy : sessionTimeoutStrategies) {
            long timeout = strategy.getTimeout();
            if (timeout > 0) return timeout;
        }
        return getGlobalSessionTimeout();
    }
}
