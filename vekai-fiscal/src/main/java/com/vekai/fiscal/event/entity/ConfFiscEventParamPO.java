package com.vekai.fiscal.event.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 事件参数配置 */
@Table(name="CONF_FISC_EVENT_PARAM")
public class ConfFiscEventParamPO implements Serializable,Cloneable{
    /** 事件代码 */
    @Id
    @GeneratedValue
    private String eventDef ;
    /** 参数代码 */
    @Id
    @GeneratedValue
    private String itemCode ;
    /** 参数名称 */
    private String itemName ;
    /** 取值表达式 */
    private String valueExpr ;
    /** 数据类型 */
    private String dataType ;
    /** 是否必需 */
    private String isRequired ;
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
    /** 参数代码 */
    public String getItemCode(){
        return this.itemCode;
    }
    /** 参数代码 */
    public void setItemCode(String itemCode){
        this.itemCode = itemCode;
    }
    /** 参数名称 */
    public String getItemName(){
        return this.itemName;
    }
    /** 参数名称 */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    /** 取值表达式 */
    public String getValueExpr(){
        return this.valueExpr;
    }
    /** 取值表达式 */
    public void setValueExpr(String valueExpr){
        this.valueExpr = valueExpr;
    }
    /** 数据类型 */
    public String getDataType(){
        return this.dataType;
    }
    /** 数据类型 */
    public void setDataType(String dataType){
        this.dataType = dataType;
    }
    /** 是否必需 */
    public String getIsRequired(){
        return this.isRequired;
    }
    /** 是否必需 */
    public void setIsRequired(String isRequired){
        this.isRequired = isRequired;
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