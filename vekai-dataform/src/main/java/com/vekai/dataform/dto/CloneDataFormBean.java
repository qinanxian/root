package com.vekai.dataform.dto;

/**
 * Created by luyu on 2017/12/29.
 */
public class CloneDataFormBean {
    private String newDataFormId;

    private String oldDataFormId;

    public String getNewDataFormId() {
        return newDataFormId;
    }

    public void setNewDataFormId(String newDataFormId) {
        this.newDataFormId = newDataFormId;
    }

    public String getOldDataFormId() {
        return oldDataFormId;
    }

    public void setOldDataFormId(String oldDataFormId) {
        this.oldDataFormId = oldDataFormId;
    }
}
