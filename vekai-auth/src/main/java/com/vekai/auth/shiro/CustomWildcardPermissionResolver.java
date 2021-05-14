package com.vekai.auth.shiro;


import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;


/**
 * for performance consideration, no implementation for
 * {@link org.apache.shiro.authz.permission.RolePermissionResolver}
 * roles are turned into permission string when {@link DatabaseRealm#doGetAuthorizationInfo}
 */
public class CustomWildcardPermissionResolver implements PermissionResolver{
    @Override
    public Permission resolvePermission(String permissionString) {
        return new CustomWildcardPermission(permissionString);
    }
}
