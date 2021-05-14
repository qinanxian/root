package com.vekai.showcase.handler;

import cn.fisok.raw.kit.DateKit;
import com.vekai.showcase.entity.DemoPerson;
import com.vekai.dataform.exception.ValidatorException;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class DemoBeanValidatePersonListHandler extends BeanDataListHandler<DemoPerson> {

    public void checkBirth(Date birth, DemoPerson person, List<DemoPerson> personList){
        System.out.println("开始校验第["+(personList.indexOf(person)+1)+"]行");
        if(DateKit.isAfter(birth, DateKit.parse("1990-01-01"))){
            throw new ValidatorException("必需要求90后");
        }
    }

}
