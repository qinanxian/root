//package com.vekai.common.filemanbak.impl;
//
//import com.vekai.common.filemanbak.bean.FileStoreResult;
//import com.vekai.common.fileman.entity.FileEntity;
//import com.vekai.common.fileman.entity.FileEntityHist;
//import com.vekai.common.fileman.service.DownloadCB;
//import com.vekai.common.fileman.service.FileEntityDownloadCallback;
//import com.vekai.common.fileman.service.FileManageService;
//import com.vekai.common.fileman.service.FileStorageService;
//import com.vekai.runtime.kit.BeanKit;
//import com.vekai.runtime.kit.StringKit;
//import com.vekai.runtime.lang.BizException;
//import com.vekai.sql.core.BeanQuery;
//import com.vekai.sql.core.BeanUpdater;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.activation.MimetypesFileTypeMap;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.List;
//import java.util.function.Consumer;
//
//public class FileManageServiceImpl implements FileManageService {
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    @Autowired
//    private BeanQuery dataQuery;
//    @Autowired
//    private BeanUpdater dataUpdater;
//
//    final FileStorageService storageService;
//
//    public FileManageServiceImpl(FileStorageService fileStorageService) {
//        this.storageService = fileStorageService;
//    }
//
//    final MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
//
//    public FileEntity getFileEntity(String fileEntityId) {
//        return dataQuery.selectOne(FileEntity.class, "select * from CMON_FILE where FILE_ID=:fileId",
//                "fileId", fileEntityId);
//    }
//
//    public FileEntity getFileEntityByShowCode(String showCode) {
//        return dataQuery.selectOne(FileEntity.class, "select * from CMON_FILE where SHOW_CODE=:showCode",
//                "showCode", showCode);
//    }
//
//    public FileEntity getFileEntity(String objectType, String objectId) {
//        return dataQuery.selectOne(FileEntity.class, "select * from CMON_FILE where OBJECT_TYPE=:objectType AND OBJECT_ID=:objectId", "objectType",objectType, "objectId",objectId);
//    }
//
//    public FileEntityHist getFileEntityHist(String fileHistEntityId) {
//        return dataQuery.selectOne(FileEntityHist.class, "select * from CMON_FILE_HIST where " +
//                "FILE_HIST_ID=:fileHistId", "fileHistId", fileHistEntityId);
//    }
//
//    public List<FileEntityHist> getFileHistList(String fileEntityId) {
//        return dataQuery.selectList(FileEntityHist.class, "select * from CMON_FILE_HIST where FILE_ID=:fileId",
//                "fileId", fileEntityId);
//    }
//
//    @Override
//    public FileEntity saveFile(String fileEntityId, MultipartFile multipartFile, boolean writeHist,
//                               Consumer<FileEntity> actionBeforeSave) throws IOException {
//        FileEntity fileEntity;
//        if (StringKit.isBlank(fileEntityId) || "null".equalsIgnoreCase(fileEntityId)) {
//            fileEntity = new FileEntity();
//            dataUpdater.save(fileEntity); // create a new fileEntityId
//            fileEntityId = fileEntity.getFileId();
//        } else {
//            fileEntity = getFileEntity(fileEntityId);
//            if (null == fileEntity)
//                throw new BizException("fileEntityId: " + fileEntityId + " not exist");
//        }
//        int version = null == fileEntity.getVersionCode() ? 0 : Integer.valueOf(fileEntity.getVersionCode());
//        if (!writeHist) {
//            String newFile = storageService.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), fileEntityId,
//                    fileEntity.getStoredContent());
//            fileEntity.setStoredContent(newFile);
//            fileEntity.setVersionCode(String.valueOf(++version));
//
//        } else {
//            FileStoreResult result = storageService.storeAlsoHistory(multipartFile, fileEntityId, fileEntity.getStoredContent(),
//                    fileEntity.getVersionCode(), fileEntity.getSignature());
//            if (FileStoreResult.EMPTY == result) {
//                if (0 == version) {
//                    dataUpdater.delete(fileEntity);
//                    return null;
//                }
//            } else {
//                if (null != result.getHistoryFileId()) {
//                    FileEntityHist fileEntityHist = this.copyFileEntity2Hist(fileEntity);
//                    dataUpdater.save(fileEntityHist);
//                }
//                if (null != result.getFileId())
//                    fileEntity.setStoredContent(result.getFileId());
//                if (null != result.getCurrentVersion())
//                    fileEntity.setVersionCode(result.getCurrentVersion().toString());
//                if (null != result.getMd5())
//                    fileEntity.setSignature(result.getMd5());
//            }
//        }
//        fileEntity.setContentType(mimetypesFileTypeMap.getContentType(multipartFile.getOriginalFilename()));
//        fileEntity.setFileSize(multipartFile.getSize());
//        fileEntity.setName(multipartFile.getOriginalFilename());
//        actionBeforeSave.accept(fileEntity);
//        dataUpdater.save(fileEntity);
//        return fileEntity;
//    }
//
//    @Override
//    public FileEntity copy(String existingFileEntityId) {
//        if (StringKit.isBlank(existingFileEntityId))
//            throw new IllegalArgumentException("existingFileEntityId shall not be null when copy");
//        FileEntity fileEntity = getFileEntity(existingFileEntityId);
//        if (null == fileEntity)
//            throw new IllegalArgumentException("existingFileEntityId is not valid");
////        FileEntity newEntity = fileEntity.copy();
//        FileEntity newEntity = BeanKit.deepClone(fileEntity);
//        dataUpdater.insert(newEntity);
//        String newFileId = storageService.copy(existingFileEntityId, fileEntity.getStoredContent(),
//                newEntity.getFileSize(), newEntity.getFileId(), newEntity.getStoredContent());
//        if (!newFileId.equals(fileEntity.getStoredContent())) {
//            newEntity.setStoredContent(newFileId);
//            dataUpdater.insert(newEntity);
//        }
//        return newEntity;
//    }
//
//    public void delete(String fileId) {
//        FileEntity fileEntity = getFileEntity(fileId);
//        if (fileEntity == null) return;
//        storageService.delete(fileId, fileEntity.getStoredContent());
//        dataUpdater.delete(fileEntity);
//    }
//
//    public InputStream openFileInputStream(String fileId) {
//        FileEntity fileEntity = getFileEntity(fileId);
//        if (fileEntity == null) return null;
//        return storageService.open(fileEntity.getFileId(), fileEntity.getStoredContent(), fileEntity.getFileSize());
//    }
//
//    public InputStream openFileHistInputStream(String fileHistId) {
//        FileEntityHist fileEntityHist = getFileEntityHist(fileHistId);
//        if (null == fileEntityHist) return null;
//        return storageService.openHist(fileEntityHist.getFileId(), fileEntityHist.getStoredContent(),
//                fileEntityHist.getVersionCode(), fileEntityHist.getFileSize());
//    }
//
//    @Override
//    public void openFileInputStreamAction(String fileEntityId, FileEntityDownloadCallback fileEntityDownloadCallback) {
//        FileEntity fileEntity = getFileEntity(fileEntityId);
//        if (null != fileEntity) {
//            storageService.openAction(fileEntityId, fileEntity.getStoredContent(), fileEntity.getFileSize(),
//                    new DownloadCallbackAdapter(fileEntityDownloadCallback, fileEntity));
//        }
//    }
//
//    @Override
//    public void openFileHistInputStreamAction(String fileHistEntityId, FileEntityDownloadCallback fileEntityDownloadCallback) {
//        FileEntityHist fileEntityHist = getFileEntityHist(fileHistEntityId);
//        if (null == fileEntityHist) {
//            storageService.openHistAction(fileEntityHist.getFileId(), fileEntityHist.getStoredContent(),
//                    fileEntityHist.getVersionCode(), fileEntityHist.getFileSize(),
//                    new DownloadCallbackAdapter(fileEntityDownloadCallback, fileEntityHist));
//        }
//    }
//
//    @Override
//    public void servletDownloadFile(String fileEntityId, HttpServletResponse response) {
//        OutputStream outputStream;
//        try {
//            outputStream = response.getOutputStream();
//        } catch (IOException ex) {
//            logger.error("open servlet response error", ex);
//            throw new BizException("download error");
//        }
//        this.openFileInputStreamAction(fileEntityId, new ServletDownloadHeaderCallback(
//                new DownloadStream(outputStream), response));
//    }
//
//    @Override
//    public void servletDownloadFileHist(String fileEntityHistId, HttpServletResponse response) {
//        OutputStream outputStream;
//        try {
//            outputStream = response.getOutputStream();
//        } catch (IOException ex) {
//            logger.error("open servlet response error", ex);
//            throw new BizException("download error");
//        }
//        this.openFileHistInputStreamAction(fileEntityHistId, new ServletDownloadHeaderCallback(
//                new DownloadStream(outputStream), response));
//    }
//
//    public void deleteFileEntity(String fileId) {
//        delete(fileId);
//    }
//
//    public void deleteFileEntityHist(String fileHistId) {
//        FileEntityHist fileEntityHist = getFileEntityHist(fileHistId);
//        if (null == fileEntityHist) return;
//        storageService.deleteHist(fileEntityHist.getFileId(), fileEntityHist.getStoredContent(),
//                fileEntityHist.getVersionCode());
//        dataUpdater.delete(fileEntityHist);
//    }
//
//    private FileEntityHist copyFileEntity2Hist(FileEntity fileEntity) {
//        FileEntityHist fileEntityHist = new FileEntityHist();
//        BeanKit.copyProperties(fileEntity, fileEntityHist);
//        return fileEntityHist;
//    }
//
//    public static class DownloadCallbackAdapter implements DownloadCB {
//        private final FileEntityDownloadCallback callback;
//        private final FileEntity fileEntity;
//
//        public DownloadCallbackAdapter(FileEntityDownloadCallback fileEntityDownloadCallback,
//                                       FileEntity fileEntity) {
//            this.callback = fileEntityDownloadCallback;
//            this.fileEntity = fileEntity;
//        }
//
//        @Override
//        public int recv(long file_size, byte[] data, int bytes) {
//            return callback.recv(fileEntity, fileEntity instanceof FileEntityHist, file_size, data, bytes);
//        }
//    }
//}
//
