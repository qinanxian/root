package com.vekai.common.landmark.service;

import com.vekai.common.landmark.entity.CmonLandmark;
import com.vekai.common.landmark.entity.CmonLandmarkItem;
import com.vekai.common.landmark.entity.ConfLandmark;
import com.vekai.common.landmark.entity.ConfLandmarkItem;
import com.vekai.common.landmark.model.LandmarkItemNode;

import java.util.List;

public interface LandmarkService {
    /**
     * 获取指定里程碑定义
     *
     * @param defKey
     * @return
     */
    ConfLandmark getConfLandmark(String defKey);

    /**
     * 获取指定里程碑定义子项定义列表
     *
     * @param defKey
     * @return
     */
    List<ConfLandmarkItem> getConfLandmarkItems(String defKey);

    /**
     * 获取里程碑实例主信息
     *
     * @param objectId
     * @param objectType
     * @return
     */
    CmonLandmark getLandmarkHeader(String objectId, String objectType);

    /**
     * 获取里程碑实例子项列表
     *
     * @param landmarkId
     * @return
     */
    List<CmonLandmarkItem> getLandmarkItems(String landmarkId);
    List<CmonLandmarkItem> getLandmarkItems(String objectId, String objectType);

    /**
     * 获取里程碑实例
     * @return
     */
    CmonLandmark getLandmarkByObject(String landmarkId);
    CmonLandmark getLandmarkByObject(String objectId, String objectType);


    /**
     * 里程碑实例转换为树形结构
     *
     * @param landmarkList
     * @return
     */
    List<LandmarkItemNode> parseLandmarkTree(List<CmonLandmarkItem> landmarkList);

    /**
     * 保存里程碑实例主项
     *
     * @param po
     * @return
     */
    int saveLandmark(CmonLandmark po);

    int saveLandmarkItem(CmonLandmarkItem po);

    /**
     * 保存里程碑实例子项
     **/
    int saveLandmarkItems(List<CmonLandmarkItem> polist);

    /**
     * 更新里程碑实例步骤
     **/
    int updateLandmarkStep(List<CmonLandmarkItem> polist, int steps);

    List<String> getAllRootParentPoint(List<CmonLandmarkItem> polist);



    CmonLandmarkItem getLandmarkItem(String objectId, String objectType, String itemKey);

    /**
     * 创建一个里程碑实例
     * @param defKey
     * @param objectType
     * @param objectId
     * @return
     */
    CmonLandmark createCmonLandmark(String defKey,String objectType,String objectId);

    /**
     * 设置里程碑节点状态
     * @param landmarkId
     * @param itemKey
     * @param status
     * @return
     */
    void updateStepStatus(String landmarkId, String itemKey, String status);


    /**
     * 激活节点
     * @param landmarkId
     * @param itemKey
     */
    void activeSetp(String landmarkId, String itemKey);
    void activeSetp(String objectId, String objectType, String itemKey);
}
