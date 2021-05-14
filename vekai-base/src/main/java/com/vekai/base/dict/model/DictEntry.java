package com.vekai.base.dict.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cn.fisok.raw.kit.StringKit;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.*;

public class DictEntry implements Serializable {
    @Id
    protected String code;
    protected String name;
    protected String nameI18nCode;
    protected String categoryCode;
    protected String sortCode;
    protected String intro;
    protected List<DictItemEntry> items = new ArrayList<>();

    @Transient
    private Map<String, DictItemEntry> itemMap = new LinkedHashMap<String, DictItemEntry>();

    public void setItems(List<DictItemEntry> items) {
        this.items = items;
    }

    @JsonIgnore
    public List<DictItemEntry> getDictItemEntrys() {
        return items;
    }

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

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
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

    public DictItemEntry getItem(String itemCode) {
        return itemMap.get(itemCode);
    }

    public void setItemMap(Map<String, DictItemEntry> itemMap) {
        this.itemMap = itemMap;
    }

    public List<DictItemEntry> getItems() {
        List<DictItemEntry> retItems = null;
        if(items==null||items.isEmpty()){
            List<DictItemEntry> itemList = new ArrayList<DictItemEntry>();
            itemList.addAll(itemMap.values());
            retItems = itemList;
        }else {
            retItems = items;
        }
        //重新按排序号排序
//        Collections.sort(retItems, new Comparator<DictItemEntry>() {
//            public int compare(DictItemEntry o1, DictItemEntry o2) {
//                String sortCode1 = StringKit.nvl(o1.getSortCode(),o1.getCode());
//                String sortCode2 = StringKit.nvl(o2.getSortCode(),o2.getCode());
//                sortCode1 = StringKit.nvl(sortCode1,"");
//                sortCode2 = StringKit.nvl(sortCode2,"");
//
//                return sortCode1.compareTo(sortCode2);
//            }
//        });
        retItems.sort(new Comparator<DictItemEntry>() {
            public int compare(DictItemEntry o1, DictItemEntry o2) {
                String sortCode1 = StringKit.nvl(o1.getSortCode(),o1.getCode());
                String sortCode2 = StringKit.nvl(o2.getSortCode(),o2.getCode());
                //当排序号相同时，用ID来排
                if(sortCode1.equals(sortCode2))return o1.getCode().compareTo(o2.getCode());

                return sortCode1.compareTo(sortCode2);
            }
        });

        return retItems;
    }


}
