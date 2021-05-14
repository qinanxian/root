package com.vekai.appframe.cmon.file.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.common.fileman.entity.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by luyu on 2018/9/21.
 */
@Component
public class FileService {

    @Autowired
    private BeanCruder beanCruder;

    public FileEntity getFileEntryByFileId(String fileId) {
        String sql = "SELECT * FROM CMON_FILE WHERE FILE_ID =:fileId";
        FileEntity fileEntity = beanCruder.selectOne(FileEntity.class,sql,"fileId",fileId);
        return fileEntity;
    }
}
