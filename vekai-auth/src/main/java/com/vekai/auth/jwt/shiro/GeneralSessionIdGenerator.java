package com.vekai.auth.jwt.shiro;


import com.vekai.auth.AuthConsts;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.WebSubject;

import java.io.Serializable;

public class GeneralSessionIdGenerator implements SessionIdGenerator{

    private final SessionIdGenerator fallbackSessionIdGenerator = new JavaUuidSessionIdGenerator();

    @Override
    public Serializable generateId(Session session) {
        Subject subject = SecurityUtils.getSubject();
        if (subject instanceof WebSubject) {
            WebSubject webSubject = (WebSubject) subject;
            String id = (String)webSubject.getServletRequest().getAttribute(AuthConsts.JWT_ATTRIBUTE);
            if (id != null)
                // not return id, at present time substring is totally useless, but still substring.
                // if not using jwt, uuid is enough
                return id; //id.substring(id.lastIndexOf(".") + 1);
        }
        return fallbackSessionIdGenerator.generateId(session);
    }
}
