package com.vekai.showcase.policy.handler;

import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.lang.FisokException;
import com.vekai.appframe.policy.model.PolicyDefinition;
import com.vekai.appframe.policy.service.PolicyApplyTo;
import com.vekai.appframe.policy.service.PolicyService;
import com.vekai.appframe.policy.service.impl.PolicyApplyToDataFormOne;
import com.vekai.appframe.policy.service.impl.PolicyApplyToDataFormValidate;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.util.DataFormValidateUtils;
import com.vekai.dataform.validator.ValidateRecord;
import com.vekai.dataform.validator.ValidateResult;
import com.vekai.showcase.entity.DemoCredit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by luyu on 2018/9/7.
 */
@Component
public class PolicyDemoInfoHandler extends BeanDataOneHandler<DemoCredit> {


    @Autowired
    PolicyService policyService;


    public DemoCredit query(DataForm dataForm, Map<String, ?> queryParameters) {
        String policyCode = (String)queryParameters.get("prodPolicy");
        PolicyDefinition policy = policyService.getPolicyDefinitionByCode(policyCode);
        if(policy == null)throw new FisokException("产品{0}不存在",policyCode);
        String prodName = policy.getPolicyName();

        DemoCredit demoCredit = super.query(dataForm,queryParameters);
        PolicyApplyTo policyApplyTo = new PolicyApplyToDataFormOne();
        policyApplyTo.applyTo(policy,dataForm,demoCredit);
        return demoCredit;
    }


    public ValidateResult validate(DataForm dataForm, DemoCredit demoCredit) {
        ValidateResult validateResult = DataFormValidateUtils.validateOne(dataForm,this,demoCredit);
        String policyCode = demoCredit.getProdPolicy();
        PolicyApplyToDataFormValidate policyApplyToDataFormValidate = ApplicationContextHolder.getBean(PolicyApplyToDataFormValidate.class);
        Map<String,ValidateRecord> validateRecordMap = policyApplyToDataFormValidate.ValidateRecordWithPolicy(demoCredit,policyCode);
        validateRecordMap.forEach((key,value) -> {
            validateResult.addRecord(key,value);
        });
        return validateResult;
    }

}
