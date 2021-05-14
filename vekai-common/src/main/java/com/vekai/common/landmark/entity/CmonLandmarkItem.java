package com.vekai.common.landmark.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Table(name="CMON_LANDMARK_ITEM")
public class CmonLandmarkItem implements Serializable,Cloneable{
    /** 流水号 */
    @Id
    @GeneratedValue
    private String serialNo ;
    /** 实例号 */
    private String landmarkId ;
    /** 子项代码 */
    private String itemKey ;
    /** 子项名称 */
    private String itemName ;
    /** 子项排序代码 */
    private String sortCode ;
    /** 子项样式 */
    private String itemStyle ;
    /** 脚本动作模板 */
    private String actionTpl ;
    /** 阶段完成时间 */
    private String finishDate ;
    /** 状态 */
    private String status ;
    /** 逻辑删除标识 */
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

    /** 流水号 */
    public String getSerialNo(){
        return this.serialNo;
    }
    /** 流水号 */
    public void setSerialNo(String serialNo){
        this.serialNo = serialNo;
    }
    /** 实例号 */
    public String getLandmarkId(){
        return this.landmarkId;
    }
    /** 实例号 */
    public void setLandmarkId(String landmarkId){
        this.landmarkId = landmarkId;
    }
    /** 子项代码 */
    public String getItemKey(){
        return this.itemKey;
    }
    /** 子项代码 */
    public void setItemKey(String itemKey){
        this.itemKey = itemKey;
    }
    /** 子项名称 */
    public String getItemName(){
        return this.itemName;
    }
    /** 子项名称 */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    /** 子项排序代码 */
    public String getSortCode(){
        return this.sortCode;
    }
    /** 子项排序代码 */
    public void setSortCode(String sortCode){
        this.sortCode = sortCode;
    }
    /** 子项样式 */
    public String getItemStyle(){
        return this.itemStyle;
    }
    /** 子项样式 */
    public void setItemStyle(String itemStyle){
        this.itemStyle = itemStyle;
    }
    /** 脚本动作模板 */
    public String getActionTpl(){
        return this.actionTpl;
    }
    /** 脚本动作模板 */
    public void setActionTpl(String actionTpl){
        this.actionTpl = actionTpl;
    }
    /** 阶段完成时间 */
    public String getFinishDate(){
        return this.finishDate;
    }
    /** 阶段完成时间 */
    public void setFinishDate(String finishDate){
        this.finishDate = finishDate;
    }
    /** 状态 */
    public String getStatus(){
        return this.status;
    }
    /** 状态 */
    public void setStatus(String status){
        this.status = status;
    }
    /** 逻辑删除标识 */
    public String getDeleteFlag(){
        return this.deleteFlag;
    }
    /** 逻辑删除标识 */
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