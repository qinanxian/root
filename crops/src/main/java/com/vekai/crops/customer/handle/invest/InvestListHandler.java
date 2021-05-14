package com.vekai.crops.customer.handle.invest;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.customer.entity.CustCapitalStructPO;
import com.vekai.crops.customer.entity.CustInvestPO;
import com.vekai.crops.customer.entity.CustRelationPO;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class InvestListHandler extends MapDataListHandler {

    @Autowired
    BeanCruder accessor;

    @Override
    @Transactional
    public Integer delete(DataForm dataForm, List<MapObject> dataList){
        MapObject mapObject = dataList.get(0);
        CustInvestPO custInvestPO = new CustInvestPO();
        BeanKit.copyProperties(mapObject, custInvestPO);
        CustRelationPO custRelationPO = accessor.selectOneById(CustRelationPO.class, custInvestPO.getRelationId());
        BeanKit.copyProperties(mapObject, custRelationPO);
        Integer result = accessor.delete(custInvestPO);
        result += accessor.delete(custRelationPO);
        CustRelationPO reverseRelation = getReverseRelation(custRelationPO);
        CustCapitalStructPO capitalStructPO = accessor.selectOneById(CustCapitalStructPO.class,reverseRelation.getId());
        result += accessor.delete(capitalStructPO);
        result += accessor.delete(reverseRelation);
        return result;
    }

    private CustRelationPO getReverseRelation(CustRelationPO relationPO){
        String sql = "select * from CUST_RELATION where CUST_ID=:custId and RELATION_CUST_ID=:relationCustId";
        String custId = relationPO.getRelationCustId();
        String relationCustId = relationPO.getCustId();
        return accessor.selectOne(CustRelationPO.class,sql, MapKit.mapOf("custId",custId,"relationCustId",relationCustId));
    }
}
