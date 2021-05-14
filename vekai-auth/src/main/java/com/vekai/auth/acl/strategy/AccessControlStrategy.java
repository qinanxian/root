package com.vekai.auth.acl.strategy;

import javax.servlet.http.HttpServletRequest;

/**
 * URL访问控制策略接口
 */
public interface AccessControlStrategy {
    /**
     * 针对每个URL地址的
     * @param url 整个URL路径
     * @param shortUrl URL短路径，如该策略匹配 /auth/admin/org/1001,本策略配置的为拦截/auth/admin,那么shorUrl=/org/1001
     * @return
     */
    boolean allow(String url, String shortUrl, HttpServletRequest request);
}
