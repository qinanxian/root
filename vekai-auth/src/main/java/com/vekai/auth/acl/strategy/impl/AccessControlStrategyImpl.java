package com.vekai.auth.acl.strategy.impl;

import com.vekai.auth.acl.strategy.AccessControlStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认策略
 */
@Component
public class AccessControlStrategyImpl implements AccessControlStrategy {
    public boolean allow(String url, String shortUrl, HttpServletRequest request) {
        return true;
    }
}
