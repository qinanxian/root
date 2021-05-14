package com.vekai.crops.obiz.creditlimit.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.crops.customer.service.CustomerService;
import com.vekai.appframe.policy.model.PolicyDefinition;
import com.vekai.appframe.policy.service.PolicyApplyTo;
import com.vekai.appframe.policy.service.PolicyService;
import com.vekai.appframe.policy.service.impl.PolicyApplyToBizObject;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class NewCreditLimitInfoHandler extends MapDataOneHandler {
    @Autowired
    CustomerService customerService;
    @Autowired
    BeanCruder beanCruder;

    @Transactional
    public int save(DataForm dataForm, MapObject object) {
        int ret = 1;

        String policyId = object.getValue("policyId").strValue();
        //2.处理产品值并应用到进件对象中
        PolicyService policyService = ApplicationContextHolder.getBean(PolicyService.class);
        PolicyDefinition policyDefinition = policyService.getPolicyDefinition(policyId);
        if(policyDefinition == null)throw new BizException("产品{0}不存在",policyId);

        Map<String,Object> properties = MapKit.newHashMap();    //产品字段和业务对象字段的转换映射
        PolicyApplyTo<Object, Map<String,Object>> policyApplyTo = new PolicyApplyToBizObject();
        policyApplyTo.setProperties(properties);
        policyApplyTo.applyTo(policyDefinition,object,MapKit.newHashMap());

        //3.保存额度
        ret = super.save(dataForm,object);

        return ret;
    }


}
