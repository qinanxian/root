package com.vekai.crops.codetodo.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="MSB_BUSINESS_TIME")
public class MsbBusinessTime implements Serializable,Cloneable{
    /** 编号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 网点编号 */
    private String networkNo ;
    /** 业务类型 */
    private String businessType ;
    /** 工作日时间段 */
    private String workTime ;
    /** 是否营业 */
    private String isBusiness ;
    /** 开始时间 */
    private String startTime ;
    /** 结束时间 */
    private String endTime ;
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
    /** 业务类型 */
    public String getBusinessType(){
        return this.businessType;
    }
    /** 业务类型 */
    public void setBusinessType(String businessType){
        this.businessType = businessType;
    }
    /** 工作日时间段 */
    public String getWorkTime(){
        return this.workTime;
    }
    /** 工作日时间段 */
    public void setWorkTime(String workTime){
        this.workTime = workTime;
    }
    /** 是否营业 */
    public String getIsBusiness(){
        return this.isBusiness;
    }
    /** 是否营业 */
    public void setIsBusiness(String isBusiness){
        this.isBusiness = isBusiness;
    }
    /** 开始时间 */
    public String getStartTime(){
        return this.startTime;
    }
    /** 开始时间 */
    public void setStartTime(String startTime){
        this.startTime = startTime;
    }
    /** 结束时间 */
    public String getEndTime(){
        return this.endTime;
    }
    /** 结束时间 */
    public void setEndTime(String endTime){
        this.endTime = endTime;
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