package com.vekai.crops.othApplications.qcfq.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
public class CardHandler extends MapDataListHandler {
    @Autowired
    private BeanCruder beancruder;
    @Transactional
    public int chgToBack (DataForm dataform, MapObject param){
        String orderId=param.getValue("orderId").strValue();
        String sql="update QCFQ_CARD_ORDER_INFO set ORDER_STATUS ='04' where ORDER_ID =:orderId";
        Map<String,Object> params = new HashMap<>();
        params.put("orderId",orderId);
        int result=beancruder.execute(sql,params);
        return result;
    }
    @Transactional
    public int chgToPass(DataForm dataForm,MapObject param){
        String orderId=param.getValue("orderId").strValue();
        String sql="update QCFQ_CARD_ORDER_INFO set ORDER_STATUS ='02' where ORDER_ID =:orderId";
        Map<String,Object> params = new HashMap<>();
        params.put("orderId",orderId);
        int result=beancruder.execute(sql,params);
        return result;
    }
    @Transactional
    public int chgToRefuse(DataForm dataForm,MapObject param){
        String orderId=param.getValue("orderId").strValue();
        String sql="update QCFQ_CARD_ORDER_INFO set ORDER_STATUS ='03' where ORDER_ID =:orderId";
        Map<String,Object> params = new HashMap<>();
        params.put("orderId",orderId);
        int result=beancruder.execute(sql,params);
        return result;
    }

}
