package com.vekai.appframe.conf.detector.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.base.detector.service.DetectorService;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.StringKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DetectorItemInfoHandler extends MapDataOneHandler {
    @Autowired
    DetectorService detectorService;
    @Override
    public int save(DataForm dataForm, MapObject object) {
        String itemCode = object.getValue("itemCode").strValue();
        if(StringKit.isNotBlank(itemCode)){
            detectorService.clearCacheItem(itemCode);
        }
        return super.save(dataForm, object);
    }
}
