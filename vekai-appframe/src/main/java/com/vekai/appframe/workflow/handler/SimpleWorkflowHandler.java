package com.vekai.appframe.workflow.handler;

import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.handler.ProcHandlerAdapter;
import com.vekai.workflow.nopub.bom.BomContext;

public class SimpleWorkflowHandler extends ProcHandlerAdapter {
    @Override
    public BomContext fillWorkflowParam(WorkflowProc proc, WorkflowTask task) {
        super.fillWorkflowParam(proc, task);

        BomContext bomContext = new BomContext();
        bomContext.put("planSum",100000);
        return bomContext;
    }
}
