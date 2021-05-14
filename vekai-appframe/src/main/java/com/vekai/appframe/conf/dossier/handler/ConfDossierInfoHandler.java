package com.vekai.appframe.conf.dossier.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class ConfDossierInfoHandler extends MapDataOneHandler {
    @Autowired
    BeanCruder beanCruder;

    @Override
    public MapObject query(DataForm dataForm, Map<String, ?> queryParameters) {
        MapObject ret = super.query(dataForm, queryParameters);
        if(ret != null && !ret.getValue("dossierDefKey").isBlank()){
            dataForm.getElement("dossierDefKey")
                    .getElementUIHint()
                    .setReading(true)
                    .setReadonly(true);
        }

        return ret;
    }

    @Override
    @Transactional
    public int save(DataForm dataForm, MapObject object) {
        String dossierDefId = object.getValue("dossierDefId").strValue();
        String dossierDefKey = object.getValue("dossierDefKey").strValue();
        if(StringKit.isBlank(dossierDefId)){
            int count = beanCruder.selectCount("SELECT * FROM CONF_DOSSIER WHERE DOSSIER_DEF_KEY=:dossierDefKey",
                    MapKit.mapOf("dossierDefKey",dossierDefKey));
            if(count>0){
                throw new BizException("定义KEY={0}已经存在",dossierDefKey);
            }
        }
        return super.save(dataForm, object);
    }

    @Override
    public int delete(DataForm dataForm, MapObject object) {
        String dossierDefKey = object.getValue("dossierDefKey").strValue();
        int r = super.delete(dataForm, object);
        r += beanCruder.execute("DELETE FROM CONF_DOSSIER_ITEM WHERE DOSSIER_DEF_KEY=:dossierDefKey",
                MapKit.mapOf("dossierDefKey",dossierDefKey));
        return r;
    }
}
