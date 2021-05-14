package com.vekai.lact.core;

import cn.fisok.raw.lang.MapObject;
import com.vekai.lact.BaseTest;
import com.vekai.lact.core.transaction.LactTransaction;
import com.vekai.lact.model.LactLoan;
import org.springframework.beans.factory.annotation.Autowired;

public class LactTransBase extends BaseTest {
    @Autowired
    protected LactEngine engine;

    protected void invokeTrans(String transCode, LactLoan dataObject, MapObject params){
        LactTransaction lacts = engine.getTransaction(transCode);

        lacts.load(dataObject,params);
        lacts.handle(dataObject);
    }
}
