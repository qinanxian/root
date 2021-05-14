package com.vekai.crops.codetodo.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HolidayConfigurationHandler extends MapDataOneHandler {

    @Override
    public int save(DataForm dataForm, MapObject object) {
        String holyYear = object.getValue("holyYear").strValue();
        String holyday = object.getValue("holyday").strValue();
        String beizhu = object.getValue("beizhu").strValue();
        String holyId = object.getValue("holyId").strValue();
        if(holyId==null){
            String s = UUID.randomUUID().toString().replaceAll("-", "");
            object.putValue("holyId", s);
            return super.save(dataForm, object);
        }
        return super.update(dataForm, object);
    }
}
