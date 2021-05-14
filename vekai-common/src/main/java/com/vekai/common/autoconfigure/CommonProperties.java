package com.vekai.common.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.vekai.common", ignoreUnknownFields = true)
public class CommonProperties {
    private String fileStorageRoot = "/data/crops/fileman";
    private String temporaryStorage ="/data/crops/temporary";
    private int fileBufferSize = 4096;
    private String fileStore = "LocalDisk";

    public String getFileStorageRoot() {
        return fileStorageRoot;
    }

    public void setFileStorageRoot(String fileStorageRoot) {
        this.fileStorageRoot = fileStorageRoot;
    }

    public String getTemporaryStorage() {
        return temporaryStorage;
    }

    public void setTemporaryStorage(String temporaryStorage) {
        this.temporaryStorage = temporaryStorage;
    }

    public int getFileBufferSize() {
        return fileBufferSize;
    }

    public void setFileBufferSize(int fileBufferSize) {
        this.fileBufferSize = fileBufferSize;
    }

    public String getFileStore() {
        return fileStore;
    }

    public void setFileStore(String fileStore) {
        this.fileStore = fileStore;
    }
}

