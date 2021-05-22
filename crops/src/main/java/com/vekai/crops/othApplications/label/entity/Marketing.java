package com.vekai.crops.othApplications.label.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="MARKETING")
public class Marketing implements Serializable,Cloneable{
    /** 活动ID */
    @Id
    @GeneratedValue(generator = "JDBC")
    private String id ;
    /** 活动名称 */
    private String activityName ;
    /** 活动开始时间 */
    private Date activityStartTime ;
    /** 活动结束时间 */
    private Date activityEntTime ;
    /** 活动网址 */
    private String activityUrl ;
    /** 目标客户 */
    private String targetCustomer ;
    /** 活动状态 */
    private String activityStatus ;
    /** 网格经纬度A */
    private String latandlongA ;
    /** 网格经纬度B */
    private String latandlongB ;
    /** 网格经纬度C */
    private String latandlongC ;
    /** 网格经纬度D */
    private String latandlongD ;
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

    /** 活动ID */
    public String getId(){
        return this.id;
    }
    /** 活动ID */
    public void setId(String id){
        this.id = id;
    }
    /** 活动名称 */
    public String getActivityName(){
        return this.activityName;
    }
    /** 活动名称 */
    public void setActivityName(String activityName){
        this.activityName = activityName;
    }
    /** 活动开始时间 */
    public Date getActivityStartTime(){
        return this.activityStartTime;
    }
    /** 活动开始时间 */
    public void setActivityStartTime(Date activityStartTime){
        this.activityStartTime = activityStartTime;
    }
    /** 活动结束时间 */
    public Date getActivityEntTime(){
        return this.activityEntTime;
    }
    /** 活动结束时间 */
    public void setActivityEntTime(Date activityEntTime){
        this.activityEntTime = activityEntTime;
    }
    /** 活动网址 */
    public String getActivityUrl(){
        return this.activityUrl;
    }
    /** 活动网址 */
    public void setActivityUrl(String activityUrl){
        this.activityUrl = activityUrl;
    }
    /** 目标客户 */
    public String getTargetCustomer(){
        return this.targetCustomer;
    }
    /** 目标客户 */
    public void setTargetCustomer(String targetCustomer){
        this.targetCustomer = targetCustomer;
    }
    /** 活动状态 */
    public String getActivityStatus(){
        return this.activityStatus;
    }
    /** 活动状态 */
    public void setActivityStatus(String activityStatus){
        this.activityStatus = activityStatus;
    }
    /** 网格经纬度A */
    public String getLatandlongA(){
        return this.latandlongA;
    }
    /** 网格经纬度A */
    public void setLatandlongA(String latandlongA){
        this.latandlongA = latandlongA;
    }
    /** 网格经纬度B */
    public String getLatandlongB(){
        return this.latandlongB;
    }
    /** 网格经纬度B */
    public void setLatandlongB(String latandlongB){
        this.latandlongB = latandlongB;
    }
    /** 网格经纬度C */
    public String getLatandlongC(){
        return this.latandlongC;
    }
    /** 网格经纬度C */
    public void setLatandlongC(String latandlongC){
        this.latandlongC = latandlongC;
    }
    /** 网格经纬度D */
    public String getLatandlongD(){
        return this.latandlongD;
    }
    /** 网格经纬度D */
    public void setLatandlongD(String latandlongD){
        this.latandlongD = latandlongD;
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
