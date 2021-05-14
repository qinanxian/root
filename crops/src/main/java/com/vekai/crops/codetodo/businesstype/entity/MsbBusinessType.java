package com.vekai.crops.codetodo.businesstype.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="MSB_BUSINESS_TYPE")
public class MsbBusinessType implements Serializable,Cloneable{
    /** 编号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 业务类型名称 */
    private String businessTypeName ;
    /** 温馨提示 */
    private String tip ;
    /** 业务可办理渠道 */
    private String channel ;
    /** 流程图片文件ID */
    private String flowFileId ;
    /** 删除标识 */
    private String deleteFlag ;
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
    /** 业务类型名称 */
    public String getBusinessTypeName(){
        return this.businessTypeName;
    }
    /** 业务类型名称 */
    public void setBusinessTypeName(String businessTypeName){
        this.businessTypeName = businessTypeName;
    }
    /** 温馨提示 */
    public String getTip(){
        return this.tip;
    }
    /** 温馨提示 */
    public void setTip(String tip){
        this.tip = tip;
    }
    /** 业务可办理渠道 */
    public String getChannel(){
        return this.channel;
    }
    /** 业务可办理渠道 */
    public void setChannel(String channel){
        this.channel = channel;
    }
    /** 流程图片文件ID */
    public String getFlowFileId(){
        return this.flowFileId;
    }
    /** 流程图片文件ID */
    public void setFlowFileId(String flowFileId){
        this.flowFileId = flowFileId;
    }
    /** 删除标识 */
    public String getDeleteFlag(){
        return this.deleteFlag;
    }
    /** 删除标识 */
    public void setDeleteFlag(String deleteFlag){
        this.deleteFlag = deleteFlag;
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