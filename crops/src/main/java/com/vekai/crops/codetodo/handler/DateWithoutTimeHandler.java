package com.vekai.crops.codetodo.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DateWithoutTimeHandler extends MapDataListHandler {

    protected final Logger logger = LoggerFactory.getLogger(DateWithoutTimeHandler.class);

    @Override
    public PaginResult<MapObject> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        PaginResult<MapObject> result = super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);

        List<MapObject> dataList = result.getDataList();
        dataList.stream().forEach(mapData -> {
            String startDate = mapData.getValue("startDate").strValue();
            String endDate = mapData.getValue("endDate").strValue();
            String[] startDateArray = startDate.split(" ");
            String[] endDateArray = endDate.split(" ");
            mapData.putValue("startDate",startDateArray[0]);
            mapData.putValue("endDate",endDateArray[0]);
        });
        return result;
    }
}
