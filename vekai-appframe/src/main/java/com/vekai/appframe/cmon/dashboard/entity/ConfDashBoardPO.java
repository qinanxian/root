package com.vekai.appframe.cmon.dashboard.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 主页小组件配置 */
@Table(name="CONF_DASH_BOARD")
public class ConfDashBoardPO implements Serializable,Cloneable{
    /** 代码 */
    @Id
    @GeneratedValue
    private String boardKey ;
    /** 名称 */
    private String name ;
    /** 排序代码 */
    private String sortCode ;
    /** 说明 */
    private String intro ;
    /** URI */
    private String uri ;
    /** 宽度 */
    private Integer sizeW ;
    /** 高度 */
    private Integer sizeH ;
    /** 是否全部可见 */
    private String isPublic ;
    /** 乐观锁 */
    private Integer revision ;
    /** 创建人 */
    private String createdBy ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新人 */
    private String updatedBy ;
    /** 更新时间 */
    private Date updatedTime ;
    /** 图标 */
    private String style;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    /** 代码 */
    public String getBoardKey(){
        return this.boardKey;
    }
    /** 代码 */
    public void setBoardKey(String boardKey){
        this.boardKey = boardKey;
    }
    /** 名称 */
    public String getName(){
        return this.name;
    }
    /** 名称 */
    public void setName(String name){
        this.name = name;
    }
    /** 排序代码 */
    public String getSortCode(){
        return this.sortCode;
    }
    /** 排序代码 */
    public void setSortCode(String sortCode){
        this.sortCode = sortCode;
    }
    /** 说明 */
    public String getIntro(){
        return this.intro;
    }
    /** 说明 */
    public void setIntro(String intro){
        this.intro = intro;
    }
    /** URI */
    public String getUri(){
        return this.uri;
    }
    /** URI */
    public void setUri(String uri){
        this.uri = uri;
    }
    /** 宽度 */
    public Integer getSizeW(){
        return this.sizeW;
    }
    /** 宽度 */
    public void setSizeW(Integer sizeW){
        this.sizeW = sizeW;
    }
    /** 高度 */
    public Integer getSizeH(){
        return this.sizeH;
    }
    /** 高度 */
    public void setSizeH(Integer sizeH){
        this.sizeH = sizeH;
    }
    /** 是否全部可见 */
    public String getIsPublic(){
        return this.isPublic;
    }
    /** 是否全部可见 */
    public void setIsPublic(String isPublic){
        this.isPublic = isPublic;
    }
    /** 乐观锁 */
    public Integer getRevision(){
        return this.revision;
    }
    /** 乐观锁 */
    public void setRevision(Integer revision){
        this.revision = revision;
    }
    /** 创建人 */
    public String getCreatedBy(){
        return this.createdBy;
    }
    /** 创建人 */
    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }
    /** 创建时间 */
    public Date getCreatedTime(){
        return this.createdTime;
    }
    /** 创建时间 */
    public void setCreatedTime(Date createdTime){
        this.createdTime = createdTime;
    }
    /** 更新人 */
    public String getUpdatedBy(){
        return this.updatedBy;
    }
    /** 更新人 */
    public void setUpdatedBy(String updatedBy){
        this.updatedBy = updatedBy;
    }
    /** 更新时间 */
    public Date getUpdatedTime(){
        return this.updatedTime;
    }
    /** 更新时间 */
    public void setUpdatedTime(Date updatedTime){
        this.updatedTime = updatedTime;
    }
}
