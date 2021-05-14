package com.vekai.auth.authorization;


import java.util.List;
import java.util.Set;

/**
 * PermissionResource and Permission share something in common,
 * but to distinguish Resource with Permission,
 * PermissionResource does not extend {@link org.apache.shiro.authz.Permission}
 */
public interface PermissionResource {
    List<Set<String>> getParts();
    PermissionResourceType getPermissionResourceType();
    boolean contains(PermissionResource permissionResource);
}
