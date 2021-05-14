package com.vekai.crops.customer.handle.capitalstruct;

import cn.fisok.raw.lang.MapObject;
import com.vekai.crops.constant.BizConst;
import com.vekai.crops.customer.constant.CustomerConst;
import com.vekai.crops.customer.entity.*;
import com.vekai.crops.customer.service.CustomerService;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CapitalStructInfoHandler extends MapDataOneHandler {

    @Autowired
    BeanCruder beanCruder;
    @Autowired
    CustomerService customerService;

    @Override
    @Transactional
    public int save(DataForm dataForm, MapObject object) {
        int result = 0;
        String relationId = object.getValue("relationId").strValue();
        String custId = object.getValue("custId").strValue();
        String stockholderCustId = object.getValue("stockholderCustId").strValue();
        CustCapitalStructPO capitalStructPO = this.getCapitalStructPO(relationId);
        BeanKit.copyProperties(object,capitalStructPO);
        CustRelationPO entRelationPO = null;
        if (StringKit.isBlank(relationId)) {
            if (StringKit.isBlank(stockholderCustId)) {
                CustomerPO customerPO = this.createCustomer(object);
                object.put("stockholderCustId",customerPO.getCustId());
            }
            entRelationPO = this.createEntRelationPO(capitalStructPO,object);
            capitalStructPO.setRelationId(entRelationPO.getId());
            result += beanCruder.save(capitalStructPO);
            this.createAnotherRelationPO(custId,capitalStructPO);
        } else {
            result += beanCruder.save(capitalStructPO);
            entRelationPO = customerService.getCustRelation(capitalStructPO.getRelationId());
            transferProperty(capitalStructPO,entRelationPO);
            result += beanCruder.save(entRelationPO);
            this.updateInvestInfo(capitalStructPO,custId,stockholderCustId);
        }
        return result;
    }

    private void updateInvestInfo(CustCapitalStructPO capitalStructPO, String custId, String stockholderCustId) {
        if (CustomerConst.CUSTOMER_STOCKHOLDER_TYPE_ENT.equals(capitalStructPO.getStockholderType())) {
            String sql = "SELECT * FROM CUST_RELATION WHERE CUST_ID=:stockholderCustId AND RELATION_CUST_ID=:custId";
            CustRelationPO relationPO = beanCruder.selectOne(CustRelationPO.class,sql,
                    MapKit.mapOf("stockholderCustId",stockholderCustId,"custId",custId));
            String investSql = "SELECT * FROM CUST_INVEST WHERE RELATION_ID = :relationId";
            CustInvestPO investPO = beanCruder.selectOne(CustInvestPO.class,investSql,"relationId",relationPO.getId());
            this.transferInvestPO(capitalStructPO,investPO);
            beanCruder.save(investPO);
        }
    }

    private void transferInvestPO(CustCapitalStructPO capitalStructPO, CustInvestPO custInvestPO) {
        custInvestPO.setInvestRatio(capitalStructPO.getInvestmentRate());
        custInvestPO.setInvestCurrency(capitalStructPO.getInvestmentCurrency());
        custInvestPO.setInvestType(capitalStructPO.getInvestmentWay());
        custInvestPO.setWarrantNumber(capitalStructPO.getStockWarrantCode());
    }

    private CustomerPO createCustomer(MapObject object) {
        String stockholderType = object.getValue("stockholderType").strValue();
        CustomerPO customerPO = new CustomerPO();
        customerPO.setCertType(object.getValue("stockholderCertType").strValue());
        customerPO.setCertId(object.getValue("stockholderCertId").strValue());
        customerPO.setCustName(object.getValue("stockholderName").strValue());
        if (CustomerConst.CUSTOMER_STOCKHOLDER_TYPE_ENT.equals(stockholderType)) {
            customerPO.setCustType(BizConst.CUSTOMER_TYPE_ENT);
            beanCruder.save(customerPO);
            CustEnterprisePO custEnterprisePO = new CustEnterprisePO();
            custEnterprisePO.setCustName(customerPO.getCustName());
            custEnterprisePO.setCustId(customerPO.getCustId());
            beanCruder.save(custEnterprisePO);
        } else {
            customerPO.setCustType(BizConst.CUSTOMER_TYPE_IND);
            beanCruder.save(customerPO);
            CustIndividualPO individualPO = new CustIndividualPO();
            individualPO.setCertId(customerPO.getCertId());
            individualPO.setChnName(customerPO.getCustName());
            individualPO.setCustId(customerPO.getCustId());
            beanCruder.save(individualPO);
        }
        return customerPO;
    }

    private CustRelationPO createAnotherRelationPO(String custId, CustCapitalStructPO capitalStructPO) {
        CustRelationPO anotherRelationPO = new CustRelationPO();
        anotherRelationPO.setCustId(capitalStructPO.getStockholderCustId());
        anotherRelationPO.setStatus(BizConst.EFFECT_STATUS_VALID);
        anotherRelationPO.setRelationCustId(custId);
        CustomerPO customerPO = customerService.getCustomerById(custId);
        anotherRelationPO.setRelationCertType(customerPO.getCertType());
        anotherRelationPO.setRelationCertId(customerPO.getCertId());
        anotherRelationPO.setRelationCustName(customerPO.getCustName());

        String relationShip = customerService.getRelationShip(capitalStructPO.getInvestmentWay());
        anotherRelationPO.setRelationship(relationShip);
        beanCruder.save(anotherRelationPO);

        if (CustomerConst.CUSTOMER_STOCKHOLDER_TYPE_ENT.equals(capitalStructPO.getStockholderType())) {
            CustInvestPO custInvestPO = new CustInvestPO();
            this.transferInvestPO(capitalStructPO,custInvestPO);
            custInvestPO.setRelationId(anotherRelationPO.getId());
            beanCruder.save(custInvestPO);
        }
        return anotherRelationPO;
    }

    private CustRelationPO createEntRelationPO(CustCapitalStructPO capitalStructPO, MapObject object) {
        CustRelationPO entRelationPO = new CustRelationPO();
        BeanKit.copyProperties(object,entRelationPO);
        transferProperty(capitalStructPO,entRelationPO);
        entRelationPO.setStatus(BizConst.EFFECT_STATUS_VALID);
        entRelationPO.setRelationCustId(capitalStructPO.getStockholderCustId());
        beanCruder.save(entRelationPO);
        return entRelationPO;
    }

    private CustCapitalStructPO getCapitalStructPO(String relationId) {
        CustCapitalStructPO capitalStructPO = null;
        if (!StringKit.isBlank(relationId)) {
            capitalStructPO = customerService.getCapitalStruct(relationId);
        } else {
            capitalStructPO = new CustCapitalStructPO();
        }
        return capitalStructPO;
    }

    private void transferProperty(CustCapitalStructPO capitalStructPO, CustRelationPO relationPO) {
        String investmentWay = capitalStructPO.getInvestmentWay();
        String relationShip = customerService.getRelationShip(investmentWay);
        relationPO.setRelationship(relationShip);
        relationPO.setRelationCustName(capitalStructPO.getStockholderName());
        relationPO.setRelationCertType(capitalStructPO.getStockholderCertType());
        relationPO.setRelationCertId(capitalStructPO.getStockholderCertId());
    }
}
