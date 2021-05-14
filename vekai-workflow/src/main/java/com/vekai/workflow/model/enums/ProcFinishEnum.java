package com.vekai.workflow.model.enums;

/**
 * 流程实例结束类型
 */
public enum ProcFinishEnum {
    COMPLETED("批准","completed"),
    ABOLISHED("流程作废","abolished"),
    REJECT("流程否决","reject");

    private String label;
    private String name;

    private ProcFinishEnum(String label, String name) {
        this.label = label;
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }
}
