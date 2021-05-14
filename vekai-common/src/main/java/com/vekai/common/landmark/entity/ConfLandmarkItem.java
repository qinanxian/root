package com.vekai.common.landmark.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;



/** 里程进展 */
@Table(name="CONF_LANDMARK_ITEM")
public class ConfLandmarkItem implements Serializable,Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 定义代码 */
    @Id
    @GeneratedValue
    private String defKey ;
    /** 子项代码 */
    @Id
    @GeneratedValue
    private String itemKey ;
    /** 子项名称 */
    private String itemName ;
    /** 子项层级 */
    private Integer hierarchy ;
    /** 排序代码 */
    private String sortCode ;
    /** 子项样式 */
    private String itemStyle ;
    /** 子项说明 */
    private String intro ;
    /** 脚本动作模板 */
    private String actionTpl ;
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

    /** 定义代码 */
    public String getDefKey(){
        return this.defKey;
    }
    /** 定义代码 */
    public void setDefKey(String defKey){
        this.defKey = defKey;
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
    /** 子项层级 */
    public Integer getHierarchy(){
        return this.hierarchy;
    }
    /** 子项层级 */
    public void setHierarchy(Integer hierarchy){
        this.hierarchy = hierarchy;
    }
    /** 排序代码 */
    public String getSortCode(){
        return this.sortCode;
    }
    /** 排序代码 */
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
    /** 子项说明 */
    public String getIntro(){
        return this.intro;
    }
    /** 子项说明 */
    public void setIntro(String intro){
        this.intro = intro;
    }
    /** 脚本动作模板 */
    public String getActionTpl(){
        return this.actionTpl;
    }
    /** 脚本动作模板 */
    public void setActionTpl(String actionTpl){
        this.actionTpl = actionTpl;
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
