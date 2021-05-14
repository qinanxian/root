package com.vekai.fiscal.event.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 记账事件参数 */
@Table(name="FISC_EVENT_PARAM")
public class FiscEventParamPO implements Serializable,Cloneable{
    /** 事件ID */
    private String eventId ;
    /** 事件参数ID */
    @Id
    @GeneratedValue
    private String eventParamId ;
    /** 事件参数代码 */
    private String itemCode ;
    /** 事件参数名称 */
    private String itemName ;
    /** 数据类型 */
    private String dataType ;
    /** 显示数值 */
    private String dataText ;
    /** 字串值 */
    private String strValue ;
    /** 整数值 */
    private Integer intValue ;
    /** 小数值 */
    private Double floatValue ;
    /** 日期值 */
    private Date dateValue ;
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

    /** 事件ID */
    public String getEventId(){
        return this.eventId;
    }
    /** 事件ID */
    public void setEventId(String eventId){
        this.eventId = eventId;
    }
    /** 事件参数ID */
    public String getEventParamId(){
        return this.eventParamId;
    }
    /** 事件参数ID */
    public void setEventParamId(String eventParamId){
        this.eventParamId = eventParamId;
    }
    /** 事件参数代码 */
    public String getItemCode(){
        return this.itemCode;
    }
    /** 事件参数代码 */
    public void setItemCode(String itemCode){
        this.itemCode = itemCode;
    }
    /** 事件参数名称 */
    public String getItemName(){
        return this.itemName;
    }
    /** 事件参数名称 */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    /** 数据类型 */
    public String getDataType(){
        return this.dataType;
    }
    /** 数据类型 */
    public void setDataType(String dataType){
        this.dataType = dataType;
    }
    /** 显示数值 */
    public String getDataText(){
        return this.dataText;
    }
    /** 显示数值 */
    public void setDataText(String dataText){
        this.dataText = dataText;
    }
    /** 字串值 */
    public String getStrValue(){
        return this.strValue;
    }
    /** 字串值 */
    public void setStrValue(String strValue){
        this.strValue = strValue;
    }
    /** 整数值 */
    public Integer getIntValue(){
        return this.intValue;
    }
    /** 整数值 */
    public void setIntValue(Integer intValue){
        this.intValue = intValue;
    }
    /** 小数值 */
    public Double getFloatValue(){
        return this.floatValue;
    }
    /** 小数值 */
    public void setFloatValue(Double floatValue){
        this.floatValue = floatValue;
    }
    /** 日期值 */
    public Date getDateValue(){
        return this.dateValue;
    }
    /** 日期值 */
    public void setDateValue(Date dateValue){
        this.dateValue = dateValue;
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