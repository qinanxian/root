package com.vekai.auth.shiro;


import com.vekai.auth.AuthConsts;
import com.vekai.auth.authorization.ResourcePermission;
import com.vekai.auth.authorization.WildcardResource;
import com.vekai.auth.authorization.PermissionResource;
import com.vekai.auth.authorization.PermissionResourceType;
import org.apache.shiro.authz.Permission;

import java.util.*;

public class CustomWildcardPermission implements ResourcePermission {
    protected final ReadWriteType readWriteType;
    protected final String rawPermissionExpr;
    protected final PermissionResource permissionResource;

    // should be like r@menu:menuPermissionCode;
    public CustomWildcardPermission(String fullPermissionExpr) {
        this.rawPermissionExpr = fullPermissionExpr;

        // parse expression
        String tempExpr = rawPermissionExpr;
        int index = tempExpr.indexOf(AuthConsts.RW_DIVIDER_TOKEN);
        if (index == -1)
            readWriteType = ReadWriteType.All;
        else {
            String rw = tempExpr.substring(0, index).trim();
            readWriteType = ReadWriteType.from(rw);
        }
        if (readWriteType == ReadWriteType.None)
            throw new IllegalArgumentException("ReadWriteType error");
        tempExpr = tempExpr.substring(index + 1);
        index = tempExpr.indexOf(AuthConsts.SCHEME_DIVIDER_TOKEN);
        if (index == -1)
            throw new IllegalArgumentException("PermissionResourceType must exist");
        String type = tempExpr.substring(0, index).trim();
        PermissionResourceType permissionResourceType = PermissionResourceType.from(type);
        tempExpr = tempExpr.substring(index + 1);
        permissionResource = new WildcardResource(permissionResourceType, tempExpr);
    }

    private CustomWildcardPermission(ReadWriteType readWriteType, PermissionResourceType permissionResourceType, String resourceCode) {
        this.readWriteType = readWriteType;
        this.permissionResource = new WildcardResource(permissionResourceType, resourceCode);
        this.rawPermissionExpr = "";
    }

    public ReadWriteType getReadWriteType() {
        return readWriteType;
    }

    public String getRawPermissionExpr() {
        return rawPermissionExpr;
    }

    public PermissionResource getPermissionResource() {
        return permissionResource;
    }

    @Override
    public boolean implies(Permission p) {
        // By default only supports comparisons with other CustomWildcardPermission
        if (!(p instanceof CustomWildcardPermission)) {
            return false;
        }

        CustomWildcardPermission wp = (CustomWildcardPermission) p;
        return this.readWriteType.implies(wp.readWriteType) &&
                this.permissionResource.contains(wp.permissionResource);

    }

    @Override
    public ReadWriteType relate(PermissionResource otherResource) {
        if (permissionResource.getPermissionResourceType() != otherResource.getPermissionResourceType())
            return ReadWriteType.None;
        if (permissionResource.contains(otherResource))
            return readWriteType;
        return ReadWriteType.None;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(readWriteType.getCanonicalName()).append(AuthConsts.RW_DIVIDER_TOKEN)
                .append(permissionResource.toString());
        return buffer.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CustomWildcardPermission) {
            CustomWildcardPermission wp = (CustomWildcardPermission) o;
            return readWriteType == wp.readWriteType &&
                    permissionResource.equals(wp.permissionResource);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionResource, readWriteType);
    }

    public static String constructPermitStr(String rw, PermissionResourceType permissionResourceType, String code) {
        if (null == code)
            throw new IllegalArgumentException("argument code canot be null");
        StringBuilder buffer = new StringBuilder(ReadWriteType.from(rw).getCanonicalName()).append(AuthConsts.RW_DIVIDER_TOKEN)
                .append(permissionResourceType.getScheme()).append(AuthConsts.SCHEME_DIVIDER_TOKEN);
        if (code.startsWith(AuthConsts.ROOT_PART_TOKEN))
            buffer.append(code);
        else
            buffer.append(AuthConsts.ROOT_PART_TOKEN).append(code);
        return buffer.toString();
    }

    public static CustomWildcardPermission constructPermit
            (ReadWriteType readWriteType, PermissionResourceType permissionResourceType, String resourceCode) {
        return new CustomWildcardPermission(readWriteType, permissionResourceType, resourceCode);
    }
}
