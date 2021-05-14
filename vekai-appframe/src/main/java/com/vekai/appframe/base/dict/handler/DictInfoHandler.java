package com.vekai.appframe.base.dict.handler;

import com.vekai.base.dict.service.DictService;
import com.vekai.base.dict.service.impl.po.DictPO;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DictInfoHandler extends BeanDataOneHandler<DictPO> {

    @Autowired
    protected DictService dictService;

    @Override
    public int save(DataForm dataForm, DictPO object) {
        dictService.clearCacheItem(object.getCode());
        return super.save(dataForm, object);
    }

    public DictPO query(DataForm dataForm, Map<String, ?> queryParameters) {
        return super.query(dataForm, queryParameters);
    }
}
