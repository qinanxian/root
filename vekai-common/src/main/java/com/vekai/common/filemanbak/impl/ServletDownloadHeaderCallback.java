//package com.vekai.common.filemanbak.impl;
//
//import com.vekai.common.fileman.entity.FileEntity;
//import com.vekai.common.filemanbak.service.DownloadCB;
//import com.vekai.common.filemanbak.service.FileEntityDownloadCallback;
//
//import javax.servlet.http.HttpServletResponse;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//
//import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
//import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
//
//public class ServletDownloadHeaderCallback implements FileEntityDownloadCallback {
//    private final DownloadCB delegate;
//    private boolean started = false;
//    private final HttpServletResponse response;
//
//    public ServletDownloadHeaderCallback(DownloadCB delegate, HttpServletResponse response) {
//        this.delegate = delegate;
//        this.response = response;
//    }
//
//    @Override
//    public int recv(FileEntity fileEntity, boolean isHist, long fileSize, byte[] data, int bytes) {
//        if (!started) {
//            started = true;
//            response.setHeader(CONTENT_TYPE, fileEntity.getContentType());
//            try {
//                response.addHeader(CONTENT_DISPOSITION, "attachment; filename=\"" +
//                        URLEncoder.encode(fileEntity.getName(), "UTF-8") + "\"");
//            } catch (UnsupportedEncodingException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//        return delegate.recv(fileSize, data, bytes);
//    }
//}
