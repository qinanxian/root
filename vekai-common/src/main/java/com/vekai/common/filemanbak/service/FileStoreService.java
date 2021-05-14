package com.vekai.common.filemanbak.service;

import com.vekai.common.fileman.entity.FileEntity;
import com.vekai.common.fileman.entity.FileEntityHist;

import java.io.InputStream;

public interface FileStoreService {
    int storeFile(FileEntity fileEntity, InputStream inputStream);
    int storeFileHist(FileEntityHist fileEntityHist, InputStream inputStream);
    int deleFile(FileEntity fileEntity);
    int deleteFileHist(FileEntityHist fileEntityHist);
}
