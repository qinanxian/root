package com.vekai.common.landmark.model;

import com.vekai.common.landmark.entity.CmonLandmarkItem;

import java.io.Serializable;
import java.util.List;

/**
 * 里程碑节点
 * Created by 杨松<yangsong158@qq.com> on 2018-06-10
 */
public class LandmarkItem extends CmonLandmarkItem implements Serializable,Cloneable{
    protected List<LandmarkItem> children;

    public List<LandmarkItem> getChildren() {
        return children;
    }

    public void setChildren(List<LandmarkItem> children) {
        this.children = children;
    }
}
