package com.vekai.crops.customer.handle.invest;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.constant.BizConst;
import com.vekai.crops.customer.entity.CustCapitalStructPO;
import com.vekai.crops.customer.entity.CustInvestPO;
import com.vekai.crops.customer.entity.CustRelationPO;
import com.vekai.crops.customer.entity.CustomerPO;
import com.vekai.crops.customer.service.CustomerService;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InvestInfoHandler extends MapDataOneHandler {

    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    CustomerService customerService;

    @Override
    @Transactional
    public int save(DataForm dataForm, MapObject object) {
        CustRelationPO relationPO = new CustRelationPO();
        CustRelationPO reverseRelationPO = new CustRelationPO();
        CustInvestPO custInvestPO = new CustInvestPO();
        BeanKit.copyProperties(object,custInvestPO);
        int result = 0;
        if (custInvestPO.getRelationId() == null) {
            String custId = object.getValue("custId").strValue();
            String relationCustId = object.getValue("relationCustId").strValue();
            //CUST_RELATION中custId与relationCustId正反存两次
            transferRelationPO(custInvestPO,relationPO,relationCustId,custId);
            transferRelationPO(custInvestPO,reverseRelationPO,custId,relationCustId);
            result += beanCruder.save(relationPO);
            result += beanCruder.save(reverseRelationPO);

            custInvestPO.setRelationId(relationPO.getId());
            result += beanCruder.save(custInvestPO);
            CustCapitalStructPO custCapitalStructPO = new CustCapitalStructPO();
            custCapitalStructPO = transferCustCapitalStructPO(custCapitalStructPO,object,reverseRelationPO);
            result += beanCruder.save(custCapitalStructPO);
        } else {
            result += beanCruder.save(custInvestPO);
            relationPO = beanCruder.selectOneById(CustRelationPO.class, custInvestPO.getRelationId());
            CustRelationPO reverseRelation = getReverseRelation(relationPO);
            CustCapitalStructPO capitalStructPO = beanCruder.selectOneById(CustCapitalStructPO.class, reverseRelation.getId());
            capitalStructPO = transferCustCapitalStructPO(capitalStructPO,object,reverseRelation);
            result += beanCruder.save(capitalStructPO);
        }
        return result;
    }

    private CustRelationPO transferRelationPO(CustInvestPO custInvestPO, CustRelationPO relationPO,String custId,String relationCustId) {
        String investType = custInvestPO.getInvestType();
        String relationShip = customerService.getRelationShip(investType);
        CustomerPO customerPO = beanCruder.selectOneById(CustomerPO.class, custId);
        relationPO.setCustId(relationCustId);
        relationPO.setRelationship(relationShip);
        relationPO.setRelationCustId(custId);
        relationPO.setRelationCustName(customerPO.getCustName());
        relationPO.setRelationCertType(customerPO.getCertType());
        relationPO.setRelationCertId(customerPO.getCertId());
        relationPO.setStatus(BizConst.EFFECT_STATUS_VALID);
        return relationPO;
    }

    private CustCapitalStructPO transferCustCapitalStructPO(CustCapitalStructPO custCapitalStructPO, MapObject object, CustRelationPO relationPO){
        String custId = object.getValue("custId").strValue();
        CustomerPO customerPO = beanCruder.selectOneById(CustomerPO.class, custId);
        if(BizConst.CUSTOMER_TYPE_IND.equals(customerPO)){
            custCapitalStructPO.setStockholderType("20");
        }else{
            custCapitalStructPO.setStockholderType("10");
        }
        custCapitalStructPO.setRelationId(relationPO.getId());
        custCapitalStructPO.setStockholderName(customerPO.getCustName());
        custCapitalStructPO.setStockholderCertType(customerPO.getCertType());
        custCapitalStructPO.setStockholderCertId(customerPO.getCertId());
        custCapitalStructPO.setStockholderCustId(customerPO.getCustId());
        custCapitalStructPO.setInvestmentWay(object.getValue("investType").strValue());
        custCapitalStructPO.setInvestmentCurrency(object.getValue("investCurrency").strValue());
        custCapitalStructPO.setInvestmentRate(object.getValue("investRatio").doubleValue());
        custCapitalStructPO.setStockWarrantCode(object.getValue("warrantNumber").strValue());
        custCapitalStructPO.setIntro(object.getValue("remark").strValue());
        return custCapitalStructPO;
    }

    private CustRelationPO getReverseRelation(CustRelationPO relationPO){
        String sql = "select * from CUST_RELATION where CUST_ID=:custId and RELATION_CUST_ID=:relationCustId";
        String custId = relationPO.getRelationCustId();
        String relationCustId = relationPO.getCustId();
        return beanCruder.selectOne(CustRelationPO.class,sql, MapKit.mapOf("custId",custId,"relationCustId",relationCustId));
    }

}
