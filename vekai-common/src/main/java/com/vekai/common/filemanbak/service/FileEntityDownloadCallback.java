package com.vekai.common.filemanbak.service;

import com.vekai.common.fileman.entity.FileEntity;

@FunctionalInterface
public interface FileEntityDownloadCallback {
    int recv(FileEntity fileEntity, boolean isHist, long fileSize, byte[] data, int bytes);
}
