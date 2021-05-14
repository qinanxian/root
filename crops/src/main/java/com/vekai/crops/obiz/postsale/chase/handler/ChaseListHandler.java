package com.vekai.crops.obiz.postsale.chase.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.MapObjectCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ChaseListHandler extends MapDataListHandler {
    @Autowired
    MapObjectCruder mapObjectCruder;

    @Override
    public Integer delete(DataForm dataForm, List<MapObject> dataList) {
        //先删除任务子项
        List<String> chaseIdList = new ArrayList<>();
        dataList.forEach(row -> {
            String chaseId = row.getValue("chaseId").strValue();
            chaseIdList.add(chaseId);
        });
        if (chaseIdList.size() > 0) {
            String sql = "DELETE FROM OBIZ_CHASE_TASK WHERE CHASE_ID=:chaseIdList";
            mapObjectCruder.execute(sql, MapKit.mapOf("chaseIdList", chaseIdList));
        }

        return super.delete(dataForm, dataList);
    }
}
