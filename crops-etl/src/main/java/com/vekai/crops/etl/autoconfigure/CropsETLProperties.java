package com.vekai.crops.etl.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.vekai.crops.etl", ignoreUnknownFields = true)
public class CropsETLProperties {
    // kettle定义文件存储路径
    private String kettleSavePath;

    public String getKettleSavePath() {
        return kettleSavePath;
    }

    public void setKettleSavePath(String kettleSavePath) {
        this.kettleSavePath = kettleSavePath;
    }
}
