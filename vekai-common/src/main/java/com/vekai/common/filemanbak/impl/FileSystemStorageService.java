//package com.vekai.common.filemanbak.impl;
//
//import com.vekai.common.filemanbak.bean.FileStoreResult;
//import com.vekai.common.fileman.service.DownloadCB;
//import com.vekai.common.fileman.service.FileNaming;
//import com.vekai.common.fileman.service.FileStorageService;
//import com.vekai.runtime.kit.FileKit;
//import com.vekai.runtime.kit.LogKit;
//import com.vekai.runtime.kit.StringKit;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.*;
//import java.math.BigInteger;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.security.MessageDigest;
//import java.util.function.BiConsumer;
//
////todo fileName shall not be from originalFileName()
//public class FileSystemStorageService implements FileStorageService {
//    final Logger logger = LoggerFactory.getLogger(this.getClass());
//    private final static int DEFAULT_LARGE_SIZE = 1024 * 1024 * 15; // (15MB);
//    private final int largeSize;
//
//    public FileSystemStorageService() {
//        this(DEFAULT_LARGE_SIZE);
//    }
//
//    public FileSystemStorageService(int largeSizeSetting) {
//        this.largeSize = largeSizeSetting;
//    }
//
//
//    private FileNaming fileNaming;
//
//    public void setFileNaming(FileNaming fileNaming) {
//        this.fileNaming = fileNaming;
//    }
//
//    // return {
//    //  fileId:         just fileName
//    //  md5:            newSignature
//    //  historyFileId:  historyVersionCode "0" means no need version and no history file, otherwise new created history versionCode
//    // }
//
//    // if front end can calculate MD5, it will be good
//    @Override
//    public FileStoreResult storeAlsoHistory(MultipartFile multipartFile, String fileEntityId, String lastFileName,
//                                            String lastVersion, String lastSignature) {
//        if (multipartFile.isEmpty())
//            return FileStoreResult.EMPTY;
//        boolean hisExist = !(StringKit.isEmpty(lastSignature) || StringKit.isEmpty(lastVersion) ||
//                "0".equals(lastVersion));
//        if (!hisExist) lastVersion = "0";
//        String fileName = getFileName(multipartFile.getOriginalFilename());
//        String realFileName = fileNaming.getFileRealPath(fileName, fileEntityId);
//        Path realPath = Paths.get(realFileName);
//        Path historyPath = Paths.get(fileNaming.getFileHistRealPath(fileName, fileEntityId, lastVersion));
//        long size = multipartFile.getSize();
//        try {
//            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
//            String currentSignature = null;
//            if (hisExist && size >= largeSize) {
//                InputStream inputStream = multipartFile.getInputStream();
//                byte[] buffer = new byte[1024 * 256];
//                int len;
//                while ((len = inputStream.read(buffer)) >= 0) {
//                    if (len == 0) continue;
//                    messageDigest.update(buffer, 0, len);
//                }
//                currentSignature = new BigInteger(1, messageDigest.digest()).toString(16);
//                if (currentSignature.equals(lastSignature)) {
//                    if (fileName.equals(lastFileName))
//                        return FileStoreResult.EMPTY;
//                    Path originalPath = Paths.get(fileNaming.getFileRealPath(lastFileName, fileEntityId));
//                    Files.move(originalPath, realPath);
//                    return new FileStoreResult(fileName, null, null, null);
//                }
//            }
//            if (Files.exists(realPath)) {
//                try {
//                    Files.move(realPath, historyPath);
//                }catch (Exception e){
//                    LogKit.warn("",e);
//                }
//            }
//            internalStore(multipartFile.getInputStream(), realFileName, null == currentSignature ? (bytes, len) ->
//                    messageDigest.update(bytes, 0, len) : null);
//            if (null == currentSignature)
//                currentSignature = new BigInteger(1, messageDigest.digest()).toString(16);
//            if (!hisExist)
//                return new FileStoreResult(fileName, currentSignature, null, 1);
//            else {
//                int versionNum = Integer.valueOf(lastVersion);
//                if (!currentSignature.equals(lastSignature)) {
//                    ++versionNum;
//                    return new FileStoreResult(fileName, currentSignature, lastFileName, versionNum);
//                } else {
//
//                    if (Files.exists(historyPath))
//                        Files.delete(historyPath);
//                    return new FileStoreResult(fileName, null, null, null);
//                }
//            }
//        } catch (Exception ex) {
//            logger.error("write multipartFile to FileSystem error", ex);
//            throw new RuntimeException(ex);
//        }
//
//    }
//
//    private String getFileName(String originalName) {
//        if (StringKit.isBlank(originalName))
//            return String.valueOf(System.nanoTime());
//        int index;
//        if ((index = originalName.lastIndexOf("/")) >= 0)
//            originalName = originalName.substring(index + 1);
//        if ((index = originalName.lastIndexOf("\\")) >= 0)
//            originalName = originalName.substring(index + 1);
//        return originalName;
//    }
//
//    @Override
//    public String store(InputStream input, String fileName, String fileEntityId, String lastFileName) {
//        fileName = getFileName(fileName);
//        deleteExisting(fileName, fileEntityId, lastFileName);
//        internalStore(input, fileNaming.getFileRealPath(fileName, fileEntityId), null);
//        return fileName;
//    }
//
//    @Override
//    public String copy(String srcFileEntityId, String srcFileName, long fileSize, String destFileEntityId, String destFileName) {
//        String srcRealName = fileNaming.getFileRealPath(srcFileName, srcFileEntityId);
//        String destRealName = fileNaming.getFileRealPath(destFileName, destFileEntityId);
//        try {
//            Files.move(Paths.get(srcRealName), Paths.get(destRealName));
//            return destFileName;
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//
//    private String internalStore(InputStream input, String realName, BiConsumer<byte[], Integer> biConsumer) {
//        File realFile = new File(realName);
//        try (FileOutputStream fos = new FileOutputStream(FileKit.touchFile(realFile), false);
//             InputStream inputS = input) {
//            byte[] buffer = new byte[1024 * 256];
//            int len;
//            while ((len = inputS.read(buffer)) >= 0) {
//                if (len == 0) continue;
//                fos.write(buffer, 0, len);
//                if (null != biConsumer)
//                    biConsumer.accept(buffer, len);
//            }
//        } catch (IOException ex) {
//            logger.error("write input to FileSystem error,file:"+realFile.getAbsolutePath(), ex);
//            throw new RuntimeException(ex);
//        }
//        return realName;
//    }
//
//    private void deleteExisting(String fileName, String fileEntityId, String lastFileName) {
//        if (StringKit.isNoneBlank(lastFileName) && !fileName.equals(lastFileName)) {
//            String existingFile = fileNaming.getFileRealPath(lastFileName, fileEntityId);
//            Path existingPath = Paths.get(existingFile);
//            if (Files.exists(existingPath)) {
//                try {
//                    Files.delete(existingPath);
//                } catch (IOException ex) {
//                    logger.error("delete file " + existingFile + " error", ex);
//                    // ignore error
//                }
//            }
//        }
//    }
//
//    @Override
//    public String store(byte[] inputBytes, String fileName, String fileEntityId, String lastFileName) {
//        fileName = getFileName(fileName);
//        deleteExisting(fileName, fileEntityId, lastFileName);
//        String realName = fileNaming.getFileRealPath(getFileName(fileName), fileEntityId);
//        try (FileOutputStream fos = new FileOutputStream(realName, false)) {
//            fos.write(inputBytes);
//        } catch (IOException ex) {
//            logger.error("write input to FileSystem error", ex);
//            throw new RuntimeException(ex);
//        }
//        return fileName;
//    }
//
//    @Override
//    public InputStream open(String fileEntityId, String fileName, long knownSize) {
//        try {
//            if (knownSize <= 0)
//                return new ByteArrayInputStream(new byte[0]);
//            FileInputStream fis = new FileInputStream(fileNaming.getFileRealPath(fileName, fileEntityId));
//            return fis;
//        } catch (IOException ex) {
//            logger.error("open fileId " + fileEntityId + " fileName " + fileName + " error", ex);
//            throw new RuntimeException(ex);
//        }
//    }
//
//    @Override
//    public InputStream openHist(String fileEntityId, String fileName, String versionCode, long knownSize) {
//        try {
//            if (knownSize <= 0)
//                return new ByteArrayInputStream(new byte[0]);
//            return new FileInputStream(fileNaming.getFileHistRealPath(fileName, fileEntityId, versionCode));
//        } catch (IOException ex) {
//            logger.error("open fileId " + fileEntityId + " fileName " + fileName + " error", ex);
//            throw new RuntimeException(ex);
//        }
//    }
//
//    @Override
//    public void openHistAction(String fileEntityId, String fileName, String versionCode, long knownSize, DownloadCB downloadCallback) {
//        try {
//            if (knownSize <= 0){
//                downloadCallback.recv(0, new byte[0], 0);
//                return;
//            }
//            actionOnInputStream(fileNaming.getFileHistRealPath(fileName, fileEntityId, versionCode), downloadCallback);
//        } catch (IOException ex) {
//            logger.error("open fileEntityId " + fileEntityId + " fileName " + fileName + " error", ex);
//            throw new RuntimeException(ex);
//        }
//    }
//
//    @Override
//    public void openAction(String fileEntityId, String fileName, long knownSize, DownloadCB downloadCallback) {
//        try {
//            if (knownSize <= 0){
//                downloadCallback.recv(0, new byte[0], 0);
//                return;
//            }
//            actionOnInputStream(fileNaming.getFileRealPath(fileName, fileEntityId), downloadCallback);
//        } catch (IOException ex) {
//            logger.error("open fileEntityId " + fileEntityId + " fileName " + fileName + " error", ex);
//            throw new RuntimeException(ex);
//        }
//    }
//
//    private void actionOnInputStream(String path, DownloadCB downloadCallback) throws IOException{
//        FileInputStream fis = new FileInputStream(path);
//        int len = fis.available();
//        int bufferSize = 1024 * 256 < len ? 1024 * 256 : len;
//        int readLen;
//        byte[] buffer = new byte[bufferSize];
//        while ((readLen = fis.read(buffer)) >= 0) {
//            if (readLen == 0) continue;
//            downloadCallback.recv(len, buffer, readLen);
//        }
//    }
//
//    @Override
//    public void delete(String fileEntityId, String fileName) {
//        String file = fileNaming.getFileRealPath(fileName, fileEntityId);
//        Path path = Paths.get(file);
//        delete(path);
//    }
//
//    private void delete(Path path) {
//        if (Files.exists(path)) {
//            try {
//                Files.delete(path);
//            } catch (IOException ex) {
//                logger.error("delete file " + path.toString(), ex);
//                throw new RuntimeException(ex);
//            }
//        }
//    }
//
//    @Override
//    public void deleteHist(String fileEntityId, String fileName, String versionCode) {
//        String file = fileNaming.getFileHistRealPath(fileName, fileEntityId, versionCode);
//        Path path = Paths.get(file);
//        delete(path);
//    }
//}
