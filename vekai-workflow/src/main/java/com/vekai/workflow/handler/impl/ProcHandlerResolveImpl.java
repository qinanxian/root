package com.vekai.workflow.handler.impl;

import cn.fisok.raw.holder.ApplicationContextHolder;
import com.vekai.workflow.autoconfigure.WorkflowProperties;
import com.vekai.workflow.handler.ProcHandler;
import com.vekai.workflow.handler.ProcHandlerResolve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProcHandlerResolveImpl implements ProcHandlerResolve {
    @Autowired
    protected WorkflowProperties properties;

    @Override
    public List<ProcHandler> findHandlers(String definitionKey) {
        List<String> handlerClasses = properties.getHandlerMapping().get(definitionKey);
        List<ProcHandler> handlers = new ArrayList<>();

        if (handlerClasses == null || 0 == handlerClasses.size() || !handlerClasses
            .contains(WorkflowDBProcHandler.class.getName())) {
            /**
             * 默认将WorkflowDBProcHandler加入流程handler
             */
            ProcHandler workflowDBProcHandler =
                ApplicationContextHolder.getBean(WorkflowDBProcHandler.class);
            handlers.add(workflowDBProcHandler);
        }

        if (null != handlerClasses) {
            for (String clazz : handlerClasses) {
                ProcHandler handler = ApplicationContextHolder.getBeanByClassName(clazz);
                handlers.add(handler);
            }
        }
        return handlers;
    }
}
