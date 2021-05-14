package com.vekai.crops.customer.handle.enterprise;

import com.vekai.crops.customer.entity.CustEnterprisePO;
import com.vekai.crops.customer.entity.CustomerPO;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by luyu on 2018/1/19.
 */
@Component
public class EnterpriseInfoHandler extends BeanDataOneHandler<CustEnterprisePO> {

    @Autowired
    BeanCruder accessor;


    @Transactional
    public int save(DataForm dataForm, CustEnterprisePO enterprise) {
        CustomerPO customer = new CustomerPO();
        BeanKit.copyProperties(enterprise, customer);
//        CustEnterprisePO enterprise = new CustEnterprisePO();
//        BeanKit.copyProperties(object, enterprise);
        Integer result = accessor.save(customer);
        result += accessor.save(enterprise);
        return result;
    }
}
