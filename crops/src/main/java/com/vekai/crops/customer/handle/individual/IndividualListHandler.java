package com.vekai.crops.customer.handle.individual;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.constant.BizConst;
import com.vekai.crops.customer.entity.CustPermitPO;
import com.vekai.crops.customer.service.CustomerService;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class IndividualListHandler extends MapDataListHandler {

    @Autowired
    BeanCruder beanCruder;
    @Autowired
    CustomerService customerService;

    @Transactional
    @Override
    public Integer delete(DataForm dataForm, List<MapObject> dataList) {
        Integer result = 0;
        for (MapObject mapObject :dataList) {
            String custId = mapObject.getValue("custId").strValue();
            CustPermitPO custPermit = customerService.getCustPermit(AuthHolder.getUser().getId(),custId);
            //有主办权的，直接删除.没有主办权的，只是删除对该客户的权限
            if (!BizConst.YES_NO_Y.equals(custPermit.getAllowHold())) {
                result += beanCruder.delete(custPermit);
            } else {
                String custSql = "DELETE FROM CUST_BASE WHERE CUST_ID=:custId";
                result += beanCruder.execute(custSql,MapKit.mapOf("custId",custId));
                String entSql = "DELETE FROM CUST_IND WHERE CUST_ID=:custId";
                beanCruder.execute(entSql,MapKit.mapOf("custId",custId));
            }
        }
        return result;
    }
}
