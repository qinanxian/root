package com.vekai.common.fileman.entity;

import cn.fisok.raw.kit.BeanKit;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "CMON_FILE_HIST")
public class FileEntityHist extends FileEntity {
    @Id
    @GeneratedValue
    private String fileHistId;

    public String getFileHistId() {
        return fileHistId;
    }

    public void setFileHistId(String fileHistId) {
        this.fileHistId = fileHistId;
    }

    public FileEntityHist(){}

    public FileEntityHist(FileEntity fileEntity) {
        if (null != fileEntity) {
            BeanKit.copyProperties(fileEntity, this);
            this.setFileId(fileEntity.getFileId());
        }
    }

    public static FileEntityHist createHist(FileEntity fileEntity) {
        if (null == fileEntity) return null;
        return new FileEntityHist(fileEntity);
    }
}
