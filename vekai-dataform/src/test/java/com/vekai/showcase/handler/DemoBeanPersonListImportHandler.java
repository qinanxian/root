package com.vekai.showcase.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.showcase.entity.DemoPerson;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemoBeanPersonListImportHandler extends BeanDataListHandler<DemoPerson> {

    @Autowired
    protected BeanCruder beanCruder;

    public int deleteAllData(DataForm dataForm, MapObject param){
        return beanCruder.execute("delete from DEMO_PERSON");
    }
}
