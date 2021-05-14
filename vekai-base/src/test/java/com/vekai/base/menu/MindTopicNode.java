package com.vekai.base.menu;

import java.io.Serializable;
import java.util.List;

public class MindTopicNode implements Serializable {
    private String id;
    private String topic;
    private boolean expanded;
    private String globalId;
    private String contextId;
    private String type;
    private String icon;
    private List<MindTopicNode> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getGlobalId() {
        return globalId;
    }

    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<MindTopicNode> getChildren() {
        return children;
    }

    public void setChildren(List<MindTopicNode> children) {
        this.children = children;
    }
}
