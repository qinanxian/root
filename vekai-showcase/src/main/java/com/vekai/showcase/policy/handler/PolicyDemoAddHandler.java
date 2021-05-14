package com.vekai.showcase.policy.handler;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.showcase.entity.DemoCredit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by luyu on 2018/9/7.
 */
@Component
public class PolicyDemoAddHandler extends BeanDataOneHandler<DemoCredit> {

    @Autowired
    BeanCruder beanCruder;

    @Transactional
    @Override
    public int save(DataForm dataForm, DemoCredit demoCredit) {
        return beanCruder.save(demoCredit);
    }
}
