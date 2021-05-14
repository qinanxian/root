package com.vekai.appframe.workflow.handler;

import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.handler.ProcHandlerAdapter;
import com.vekai.workflow.nopub.bom.BomContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apachechen on 2018/3/13.
 */
@Component
public class MultiInstanceHandler extends ProcHandlerAdapter{

    @Override
    public BomContext fillWorkflowParam(WorkflowProc proc, WorkflowTask task) {
        super.fillWorkflowParam(proc, task);
        BomContext bomContext = new BomContext();
        List<String> userList = new ArrayList<>();
        userList.add("yyluo");
        userList.add("cdzhang");
        userList.add("wkchen");
        bomContext.put("userList",userList);
        return bomContext;
    }

}
