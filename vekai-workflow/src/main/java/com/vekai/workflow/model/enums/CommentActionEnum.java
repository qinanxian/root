package com.vekai.workflow.model.enums;

public enum CommentActionEnum {
    ADD("增加","add"),
    DELETE("删除","delete"),
    UPDATE("修改","update"),
    ;

    private String label;
    private String name;

    private CommentActionEnum(String label, String name) {
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
