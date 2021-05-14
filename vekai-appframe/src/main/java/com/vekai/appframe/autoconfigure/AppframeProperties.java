package com.vekai.appframe.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 基础框架本配置类
 */
@ConfigurationProperties(prefix = "com.vekai.appframe", ignoreUnknownFields = true)
public class AppframeProperties {
    /*批量上传文件临时存放目录*/
    private String temporaryBatchStorage = "/data/amix/batch/temporary";

    public String getTemporaryBatchStorage() {
        return temporaryBatchStorage;
    }

    public void setTemporaryBatchStorage(String temporaryBatchStorage) {
        this.temporaryBatchStorage = temporaryBatchStorage;
    }
}
