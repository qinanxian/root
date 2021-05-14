package com.vekai.auth.shiro;

import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

/**
 * 简单包装下，不抛出UnknownSessionException异常
 */
public class FisokWebSecurityManager extends DefaultWebSecurityManager {
    public Subject createSubject(SubjectContext subjectContext) {
        return super.createSubject(subjectContext);
    }
}
