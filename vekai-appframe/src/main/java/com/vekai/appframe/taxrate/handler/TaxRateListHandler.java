package com.vekai.appframe.taxrate.handler;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.appframe.constant.AppframeConst;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.MapObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaxRateListHandler extends MapDataListHandler {

    @Autowired
    BeanCruder beanCruder;

    public void changeStatus(DataForm dataForm, MapObject data) {
        String status = data.getObject("status", String.class);
        String taxRateId = data.getObject("taxRateId", String.class);
        if(status.equals(AppframeConst.EFFECT_STATUS_VALID))
            status = AppframeConst.EFFECT_STATUS_INVALID;
        else
            status = AppframeConst.EFFECT_STATUS_VALID;

        String sql = "update FNAT_TAX_RATE set STATUS = :status where TAX_RATE_ID = :id";
        beanCruder.execute(sql, MapKit.mapOf("status", status, "id", taxRateId));
    }

}
