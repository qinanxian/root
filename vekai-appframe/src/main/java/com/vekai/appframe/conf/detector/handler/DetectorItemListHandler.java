package com.vekai.appframe.conf.detector.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.base.detector.service.DetectorService;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.StringKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class DetectorItemListHandler extends MapDataListHandler {

    @Autowired
    DetectorService detectorService;

    private void clearCache(List<MapObject> dataList){
        dataList.forEach(dataItem->{
            String itemCode = dataItem.getValue("itemCode").strValue();
            if(StringKit.isBlank(itemCode))return;
            detectorService.clearCacheItem(itemCode);
        });
    }
    @Override
    public int save(DataForm dataForm, List<MapObject> dataList) {
        clearCache(dataList);
        return super.save(dataForm, dataList);
    }

    @Override
    public Integer delete(DataForm dataForm, List<MapObject> dataList) {
        clearCache(dataList);
        return super.delete(dataForm, dataList);
    }
}
