package com.vekai.workflow.model;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;

public class ProcFormProperty implements FormProperty {
    private FormProperty formProperty;

    public ProcFormProperty(FormProperty formProperty) {
        this.formProperty = formProperty;
    }

    @Override
    public String getId() {
        return formProperty.getId();
    }

    @Override
    public String getName() {
        return formProperty.getName();
    }

    @Override
    public FormType getType() {
        return formProperty.getType();
    }

    @Override
    public String getValue() {
        return formProperty.getValue();
    }

    @Override
    public boolean isReadable() {
        return formProperty.isReadable();
    }

    @Override
    public boolean isWritable() {
        return formProperty.isWritable();
    }

    @Override
    public boolean isRequired() {
        return formProperty.isRequired();
    }
}
