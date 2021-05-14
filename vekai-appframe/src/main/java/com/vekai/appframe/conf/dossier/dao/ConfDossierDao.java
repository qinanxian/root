package com.vekai.appframe.conf.dossier.dao;

import cn.fisok.sqloy.annotation.SqlParam;
import com.vekai.appframe.conf.dossier.model.ConfDossier;
import com.vekai.appframe.conf.dossier.model.ConfDossierItem;
import com.vekai.appframe.conf.dossier.model.ConfDossierItemFileEntity;
import cn.fisok.sqloy.annotation.SQLDao;

import java.util.List;

@SQLDao
public interface ConfDossierDao {
    ConfDossier queryConfDossier(@SqlParam("dossierDefKey") String dossierDefKey);
    List<ConfDossierItem> queryConfDossierItems(@SqlParam("dossierDefKey") String dossierDefKey);
    ConfDossierItem queryConfDossierItem(@SqlParam("itemDefId") String itemDefId);
    ConfDossierItem queryConfDossierItemByDefKey(@SqlParam("defKey") String defKey, @SqlParam("itemDefKey") String itemDefKey);
    List<ConfDossierItemFileEntity> queryTplFileEntities(@SqlParam("dossierDefKey") String dossierDefKey);
}
