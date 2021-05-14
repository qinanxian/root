package com.vekai.appframe.cmon.version.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class VersionInfoHandler extends MapDataOneHandler {


    @Autowired
    BeanCruder beanCruder;


    @Override
    @Transactional
    public int save(DataForm dataForm, MapObject object) {

        Date releaseTime = object.getValue("releaseTime").dateValue();
        Date nextReleaseTime = object.getValue("nextReleaseTime").dateValue();
        int res=releaseTime.compareTo(nextReleaseTime);
        int result=super.save(dataForm, object);
        if(res<0){
            return result;
        }else{
            throw new BizException("下次发布日期必须大于发布日期");
        }

    }


}
