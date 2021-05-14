package com.vekai.auth.authorization;

import com.vekai.auth.shiro.CustomWildcardPermission;
import com.vekai.auth.shiro.ReadWriteType;
import com.vekai.base.menu.model.RWSortEntity;
import org.apache.shiro.authz.Permission;

import java.util.Collection;
import java.util.Iterator;

public class MenuAuthorizationSupport extends DefaultAuthorizationSupport{
    @Override
    public <T extends RWSortEntity> void setPermissionResourceRW(
            Collection<Permission> permissions, Collection<T> permissionResources,
            PermissionResourceType permissionResourceType, boolean removeIfRWNone) {
        if (null == permissionResources || permissionResources.isEmpty()) return;
        if (permissionResourceType != PermissionResourceType.Menu)
            throw new IllegalStateException("call incorrect AuthorizationSupport");
        Iterator<T> menuIterator = permissionResources.iterator();
        while (menuIterator.hasNext()) {
            T resource = menuIterator.next();
            WildcardResource wildcardResource = new WildcardResource(permissionResourceType, resource.getSortCode());
            ReadWriteType readWriteType = ReadWriteType.None;
            boolean shouldExistAsParent = false;
            for(Permission permission : permissions) {
                CustomWildcardPermission resourcePermission = (CustomWildcardPermission)permission;
                readWriteType = readWriteType.merge(resourcePermission.relate(wildcardResource));
                if (readWriteType == ReadWriteType.All)
                    break;
                if (!shouldExistAsParent && wildcardResource.contains(resourcePermission.getPermissionResource()))
                    shouldExistAsParent = true;
            }
            if (readWriteType == ReadWriteType.None && removeIfRWNone && !shouldExistAsParent) {
                menuIterator.remove();
            } else
                resource.setRw(readWriteType.getCanonicalName());
        }
    }
}
