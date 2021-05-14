package com.vekai.crops.etl.handler;

import cn.fisok.raw.lang.MapObject;
import com.alibaba.fastjson.JSONObject;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleJobListHandler extends MapDataListHandler {
    @Autowired
    private EtlServerRequest etlServerRequest;


    @Override
    public Integer delete(DataForm dataForm, List<MapObject> dataList) {
        // 先删除，再通知ETL
        super.delete(dataForm, dataList);

        for (MapObject job:dataList) {
            String jobId = job.getValue("jobId").strValue();
            String jobGroup = job.getValue("jobGroup").strValue();
            JSONObject jsonObject = etlServerRequest.deleteJob(jobId, jobGroup);
            if(!jsonObject.getBoolean("valid")){
                throw new RuntimeException(jsonObject.getString("msg"));
            }
        }
        return 1;
    }
}
