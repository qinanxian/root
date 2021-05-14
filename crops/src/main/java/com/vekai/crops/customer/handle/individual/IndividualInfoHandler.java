package com.vekai.crops.customer.handle.individual;

import com.vekai.crops.customer.entity.CustIndividualPO;
import com.vekai.crops.customer.entity.CustomerPO;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by luyu on 2018/1/19.
 */
@Component
public class IndividualInfoHandler extends MapDataOneHandler {

    @Autowired
    BeanCruder accessor;

    @Override
    @Transactional
    public int save(DataForm dataForm, MapObject object) {
        String custId = object.get("custId").toString();
        CustomerPO customer = accessor.selectOne(CustomerPO.class, "select * from CUST_BASE WHERE CUST_ID=:custId", MapKit.mapOf("custId", custId));
        customer.setCertId(object.get("certId").toString());
        customer.setCertType(object.get("certType").toString());
        BeanKit.copyProperties(object, customer);
        CustIndividualPO individual = new CustIndividualPO();
        BeanKit.copyProperties(object, individual);
        Integer result = accessor.save(customer);
        result += accessor.save(individual);
        return result;
    }
}
