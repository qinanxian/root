package com.vekai.appframe.policy.service;

import com.vekai.appframe.policy.model.PolicyDefinition;

import java.util.Map;

/**
 * 产品政策对象应用于业务对象
 * @param <B> 产品业务对象
 * @param <P> 产品参数对象
 */
public interface PolicyApplyTo<B,P> {
    void applyTo(PolicyDefinition policyDefinition, B object, P params);
    void setProperties(Map<String,Object> properties);
}
