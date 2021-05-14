package com.vekai.appframe.workflow.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.StringKit;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WorkflowDesignerListHandler extends MapDataListHandler {
    @Override
    public PaginResult<MapObject> query(DataForm dataForm, Map<String, ?> queryParameters,
                                        Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {

        String category = StringKit.nvl(queryParameters.get("category"),"_ALL_");
        if ("_ALL_".equals(category)){
            dataForm.getQuery().setWhere("1=1");
        }

        return super
            .query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
    }
}
