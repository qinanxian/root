package com.vekai.fiscal.event.service;

import com.vekai.fiscal.BaseTest;
import com.vekai.fiscal.event.model.FiscalEvent;
import com.vekai.fiscal.event.model.def.FiscalEventDef;
import cn.fisok.raw.kit.JSONKit;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FisicalEventServiceTest extends BaseTest {

    @Autowired
    FisicalEventService fisicalEventService;

    @Test
    public void testGetDef(){
        Assert.assertNotNull(fisicalEventService);

        FiscalEventDef eventDef = fisicalEventService.getFiscalEventDef("LoanIssuance");
        Assert.assertNotNull(eventDef);
        Assert.assertEquals(eventDef.getEventName(),"贷款发放");
        System.out.println(JSONKit.toJsonString(eventDef,true));

    }

    @Test
    public void testPushEvent(){
        EventData eventData = new EventData("CNY",86000);
        eventData.setMainCorpId("0001");
        eventData.setObjectType("Mock");
        eventData.setObjectId("MockData001");
        eventData.addParam("contractId","HT9981");
        eventData.addParam("customerId","HK9527");
        eventData.addParam("customerName","周三鑫");

        FiscalEvent event = fisicalEventService.publishEvent("LoanIssuance",eventData);
        System.out.println(JSONKit.toJsonString(event,true));
    }
}
