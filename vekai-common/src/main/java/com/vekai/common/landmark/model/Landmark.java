package com.vekai.common.landmark.model;

import com.vekai.common.landmark.entity.CmonLandmark;

import java.io.Serializable;
import java.util.List;

/**
 * 里程碑对象
 * Created by 杨松<yangsong158@qq.com> on 2018-06-10
 */
public class Landmark extends CmonLandmark implements Serializable,Cloneable{
    private List<LandmarkItem> items;

    public List<LandmarkItem> getItems() {
        return items;
    }

    public void setItems(List<LandmarkItem> items) {
        this.items = items;
    }
}
