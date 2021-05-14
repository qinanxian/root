package com.vekai.crops.customer.handle.extra;

import com.vekai.crops.customer.entity.CustomerPO;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.lang.MapObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomerListForFundManagementHandler extends BeanDataListHandler<CustomerPO> {

    @Transactional
    public void applyForFundInvestor(DataForm dataForm, MapObject mapObject) {
    }

}
