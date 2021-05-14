package com.vekai.crops.othApplications.qcfq.handler;

import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BusinessHandler extends MapDataListHandler {
    @Autowired
    private BeanCruder beancruder;

    @Transactional
    public int chgsta(DataForm dataform, MapObject param){

        String busId = param.getValue("busId").strValue();
        String sql="update QCFQ_BUSINESS_INFO set status='02' where ID=:busId";
        Map<String,Object> params = new HashMap<>();
        params.put("busId",busId);
        int result=beancruder.execute(sql,params);
        return result;

    }

}
