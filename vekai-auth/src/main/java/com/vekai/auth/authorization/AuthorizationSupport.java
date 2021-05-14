package com.vekai.auth.authorization;


import com.vekai.base.menu.model.RWSortEntity;
import org.apache.shiro.authz.Permission;

import java.util.Collection;

public interface AuthorizationSupport {
    Collection<Permission> retrieveCurrentUserPermissionCollection();

    Collection<Permission> retrieveCurrentUserPermissionCollection(PermissionResourceType permissionResourceType);

    <T extends RWSortEntity> void setPermissionResourceRW(
            Collection<Permission> permissions, Collection<T> permissionResources,
            PermissionResourceType permissionResourceType, boolean removeIfRWNone);
}
