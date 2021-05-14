package com.vekai.appframe.auth.handler;

import cn.fisok.sqloy.core.PaginResult;
import com.vekai.auth.entity.Org;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.StringKit;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrgListHandler extends BeanDataListHandler<Org> {

    public PaginResult<Org> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        String orgId = StringKit.nvl(queryParameters.get("orgId"), "_ALL_");
        if ("_ALL_".equals(orgId)) {
            dataForm.getQuery().setWhere("1=1");
        }

        return super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
    }
}
