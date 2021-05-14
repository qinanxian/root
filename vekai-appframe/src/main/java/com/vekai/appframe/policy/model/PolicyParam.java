package com.vekai.appframe.policy.model;

import com.vekai.appframe.policy.entity.PolcParamEntity;

import javax.persistence.Transient;

public class PolicyParam extends PolcParamEntity {

    @Transient
    protected String valueArray;
    @Transient
    protected Object expressionValue;

    public PolicyParam() {
    }

    public PolicyParam(String paramCode, String paramName) {
        super(paramCode, paramName);
    }

    public PolicyParam(String paramCode, String paramName, String groupName) {
        super(paramCode, paramName, groupName);
    }

    public PolicyParam(String paramCode, String paramName, String groupName, String editorMode) {
        super(paramCode, paramName, groupName, editorMode);
    }

    public String getValueArray() {
        return valueArray;
    }

    public void setValueArray(String valueArray) {
        this.valueArray = valueArray;
    }

    public Object getExpressionValue() {
        return expressionValue;
    }

    public void setExpressionValue(Object expressionValue) {
        this.expressionValue = expressionValue;
    }
}
