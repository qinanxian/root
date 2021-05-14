package com.vekai.auth.autoconfigure;

import java.util.List;
import java.util.Map;

/**
 * 审计处理配置
 */
public class AuditProperties {
    private boolean enable = true;
    private List<String> igonreColumns;
    private List<String> disableTables;
    private Map<String,String> actionSetting;   //审计哪些数据表的哪些动作，KEY为数据表正则表达式，VALUE为动作，

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<String> getIgonreColumns() {
        return igonreColumns;
    }

    public void setIgonreColumns(List<String> igonreColumns) {
        this.igonreColumns = igonreColumns;
    }

    public List<String> getDisableTables() {
        return disableTables;
    }

    public void setDisableTables(List<String> disableTables) {
        this.disableTables = disableTables;
    }

    public Map<String, String> getActionSetting() {
        return actionSetting;
    }

    public void setActionSetting(Map<String, String> actionSetting) {
        this.actionSetting = actionSetting;
    }
}
