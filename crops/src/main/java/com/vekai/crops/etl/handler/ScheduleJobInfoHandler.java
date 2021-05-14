package com.vekai.crops.etl.handler;

import cn.fisok.raw.lang.MapObject;
import com.alibaba.fastjson.JSONObject;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ScheduleJobInfoHandler extends MapDataOneHandler {
    @Autowired
    private EtlServerRequest etlServerRequest;

    @Override
    public MapObject query(DataForm dataForm, Map<String, ?> queryParameters) {
        return super.query(dataForm, queryParameters);
    }

    @Override
    public int save(DataForm dataForm, MapObject object) {
        int save = super.save(dataForm, object);
        String jobId = object.getValue("jobId").strValue();
        JSONObject jsonObject = etlServerRequest.saveOrupdate(jobId);
        if(!jsonObject.getBoolean("valid")){
            throw new RuntimeException(jsonObject.getString("msg"));
        }
        return save;
    }

}
