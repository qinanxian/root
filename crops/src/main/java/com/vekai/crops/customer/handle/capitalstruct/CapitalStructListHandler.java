package com.vekai.crops.customer.handle.capitalstruct;

import com.vekai.crops.customer.constant.CustomerConst;
import com.vekai.crops.customer.entity.CustCapitalStructPO;
import com.vekai.crops.customer.entity.CustInvestPO;
import com.vekai.crops.customer.entity.CustRelationPO;
import com.vekai.crops.customer.service.CustomerService;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CapitalStructListHandler extends BeanDataListHandler<CustCapitalStructPO> {

    @Autowired
    CustomerService customerService;

    @Override
    @Transactional
    public Integer delete(DataForm dataForm, List<CustCapitalStructPO> dataList) {
        int result = 0;
        for (CustCapitalStructPO capitalStructPO : dataList) {
            String relationId = capitalStructPO.getRelationId();
            CustRelationPO custRelationPO = customerService.getCustRelation(relationId);

            if (CustomerConst.CUSTOMER_STOCKHOLDER_TYPE_ENT.equals(capitalStructPO.getStockholderType())) {
                String sql = "SELECT * FROM CUST_RELATION where CUST_ID = :custId and RELATION_CUST_ID =:relationCustId";
                CustRelationPO revertRelationPO = beanCruder.selectOne(CustRelationPO.class,sql,
                        MapKit.mapOf("custId",custRelationPO.getRelationCustId(),"relationCustId",custRelationPO.getCustId()));
                CustInvestPO custInvestPO = customerService.getCustInvest(revertRelationPO.getId());
                beanCruder.delete(custInvestPO);
                beanCruder.delete(revertRelationPO);
            }

            result += beanCruder.delete(custRelationPO);
            result += beanCruder.delete(capitalStructPO);
        }
        return result;
    }

}
