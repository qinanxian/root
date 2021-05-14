package com.vekai.workflow.handler;

import com.vekai.workflow.nopub.bom.BomContext;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import org.springframework.stereotype.Component;

/**
 * Created by apachechen on 2018/2/6.
 */
@Component
public class MustHandler extends ProcHandlerAdapter{

    @Override
    public BomContext fillWorkflowParam(WorkflowProc proc, WorkflowTask task) {
        super.fillWorkflowParam(proc,task);
        BomContext bomContext = new BomContext();
        bomContext.putValue("user.id","0001");
        bomContext.putValue("judge.money",500);
        bomContext.putValue("judge.size",60);
        return bomContext;
    }
}
