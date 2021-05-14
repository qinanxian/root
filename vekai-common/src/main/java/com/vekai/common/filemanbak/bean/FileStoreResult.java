package com.vekai.common.filemanbak.bean;

public class FileStoreResult {
    // means no change has been made
    public static final FileStoreResult EMPTY = new FileStoreResult(null, null, null, null);

    private final String fileId;
    private final String md5;
    private final String historyFileId;
    private final Integer currentVersion;

    public FileStoreResult(String fileId, String md5, String historyFileId, Integer currentVersion) {
        this.fileId = fileId;
        this.md5 = md5;
        this.historyFileId = historyFileId;
        this.currentVersion = currentVersion;
    }

    public String getFileId() {
        return fileId;
    }

    public String getMd5() {
        return md5;
    }

    public String getHistoryFileId() {
        return historyFileId;
    }

    public Integer getCurrentVersion() {
        return currentVersion;
    }
}
