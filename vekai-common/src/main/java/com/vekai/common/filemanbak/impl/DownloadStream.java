//package com.vekai.common.filemanbak.impl;
//
//import com.vekai.common.fileman.service.DownloadCB;
//
//import java.io.IOException;
//import java.io.OutputStream;
//
//public class DownloadStream implements DownloadCB {
//    private OutputStream out;
//    private long currentBytes = 0;
//
//    public DownloadStream(OutputStream out) {
//        super();
//        this.out = out;
//    }
//
//    /**
//     * recv file content callback function, may be called more than once when the file downloaded
//     *
//     * @param fileSize file size
//     * @param data     data buff
//     * @param bytes    data bytes
//     * @return 0 success, return none zero(errno) if fail
//     */
//    public int recv(long fileSize, byte[] data, int bytes) {
//        try {
//            out.write(data, 0, bytes);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return -1;
//        }
//
//        currentBytes += bytes;
//        if (this.currentBytes == fileSize) {
//            this.currentBytes = 0;
//        }
//
//        return 0;
//    }
//}
