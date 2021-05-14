package com.vekai.appframe.auth.handler;

import com.vekai.auth.entity.Role;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.sqloy.core.PaginResult;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SimpleRoleListHandler extends BeanDataListHandler<Role> {

    public PaginResult<Role> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {

        return super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
    }
}
