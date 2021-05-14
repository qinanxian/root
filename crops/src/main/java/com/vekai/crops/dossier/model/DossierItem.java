package com.vekai.crops.dossier.model;

import com.vekai.crops.dossier.entity.CmonDossierItemPO;
import com.vekai.common.fileman.entity.FileEntity;

import javax.persistence.Transient;
import java.util.List;

public class DossierItem extends CmonDossierItemPO {
    @Transient
    private List<? extends FileEntity> fileEntities;

    public List<? extends FileEntity> getFileEntities() {
        return fileEntities;
    }

    public void setFileEntities(List<? extends FileEntity> fileEntities) {
        this.fileEntities = fileEntities;
    }
}
