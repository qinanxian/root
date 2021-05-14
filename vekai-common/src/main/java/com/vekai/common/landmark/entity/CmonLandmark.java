package com.vekai.common.landmark.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name="CMON_LANDMARK")
public class CmonLandmark implements Serializable,Cloneable{
    /** 实例号 */
    @Id
    @GeneratedValue
    private String landmarkId ;
    /** 业务对象号 */
    private String objectId ;
    /** 业务对象类型 */
    private String objectType ;
    /** 里程碑定义号 */
    private String defKey ;
    /** 里程碑定义名 */
    private String defName ;
    /** 当前活动节点 */
    private String activeItemKey ;
    /** 状态 */
    private String status ;
    /** 摘要说明 */
    private String intro ;
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

    /** 实例号 */
    public String getLandmarkId(){
        return this.landmarkId;
    }
    /** 实例号 */
    public void setLandmarkId(String landmarkId){
        this.landmarkId = landmarkId;
    }
    /** 业务对象号 */
    public String getObjectId(){
        return this.objectId;
    }
    /** 业务对象号 */
    public void setObjectId(String objectId){
        this.objectId = objectId;
    }
    /** 业务对象类型 */
    public String getObjectType(){
        return this.objectType;
    }
    /** 业务对象类型 */
    public void setObjectType(String objectType){
        this.objectType = objectType;
    }
    /** 里程碑定义号 */
    public String getDefKey(){
        return this.defKey;
    }
    /** 里程碑定义号 */
    public void setDefKey(String defKey){
        this.defKey = defKey;
    }
    /** 里程碑定义名 */
    public String getDefName(){
        return this.defName;
    }
    /** 里程碑定义名 */
    public void setDefName(String defName){
        this.defName = defName;
    }
    /** 当前活动节点 */
    public String getActiveItemKey(){
        return this.activeItemKey;
    }
    /** 当前活动节点 */
    public void setActiveItemKey(String activeItemKey){
        this.activeItemKey = activeItemKey;
    }
    /** 状态 */
    public String getStatus(){
        return this.status;
    }
    /** 状态 */
    public void setStatus(String status){
        this.status = status;
    }
    /** 摘要说明 */
    public String getIntro(){
        return this.intro;
    }
    /** 摘要说明 */
    public void setIntro(String intro){
        this.intro = intro;
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