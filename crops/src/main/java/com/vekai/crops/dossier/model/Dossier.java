package com.vekai.crops.dossier.model;

import com.vekai.crops.dossier.entity.CmonDossierPO;

import javax.persistence.Transient;
import java.util.List;

public class Dossier extends CmonDossierPO {
    @Transient
    private List<DossierItem> itemList;

    public List<DossierItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<DossierItem> itemList) {
        this.itemList = itemList;
    }
}
