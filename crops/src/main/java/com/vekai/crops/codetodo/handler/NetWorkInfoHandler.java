package com.vekai.crops.codetodo.handler;

import cn.fisok.raw.lang.MapObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.vekai.crops.util.JsonUtil;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NetWorkInfoHandler extends MapDataOneHandler {

    protected final Logger logger = LoggerFactory.getLogger(NetWorkInfoHandler.class);

    @Override
    public MapObject query(DataForm dataForm, Map<String, ?> queryParameters){
        MapObject query = super.query(dataForm, queryParameters);
//        for (int i = 0; i < query.size(); i++) {
            String district = query.getValue("districts").strValue();
            try {
                JsonNode districtJson = JsonUtil.getJson(district);
                String address = JsonUtil.getJsonStringValue(districtJson, "address");
                query.putValue("districts",address);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("数据转换的handler出错，错误信息："+e.getMessage());
            }
//        }
        return query;
    }
}
