package com.vekai.base.pilot.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 系统开始节点（有且仅有一个）
 */
public class StartNode extends BaseNode implements Serializable {
    private Map<String,Object> properties;
    private List<MenuNode> children;

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public List<MenuNode> getChildren() {
        return children;
    }

    public void setChildren(List<MenuNode> children) {
        this.children = children;
    }
}
