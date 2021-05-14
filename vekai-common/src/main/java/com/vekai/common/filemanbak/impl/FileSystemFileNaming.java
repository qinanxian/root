//package com.vekai.common.filemanbak.impl;
//
//import com.vekai.common.autoconfigure.CommonProperties;
//import com.vekai.common.fileman.service.FileNaming;
//
//public class FileSystemFileNaming implements FileNaming {
//    private String directory = "/";
//
//
//    private final CommonProperties commonProperties;
//
//    public FileSystemFileNaming(CommonProperties commonProperties) {
//        this.commonProperties = commonProperties;
//    }
//
//    public String getDirectory() {
//        return directory;
//    }
//
//    @Override
//    public void setDirectory(String directory) {
//        this.directory = directory;
//    }
//
//    @Override
//    public String getFileRealPath(final String fileName, String fileEntityId) {
//        String startPath = commonProperties.getFileStorageRoot() + directory;
//        // String endPath = fileEntityId + "-" + fileName;
//        String endPath = fileEntityId +fileName.substring(fileName.lastIndexOf("."));
//        if (!startPath.endsWith("/")) startPath = startPath + "/";
//        if (endPath.startsWith("/")) endPath = endPath.substring(1);
//        return startPath + endPath;
//    }
//
//    @Override
//    public String getFileHistRealPath(final String fileName, String fileEntityId, String versionCode ) {
//        String startPath = commonProperties.getFileHistStorageRoot() + directory;
//        String endPath = fileEntityId + "-" + versionCode + "-" + fileName;
//        if (!startPath.endsWith("/")) startPath = startPath + "/";
//        if (endPath.startsWith("/")) endPath = endPath.substring(1);
//        return startPath + endPath;
//    }
//
//    @Override
//    public String getTempFileRealPath(final String fileName) {
//        String startPath = commonProperties.getTemporaryStorage() + directory;
//        String endPath = fileName;
//        if (!startPath.endsWith("/")) startPath = startPath + "/";
//        if (endPath.startsWith("/")) endPath = endPath.substring(1);
//        return startPath + endPath;
//    }
//}
