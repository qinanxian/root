package com.vekai.showcase.handler;

import cn.fisok.raw.kit.DateKit;
import com.vekai.showcase.entity.DemoPerson;
import com.vekai.dataform.exception.ValidatorException;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DemoPersonInfoHandler extends BeanDataOneHandler<DemoPerson> {

    public void checkHeight1(Double height, DemoPerson person){
        if(height>=300){
            throw new ValidatorException("身高超过3米了");
        }
    }
    public void checkHeight2(Double height, DemoPerson person){
        if(height>=390){
            throw new ValidatorException("身高超过3.9米了");
        }
    }
    public void checkBirth(Date birth, DemoPerson person){
        if(DateKit.isAfter(DateKit.parse("2000-01-01"),birth)){
            throw new ValidatorException("零零后暂时不满足录入条件");
        }
    }
}
