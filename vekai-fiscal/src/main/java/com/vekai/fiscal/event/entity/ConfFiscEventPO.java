package com.vekai.fiscal.event.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 记账事件配置 */
@Table(name="CONF_FISC_EVENT")
public class ConfFiscEventPO implements Serializable,Cloneable{
    /** 事件代码 */
    @Id
    @GeneratedValue
    private String eventDef ;
    /** 排序代码 */
    private String sortCode ;
    /** 事件名称 */
    private String eventName ;
    /** 事件分类 */
    private String category ;
    /** 事件说明 */
    private String eventIntro ;
    /** 业务对象类型 */
    private String objectType ;
    /** 业务对象模型类型;JavaBean或Map */
    private String dataModelType ;
    /** 业务对象模型 */
    private String dataModel ;
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

    /** 事件代码 */
    public String getEventDef(){
        return this.eventDef;
    }
    /** 事件代码 */
    public void setEventDef(String eventDef){
        this.eventDef = eventDef;
    }
    /** 排序代码 */
    public String getSortCode(){
        return this.sortCode;
    }
    /** 排序代码 */
    public void setSortCode(String sortCode){
        this.sortCode = sortCode;
    }
    /** 事件名称 */
    public String getEventName(){
        return this.eventName;
    }
    /** 事件名称 */
    public void setEventName(String eventName){
        this.eventName = eventName;
    }
    /** 事件分类 */
    public String getCategory(){
        return this.category;
    }
    /** 事件分类 */
    public void setCategory(String category){
        this.category = category;
    }
    /** 事件说明 */
    public String getEventIntro(){
        return this.eventIntro;
    }
    /** 事件说明 */
    public void setEventIntro(String eventIntro){
        this.eventIntro = eventIntro;
    }
    /** 业务对象类型 */
    public String getObjectType(){
        return this.objectType;
    }
    /** 业务对象类型 */
    public void setObjectType(String objectType){
        this.objectType = objectType;
    }
    /** 业务对象模型类型;JavaBean或Map */
    public String getDataModelType(){
        return this.dataModelType;
    }
    /** 业务对象模型类型;JavaBean或Map */
    public void setDataModelType(String dataModelType){
        this.dataModelType = dataModelType;
    }
    /** 业务对象模型 */
    public String getDataModel(){
        return this.dataModel;
    }
    /** 业务对象模型 */
    public void setDataModel(String dataModel){
        this.dataModel = dataModel;
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