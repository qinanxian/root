package com.vekai.appframe.cmon.dashboard.dto;

import java.io.Serializable;

/**
 * Created by luyu on 2018/5/28.
 */
public class DashBoard implements Serializable,Cloneable{

    /** 代码 */
    private String boardKey ;
    /** 名称 */
    private String name ;
    /** X轴 */
    private Integer axisX ;
    /** Y轴 */
    private Integer axisY ;
    /** 宽度 */
    private Integer sizeW ;
    /** 高度 */
    private Integer sizeH ;
    /** 排序代码 */
    private String sortCode ;
    /** 说明 */
    private String intro ;
    /** 图标 */
    private String style;
    /** URI */
    private String uri ;
    /** 是否使用 */
    private String isUsed;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getBoardKey() {
        return boardKey;
    }

    public void setBoardKey(String boardKey) {
        this.boardKey = boardKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAxisX() {
        return axisX;
    }

    public void setAxisX(Integer axisX) {
        this.axisX = axisX;
    }

    public Integer getAxisY() {
        return axisY;
    }

    public void setAxisY(Integer axisY) {
        this.axisY = axisY;
    }

    public Integer getSizeW() {
        return sizeW;
    }

    public void setSizeW(Integer sizeW) {
        this.sizeW = sizeW;
    }

    public Integer getSizeH() {
        return sizeH;
    }

    public void setSizeH(Integer sizeH) {
        this.sizeH = sizeH;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }
}
