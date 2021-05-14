package com.vekai.appframe.cmon.doclist.model;

import com.vekai.appframe.cmon.doclist.entity.CmonDoclistPO;

import java.util.List;

public class DoclistObject extends CmonDoclistPO {
    List<DoclistItem> itemList = null;

    public List<DoclistItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<DoclistItem> itemList) {
        this.itemList = itemList;
    }
}
