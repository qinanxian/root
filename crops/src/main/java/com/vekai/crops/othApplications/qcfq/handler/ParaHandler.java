package com.vekai.crops.othApplications.qcfq.handler;

import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParaHandler extends MapDataOneHandler {
    @Autowired
    private BeanCruder beancruder;

    @Override
    public int save(DataForm dataForm, MapObject object) {
        String year = object.getValue("years").strValue();
        String timeLimit = object.getValue("timeLimit").strValue();
        if(year.equals(timeLimit)){
            return super.save(dataForm, object);
        }else{
            throw new BizException("年份与月份不匹配");
        }

    }
}
