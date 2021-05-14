package com.vekai.appframe.base.dict.handler;

import com.vekai.base.dict.service.impl.po.DictItemPO;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DictItemInfoHandler extends BeanDataOneHandler<DictItemPO> {

    public void initDataForm(DataForm dataForm) {
        super.initDataForm(dataForm);
    }

    public DictItemPO query(DataForm dataForm, Map<String, ?> queryParameters) {

        return super.query(dataForm, queryParameters);
    }
}
