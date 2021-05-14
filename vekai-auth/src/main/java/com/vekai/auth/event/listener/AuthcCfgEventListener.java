package com.vekai.auth.event.listener;

import com.vekai.auth.event.AuthcCfgModifiedEvent;
import cn.fisok.raw.kit.StringKit;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.transaction.event.TransactionalEventListener;

public class AuthcCfgEventListener {
    private final String authcCacheName;
    private final CacheManager cacheManager;
    public AuthcCfgEventListener(String authcCacheName, CacheManager cacheManager) {
        this.authcCacheName = authcCacheName;
        this.cacheManager = cacheManager;
    }

    @TransactionalEventListener
    public void onUserPwdModifed(AuthcCfgModifiedEvent.UserPwdModifiedEvent event) {
        if (StringKit.isEmpty(event.getUserCode())) {
            getCache().clear();
        } else {
            getCache().remove(event.getUserCode());
        }
    }

    private Cache<Object, AuthorizationInfo> getCache() {
        return cacheManager.getCache(authcCacheName);
    }

}
