package com.vekai.common.filemanbak.service;

import com.vekai.common.fileman.entity.FileEntity;
import com.vekai.common.fileman.entity.FileEntityHist;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

public interface FileManageService {

    FileEntity getFileEntity(String fileEntityId);
    FileEntity getFileEntity(String objectType, String objectId);
    public FileEntity getFileEntityByShowCode(String showCode);
    FileEntityHist getFileEntityHist(String fileHistEntityId);
    List<FileEntityHist> getFileHistList(String fileEntityId);

    /**
     * 保存文件
     * @param fileEntityId if null create fileEntity, otherwise update fileEntity
     * @param multipartFile 文件输入流
     * @param writeHist 是否记录历史
     * @return
     * @throws IOException
     */
    FileEntity saveFile(String fileEntityId, MultipartFile multipartFile, boolean writeHist,
                        Consumer<FileEntity> actionBeforeSave) throws IOException ;

    FileEntity copy(String existingFileEntityId);

    void deleteFileEntity(String fileId);
    void deleteFileEntityHist(String fileHistId);

    InputStream openFileInputStream(String fileId);
    InputStream openFileHistInputStream(String fileHistId);

    void openFileInputStreamAction(String fileEntityId, FileEntityDownloadCallback fileEntityDownloadCallback);

    void openFileHistInputStreamAction(String fileHistEntityId, FileEntityDownloadCallback fileEntityDownloadCallback);

    void servletDownloadFile(String fileEntityId, HttpServletResponse response);

    void servletDownloadFileHist(String fileEntityHistId, HttpServletResponse response);
}
