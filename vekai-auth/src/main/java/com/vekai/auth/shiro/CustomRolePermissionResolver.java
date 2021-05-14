package com.vekai.auth.shiro;


import com.vekai.auth.entity.Permit;
import com.vekai.auth.service.AuthService;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


public class CustomRolePermissionResolver implements RolePermissionResolver{

    @Autowired
    private AuthService authService;

    @Autowired
    private PermissionResolver permissionResolver;

    @Override
    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        Set<Permit> permits = authService.getRolePermissionByRoleCode(roleString);
        if (null == permits || permits.isEmpty()) return Collections.EMPTY_SET;
        return permits.stream()
                .map(permit -> permissionResolver.resolvePermission(permit.getCode()))
                .collect(Collectors.toSet());
    }
}
