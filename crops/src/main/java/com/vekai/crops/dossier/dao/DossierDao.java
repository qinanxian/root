package com.vekai.crops.dossier.dao;

import cn.fisok.sqloy.annotation.SqlParam;
import com.vekai.crops.dossier.model.Dossier;
import com.vekai.crops.dossier.model.DossierItem;
import com.vekai.common.fileman.entity.FileEntity;
import cn.fisok.sqloy.annotation.SQLDao;

import java.util.List;

@SQLDao
public interface DossierDao {
    Dossier queryDossier(@SqlParam("dossierId") String dossierId);
    Dossier queryDossierByObjectType(@SqlParam("objectType") String objectType, @SqlParam("objectId") String objectId);
    List<DossierItem> queryDossierItems(@SqlParam("dossierId") String dossierId);
    List<FileEntity> queryFileEntities(@SqlParam("dossierId") String dossierId);
}
