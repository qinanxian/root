package com.vekai.auth.shiro;


import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import java.util.Collection;

// this hack is not good, the better way is to rewrite springshirofilter which is private
// and three relevant class must be rewrite. So here it is
public class CustomWebSecurityManager extends DefaultWebSecurityManager {
    private boolean sessionCreationEnabled = true;

    public CustomWebSecurityManager(Realm singleRealm) {
        super(singleRealm);
    }

    public CustomWebSecurityManager(Collection<Realm> realms) {
        super(realms);
    }

    @Override
    protected SubjectContext createSubjectContext() {
        SubjectContext subjectContext = super.createSubjectContext();
        subjectContext.setSessionCreationEnabled(sessionCreationEnabled);
        return subjectContext;
    }

    @Override
    public Subject createSubject(SubjectContext subjectContext) {
        subjectContext.setSessionCreationEnabled(sessionCreationEnabled);
        return super.createSubject(subjectContext);
    }

    public CustomWebSecurityManager setSessionCreationEnabled(boolean sessionCreationEnabled) {
        this.sessionCreationEnabled = sessionCreationEnabled;
        return this;
    }
}
