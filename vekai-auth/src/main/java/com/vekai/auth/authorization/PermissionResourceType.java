package com.vekai.auth.authorization;


import com.vekai.auth.AuthConsts;

import java.util.HashMap;
import java.util.Map;

public enum PermissionResourceType {
    Menu("menu"), DataForm("dataform"), Rest("rest"), Ud("ud"), All(AuthConsts.WILDCARD_TOKEN);
    private static final Map<String, PermissionResourceType> map = new HashMap<>(PermissionResourceType.values().length, 1);

    static {
        for (PermissionResourceType permissionResourceType : PermissionResourceType.values())
            map.put(permissionResourceType.scheme, permissionResourceType);
    }

    private final String scheme;

    PermissionResourceType(String scheme) {
        this.scheme = scheme;
    }

    public String getScheme() {
        return scheme;
    }

    public static PermissionResourceType from(String str) {
        if (null == str || "".equals(str) || AuthConsts.WILDCARD_TOKEN.equals(str))
            return PermissionResourceType.All;
        PermissionResourceType permissionResourceType = map.get(str);
        if (null == permissionResourceType)
            throw new IllegalArgumentException(str + " is not valid PermissionResourceType");
        return permissionResourceType;
    }
}
