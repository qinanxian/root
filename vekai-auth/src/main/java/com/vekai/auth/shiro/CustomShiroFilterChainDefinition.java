package com.vekai.auth.shiro;


import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomShiroFilterChainDefinition implements ShiroFilterChainDefinition{
    final private Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

    public void addPathDefinition(String antPath, String definition) {
        filterChainDefinitionMap.put(antPath, definition);
    }

    public void addPathDefinitions(Map<String, String> pathDefinitions) {
        filterChainDefinitionMap.putAll(pathDefinitions);
    }

    @Override
    public Map<String, String> getFilterChainMap() {
        return filterChainDefinitionMap;
    }
}
