package com.vekai.common.fileman.service;

import com.vekai.common.fileman.entity.FileEntity;
import com.vekai.common.fileman.entity.FileEntityHist;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

public interface FileManService {
    /**
     * 取文件记录实体
     * @param fileId
     * @return
     */
    FileEntity getFileEntity(String fileId);

    /**
     * 使用展示码取文件记录实体
     * @param showCode
     * @return
     */
    FileEntity getFileEntityByShowCode(String showCode);

    /**
     * 取文件历史记录实体
     * @param fileHistId
     * @return
     */
    FileEntityHist getFileEntityHist(String fileHistId);

    /**
     * 取文件历史记录列表
     * @param fileId
     * @return
     */
    List<FileEntityHist> getFileEntityHistList(String fileId);

    /**
     * 删除文件记录
     * @param fileId
     * @return
     */
    int deleteFileEntity(String fileId);

    /**
     * 查找文件存储服务
     * @param storeServiceName
     * @return
     */
    FileStoreService lookupStoreService(String storeServiceName);

    /**
     * 打开文件输入流
     * @param fileEntity
     * @return
     */
    InputStream openFileInputStream(FileEntity fileEntity);

    /**
     * 打开历史文件输入流
     * @param fileEntityHist
     * @return
     */
    InputStream openFileHistInputStream(FileEntityHist fileEntityHist);

    /**
     * 存储文件
     * @param fileEntity 文件记录数据实体
     * @param inputStream 文件输入流
     * @param writeHist 是否记录历史
     * @param beforeSave 保存前的插入处理
     * @return
     */
    int saveAndStoreFile(FileEntity fileEntity, InputStream inputStream,boolean writeHist, Consumer<FileEntity> beforeSave);
}
