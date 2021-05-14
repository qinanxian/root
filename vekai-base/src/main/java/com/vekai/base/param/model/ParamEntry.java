package com.vekai.base.param.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luyu on 2018/1/17.
 */
public class ParamEntry implements Serializable {

    private String code;
    private String name;
    private String nameI18nCode;
    private String sortCode;
    private String intro;

    private Map<String, ParamItemEntry> itemMap = new LinkedHashMap<String, ParamItemEntry>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameI18nCode() {
        return nameI18nCode;
    }

    public void setNameI18nCode(String nameI18nCode) {
        this.nameI18nCode = nameI18nCode;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }


    public ParamItemEntry getItem(String itemCode) {
        return itemMap.get(itemCode);
    }

    public void setItemMap(Map<String, ParamItemEntry> itemMap) {
        this.itemMap = itemMap;
    }

    public List<ParamItemEntry> getItems() {
        List<ParamItemEntry> itemList = new ArrayList<ParamItemEntry>();
        itemList.addAll(itemMap.values());
        return itemList;
    }
}
