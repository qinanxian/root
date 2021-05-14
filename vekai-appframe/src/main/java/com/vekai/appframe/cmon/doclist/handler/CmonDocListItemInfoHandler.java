package com.vekai.appframe.cmon.doclist.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.sqloy.core.MapObjectUpdater;
import org.springframework.stereotype.Component;

@Component
public class CmonDocListItemInfoHandler extends MapDataOneHandler {

    private static final String DEFAULT_SORT_CODE = "9999";

    public int save(DataForm dataForm, MapObject object) {
        String objectId = object.getValue("objectId").strValue();
        String objectType = object.getValue("objectType").strValue();
        MapObject docListObject = mapObjectCruder.getMapObjectQuery().selectOne(
                "SELECT * FROM CMON_DOCLIST WHERE OBJECT_ID=:objectId AND OBJECT_TYPE=:objectType",
                MapKit.mapOf("objectId", objectId, "objectType", objectType));
        ValidateKit.notNull(docListObject,"缺少文档实例信息！");
        String doclistId = docListObject.getValue("doclistId").strValue();

        String table = StringKit.nvl(dataForm.getDataModel(), "CMON_DOCLIST_ITEM");
        MapObject dbMapdata = getDbMapData(dataForm, object);
        dbMapdata.put("doclistId", doclistId);
        dbMapdata.put("sortCode", DEFAULT_SORT_CODE);
        dbMapdata.remove("''");
        MapObject dbPkMapdata = getDbPkMapData(dataForm, object);
        togglePrimaryKey(dataForm, dbMapdata);

//        int result = mapObjectCruder.getMapObjectUpdater()
//                .setNameConverter(getNameConverter(dataForm))
//                .save(table, dbMapdata, dbPkMapdata);
//
//        return result;
        MapObjectUpdater updater = mapObjectCruder.getMapObjectUpdater();
        //在模板类中定义操作，这样，可以把其他需要统一处理的东西放到被调用的方法中处理
        return updater.exec(getNameConverter(dataForm), ()->{
            return updater.save(table, dbMapdata, dbPkMapdata);
        });
    }
}
