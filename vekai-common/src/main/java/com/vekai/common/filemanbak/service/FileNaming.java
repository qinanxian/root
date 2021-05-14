package com.vekai.common.filemanbak.service;

public interface FileNaming {
    void setDirectory(String directory);
    String getFileRealPath(final String fileName, String fileEntityId);
    String getFileHistRealPath(final String fileName, String fileEntityId, String versionCode);
    String getTempFileRealPath(final String fileName);
}
