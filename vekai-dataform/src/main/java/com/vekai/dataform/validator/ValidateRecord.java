package com.vekai.dataform.validator;

import java.io.Serializable;

/**
 * 校验记录项，针对每条记录的每一个数据项的校验结果
 */
public class ValidateRecord implements Serializable {
    /**
     * 是否为必需的，强制的
     */
    private boolean mandatory = false;
    private boolean passed = true;
    private String message;
    private ValidateType validateType = ValidateType.Validator;

    public boolean isMandatory() {
        return mandatory;
    }

    public ValidateRecord setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public boolean isPassed() {
        return passed;
    }

    public ValidateRecord setPassed(boolean passed) {
        this.passed = passed;
        return this;
    }

    public ValidateType getValidateType() {
        return validateType;
    }

    public ValidateRecord setValidateType(ValidateType validateType) {
        this.validateType = validateType;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ValidateRecord setMessage(String message) {
        this.message = message;
        return this;
    }
}
