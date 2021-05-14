package com.vekai.appframe.conf.fiscalgaap.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.fiscal.entity.ConfFiscGaapEntryPO;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Component
public class FiscGaapListHandler extends MapDataListHandler {

    @Autowired
    BeanCruder beanCruder;

    @Override
    @Transactional
    public Integer delete(DataForm dataForm, List<MapObject> dataList) {
        for (MapObject mapObject : dataList) {
            String gaapDef = mapObject.getValue("gaapDef").strValue();
            String deleteEntrySql = "SELECT * FROM CONF_FISC_GAAP_ENTRY WHERE GAAP_DEF =:gaapDef";
            List<ConfFiscGaapEntryPO> confFiscGaapEntryPOList = beanCruder.selectList(
                    ConfFiscGaapEntryPO.class,deleteEntrySql, MapKit.mapOf("gaapDef",gaapDef));

            if(confFiscGaapEntryPOList.size() > 0) {
                for (ConfFiscGaapEntryPO confFiscGaapEntryPO : confFiscGaapEntryPOList) {
                    String sql = "DELETE FROM CONF_FISC_GAAP_ASSIST WHERE GAAP_ENTRY_DEF=:gaapEntryDef";
                    beanCruder.execute(sql,MapKit.mapOf("gaapEntryDef",confFiscGaapEntryPO.getGaapEntryDef()));
                }
                beanCruder.delete(confFiscGaapEntryPOList);
            }
        }
        return super.delete(dataForm,dataList);
    }
}
