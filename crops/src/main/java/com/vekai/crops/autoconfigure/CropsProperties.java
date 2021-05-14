package com.vekai.crops.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.vekai.crops", ignoreUnknownFields = true)
public class CropsProperties {
    private boolean enableWebsocker = true;

    public boolean isEnableWebsocker() {
        return enableWebsocker;
    }

    public void setEnableWebsocker(boolean enableWebsocker) {
        this.enableWebsocker = enableWebsocker;
    }
}
