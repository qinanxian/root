package com.vekai.showcase.detector.initor;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.base.detector.DetectorContext;
import com.vekai.base.detector.DetectorContextInitor;
import com.vekai.showcase.entity.DemoPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DemoDetectorContextInitor extends DetectorContextInitor {
    @Autowired
    protected BeanCruder beanCruder;

    @Override
    public void init(DetectorContext context) {
        String personCode = context.getParam("personCode").strValue();
        DemoPerson person = beanCruder.selectOne(DemoPerson.class,
                "select * from DEMO_PERSON where code=:code"
                , "code", personCode);

        Random rand = new Random();
        setContextParam(context, "person", person);
        setContextParam(context, "randNumber", rand.nextInt(10));
    }
}
