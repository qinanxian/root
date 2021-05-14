package com.vekai.showcase.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.fiscal.event.service.EventData;
import com.vekai.fiscal.event.service.FisicalEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by luyu on 2018/6/26.
 */
@Component
public class LoanIssuanceTriggerInfoHandler extends MapDataOneHandler {

    @Autowired
    FisicalEventService fisicalEventService;


    @Transactional
    public Integer trigger(DataForm dataForm, MapObject object) {
        String customerId = object.getValue("customerId").strValue();
        String customerName = object.getValue("customerName").strValue();
        String contractId = object.getValue("contractId").strValue();
        double occurAmt = object.getValue("occurAmt").doubleValue();
        String currency = object.getValue("currency").strValue();
        EventData eventData = new EventData(currency,occurAmt);
        eventData.setExchangeRateType(object.getValue("exchangeRateType").strValue());
        eventData.setExchangeRateValue(object.getValue("exchangeRate").doubleValue());
        eventData.setOriginalCurrency(object.getValue("originalCurrency").strValue());
        eventData.setOriginalAmt(object.getValue("originalAmt").doubleValue());
        eventData.setOccurTime(object.getValue("occurTime").dateValue());
        eventData.addParam("customerId",customerId);
        eventData.addParam("customerName",customerName);
        eventData.addParam("contractId",contractId);
        fisicalEventService.publishEvent("LoanIssuance",eventData);
        return 1;
    }
}
