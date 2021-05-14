package com.vekai.appframe.policy.service.impl;

import com.vekai.appframe.constant.AppframeConst;
import com.vekai.appframe.policy.service.PolicyApplyTo;
import com.vekai.appframe.policy.service.PolicyService;
import com.vekai.appframe.policy.model.PolicyDefinition;
import com.vekai.appframe.policy.model.PolicyParam;
import com.vekai.dataform.validator.ValidateRecord;
import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luyu on 2018/9/14.
 */
public class PolicyApplyToDataFormValidate implements PolicyApplyTo<Object, Map<String,Object>> {

    private Map<String,Object> properties = MapKit.newHashMap();


    /**
     * 根据产品政策，对需要保存业务模型进行校验
     * 目前只有针对范围性的值做校验
     * @param bizObject   业务模型对象
     * @param policyCode 新的policyCode
     * @return
     */
    public Map<String, ValidateRecord> ValidateRecordWithPolicy(Object bizObject, String policyCode) {
        Map<String,ValidateRecord> validateRecordMap = new HashMap<>();
        PolicyService policyService = ApplicationContextHolder.getBean(PolicyService.class);
        PolicyDefinition policy = policyService.getPolicyDefinitionByCode(policyCode);
        policy = policyService.parse(policy,MapKit.newEmptyMap());
        List<PolicyParam> policyParams = policy.getPolicyParams();

        policyParams.stream().filter(param -> "Range".equals(param.getDataValueMode())).forEach(param -> {
            String dataType = param.getDataType();
            Object bizObjectValue = policyService.transferDataType(dataType,
                    BeanKit.getPropertyValue(bizObject, StringKit.nvl(this.properties.get(param.getParamCode()),param.getParamCode())));
            ValidateRecord validateRecord = null;
            Object valueMaxExpr = null;
            Object valueMinExpr = null;
            if (AppframeConst.YES_NO_Y.endsWith(param.getIsExpression())) {
                Object expressionValue = param.getExpressionValue();
                if (expressionValue.getClass().isArray()) {
                    int length = Array.getLength(expressionValue);
                    Object[] os = new Object[length];
                    for (int i = 0; i < os.length; i++) {
                        os[i] = Array.get(expressionValue, i);
                    }
                    valueMaxExpr = os[1];
                    valueMinExpr = os[0];
                }

            } else {
                valueMaxExpr = param.getValueMaxExpr();
                valueMinExpr = param.getValueMinExpr();
            }
            if ("Double".equals(dataType)) {
                if ((Double)bizObjectValue > ValueObject.valueOf(valueMaxExpr).doubleValue() || (Double)bizObjectValue < ValueObject.valueOf(valueMinExpr).doubleValue()) {
                    validateRecord = new ValidateRecord();
                    validateRecord.setMandatory(Boolean.TRUE)
                            .setPassed(Boolean.FALSE)
                            .setMessage(MessageFormat.format("{0}参数不满足产品条件",param.getParamName()));
                }
            } else if ("Integer".equals(dataType)) {
                if ((Integer)bizObjectValue > ValueObject.valueOf(valueMaxExpr).intValue() || (Integer)bizObjectValue < ValueObject.valueOf(valueMaxExpr).intValue()) {
                    validateRecord = new ValidateRecord();
                    validateRecord.setMandatory(Boolean.TRUE)
                            .setPassed(Boolean.FALSE)
                            .setMessage(MessageFormat.format("{0}参数不满足产品条件",param.getParamName()));
                }
            }
            if (validateRecord != null) {
                validateRecordMap.put(StringKit.nvl(this.properties.get(param.getParamCode()),param.getParamCode()),validateRecord);
            }
        });
        return validateRecordMap;
    }

    @Override
    public void applyTo(PolicyDefinition policyDefinition, Object object, Map<String, Object> params) {
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
