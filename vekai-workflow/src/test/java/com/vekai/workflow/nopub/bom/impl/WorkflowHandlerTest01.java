package com.vekai.workflow.nopub.bom.impl;

import com.vekai.workflow.nopub.bom.BomContext;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.handler.ProcHandlerAdapter;

public class WorkflowHandlerTest01 extends ProcHandlerAdapter {
    @Override
    public BomContext fillWorkflowParam(WorkflowProc proc, WorkflowTask tasks) {
        return super.fillWorkflowParam(proc, tasks);
    }
}
