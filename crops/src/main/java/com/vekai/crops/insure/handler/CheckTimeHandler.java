package com.vekai.crops.insure.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckTimeHandler extends MapDataOneHandler{

    @Autowired
    private BeanCruder beanCruder;

    /**
     * 对保单起始日期、起始时间、截止时间、截止日期做处理
     *
     * @param dataForm
     * @param object
     * @return
     */
    @Override
    public int save(DataForm dataForm, MapObject object) {

        String startTime="",expireTime="",revTime="",recTime="",createdTime="",updatedTime="";
        //保单收到时间
        revTime = object.getValue("revTime").strValue();
        //保单起始时间
        startTime = object.getValue("startTime").strValue();
        //保单到期时间
        expireTime = object.getValue("expireTime").strValue();
        //保单领取时间
        recTime = object.getValue("recTime").strValue();
        //保单新建时间
        createdTime = object.getValue("createdTime").strValue();
        //保单更新时间
        updatedTime = object.getValue("updatedTime").strValue();

        if((isNotEmpty(revTime))&&(revTime.length()>10)){
            revTime = revTime.substring(0,10);
        }
        if((isNotEmpty(startTime))&&(startTime.length()>10)){
            startTime = startTime.substring(0,10);
        }
        if((isNotEmpty(expireTime))&&(expireTime.length()>10)){
            expireTime = expireTime.substring(0,10);
        }
        if((isNotEmpty(recTime))&&(recTime.length()>10)){
            recTime = recTime.substring(0,10);
        }
        if((isNotEmpty(createdTime))&&(createdTime.length()>19)){
            createdTime = createdTime.substring(0,19);
        }
        if((isNotEmpty(updatedTime))&&(updatedTime.length()>19)){
            updatedTime = updatedTime.substring(0,19);
        }
        object.putValue("startTime",startTime);
        object.putValue("expireTime",expireTime);
        object.putValue("revTime",revTime);
        object.putValue("recTime",recTime);
        object.putValue("createdTime",createdTime);
        object.putValue("updatedTime",updatedTime);

        return super.save(dataForm, object);
    }

    private boolean isNotEmpty(String str) {
        boolean result = true ;
        if((str==null) || (str.equals(""))){
            result = false ;
        }
        return  result;
    }
}
