package com.vekai.appframe.conf.doclist.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.appframe.conf.doclist.entity.ConfDocListItemPO;
import com.vekai.appframe.conf.doclist.entity.ConfDocListPO;
import com.vekai.appframe.conf.doclist.entity.ConfDocListGroupPO;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConfDocListService {

    @Autowired
    BeanCruder beanCruder;

    public ConfDocListItemPO getDocListItemByUniqueCode(String docListCode, String uniqueCode) {
        String sql = "SELECT * FROM CONF_DOCLIST_ITEM WHERE DOCLIST_CODE=:doclistCode AND UNIQUE_CODE=:uniqueCode";
        return beanCruder.selectOne(ConfDocListItemPO.class, sql,
                MapKit.mapOf("doclistCode", docListCode, "uniqueCode", uniqueCode));
    }

    public ConfDocListPO getDocListByCode(String doclistCode) {
        ConfDocListPO confDoclistPO = beanCruder.selectOneById(ConfDocListPO.class, MapKit.mapOf("doclistCode",
                doclistCode));
        return confDoclistPO;
    }

    public ConfDocListGroupPO getDocListGroup(String doclistCode, String groupCode) {
        ConfDocListGroupPO confDocListGroupPO = beanCruder.selectOne(ConfDocListGroupPO.class, "SELECT * FROM " +
                        "CONF_DOCLIST_GROUP WHERE " +
                        "DOCLIST_CODE=:doclistCode and GROUP_CODE=:groupCode",
                MapKit.mapOf("doclistCode", doclistCode, "groupCode", groupCode));
        return confDocListGroupPO;
    }

    public List<ConfDocListGroupPO> getDocListGroups(String doclistCode) {
        List<ConfDocListGroupPO> groupList = beanCruder.selectList(ConfDocListGroupPO.class, "SELECT * FROM " +
                        "CONF_DOCLIST_GROUP WHERE " +
                        "DOCLIST_CODE=:doclistCode",
                MapKit.mapOf("doclistCode", doclistCode));
        return groupList;
    }

    public List<ConfDocListItemPO> getDocListItems(String doclistCode) {
        List<ConfDocListItemPO> itemList = beanCruder.selectList(ConfDocListItemPO.class, "SELECT * FROM " +
                        "CONF_DOCLIST_ITEM WHERE " +
                        "DOCLIST_CODE=:doclistCode",
                MapKit.mapOf("doclistCode", doclistCode));
        return itemList;
    }

    public ConfDocListItemPO getDocListItem(String doclistCode,String itemCode) {
        return beanCruder.selectOneById(ConfDocListItemPO.class,doclistCode,itemCode);
    }
}
