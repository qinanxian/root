package com.vekai.workflow.liteflow.handler.impl;

import cn.fisok.raw.holder.ApplicationContextHolder;
import com.vekai.workflow.autoconfigure.WorkflowProperties;
import com.vekai.workflow.liteflow.handler.LiteHandler;
import com.vekai.workflow.liteflow.handler.LiteHandlerResolve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LiteHandlerResolveImpl implements LiteHandlerResolve {
    @Autowired
    protected WorkflowProperties properties;

    @Override
    public List<LiteHandler> findHandlers(String definitionKey) {
        List<String> handlerClasses = properties.getHandlerMapping().get(definitionKey);
        List<LiteHandler> handlers = new ArrayList<>();

        if (null != handlerClasses) {
            for (String clazz : handlerClasses) {
                LiteHandler handler = ApplicationContextHolder.getBeanByClassName(clazz);
                handlers.add(handler);
            }
        }
        return handlers;
    }
}
