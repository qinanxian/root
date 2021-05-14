package com.vekai.base.pilot.model;

import java.io.Serializable;
import java.util.List;

/**
 * 权限节点对象
 */
public class PermitNode<T extends BaseNode> extends BaseNode implements Serializable {
    private List<T> children;

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }
}
