package com.vekai.common.filemanbak.service;

public interface DownloadCB {
    int recv(long file_size, byte[] data, int bytes);
}
