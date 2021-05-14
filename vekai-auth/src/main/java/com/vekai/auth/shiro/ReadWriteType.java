package com.vekai.auth.shiro;


import com.vekai.auth.AuthConsts;

import java.util.HashMap;
import java.util.Map;

public enum ReadWriteType {
    None("", 0),
    Read(AuthConsts.READ_TOKEN, 1),
    Write(AuthConsts.WRITE_TOKEN, 2),
    All(AuthConsts.WILDCARD_TOKEN, 1 | 2);

    private final String id;
    private final int rightBit;

    private final static Map<Integer, ReadWriteType> maps = new HashMap<>(ReadWriteType.values().length, 1);

    static {
        for (ReadWriteType readWriteType : ReadWriteType.values()) {
            maps.put(readWriteType.rightBit, readWriteType);
        }
    }

    ReadWriteType(String id, int rightBit) {
        this.id = id;
        this.rightBit = rightBit;
    }

    public String getCanonicalName() {
        return id;
    }

    public boolean implies(ReadWriteType readWriteType) {
        return this == All || this == readWriteType;
    }

    public ReadWriteType merge(ReadWriteType otherRW) {
        if (null == otherRW) return this;
        return maps.get(rightBit | otherRW.rightBit);
    }

    public static boolean isAll(String permissionExpr) {
        int index;
        String str;
        return (index = permissionExpr.indexOf("@")) == -1 ||
                "".equals(str = permissionExpr.substring(0, index).trim()) ||
                AuthConsts.WILDCARD_TOKEN.equals(str);
    }

    public static ReadWriteType from(String rw) {
        if (null == rw || "".equals(rw) || AuthConsts.WILDCARD_TOKEN.equals(rw))
            return ReadWriteType.All;
        boolean isR, isW;
        isR = isW = false;
        String[] rws = rw.split(AuthConsts.SUBPART_DIVIDER_TOKEN);
        for (String s : rws) {
            if (AuthConsts.READ_TOKEN.equals(s))
                isR = true;
            else if (AuthConsts.WRITE_TOKEN.equals(s))
                isW = true;
        }
        if (isR && isW) return ReadWriteType.All;
        if (isR) return ReadWriteType.Read;
        if (isW) return ReadWriteType.Write;
        return ReadWriteType.None;
    }
}
