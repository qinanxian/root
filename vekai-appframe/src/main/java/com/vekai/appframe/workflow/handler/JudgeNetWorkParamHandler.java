package com.vekai.appframe.workflow.handler;

import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.handler.ProcHandlerAdapter;
import com.vekai.workflow.nopub.bom.BomContext;
import com.vekai.workflow.service.WorkflowPageParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by apachechen on 2018/3/2.
 */
@Component
public class JudgeNetWorkParamHandler extends ProcHandlerAdapter {

    @Autowired
    private WorkflowPageParamService workflowPageParamService;

    @Override
    public void afterProcStart(WorkflowProc proc, List<WorkflowTask> tasks) {
        super.afterProcStart(proc, tasks);
        String procId = proc.getProcId();
        Map<String,Object> paramPage = new HashMap();
        paramPage.put("procId",procId);
        workflowPageParamService.appendPageParam(proc.getProcInstId(),paramPage);
    }

    @Override
    public BomContext fillWorkflowParam(WorkflowProc proc, WorkflowTask task) {
        super.fillWorkflowParam(proc, task);
        BomContext bomContext = new BomContext();
        bomContext.put("money",50000);
        bomContext.put("size",600);
        return bomContext;
    }
}
