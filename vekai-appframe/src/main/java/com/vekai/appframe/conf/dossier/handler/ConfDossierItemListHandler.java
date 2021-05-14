package com.vekai.appframe.conf.dossier.handler;

import com.vekai.appframe.conf.dossier.model.ConfDossierItem;
import com.vekai.appframe.conf.dossier.service.ConfDossierService;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConfDossierItemListHandler extends BeanDataListHandler<ConfDossierItem> {
    @Autowired
    protected ConfDossierService confDossierService;
    @Override
    public int save(DataForm dataForm, List<ConfDossierItem> dataList) {
        return super.save(dataForm, dataList);
    }
}
