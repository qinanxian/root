package com.vekai.appframe.base.menu.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by luyu on 2018/8/20.
 */
@Component
public class MenuListHandler extends MapDataListHandler {

    @Override
    public PaginResult<MapObject> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        PaginResult<MapObject> ret = super.query(dataForm,queryParameters,filterParameters,sortMap,pageSize,pageIndex);
        List<MapObject> result = ret.getDataList();
        result.stream().forEach(mapData -> {
            String id = mapData.getValue("id").strValue();
            if (id.contains("-")) {
                mapData.putValue("parentMenu",id.substring(0,id.lastIndexOf("-")));
            }
        });
        return ret;
    }
}

