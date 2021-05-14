package com.vekai.appframe.holiday.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 节假日 */
@Table(name="FNAT_HOLIDAY")
public class Holiday implements Serializable,Cloneable{
    /** 日期 */
    @Id
    private Date holidayDate ;
    /** 假日类型 */
    private String holidayType ;
    /** 假日名称 */
    private String holidayName ;
    /** 假日说明 */
    private String note ;
    /** 创建人 */
    private String createdBy ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新人 */
    private String updatedBy ;
    /** 更新时间 */
    private Date updatedTime ;

    /** 日期 */
    public Date getHolidayDate(){
        return this.holidayDate;
    }
    /** 日期 */
    public void setHolidayDate(Date holidayDate){
        this.holidayDate = holidayDate;
    }
    /** 假日类型 */
    public String getHolidayType(){
        return this.holidayType;
    }
    /** 假日类型 */
    public void setHolidayType(String holidayType){
        this.holidayType = holidayType;
    }
    /** 假日名称 */
    public String getHolidayName(){
        return this.holidayName;
    }
    /** 假日名称 */
    public void setHolidayName(String holidayName){
        this.holidayName = holidayName;
    }
    /** 假日说明 */
    public String getNote(){
        return this.note;
    }
    /** 假日说明 */
    public void setNote(String note){
        this.note = note;
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
