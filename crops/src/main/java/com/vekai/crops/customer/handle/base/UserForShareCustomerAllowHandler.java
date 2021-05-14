package com.vekai.crops.customer.handle.base;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.constant.BizConst;
import com.vekai.crops.customer.entity.CustPermitPO;
import com.vekai.crops.customer.service.CustomerService;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.lang.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by luyu on 2018/5/22.
 */
@Component
public class UserForShareCustomerAllowHandler extends MapDataOneHandler {

    @Autowired
    CustomerService customerService;
    @Autowired
    BeanCruder beanCruder;

    @Override
    @Transactional
    public int save(DataForm dataForm, MapObject param) {
        Integer result = 0;
        String custId = param.getValue("custId").strValue();
        String userId = param.getValue("userId").strValue();
        String isAdmin = param.getValue("isAdmin").strValue();

        if (!BizConst.YES_NO_Y.equals(isAdmin)) {
            CustPermitPO custPermitPO = customerService.getCustPermit(AuthHolder.getUser().getId(), custId);
            Optional.ofNullable(custPermitPO).orElseThrow(() -> new BizException(MessageHolder.getMessage("","customer.permit.not.exist")));
            validateOwnEnoughAllow(custPermitPO,param);
        }
        validatePermitExisted(custId,userId);
        result += createPermit(param);
        return result;
    }

    private Integer createPermit(MapObject object) {
        CustPermitPO custPermitPO = new CustPermitPO();
        BeanKit.copyProperties(object,custPermitPO);
        initAllow(custPermitPO);
        return beanCruder.save(custPermitPO);
    }

    private void initAllow(CustPermitPO custPermitPO) {
        if (custPermitPO.getAllowHold() == null)
            custPermitPO.setAllowHold(BizConst.YES_NO_N);
        if (custPermitPO.getAllowBusiness() == null)
            custPermitPO.setAllowBusiness(BizConst.YES_NO_N);
        if (custPermitPO.getAllowEdit() == null)
            custPermitPO.setAllowEdit(BizConst.YES_NO_N);
        if (custPermitPO.getAllowView() == null)
            custPermitPO.setAllowView(BizConst.YES_NO_N);
    }

    private void validateOwnEnoughAllow(CustPermitPO custPermitPO, MapObject object) {
        String allowBusiness = object.getValue("allowBusiness").strValue();
        String allowEdit = object.getValue("allowEdit").strValue();
        String allowView = object.getValue("allowView").strValue();
        if (BizConst.YES_NO_Y.equals(allowBusiness)) {
            if (!BizConst.YES_NO_Y.equals(custPermitPO.getAllowBusiness()))
                throw new BizException(MessageHolder.getMessage("","customer.permit.allow.not.enough"));
        }
        if (BizConst.YES_NO_Y.equals(allowEdit)) {
            if (!BizConst.YES_NO_Y.equals(custPermitPO.getAllowEdit()))
                throw new BizException(MessageHolder.getMessage("","customer.permit.allow.not.enough"));
        }
        if (BizConst.YES_NO_Y.equals(allowView)) {
            if (!BizConst.YES_NO_Y.equals(custPermitPO.getAllowView()))
                throw new BizException(MessageHolder.getMessage("","customer.permit.allow.not.enough"));
        }
    }

    private void validatePermitExisted(String custId, String userId) {
        CustPermitPO permit = customerService.getCustPermit(userId, custId);
        if (permit != null)
            throw new BizException(MessageHolder.getMessage("","customer.permit.has.existed"));
    }
}
