package com.vekai.crops.customer.handle.base;

import cn.fisok.raw.lang.MapObject;
import com.vekai.crops.constant.BizConst;
import com.vekai.crops.customer.entity.CustPermitPO;
import com.vekai.crops.customer.service.CustomerService;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by luyu on 2018/5/23.
 */
@Component
public class HoldAllowSelectUserListHandler extends MapDataListHandler {

    @Autowired
    CustomerService customerService;
    @Autowired
    BeanCruder beanCruder;

    @Transactional
    public Integer turnOver(DataForm dataform, MapObject param) {
        Integer result = 0;
        String custId = param.getValue("custId").strValue();
        String userId = param.getValue("id").strValue();
        String isAdmin = param.getValue("isAdmin").strValue();

        if (BizConst.YES_NO_Y.equals(isAdmin)) {
            String sql = "DELETE FROM CUST_PERMIT WHERE CUST_ID=:custId AND ALLOW_HOLD=:allowHold";
            result += beanCruder.execute(sql, MapKit.mapOf("custId",custId,"allowHold","Y"));
        } else {
            CustPermitPO ownPermit  = customerService.getCustPermit(AuthHolder.getUser().getId(),custId);
            Optional.ofNullable(ownPermit).orElseThrow(() -> new BizException(MessageHolder.getMessage("","customer.permit.not.exist")));
            validateOwnEnoughAllow(ownPermit);
            result += beanCruder.delete(ownPermit);
        }
        result += createOrUpdatePermitWithHold(custId,userId);
        return result;
    }

    private Integer createOrUpdatePermitWithHold(String custId, String userId) {
        CustPermitPO existPermit  = customerService.getCustPermit(userId,custId);
        if (existPermit == null)
            existPermit = new CustPermitPO();
        existPermit.setAllowHold(BizConst.YES_NO_Y);
        existPermit.setAllowBusiness(BizConst.YES_NO_Y);
        existPermit.setAllowEdit(BizConst.YES_NO_Y);
        existPermit.setAllowView(BizConst.YES_NO_Y);
        existPermit.setUserId(userId);
        existPermit.setCustId(custId);
        return beanCruder.save(existPermit);
    }

    private void validateOwnEnoughAllow(CustPermitPO ownPermit) {
        String allowHold = ownPermit.getAllowHold();
        if (!BizConst.YES_NO_Y.equals(allowHold))
            throw new BizException(MessageHolder.getMessage("","customer.permit.allow.not.enough"));
    }
}
