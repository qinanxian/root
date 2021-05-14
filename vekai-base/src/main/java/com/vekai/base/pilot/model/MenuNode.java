package com.vekai.base.pilot.model;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单节点
 */
public class MenuNode<T extends BaseNode> extends BaseNode implements Serializable {
    private String enabled;
    private String url;
    private String param;
    private List<T> children;

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }
}
