package com.vekai.crops.codetodo.network.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="MSB_NETWORK_INFO")
public class MsbNetworkInfo implements Serializable,Cloneable{
    /** 编号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 网点编号 */
    private String networkNo ;
    /** 可办业务类型 */
    private String businessType ;
    /** 网点标签 */
    private String label ;
    /** 工作日上午高峰期时间 */
    private String workMorningHeightTime ;
    /** 工作日下午高峰期时间 */
    private String workAfternoonHeightTime ;
    /** 节假日上午高峰期时间 */
    private String holidayMorningHeightTime ;
    /** 节假日下午高峰期时间 */
    private String holidayAfternoonHeightTime ;
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

    /** 编号 */
    public String getId(){
        return this.id;
    }
    /** 编号 */
    public void setId(String id){
        this.id = id;
    }
    /** 网点编号 */
    public String getNetworkNo(){
        return this.networkNo;
    }
    /** 网点编号 */
    public void setNetworkNo(String networkNo){
        this.networkNo = networkNo;
    }
    /** 可办业务类型 */
    public String getBusinessType(){
        return this.businessType;
    }
    /** 可办业务类型 */
    public void setBusinessType(String businessType){
        this.businessType = businessType;
    }
    /** 网点标签 */
    public String getLabel(){
        return this.label;
    }
    /** 网点标签 */
    public void setLabel(String label){
        this.label = label;
    }
    /** 工作日上午高峰期时间 */
    public String getWorkMorningHeightTime(){
        return this.workMorningHeightTime;
    }
    /** 工作日上午高峰期时间 */
    public void setWorkMorningHeightTime(String workMorningHeightTime){
        this.workMorningHeightTime = workMorningHeightTime;
    }
    /** 工作日下午高峰期时间 */
    public String getWorkAfternoonHeightTime(){
        return this.workAfternoonHeightTime;
    }
    /** 工作日下午高峰期时间 */
    public void setWorkAfternoonHeightTime(String workAfternoonHeightTime){
        this.workAfternoonHeightTime = workAfternoonHeightTime;
    }
    /** 节假日上午高峰期时间 */
    public String getHolidayMorningHeightTime(){
        return this.holidayMorningHeightTime;
    }
    /** 节假日上午高峰期时间 */
    public void setHolidayMorningHeightTime(String holidayMorningHeightTime){
        this.holidayMorningHeightTime = holidayMorningHeightTime;
    }
    /** 节假日下午高峰期时间 */
    public String getHolidayAfternoonHeightTime(){
        return this.holidayAfternoonHeightTime;
    }
    /** 节假日下午高峰期时间 */
    public void setHolidayAfternoonHeightTime(String holidayAfternoonHeightTime){
        this.holidayAfternoonHeightTime = holidayAfternoonHeightTime;
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