package com.vekai.auth.authorization;

import com.vekai.auth.shiro.CustomWildcardPermission;
import com.vekai.auth.shiro.DatabaseRealm;
import com.vekai.auth.shiro.ReadWriteType;
import com.vekai.base.menu.model.RWSortEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;
import java.util.stream.Collectors;

public class DefaultAuthorizationSupport implements AuthorizationSupport{

    @Autowired
    private DatabaseRealm databaseRealm;

    public Collection<Permission> retrieveCurrentUserPermissionCollection() {
        Subject subject = SecurityUtils.getSubject();
        AuthorizationInfo authorizationInfo = databaseRealm.getAuthorizationInfo(subject.getPrincipals());
        if (null == authorizationInfo) return null;
        if (CollectionUtils.isEmpty(authorizationInfo.getStringPermissions()) &&
                CollectionUtils.isEmpty(authorizationInfo.getRoles())) {
            return Collections.emptyList();
        }
        Collection<Permission> permissions = authorizationInfo.getObjectPermissions();
        if (null == permissions) {
            permissions = new HashSet<>();
            PermissionResolver permissionResolver = databaseRealm.getPermissionResolver();
            RolePermissionResolver rolePermissionResolver = databaseRealm.getRolePermissionResolver();
            Collection<String> stringPs = authorizationInfo.getStringPermissions();
            if (null != stringPs && !stringPs.isEmpty())
                permissions.addAll(stringPs.stream()
                        .map(permissionResolver::resolvePermission)
                        .collect(Collectors.toSet()));
            Collection<String> roles = authorizationInfo.getRoles();
            if (null != roles && !roles.isEmpty())
                permissions.addAll(roles.stream()
                        .map(rolePermissionResolver::resolvePermissionsInRole)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet()));
            if (authorizationInfo instanceof SimpleAuthorizationInfo) {
                ((SimpleAuthorizationInfo) authorizationInfo).setObjectPermissions(new HashSet<>(permissions));
            }
        }
        return permissions;
    }

    @Override
    public Collection<Permission> retrieveCurrentUserPermissionCollection(PermissionResourceType permissionResourceType) {
        Objects.requireNonNull(permissionResourceType, "argument permissionResourceType cannot be null");
        Collection<Permission> permissions = retrieveCurrentUserPermissionCollection();
        if (null == permissions || permissions.isEmpty())
            return permissions;
        return permissions.stream().filter(permission ->
                ((CustomWildcardPermission) permission).getPermissionResource().getPermissionResourceType()
                        == permissionResourceType)
                .collect(Collectors.toSet());
    }

    @Override
    public <T extends RWSortEntity> void setPermissionResourceRW(
            Collection<Permission> permissions, Collection<T> permissionResources,
            PermissionResourceType permissionResourceType, boolean removeIfRWNone) {
        if (null == permissionResources || permissionResources.isEmpty()) return;
        Iterator<T> menuIterator = permissionResources.iterator();
        while (menuIterator.hasNext()) {
            T resource = menuIterator.next();
            WildcardResource wildcardResource = new WildcardResource(permissionResourceType, resource.getSortCode());
            ReadWriteType readWriteType = ReadWriteType.None;
            for(Permission permission : permissions) {
                ResourcePermission resourcePermission = (ResourcePermission)permission;
                readWriteType = readWriteType.merge(resourcePermission.relate(wildcardResource));
                if (readWriteType == ReadWriteType.All)
                    break;
            }
            if (readWriteType == ReadWriteType.None && removeIfRWNone) {
                menuIterator.remove();
            } else
                resource.setRw(readWriteType.getCanonicalName());
        }
    }
}
