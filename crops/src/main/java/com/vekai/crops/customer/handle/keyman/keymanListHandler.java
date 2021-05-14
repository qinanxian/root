package com.vekai.crops.customer.handle.keyman;

import cn.fisok.raw.lang.MapObject;
import com.vekai.crops.constant.BizConst;
import com.vekai.crops.customer.constant.CustomerConst;
import com.vekai.crops.customer.entity.CustRelationPO;
import com.vekai.crops.customer.service.CustomerService;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class keymanListHandler extends BeanDataListHandler<CustRelationPO> {

    @Autowired
    CustomerService customerService;

    @Transactional
    public Integer setKeymanInvalid(DataForm dataForm, MapObject param) {
        String id = param.getValue("id").strValue();
        CustRelationPO keyman = customerService.getCustRelation(id);
        if (BizConst.EFFECT_STATUS_INVALID.equals(keyman.getStatus())) {
            throw new BizException(MessageHolder.getMessage("","customer.keyman.has.invalid"));
        }
        keyman.setStatus(BizConst.EFFECT_STATUS_INVALID);
        return beanCruder.save(keyman);
    }


    @Transactional
    public Integer setKeymanValid(DataForm dataForm, MapObject param) {

        String id = param.getValue("id").strValue();
        String custId = param.getValue("custId").strValue();
        String relationship = param.getValue("relationship").strValue();
        if(CustomerConst.CUSTOMER_RELATIONSHIP_LEGAL_REPRESENTATIVE.equals(relationship)){
        	int count = getKeymanCount(custId,relationship);
        	if(count > 0){
        		throw new BizException(MessageHolder.getMessage(
        		        "","customer.relation.info.has.exist.legal.representative"));
        	}
        }
        CustRelationPO keyman = customerService.getCustRelation(id);
        if (BizConst.EFFECT_STATUS_VALID.equals(keyman.getStatus())) {
            throw new BizException(MessageHolder.getMessage("","customer.keyman.has.valid"));
        }
        keyman.setStatus(BizConst.EFFECT_STATUS_VALID);
        return beanCruder.save(keyman);
    }

    private int getKeymanCount(String custId,String relationship) {
    	Map<String,String> param = MapKit.mapOf("custId",custId,"relationship",relationship);
    	int count = beanCruder.selectCount("SELECT * FROM CUST_RELATION where CUST_ID =:custId and RELATIONSHIP=:relationship and status='VALID'",param);
        return count;
    }
}
