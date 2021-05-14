package com.vekai.workflow.model.enums;

/**
 * 流程节点跳转类型（指没有画线的跳转）
 */
public enum TaskJumpEnum {
    /**
     * 任务撤回
     */
    RETRIEVE("撤回", "retrieve"),
    BACKFRONT("退回", "backfront"),
    BACKORIGIN("驳回","backorigin"),
    /** 退回之前的任一节点，可按退回路线返回 **/
    BACKTRACK("退回修改","backtrack"),
    FORWARD("跳转","forward");


    private String label;
    private String name;

    private TaskJumpEnum(String label, String name) {
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
