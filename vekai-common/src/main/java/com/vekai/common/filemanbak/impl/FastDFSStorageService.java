//package com.vekai.common.filemanbak.impl;
//
//import com.vekai.common.autoconfigure.CommonProperties;
//import com.vekai.common.filemanbak.bean.FileStoreResult;
//import com.vekai.common.fileman.service.DownloadCB;
//import com.vekai.common.fileman.service.FileNaming;
//import com.vekai.common.fileman.service.FileStorageService;
//import com.vekai.runtime.kit.StringKit;
//import org.csource.fastdfs.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.annotation.PreDestroy;
//import java.io.*;
//import java.math.BigInteger;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.security.MessageDigest;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//public class FastDFSStorageService implements FileStorageService {
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    private final CommonProperties.Dfs dfs;
//
//    private final static int DEFAULT_DOWNLOAD_LARGE_SIZE = 1024 * 1024 * 5; // (5MB);
//    private TrackerClient trackerClient;
//    private volatile TrackerServer trackerServer;
//
//
//    @Autowired
//    FileNaming fileNaming;
//
//    private ScheduledExecutorService scheduleExecutor;
//    private volatile long lastTimeFlag;
//
//    public FastDFSStorageService(CommonProperties.Dfs dfs) {
//        this.dfs = dfs;
//        this.trackerClient = new TrackerClient();
//        try {
//            this.trackerServer = this.trackerClient.getConnection();
//            if (null == trackerServer)
//                failIfNonzero(trackerClient.getErrorCode());
//        } catch (IOException ex) {
//            logger.error("create tracker server error", ex);
//            throw wrapIfNecessary(ex);
//        }
//        this.lastTimeFlag = System.currentTimeMillis();
//        scheduleExecutor = Executors.newSingleThreadScheduledExecutor();
//        cycleCheck();
//    }
//
//    @PreDestroy
//    public void destroy() {
//        this.scheduleExecutor.shutdownNow();
//    }
//
//    private void cycleCheck() {
//        final long interval = dfs.getTrackerCheckCycleSeconds() * 1000;
//        scheduleExecutor.scheduleWithFixedDelay(() -> {
//            if (System.currentTimeMillis() - lastTimeFlag < interval || Thread.currentThread().isInterrupted())
//                return;
//            boolean isOk = false;
//            try {
//                isOk = ProtoCommon.activeTest(this.trackerServer.getSocket());
//            } catch (IOException ex) {
//                logger.warn("trackServer connection test fail", ex);
//            }
//            if (!isOk) {
//                try {
//                    this.trackerServer = this.trackerClient.getConnection();
//                    if (null == trackerServer)
//                        logger.error("cannot connect to trackerServer");
//                } catch (IOException ex) {
//                    logger.error("cannot connect to trackerServer", ex);
//                    this.trackerServer = null;
//                }
//            }
//            lastTimeFlag = System.currentTimeMillis();
//        }, 30, dfs.getTrackerCheckCycleSeconds(), TimeUnit.SECONDS);
//    }
//
////    private void cleanTempFile() {
////        this.scheduleExecutor.scheduleAtFixedRate(() -> {
////
////        }, 10, 10, TimeUnit.MINUTES);
////    }
//
//    protected StorageClient1 createStorageClient() {
//        if (null == this.trackerServer)
//            throw new RuntimeException("cannot connect to tracker server");
//        StorageClient1 client = new StorageClient1(this.trackerServer, null);
//        lastTimeFlag = System.currentTimeMillis();
//        return client;
//    }
//    @Override
//    public FileStoreResult storeAlsoHistory(MultipartFile multipartFile, String fileEntityId, String lastFileName,
//                                            String lastVersion, String lastSignature) {
//        if (multipartFile.isEmpty()) return FileStoreResult.EMPTY;
//        long size = multipartFile.getSize();
//        String fileExt = getFileExt(multipartFile.getOriginalFilename());
//        try {
//            StorageClient1 client = createStorageClient();
//            UploadCallback callback = new UploadCallback(multipartFile, logger);
//            String newFileId = client.upload_file1(null, size, callback, fileExt, null);
//            checkIfResNull(newFileId, client);
//            String currentSignature = new BigInteger(1, callback.messageDigest.digest()).toString(16);
//            if (null == lastFileName || null == lastSignature)
//                return new FileStoreResult(newFileId, currentSignature, null, 1);
//            if (currentSignature.equals(lastSignature)) {
//                int res = client.delete_file1(newFileId);
//                if (0 != res)
//                    logger.warn("fastdfs delete fileId: " + newFileId + ", res code:", res);
//                return FileStoreResult.EMPTY;
//            }
//            Integer versionNum = Integer.valueOf(lastSignature);
//            return new FileStoreResult(newFileId, currentSignature, lastFileName, versionNum + 1);
//        } catch (Exception ex) {
//            logger.error("fastdfs store fileEntityId " + fileEntityId + " fileName " +
//                    multipartFile.getOriginalFilename() + " error", ex);
//            throw wrapIfNecessary(ex);
//        }
//    }
//
//
//
//    static class UploadCallback implements org.csource.fastdfs.UploadCallback {
//        private final InputStream inputStream;
//        private final long fileSize;
//        private final MessageDigest messageDigest;
//        private final Logger logger;
//
//        public UploadCallback(MultipartFile multipartFile, Logger logger) {
//            try {
//                this.inputStream = multipartFile.getInputStream();
//                this.fileSize = multipartFile.getSize();
//                this.messageDigest = MessageDigest.getInstance("MD5");
//                this.logger = logger;
//            } catch (Exception ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//
//        @Override
//        public int send(OutputStream out) throws IOException {
//            long remainBytes = fileSize;
//            byte[] buff = new byte[256 * 1024];
//            int bytes;
//            while (remainBytes > 0) {
//                try {
//                    if ((bytes = inputStream.read(buff, 0, remainBytes > buff.length ? buff.length : (int) remainBytes)) < 0) {
//                        return -1;
//                    }
//                } catch (IOException ex) {
//                    logger.error("inputStream read error", ex);
//                    return -1;
//                }
//                if (bytes == 0) continue;
//                out.write(buff, 0, bytes);
//                remainBytes -= bytes;
//                messageDigest.update(buff, 0, bytes);
//            }
//
//            return 0;
//        }
//    }
//
//    private String getFileExt(String originalName) {
//        if (StringKit.isBlank(originalName))
//            return null;
//        int index;
//        if ((index = originalName.lastIndexOf(".")) > 0)
//            return originalName.substring(index + 1);
//        return null;
//    }
//
//    @Override
//    public String store(InputStream input, String fileName, String fileEntityId, String lastFileName) {
//        try {
//            StorageClient1 client = createStorageClient();
//            if (StringKit.isNoneBlank(lastFileName)) {
//                int res = client.truncate_file1(lastFileName);
//                failIfNonzero(res);
//                res = client.modify_file1(lastFileName, 0, input.available(),
//                        new UploadStream(input, input.available()));
//                failIfNonzero(res);
//                return lastFileName;
//            } else {
//                String newFileId = client.upload_file1(null, input.available(), new UploadStream(input,
//                        input.available()), getFileExt(fileName), null);
//                return checkIfResNull(newFileId, client);
//            }
//        } catch (Exception ex) {
//            logger.error("fastdfs store fileEntityId " + fileEntityId + " fileName " + fileName + " error", ex);
//            throw wrapIfNecessary(ex);
//        } finally {
//            try {
//                input.close();
//            } catch (IOException ex) {
//                logger.error("close input stream error", ex);
//            }
//        }
//    }
//
//    @Override
//    public String store(byte[] inputBytes, String fileName, String fileEntityId, String lastFileName) {
//        try {
//            StorageClient1 client = createStorageClient();
//            if (StringKit.isNoneBlank(lastFileName)) {
//                int res = client.truncate_file1(lastFileName);
//                failIfNonzero(res);
//                res = client.modify_file1(lastFileName, 0, inputBytes);
//                failIfNonzero(res);
//                return lastFileName;
//            } else {
//                String newFileId = client.upload_file1(inputBytes, getFileExt(fileName), null);
//                return checkIfResNull(newFileId, client);
//            }
//        } catch (Exception ex) {
//            logger.error("fastdfs store fileEntityId " + fileEntityId + " fileName " + fileName + " error", ex);
//            throw wrapIfNecessary(ex);
//        }
//    }
//
//    @Override
//    public String copy(String srcFileEntityId, String srcFileName, long fileSize, String destFileEntityId, String destFileName) {
//        try {
//            StorageClient1 client = createStorageClient();
//            FileInfo fileInfo = client.get_file_info1(destFileName);
//            if (null != fileInfo) {
//                client.truncate_file1(destFileName);
//                client.modify_file1(destFileName, 0, fileSize,
//                        new CopyUploadCallback(createStorageClient(), srcFileEntityId));
//                return destFileName;
//            } else {
//                String newFileId = client.upload_file1(null, fileSize,
//                        new CopyUploadCallback(createStorageClient(), srcFileEntityId),
//                        getFileExt(srcFileName), null);
//                return newFileId;
//            }
//        } catch (Exception ex) {
//            throw wrapIfNecessary(ex);
//        }
//    }
//
//    //todo cleanup temp file
//    @Override
//    public InputStream open(String fileEntityId, String fileName, long knownSize) {
//        if (knownSize <= 0)
//            return new ByteArrayInputStream(new byte[0]);
//        StorageClient1 client = createStorageClient();
//        try {
//            if (knownSize < DEFAULT_DOWNLOAD_LARGE_SIZE) {
//                return new ByteArrayInputStream(client.download_file1(fileName));
//            } else {
//                String tempFile = fileNaming.getTempFileRealPath(fileName);
//                int res = client.download_file1(fileName, tempFile);
//                failIfNonzero(res);
//                return new TempFileInputStream(tempFile);
//            }
//        } catch (Exception ex) {
//            logger.error("fastdfs download fileEntityId " + fileEntityId + " fileName " + fileName + " error", ex);
//            throw wrapIfNecessary(ex);
//        }
//    }
//
//    static class TempFileInputStream extends FileInputStream {
//        private final String filePath;
//        public TempFileInputStream(String filePath) throws FileNotFoundException{
//            super(filePath);
//            this.filePath = filePath;
//        }
//        @Override
//        public void close() throws IOException{
//            super.close();
//            Files.delete(Paths.get(filePath));
//        }
//    }
//
//    @Override
//    public void openAction(String fileEntityId, String fileName, long knownSize, DownloadCB downloadCallback) {
//        if (knownSize <= 0) {
//            downloadCallback.recv(0, new byte[0], 0);
//            return;
//        }
//        try {
//            StorageClient1 client = createStorageClient();
//            int res = client.download_file1(fileName, new DownloadCallbackAdapter(downloadCallback));
//            failIfNonzero(res);
//        } catch (Exception ex) {
//            logger.error("fastdfs download fileEntityId " + fileEntityId + " fileName " + fileName + " error", ex);
//            throw wrapIfNecessary(ex);
//        }
//    }
//
//    @Override
//    public InputStream openHist(String fileEntityId, String fileName, String versionCode, long knownSize) {
//        return open(fileEntityId, fileName, knownSize);
//    }
//
//    @Override
//    public void openHistAction(String fileEntityId, String fileName, String versionCode, long knownSize, DownloadCB downloadCallback) {
//        openAction(fileEntityId, fileName, knownSize, downloadCallback);
//    }
//
//    private static void failIfNonzero(int res) {
//        if (res != 0)
//            throw new RuntimeException("fastdfs res code " + res);
//    }
//
//    private static <T> T checkIfResNull(T res, StorageClient client) {
//        if (null == res) {
//            throw new RuntimeException("fastdfs error code " + client.getErrorCode());
//        }
//        return res;
//    }
//
//    @Override
//    public void delete(String fileEntityId, String fileName) {
//        try {
//            StorageClient1 client = createStorageClient();
//            int res = client.delete_file1(fileName);
//            failIfNonzero(res);
//        } catch (Exception ex) {
//            logger.error("fastdfs delete fileEntityId " + fileEntityId + " fileName " + fileName + " error", ex);
//            throw wrapIfNecessary(ex);
//        }
//    }
//
//    @Override
//    public void deleteHist(String fileEntityId, String fileName, String versionCode) {
//        delete(fileEntityId, fileName);
//    }
//
//    static class DownloadCallbackAdapter implements DownloadCallback {
//        private final DownloadCB downloadCB;
//
//        public DownloadCallbackAdapter(DownloadCB downloadCB) {
//            this.downloadCB = downloadCB;
//        }
//
//        @Override
//        public int recv(long file_size, byte[] data, int bytes) {
//            return downloadCB.recv(file_size, data, bytes);
//        }
//    }
//
//    class CopyUploadCallback implements org.csource.fastdfs.UploadCallback {
//        private final StorageClient1 downloadClient;
//        private final String fileId;
//
//        CopyUploadCallback(StorageClient1 downloadClient, String fileId) {
//            this.downloadClient = downloadClient;
//            this.fileId = fileId;
//        }
//
//        @Override
//        public int send(OutputStream outputStream) throws IOException {
//            try {
//                return downloadClient.download_file1(fileId, new CopyDownloadCallback(outputStream));
//            } catch (Exception ex){
//                if (ex instanceof IOException) {
//                    throw (IOException) ex;
//                } else {
//                    throw wrapIfNecessary(ex);
//                }
//            }
//        }
//    }
//
//    static class CopyDownloadCallback implements DownloadCallback {
//        private final OutputStream outputStream;
//
//        CopyDownloadCallback(OutputStream outputStream) {
//            this.outputStream = outputStream;
//        }
//
//        @Override
//        public int recv(long file_size, byte[] data, int bytes) {
//            if (bytes > 0) {
//                try {
//                    outputStream.write(data, 0, bytes);
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//            return 0;
//        }
//    }
//}
