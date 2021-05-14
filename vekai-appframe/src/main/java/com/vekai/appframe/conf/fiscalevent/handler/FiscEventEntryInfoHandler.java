package com.vekai.appframe.conf.fiscalevent.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.fiscal.event.entity.ConfFiscEventEntryPO;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by luyu on 2018/6/12.
 */
@Component
public class FiscEventEntryInfoHandler extends MapDataOneHandler {

    @Autowired
    BeanCruder beanCruder;

    @Override
    @Transactional
    public int save(DataForm dataForm, MapObject object) {
        String eventEntryDef = object.getValue("eventEntryDef").strValue();
        ConfFiscEventEntryPO confFiscEventEntryPO = null;
        if (StringKit.isBlank(eventEntryDef)) {
            confFiscEventEntryPO = new ConfFiscEventEntryPO();
        } else {
            confFiscEventEntryPO = beanCruder.selectOneById(ConfFiscEventEntryPO.class,
                    MapKit.mapOf("eventEntryDef",eventEntryDef));
        }
        BeanKit.copyProperties(object,confFiscEventEntryPO);
        return beanCruder.save(confFiscEventEntryPO);
    }

}
