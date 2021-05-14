package com.vekai.workflow.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ConfigurationProperties(prefix = "com.vekai.workflow", ignoreUnknownFields = true)
public class WorkflowProperties {

    public WorkflowProperties() {
    }

    private Map<String,List<String>> handlerMapping = new HashMap<String,List<String>>();

    public Map<String, List<String>> getHandlerMapping() {
        return handlerMapping;
    }

    public void setHandlerMapping(Map<String, List<String>> handlerMapping) {
        this.handlerMapping = handlerMapping;
    }
}
