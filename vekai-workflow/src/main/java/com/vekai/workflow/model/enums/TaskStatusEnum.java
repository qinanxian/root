package com.vekai.workflow.model.enums;

/**
 * 任务状态
 * 此类可改为任务操作枚举
 *
 * @author 左晓敏 <xmzuo@amarsoft.com>
 * @date 20161221
 */
public enum TaskStatusEnum {
    /**
     * 正常往前
     **/
    COMPLETED("正常提交", "completed"),
    /**
     * 任务撤回
     */
    RETRIEVE("撤回", "retrieve"),
    /**
     * 退回之前的任一节点，必需重新按之前的节点重新审批
     **/
    BACKFRONT("退回", "backfront"),
    /**
     * 退回至发起岗
     **/
    BACKORIGIN("驳回", "backorigin"),
    /**
     * 退回之前的任一节点，可按退回路线返回
     **/
    BACKTRACK("退回修改", "backtrack"),
    /**
     * 主要指向前跳转
     **/
    FORWARD("跳转", "forward"),
    /**
     * 流程否决之后，不能再生效。只能重新发起新流程
     **/
    REJECT("流程否决", "reject"),
    /**
     * 发起者废除流程
     **/
    ABOLISHED("流程作废", "abolished"),
    TAKEADVICE("征求他人意见", "takeAdvice"),
    FEEDBACK("反馈意见", "feedback"),
    DELIVERTO("转他人处理", "deliverTo"),
    NOTIFY("邮件传阅", "notify");

    private String label;
    private String name;

    TaskStatusEnum(String label, String name) {
        this.label = label;
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public static TaskStatusEnum getEnum(String value){//根据值获得实例
        TaskStatusEnum e=null;
        for(TaskStatusEnum e1:TaskStatusEnum.values())
            if(value.equals(e1.getName())){
                e=e1;
                break;
            }
        return e;
    }
}
