package com.vekai.workflow.nopub.bom;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;


/**
 * 业务对象模型属性定义
 */
public class BomAttribute implements Serializable,Cloneable{

    private static final long serialVersionUID = -6818728621212127812L;

    private BomObject bomObject;
    private String name;
    private String label;
    private String expression;
    private String constraint;
    private Object value;

    public BomAttribute(){

    }

    public BomObject getBomObject() {
        return bomObject;
    }

    public void setBomObject(BomObject bomObject) {
        this.bomObject = bomObject;
    }

    public BomAttribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BomAttribute(String name, String label, String expression) {
        this.name = name;
        this.label = label;
        this.expression = expression;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getFullName(){
        if(StringUtils.isBlank(bomObject.getName()))return this.getName();
        return bomObject.getName()+"."+this.getName();
    }
}
