package com.vekai.crops.codetodo.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.PaginResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.vekai.crops.util.JsonUtil;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ParkAddressHandler extends MapDataListHandler {

    protected final Logger logger = LoggerFactory.getLogger(ParkAddressHandler.class);

    @Override
    public PaginResult<MapObject> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex){
        PaginResult<MapObject> result = super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
        List<MapObject> dataList = result.getDataList();
        dataList.stream().forEach(mapData -> {
            String district = mapData.getValue("districts").strValue();
            try {
                JsonNode districtJson = JsonUtil.getJson(district);
                String address = JsonUtil.getJsonStringValue(districtJson, "address");
                mapData.putValue("districts",address);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("数据转换的handler出错，错误信息："+e.getMessage());
            }

        });


        return result;
    }
}
