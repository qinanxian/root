package com.vekai.auth.authorization.aop;

import com.vekai.auth.autoconfigure.AuthProperties;
import com.vekai.auth.authorization.AuthorizationSupport;
import com.vekai.auth.authorization.PermissionResourceType;
import com.vekai.base.menu.model.MenuEntry;
import org.apache.shiro.authz.Permission;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Aspect
@Component
public class MenuControllerInterceptor {

    @Pointcut("execution(public * com.vekai.base.menu.controller.MenuController.getUserMenu(..))")
    public void getUserMenu(){}

    @Autowired
    AuthorizationSupport authorizationSupport;



    @Autowired
    AuthProperties authProperties;

    @AfterReturning(pointcut = "getUserMenu()",
    returning = "allMenus")
    public void filterMenu(List<MenuEntry> allMenus) {
        if (!authProperties.isAuthzFilterEnabled()) return;
        if (null == allMenus || allMenus.isEmpty()) return;

        Collection<Permission> permissions =
                authorizationSupport.retrieveCurrentUserPermissionCollection(PermissionResourceType.Menu);
        if (null == permissions || permissions.isEmpty()) {
            allMenus.clear();
            return;
        }

        authorizationSupport.setPermissionResourceRW(permissions, allMenus, PermissionResourceType.Menu, true);
    }
}
