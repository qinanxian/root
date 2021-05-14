package com.vekai.appframe.base.dict.handler;

import cn.fisok.sqloy.core.PaginResult;
import com.vekai.base.dict.service.impl.po.DictItemPO;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DictItemListHandler extends BeanDataListHandler<DictItemPO> {

    public PaginResult<DictItemPO> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        return super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
    }
}
