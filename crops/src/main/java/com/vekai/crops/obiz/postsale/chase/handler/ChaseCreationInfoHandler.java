package com.vekai.crops.obiz.postsale.chase.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.crops.constant.BizConst;
import com.vekai.crops.constant.ObjectType;
import com.vekai.crops.customer.entity.CustIndividualPO;
import com.vekai.crops.customer.entity.CustomerPO;
import com.vekai.crops.customer.service.CustomerService;
import com.vekai.crops.obiz.contract.entity.ObizContract;
import com.vekai.crops.obiz.contract.service.ContractService;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.raw.lang.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChaseCreationInfoHandler extends MapDataOneHandler {

    @Autowired
    ContractService contractService;
    @Autowired
    CustomerService customerService;

    @Override
    public int save(DataForm dataForm, MapObject object) {
        String objectType = object.getValue("objectType").strValue();
        String objectId = object.getValue("objectId").strValue();
        ValidateKit.isTrue(ObjectType.BIZ_CONTRACT==ObjectType.valueOf(objectType),"添加催收对象出错，OBJECT_TYPE不等于{0}",ObjectType.BIZ_CONTRACT);

        ObizContract contract = contractService.queryContract(objectId);
        ValidateKit.notNull(contract,"添加催收对象出错，合同{0}不存在",objectId);
        CustomerPO customer = customerService.getCustomer(contract.getCustId());
        ValidateKit.notNull(customer,"添加催收对象出错，客户{0}不存在",contract.getCustId());
        ValidateKit.notBlank(customer.getCustType(),"添加催收对象出错，客户{0}的客户类型为空",contract.getCustId());
        String custId = customer.getCustId();
        String custType = customer.getCustType();

        if(BizConst.CUSTOMER_TYPE_ENT.equals(custType)){

        }else if(BizConst.CUSTOMER_TYPE_IND.equals(custType)){
            CustIndividualPO custInd = customerService.getIndividualByCustId(custId);
            ValidateKit.notNull(customer,"添加催收对象出错，客户{0}的详情信息不存在",custInd);

            object.put("mobilePhone",custInd.getCellPhoneNo());
            object.put("email",custInd.getEmail());
            object.put("address",custInd.getContactAddress());
        }else{
            throw new BizException("添加催收对象出错，客户{0}的客户类型:{1}不合法",contract.getCustId(),customer.getCustType());
        }

        object.put("certType",customer.getCertType());
        object.put("certId",customer.getCertId());


        return super.save(dataForm, object);
    }
}
