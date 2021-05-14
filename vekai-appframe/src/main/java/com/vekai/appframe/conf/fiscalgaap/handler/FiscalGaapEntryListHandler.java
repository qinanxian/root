package com.vekai.appframe.conf.fiscalgaap.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class FiscalGaapEntryListHandler extends MapDataListHandler {

    @Autowired
    BeanCruder beanCruder;

    @Override
    @Transactional
    public int save(DataForm dataForm, List<MapObject> dataList) {

        if (dataList.size() == 0)
            return 1;
        String gaapDef = dataList.get(0).getValue("gaapDef").strValue();
        String sql = "DELETE FROM CONF_FISC_GAAP_ENTRY WHERE GAAP_DEF =:gaapDef";
        beanCruder.execute(sql, MapKit.mapOf("gaapDef", gaapDef));
        dataList.forEach(mapData -> {
            mapData.put("status","VALID");
        });
        return super.save(dataForm, dataList);
    }

    @Override
    @Transactional
    public Integer delete(DataForm dataForm, List<MapObject> dataList) {
        for (MapObject mapObject : dataList) {
            String gaapEntryDef = mapObject.getValue("gaapEntryDef").strValue();
            if(null != gaapEntryDef) {
                String sql1 = "DELETE FROM CONF_FISC_GAAP_ENTRY WHERE GAAP_ENTRY_DEF =:gaapEntryDef";
                String sql2 = "DELETE FROM CONF_FISC_GAAP_ASSIST WHERE GAAP_ENTRY_DEF =:gaapEntryDef";

                beanCruder.execute(sql1,MapKit.mapOf("gaapEntryDef",gaapEntryDef));
                beanCruder.execute(sql2,MapKit.mapOf("gaapEntryDef",gaapEntryDef));
            }
        }
        return 0;
    }

}
