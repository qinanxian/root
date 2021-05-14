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
public class FiscalGaapAssistSummaryHandler extends MapDataListHandler {

    @Autowired
    BeanCruder beanCruder;

    @Override
    @Transactional
    public int save(DataForm dataForm, List<MapObject> dataList) {
        if (dataList.size() == 0)
            return 1;
        String gaapEntryDef = dataList.get(0).getValue("gaapEntryDef").strValue();
        String sql = "DELETE FROM CONF_FISC_GAAP_ASSIST WHERE GAAP_ENTRY_DEF =:gaapEntryDef";
        beanCruder.execute(sql, MapKit.mapOf("gaapEntryDef",gaapEntryDef));
       return super.save(dataForm,dataList);
    }

}
