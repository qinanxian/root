package com.vekai.auth.authorization;


import com.vekai.auth.shiro.ReadWriteType;
import org.apache.shiro.authz.Permission;

public interface ResourcePermission extends Permission{
    ReadWriteType relate(PermissionResource resource);
}
