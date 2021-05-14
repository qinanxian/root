package com.vekai.common.landmark.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/** 里程碑 */
@Table(name="CONF_LANDMARK")
public class ConfLandmark implements Serializable,Cloneable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 定义代码 */
    @Id
    @GeneratedValue
    private String defKey ;
    /** 定义名称 */
    private String defName ;
    /** 功能说明 */
    private String intro ;
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
    /** 定义名称 */
    public String getDefName(){
        return this.defName;
    }
    /** 定义名称 */
    public void setDefName(String defName){
        this.defName = defName;
    }
    /** 功能说明 */
    public String getIntro(){
        return this.intro;
    }
    /** 功能说明 */
    public void setIntro(String intro){
        this.intro = intro;
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
