package com.vekai.appframe.workflow.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.sqloy.core.SqlQuery;
import com.vekai.workflow.service.WorkflowProcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WorkflowCommentListHandler extends MapDataListHandler {
    @Autowired
    private WorkflowProcService workflowProcService;

    @Override
    public PaginResult<MapObject> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        String procId = (String) queryParameters.get("procId");
        if(null!=procId&&!"".equals(procId)){
            /**
             * 兼容已完成流程的历史意见查询
             */
            boolean finished = workflowProcService.isFinished(procId);
            if(finished){
                SqlQuery sqlQuery = dataForm.getQuery();
                String from = sqlQuery.getFrom();
                sqlQuery.setFrom(from
                        .replace("WKFL_COMMENT","WKFL_COMMENT_HIST")
                        .replace("WKFL_TASK","WKFL_TASK_HIST"));
                dataForm.setQuery(sqlQuery);
            }
        }


        return super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
    }
}
