package com.vekai.fiscal.book.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.fiscal.event.entity.ConfFiscEventEntryPO;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by luyu on 2018/6/30.
 */
@Service
public class FiscalEventService {

    @Autowired
    BeanCruder beanCruder;

    public ConfFiscEventEntryPO getConfFiscEventEntry(String eventEntryDef) {
        String sql = "SELECT * FROM CONF_FISC_EVENT_ENTRY WHERE EVENT_ENTRY_DEF =:eventEntryDef";
        return beanCruder.selectOne(ConfFiscEventEntryPO.class,sql, MapKit.mapOf("eventEntryDef",eventEntryDef));
    }
}
