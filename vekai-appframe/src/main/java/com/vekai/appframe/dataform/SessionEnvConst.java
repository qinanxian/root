package com.vekai.appframe.dataform;

import cn.fisok.raw.kit.ListKit;

import java.util.List;

/**
 * 会话变量常量名
 */
public interface SessionEnvConst {

    /**
     * 当前用户ID
     */
    String CUR_USER = "CUR_USER";
    /**
     * 当前用户所在机构ID
     */
    String CUR_USER_ORG = "CUR_USER_ORG";
    /**
     * 当前用户所有任职机构ID列表
     */
    String CUR_USER_OFFICE_ORG_LIST = "CUR_USER_OFFICE_ORG_LIST";
    /**
     * 当前用户所机构及其子机构ID列表
     */
    String CUR_USER_SUBORG_LIST = "CUR_USER_SUBORG_LIST";
    /**
     * 当前用户所有任职机构ID列表
     */
    String CUR_USER_OFFICE_SUBORG_LIST = "CUR_USER_OFFICE_SUBORG_LIST";

    /**
     * 空值默认值
     */
    String NULL_HOLDER = "_NULL_";
    /**
     * 空列表默认值
     */
    List<String> NULL_LIST_HOLDER = ListKit.listOf(NULL_HOLDER);
}
