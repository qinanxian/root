package com.vekai.common.fileman.dao;

import cn.fisok.sqloy.annotation.SqlParam;
import com.vekai.common.fileman.entity.FileEntity;
import com.vekai.common.fileman.entity.FileEntityHist;
import cn.fisok.sqloy.annotation.SQLDao;

import java.util.List;

@SQLDao
public interface FileManDao {
    FileEntity queryFileEntity(@SqlParam("fileId") String fileId);
    FileEntity queryFileEntityByShowCode(@SqlParam("showCode") String showCode);
    FileEntity queryFileEntityByObject(@SqlParam("objectType") String objectType, @SqlParam("objectId") String objectId);
    FileEntityHist queryFileEntityHist(@SqlParam("fileHistId") String fileHistId);
    List<FileEntityHist> queryFileEntityHistList(@SqlParam("fileId") String fileId);
}
