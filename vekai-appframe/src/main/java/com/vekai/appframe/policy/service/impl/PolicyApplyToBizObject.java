package com.vekai.appframe.policy.service.impl;

import com.vekai.appframe.policy.model.PolicyDefinition;
import com.vekai.appframe.policy.model.PolicyParam;
import com.vekai.appframe.policy.service.PolicyApplyTo;
import com.vekai.appframe.policy.service.PolicyService;
import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;

import java.util.List;
import java.util.Map;

/**
 * 产品政策应用到业务对象
 */
public class PolicyApplyToBizObject implements PolicyApplyTo<Object, Map<String,Object>> {
    private Map<String,Object> properties = MapKit.newHashMap();

    public void applyTo(PolicyDefinition policyDefinition, Object object, Map<String, Object> params) {
        PolicyService policyService = ApplicationContextHolder.getBean(PolicyService.class);
        //参数对象处理
        Map<String,Object> parseMap = MapKit.newHashMap();
        parseMap.putAll(params);
        parseMap.putAll(BeanKit.bean2Map(object));
        //解析参数值
        policyService.parse(policyDefinition,params);

        //设置参数值
        List<PolicyParam> paramList = policyDefinition.getPolicyParams();
        paramList.forEach(policyParam->{
            String paramCode = policyParam.getParamCode();
            String propertyName = StringKit.nvl(properties.get(paramCode),paramCode);
            Object value = policyParam.getExpressionValue();
            if(BeanKit.propertyExists(object,propertyName)){
                BeanKit.setPropertyValue(object,propertyName,value);
            }
        });

    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}
