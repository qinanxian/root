package com.vekai.appframe.conf.fiscalevent.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by luyu on 2018/6/12.
 */
@Component
public class FiscEventParamListHandler extends MapDataListHandler {

    @Autowired
    BeanCruder beanCruder;

    @Override
    @Transactional
    public int save(DataForm dataForm, List<MapObject> dataList) {
        if (dataList.size() == 0)
            return 1;
        String eventDef = dataList.get(0).getValue("eventDef").strValue();
        String sql = "DELETE FROM CONF_FISC_EVENT_PARAM WHERE EVENT_DEF = :eventDef";
        beanCruder.execute(sql, MapKit.mapOf("eventDef",eventDef));
        return super.save(dataForm,dataList);
    }

}
