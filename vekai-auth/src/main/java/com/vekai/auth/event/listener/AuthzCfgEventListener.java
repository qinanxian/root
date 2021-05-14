package com.vekai.auth.event.listener;


import com.vekai.auth.event.AuthzCfgModifiedEvent;
import com.vekai.auth.service.AuthService;
import cn.fisok.raw.kit.StringKit;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.transaction.event.TransactionalEventListener;

public class AuthzCfgEventListener {

    private final String authzCacheName;
    private final String realName;
    private final CacheManager cacheManager;
    private final AuthService authService;

    public AuthzCfgEventListener(String authzCacheName, String realName, CacheManager cacheManager, AuthService authService) {
        this.authzCacheName = authzCacheName;
        this.realName = realName;
        this.cacheManager = cacheManager;
        this.authService = authService;
    }


    @TransactionalEventListener
    public void onRolePermissionModified(AuthzCfgModifiedEvent.RolePermissionModifiedEvent event) {
        if (StringKit.isNoneEmpty(event.getRole())) {
            getCache().clear();
        }
    }

    @TransactionalEventListener
    public void onUserAttachRole(AuthzCfgModifiedEvent.UserAttachRoleEvent event) {
        if (StringKit.isNoneEmpty(event.getUserId()) && null != event.getRoles() && !event.getRoles().isEmpty()) {
            getCache().remove(new SimplePrincipalCollection(authService.getUser(event.getUserId()), realName));
        }
    }

    @TransactionalEventListener
    public void onUserRelieveRole(AuthzCfgModifiedEvent.UserRelieveRoleEvent event) {
        if (StringKit.isNoneEmpty(event.getUserId()) && StringKit.isNoneEmpty(event.getRole())) {
            getCache().remove(new SimplePrincipalCollection(authService.getUser(event.getUserId()), realName));
        }
    }

    private Cache<Object, AuthorizationInfo> getCache() {
        return cacheManager.getCache(authzCacheName);
    }

}
