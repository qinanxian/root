package com.vekai.workflow.liteflow.model;



import java.util.List;

/**
 * 简式流程任务定义
 */
public class LiteTaskDef {
    /**
     * 任务定义KEY 如:T1->T2->T3
     */
    private String taskDefKey;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    private String taskName;
    private String nameHint;

    public String getNameHint() {
        return nameHint;
    }

    public void setNameHint(String nameHint) {
        this.nameHint = nameHint;
    }

    /**
     * 是否是并行任务
     * &是并行
     */
    private boolean isParallel;
    /**
     * 任务候选定义
     * 直接存储字符  如:  用户:xmzuo   角色: r:001
     */
    private List<String> candidates;

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }


    public boolean isParallel() {
        return isParallel;
    }

    public void setParallel(boolean parallel) {
        isParallel = parallel;
    }


    public List<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<String> candidates) {
        this.candidates = candidates;
    }
}
