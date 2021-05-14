package com.vekai.workflow.nopub.resource.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apachechen on 2018/3/5.
 */
public class WorkflowResources implements Serializable,Cloneable{

    List<WorkflowResource> button;

    List<WorkflowResource> link;

    List<WorkflowResource> fieldSet;

    public List<WorkflowResource> getButton() {
        return button;
    }

    public void setButton(List<WorkflowResource> button) {
        this.button = button;
    }

    public List<WorkflowResource> getLink() {
        return link;
    }

    public void setLink(List<WorkflowResource> link) {
        this.link = link;
    }

    public List<WorkflowResource> getFieldSet() {
        return fieldSet;
    }

    public void setFieldSet(
        List<WorkflowResource> fieldSet) {
        this.fieldSet = fieldSet;
    }
}
