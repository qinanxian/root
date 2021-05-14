package com.vekai.crops.etl.model;

/**
 * kettle步骤度量
 */
public class KettleStepStatus {
    private String stepName;
    private String copyRow;
    private String readRow;
    private String writeNum;
    private String inputNum;
    private String outputNum;
    private String updataNum;
    private String rejectNum;
    private String errorNum;
    private String statues;
    private String useTime;
    private String speed;

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getCopyRow() {
        return copyRow;
    }

    public void setCopyRow(String copyRow) {
        this.copyRow = copyRow;
    }

    public String getReadRow() {
        return readRow;
    }

    public void setReadRow(String readRow) {
        this.readRow = readRow;
    }

    public String getWriteNum() {
        return writeNum;
    }

    public void setWriteNum(String writeNum) {
        this.writeNum = writeNum;
    }

    public String getInputNum() {
        return inputNum;
    }

    public void setInputNum(String inputNum) {
        this.inputNum = inputNum;
    }

    public String getOutputNum() {
        return outputNum;
    }

    public void setOutputNum(String outputNum) {
        this.outputNum = outputNum;
    }

    public String getUpdataNum() {
        return updataNum;
    }

    public void setUpdataNum(String updataNum) {
        this.updataNum = updataNum;
    }

    public String getRejectNum() {
        return rejectNum;
    }

    public void setRejectNum(String rejectNum) {
        this.rejectNum = rejectNum;
    }

    public String getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(String errorNum) {
        this.errorNum = errorNum;
    }

    public String getStatues() {
        return statues;
    }

    public void setStatues(String statues) {
        this.statues = statues;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
