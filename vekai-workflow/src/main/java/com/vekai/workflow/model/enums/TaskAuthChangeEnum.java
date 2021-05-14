package com.vekai.workflow.model.enums;

/**
 * 任务权限变更
 * @author 左晓敏 <xmzuo@amarsoft.com>
 * @date 2018-01-02
 */
public enum TaskAuthChangeEnum {
    CLAIMTASK("任务签收","claimTask"),
    UNCLAIMTASK("取消任务签收","unClaimTask"),
    CHANGEASSIGNEE("更改受理人","changeAssignee"),
    ADDCANDIDATEUSER("添加候选人","deleteCandidateUser"),
    DELETECANDIDATEUSER("移除候选人","deleteCandidateUser"),
    ADDCANDIDATEGROUP("添加候选组","addCandidateGroup"),
    DELETECANDIDATEGROUP("移除候选组","deleteCandidateGroup"),
    ;

    private String label;
    private String name;

    private TaskAuthChangeEnum(String label, String name) {
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
