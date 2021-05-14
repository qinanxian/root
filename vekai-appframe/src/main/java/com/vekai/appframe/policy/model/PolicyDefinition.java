package com.vekai.appframe.policy.model;

import com.vekai.appframe.policy.entity.PolcDefinitionEntity;
import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.ValueObject;

import javax.persistence.Transient;
import java.util.List;

public class PolicyDefinition extends PolcDefinitionEntity {
    @Transient
    private List<PolicyParam> policyParams = null;

    public List<PolicyParam> getPolicyParams() {
        return policyParams;
    }

    public void setPolicyParams(List<PolicyParam> policyParams) {
        this.policyParams = policyParams;
    }

    public PolicyParam lookupPolicyParam(String paramCode){
        return lookupParam(paramCode);
    }

    public PolicyParam lookupParam(String paramCode){
        List<PolicyParam> paramList = getPolicyParams();
        for(PolicyParam param : paramList){
            if(param.getParamCode().equals(paramCode))return param;
        }
        return null;
    }

    public ValueObject lookupParamValue(String paramCode,boolean strict){
        PolicyParam param = lookupParam(paramCode);
        if( param == null && strict){   //如果使用严格模式并且参数对象不存在的时候则抛出异常
            throw new BizException("产品代码{0}不存在参数{1}对象",getPolicyCode(),paramCode);
        }
        return ValueObject.valueOf(param.getExpressionValue());
    }

    public ValueObject lookupParamValue(String paramCode){
        return lookupParamValue(paramCode,false);
    }
}
