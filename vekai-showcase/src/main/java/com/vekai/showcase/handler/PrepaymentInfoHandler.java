package com.vekai.showcase.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.fiscal.event.service.EventData;
import com.vekai.fiscal.event.service.FisicalEventService;
import cn.fisok.raw.lang.MapObject;

@Component
public class PrepaymentInfoHandler extends MapDataOneHandler {
	@Autowired
    FisicalEventService fisicalEventService;

    @Transactional
    public Integer trigger(DataForm dataForm, MapObject object) {
        String currency = object.getValue("currency").strValue();
        double occurAmt = object.getValue("occurAmt").doubleValue();
        //主要参数
        EventData eventData = new EventData(currency,occurAmt);
        eventData.setMainCorpId(object.getValue("mainCorpId").strValue());
        eventData.setObjectId(object.getValue("objectId").strValue());
        eventData.setObjectType(object.getValue("objectType").strValue());
        eventData.setExchangeRateType(object.getValue("exchangeRateType").strValue());
        eventData.setExchangeRateValue(object.getValue("exchangeRate").doubleValue());
        eventData.setOriginalCurrency(object.getValue("originalCurrency").strValue());
        eventData.setOriginalAmt(object.getValue("originalAmt").doubleValue());
        eventData.setOccurTime(object.getValue("occurTime").dateValue());

        //辅助参数
        eventData.addParam("customerId",object.getValue("customerId").strValue());
        eventData.addParam("customerName",object.getValue("customerName").strValue());
        eventData.addParam("contractId",object.getValue("contractId").strValue());
        eventData.addParam("payInterest",object.getValue("payInterest").strValue());
        eventData.addParam("payPrincipal",object.getValue("payPrincipal").strValue());
        fisicalEventService.publishEvent("LoanPreSettle",eventData);
        return 1;
    }
}
