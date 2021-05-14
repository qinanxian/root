package com.vekai.dataform.mapper.impl.po;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by luyu on 2017/12/28.
 */
@Table(name = "FOWK_DATAFORM_VALIDATOR")
public class FormElementValidatorPO implements Serializable,Cloneable {
    @Id
    private String dataformId;
    @Id
    private String elementCode;
    @Id
    private String code;
    private String runAt;
    private String mode;
    private String expr;
    private String triggerEvent;
    private String message;
    @Column(name = "MESSAGE_I18N_CODE")
    private String messageI18nCode;


    public String getDataformId() {
        return dataformId;
    }

    public void setDataformId(String dataformId) {
        this.dataformId = dataformId;
    }

    public String getElementCode() {
        return elementCode;
    }

    public void setElementCode(String elementCode) {
        this.elementCode = elementCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRunAt() {
        return runAt;
    }

    public void setRunAt(String runAt) {
        this.runAt = runAt;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public String getTriggerEvent() {
        return triggerEvent;
    }

    public void setTriggerEvent(String triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageI18nCode() {
        return messageI18nCode;
    }

    public void setMessageI18nCode(String messageI18nCode) {
        this.messageI18nCode = messageI18nCode;
    }
}
