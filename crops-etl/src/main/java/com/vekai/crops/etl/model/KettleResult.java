package com.vekai.crops.etl.model;

import java.util.List;

public class KettleResult {
    private List<KettleStepStatus> stepStatus;// 步骤度量
    private int errors; // 错误数量
    private String errorMsg; // 错误信息

    public List<KettleStepStatus> getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(List<KettleStepStatus> stepStatus) {
        this.stepStatus = stepStatus;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
