package com.vekai.common.landmark.service.impl;

import cn.fisok.raw.kit.*;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.common.landmark.entity.CmonLandmark;
import com.vekai.common.landmark.entity.CmonLandmarkItem;
import com.vekai.common.landmark.entity.ConfLandmark;
import com.vekai.common.landmark.entity.ConfLandmarkItem;
import com.vekai.common.landmark.model.LandmarkItemNode;
import com.vekai.common.landmark.dict.LandmarkItemStatus;
import com.vekai.common.landmark.service.LandmarkService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class LandmarkServiceImpl implements LandmarkService {
    @Autowired
    BeanCruder beanCruder;


    @Override
    public ConfLandmark getConfLandmark(String defKey) {
        String sql = "SELECT * FROM CONF_LANDMARK WHERE DEF_KEY=:defKey";
        return beanCruder.selectOne(ConfLandmark.class, sql, "defKey", defKey);
    }

    @Override
    public List<ConfLandmarkItem> getConfLandmarkItems(String defKey) {
        String sql = "SELECT * FROM CONF_LANDMARK_ITEM WHERE DEF_KEY=:defKey ORDER BY SORT_CODE ASC";
        return beanCruder.selectList(ConfLandmarkItem.class, sql, "defKey", defKey);
    }


    @Override
    public int saveLandmark(CmonLandmark po) {
        return beanCruder.save(po);
    }

    @Override
    public int saveLandmarkItems(List<CmonLandmarkItem> polist) {
        return beanCruder.save(polist);
    }

    @Override
    public List<LandmarkItemNode> parseLandmarkTree(List<CmonLandmarkItem> landmarkList) {
        ValidateKit.notNull(landmarkList);
        List<LandmarkItemNode> nodeWrapperList = ListKit.newArrayList();
        for (CmonLandmarkItem cmonLandmarkItem : landmarkList) {
            nodeWrapperList.add(BeanKit.castTo(cmonLandmarkItem,LandmarkItemNode.class));
//            nodeWrapperList.add(new TreeNodeWrapper<CmonLandmarkItem>(null, cmonLandmarkItem));
        }
//        List<LandmarkItemNode> rootList = TreeNodeKit.buildTreeBranches(nodeWrapperList, nodeObject -> nodeObject.getSortCode());
        List<LandmarkItemNode> rootList = TreeNodeKit.buildTree(nodeWrapperList, nodeObject -> nodeObject.getSortCode());
        return rootList;
    }

    @Override
    public List<CmonLandmarkItem> getLandmarkItems(String landmarkId) {
        String sql = "SELECT * FROM CMON_LANDMARK_ITEM WHERE LANDMARK_ID=:landmarkId AND DELETE_FLAG IS NULL ORDER BY SORT_CODE ASC";
        return beanCruder.selectList(CmonLandmarkItem.class, sql, "landmarkId", landmarkId);
    }

    @Override
    public List<CmonLandmarkItem> getLandmarkItems(String objectId, String objectType) {
        String sql = "SELECT B.* FROM CMON_LANDMARK A, CMON_LANDMARK_ITEM B "
                + "WHERE A.LANDMARK_ID = B.LANDMARK_ID AND A.OBJECT_ID = :objectId "
                + " AND A.OBJECT_TYPE = :objectType AND B.DELETE_FLAG IS NULL ORDER BY SORT_CODE ASC";
        return beanCruder.selectList(CmonLandmarkItem.class, sql, "objectId", objectId, "objectType", objectType);
    }

    @Override
    public CmonLandmark getLandmarkByObject(String landmarkId) {
        CmonLandmark cmonLandmark = beanCruder.selectOne(CmonLandmark.class, "select * from CMON_LANDMARK where LANDMARK_ID=:landmarkId",
                "landmarkId", landmarkId);
        return cmonLandmark;
    }

    @Override
    public CmonLandmark getLandmarkByObject(String objectId, String objectType) {
        CmonLandmark cmonLandmark = beanCruder.selectOne(CmonLandmark.class, "select * from CMON_LANDMARK where OBJECT_ID = :objectId AND OBJECT_TYPE = :objectType",
                "objectId", objectId, "objectType", objectType);
        return cmonLandmark;
    }

    @Override
    public int updateLandmarkStep(List<CmonLandmarkItem> polist, int steps) {
        //获取所有根节点
        List<String> rootList = getAllRootParentPoint(polist);
        //前进时最大数组下标
        int maxSteps = polist.size() - 1;
        //倒退时最小坐标
        int minSteps = 0;
        //获取当前步骤
        Validate.notNull(polist);
        int currentStep = -1;
        for (int i = 0; i < polist.size(); i++) {
            String status = polist.get(i).getStatus();
            if ("init".equals(status)) {
                break;
            } else {
                currentStep = i;
            }
        }

        if ((currentStep + steps > maxSteps) || (currentStep + steps < minSteps)) {
            polist.get(currentStep).setStatus("done");
        } else {
            if (steps > 0) {
                for (int x = 0; x < currentStep + steps; x++) {
                    polist.get(x).setStatus("done");
                }
                polist.get(currentStep + steps).setStatus("doing");
                if (rootList.contains(polist.get(currentStep + steps).getSortCode())) {
                    int next = currentStep + steps + 1;
                    if (next <= maxSteps) {
                        polist.get(next).setStatus("doing");
                    }
                }
            }

        }

        return saveLandmarkItems(polist);

    }


    public List<String> getAllRootParentPoint(List<CmonLandmarkItem> polist) {
        List<String> codeList = new ArrayList<>();

        int len = polist.get(0).getSortCode().length();
        for (CmonLandmarkItem item : polist) {
            if (item.getSortCode().length() < len) {
                len = item.getSortCode().length();
            }
        }

        int minLen = len;
        for (CmonLandmarkItem item : polist) {
            if (item.getSortCode().length() == minLen) {
                codeList.add(item.getSortCode());
            }
        }

        return codeList;
    }

    public void updateStepStatus(String landmarkId, String itemKey, String status) {
        beanCruder.execute("update CMON_LANDMARK_ITEM set STATUS=:status where LANDMARK_ID=:landmarkId AND ITEM_KEY=:itemKey",
                MapKit.mapOf("status", status,"landmarkId", landmarkId, "itemKey", itemKey));
    }

    @Override
    public void activeSetp(String landmarkId, String itemKey) {
        // 1. 将已激活的节点设置为完成
        beanCruder.execute("update CMON_LANDMARK_ITEM set STATUS='"+LandmarkItemStatus.DONE +
                        "' where STATUS='"+LandmarkItemStatus.DOING +"'and LANDMARK_ID=:landmarkId",
                MapKit.mapOf("landmarkId", landmarkId));


        /**
         * 2. 将末端和其父节点设置为活动状态
         */
        List<CmonLandmarkItem> landmarkItems = this.getLandmarkItems(landmarkId);
        ValidateKit.notEmpty(landmarkItems, "里程碑中Key{0}子项为空", landmarkId);

        String aimItemSortCode = null;
        for (CmonLandmarkItem item:landmarkItems) {
            if(itemKey.equals(item.getItemKey())) {
                aimItemSortCode = item.getSortCode();
            }
        }

        ValidateKit.notNull(aimItemSortCode, "未找到里程碑中Key{0}对应的子项", itemKey);

        List<CmonLandmarkItem> aimItems = new ArrayList<>();
        for (int i=aimItemSortCode.length();i>0;i--){
            String findStr = aimItemSortCode.substring(0, i);
            for (CmonLandmarkItem item:landmarkItems) {
                if(findStr.equals(item.getSortCode())) {
                    item.setStatus(LandmarkItemStatus.DOING);
                    aimItems.add(item);
                }
            }
        }

        if(!aimItems.isEmpty()) beanCruder.update(aimItems);
    }

    @Override
    public void activeSetp(String objectId, String objectType, String itemKey) {
        CmonLandmark landmark = this.getLandmarkByObject(objectId, objectType);
        ValidateKit.notNull(landmark, "里程碑实例为空");
        this.activeSetp(landmark.getLandmarkId(), itemKey);
    }


    @Override
    public CmonLandmark getLandmarkHeader(String objectId, String objectType) {
        String sql = "SELECT * FROM CMON_LANDMARK WHERE OBJECT_ID=:objectId AND OBJECT_TYPE=:objectType";
        return beanCruder.selectOne(CmonLandmark.class, sql, "objectId", objectId, "objectType", objectType);
    }


    @Override
    public CmonLandmarkItem getLandmarkItem(String objId, String objType, String itemKey) {
        String sql = "SELECT B.* FROM CMON_LANDMARK A, CMON_LANDMARK_ITEM B "
                + "WHERE A.LANDMARK_ID = B.LANDMARK_ID AND A.OBJECT_ID = :objId AND B.DELETE_FLAG IS NULL"
                + " AND A.OBJECT_TYPE = :objType"
                + " AND B.LANDMARK_ITEM_KEY = :itemKey";
        return beanCruder.selectOne(CmonLandmarkItem.class, sql, "objId", objId, "objType", objType, "itemKey", itemKey);
    }

    @Override
    public int saveLandmarkItem(CmonLandmarkItem po) {
        return beanCruder.save(po);
    }

    public CmonLandmark createCmonLandmark(String defKey, String objectType, String objectId) {
        ConfLandmark confLandmark = getConfLandmark(defKey);
        List<ConfLandmarkItem> confLandmarkItems = getConfLandmarkItems(defKey);
        ValidateKit.notNull(confLandmark, "里程碑定义[{0}]不存在", defKey);
        ValidateKit.notEmpty(confLandmarkItems, "里程碑定义[{0}]没有明细项", defKey);

        CmonLandmark landmark = new CmonLandmark();
        List<CmonLandmarkItem> landmarkItems = new ArrayList<CmonLandmarkItem>();

        //1.复制模板数据
        BeanKit.copyProperties(confLandmark, landmark);
        landmark.setObjectId(objectId);
        landmark.setObjectType(objectType);

        for (ConfLandmarkItem confLandmarkItem : confLandmarkItems) {
            CmonLandmarkItem landmarkItem = new CmonLandmarkItem();
            BeanKit.copyProperties(confLandmarkItem, landmarkItem);
            landmarkItem.setStatus(LandmarkItemStatus.INIT);
            landmarkItems.add(landmarkItem);
        }

        //2.做保存
        int ret = beanCruder.save(landmark);
        if (ret == 1) {
            landmarkItems.forEach((item) -> {
                item.setLandmarkId(landmark.getLandmarkId());
            });
            beanCruder.save(landmarkItems);
        }


        return landmark;
    }


}
