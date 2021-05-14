package com.vekai.appframe.taxrate.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.appframe.constant.AppframeConst;
import com.vekai.appframe.taxrate.model.FnatTaxRate;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class TaxRateSummaryHandler extends MapDataOneHandler {

    @Autowired
    BeanCruder beanCruder;

    /**
     * 新增税务维护时，将税务类型已存在的置为无效
     */
    @Override
    @Transactional
    public int save(DataForm dataForm, MapObject object) {
        FnatTaxRate taxRate = new FnatTaxRate();
        BeanKit.copyProperties(object, taxRate);
        if (taxRate.getTaxRateId() != null || taxRate.getStatus().equals(AppframeConst.EFFECT_STATUS_INVALID)) {
            return beanCruder.save(taxRate);
        }

        String taxRateType = object.getValue("taxRateType").strValue();
        String sql = "select * from FNAT_TAX_RATE where TAX_RATE_TYPE=:taxRateType AND STATUS=:status";

        List<FnatTaxRate> itemList = beanCruder.selectList(FnatTaxRate.class, sql,
                MapKit.mapOf("taxRateType", taxRateType, "status", AppframeConst.EFFECT_STATUS_VALID));
        for(FnatTaxRate item : itemList) {
                item.setStatus(AppframeConst.EFFECT_STATUS_INVALID);
        }

        beanCruder.save(itemList);
        return beanCruder.save(taxRate);

    }

}
