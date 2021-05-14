package com.vekai.auth.audit;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.vekai.auth.audit")
public class AutoFieldProperties {
    private String createdByPropertyName = "createdBy";
    private String updatedByPropertyName = "updatedBy";
    private String createdTimePropertyName = "createdTime";
    private String updatedTimePropertyName = "updatedTime";

    public String getCreatedByPropertyName() {
        return createdByPropertyName;
    }

    public void setCreatedByPropertyName(String createdByPropertyName) {
        this.createdByPropertyName = createdByPropertyName;
    }

    public String getUpdatedByPropertyName() {
        return updatedByPropertyName;
    }

    public void setUpdatedByPropertyName(String updatedByPropertyName) {
        this.updatedByPropertyName = updatedByPropertyName;
    }

    public String getCreatedTimePropertyName() {
        return createdTimePropertyName;
    }

    public void setCreatedTimePropertyName(String createdTimePropertyName) {
        this.createdTimePropertyName = createdTimePropertyName;
    }

    public String getUpdatedTimePropertyName() {
        return updatedTimePropertyName;
    }

    public void setUpdatedTimePropertyName(String updatedTimePropertyName) {
        this.updatedTimePropertyName = updatedTimePropertyName;
    }
}
