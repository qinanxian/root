package com.vekai.appframe.conf.detector.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class DetectorListExistItemHandler extends MapDataListHandler {

    private static final String CONF_DETECTOR_ITEM_TABLE = "CONF_DETECTOR_ITEM";

    @Override
    public Integer delete(DataForm dataForm, List<MapObject> dataList) {
        int ret = 0;

        for (MapObject mapObject : dataList) {
            String code = (String) mapObject.get("code");
            String sql = "DELETE FROM CONF_DETECTOR_ITEM WHERE DETECTOR_CODE =:code";
            ret+= mapObjectCruder.execute(sql,MapKit.mapOf("code", code));
//            int dataSize = mapObjectCruder
//                .selectCount(
//                    "select * from " + CONF_DETECTOR_ITEM_TABLE + " where ",
//                    );
//            if (dataSize == 0) continue;
//            throw new BizException(MessageHolder.getMessage("", "cmon.detectorList.has.item"));
        }
        return super.delete(dataForm, dataList);

    }
}
