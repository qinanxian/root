package com.vekai.appframe.cmon.version.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VersionListHandler extends MapDataListHandler {

    @Autowired
    BeanCruder beanCruder;


    @Override
    public Integer delete(DataForm dataForm, List<MapObject> dataList) {

        int result1=0;
        int result2=0;
        for (MapObject mapObject : dataList) {
            String versionId = mapObject.getValue("versionId").strValue();
            String sql = "delete from CMON_VERSION where VERSION_ID=:versionId";
            String sql2 = "delete from CMON_VERSION_CHANGE where VERSION_ID=:versionId";
            result1= beanCruder.execute(sql, MapKit.mapOf("versionId", versionId));
            result2= beanCruder.execute(sql2, MapKit.mapOf("versionId", versionId));
        }

        return  result1+result2;




    }




}
