package com.vekai.workflow.nopub.resource.model;

import com.vekai.workflow.nopub.resource.model.enums.WorkflowResourceRight;

import java.io.Serializable;

/**
 * 流程资源业务对象(非实体)
 */
public class WorkflowResource implements Serializable,Cloneable{

    private String id;
    private String procDefKey;
    private String type;
    private String name;
    private WorkflowResourceRight right = WorkflowResourceRight.Writeable;
    private String sortCode;
    private String action;
    private String actionHint;
    private String icon;
    private String style;
    // 信息块默认是否展开
    private String isExpanded;
    private String isInstResource;
    private String resourceLocation;

    public String getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public WorkflowResource(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkflowResourceRight getRight() {
        return right;
    }

    public void setRight(WorkflowResourceRight right) {
        this.right = right;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionHint() {
        return actionHint;
    }

    public void setActionHint(String actionHint) {
        this.actionHint = actionHint;
    }

    public String getIsExpanded() {
        return isExpanded;
    }

    public void setIsExpanded(String isExpanded) {
        this.isExpanded = isExpanded;
    }

    public String getIsInstResource() {
        return isInstResource;
    }

    public void setIsInstResource(String isInstResource) {
        this.isInstResource = isInstResource;
    }

    @Override
    public String toString() {
        return "WorkflowResource{" +
                "id='" + id + '\'' +
                ", procDefKey='" + procDefKey + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", right=" + right +
                ", sortCode='" + sortCode + '\'' +
                ", action='" + action + '\'' +
                ", icon='" + icon + '\'' +
                ", style='" + style + '\'' +
                ", isExpanded='" + isExpanded + '\'' +
                ", isInstResource='" + isInstResource + '\'' +
                '}';
    }
}
