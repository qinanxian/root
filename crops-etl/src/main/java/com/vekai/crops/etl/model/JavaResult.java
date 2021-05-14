package com.vekai.crops.etl.model;

import java.util.List;

public class JavaResult {
    private String msg;
    private String errorMsg; // 错误信息

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
