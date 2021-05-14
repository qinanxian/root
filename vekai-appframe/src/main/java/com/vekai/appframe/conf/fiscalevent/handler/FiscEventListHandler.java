package com.vekai.appframe.conf.fiscalevent.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by luyu on 2018/6/12.
 */
@Component
public class FiscEventListHandler extends MapDataListHandler{

    @Autowired
    BeanCruder beanCruder;

    @Transactional
    @Override
    public Integer delete(DataForm dataForm, List<MapObject> dataList) {

        for (MapObject mapObject : dataList) {
            String eventDef = mapObject.getValue("eventDef").strValue();
            String deleteEntrySql = "DELETE FROM CONF_FISC_EVENT_ENTRY WHERE EVENT_DEF =:eventDef";
            String deleteParamSql = "DELETE FROM CONF_FISC_EVENT_PARAM WHERE EVENT_DEF =:eventDef";
            beanCruder.execute(deleteEntrySql,MapKit.mapOf("eventDef",eventDef));
            beanCruder.execute(deleteParamSql,MapKit.mapOf("eventDef",eventDef));
        }
        return super.delete(dataForm,dataList);
    }
}
