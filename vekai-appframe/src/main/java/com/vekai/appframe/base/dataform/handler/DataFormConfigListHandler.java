package com.vekai.appframe.base.dataform.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.StringKit;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataFormConfigListHandler extends MapDataListHandler {

    @Override
    public PaginResult<MapObject> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        String dataFromPack = StringKit.nvl(queryParameters.get("dataFromPack"), "_ALL_");
        String originWhere = dataForm.getQuery().getWhere();
        if ("_ALL_".equalsIgnoreCase(dataFromPack)) {
            dataForm.getQuery().setWhere("1=1");
        }
        PaginResult<MapObject> result = super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
        dataForm.getQuery().setWhere(originWhere);
        return result;
    }

}
