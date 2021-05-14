package com.vekai.workflow.model.enums;

/**
 * 流程状态
 * @author 左晓敏 <xmzuo@amarsoft.com>
 * @date 20161221
 */
public enum ProcStatusEnum {
    /** 待提交的流程 **/
    PRESUBMIT("待提交", "presubmit"),
    /** 审批中的流程 **/
    APPROVING("审批中", "approving"),
    /**
     * 任务撤回
     */
    RETRIEVE("撤回", "retrieve"),
    BACKFRONT("退回", "backfront"),
    /** 被驳回流程 **/
    BACKORIGIN("驳回","backorigin"),
    /** 退回修改流程 **/
    BACKTRACK("退回修改","backtrack"),
    /** 征求他人意见中的流程 **/
    TAKEADVICE("征求他人意见", "takeAdvice"),
    /** 否决的流程 **/
    REJECT("流程否决","reject"),
    /** 作废的流程 **/
    ABOLISHED("流程作废","abolished"),
    /** 正常完成的流程 **/
    FINISHED("批准", "finished");

    private String label;
    private String name;

    private ProcStatusEnum(String label, String name) {
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
