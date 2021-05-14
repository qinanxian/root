package com.vekai.crops.obiz.application.model;

import java.io.Serializable;

/**
 * 申请的任务状态
 */
public class ApplicationTaskState implements Serializable {
    private String procDefKey;
    private String taskDefKey;
    private String procId;
    private String procName;
    private String taskId;
    private String taskName;
    private String taskState;
    /** 还没有发起流程 */
    public static final String NOT_YET = "NotYet";
    /** 这笔业务目前与我无关 */
    public static final String NOT_CARE = "NotCare";
    /** 首岗未提交 */
    public static final String FIRST_UN_SUBMIT = "FirstUnSubmit";
    /** 首岗已提交 */
    public static final String FIRST_SUBMITTED = "FirstSubmitted";

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public static String getNotYet() {
        return NOT_YET;
    }

    public static String getNotCare() {
        return NOT_CARE;
    }

    public static String getFirstUnSubmit() {
        return FIRST_UN_SUBMIT;
    }

    public static String getFirstSubmitted() {
        return FIRST_SUBMITTED;
    }
}
