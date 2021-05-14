package com.vekai.workflow.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: qyyao
 * @Description:
 * @Date: Created in 12:43 2019/2/23
 */
@Table(name = "WKFL_COMMON_COMMENT")
public class WorkflowCommonComment implements Serializable,Cloneable {


    /** 主键 */
    @Id
    @GeneratedValue
    private String id ;
    /** 常用语 */
    private String commonComment ;
    /** 热度 */
    private String hotSpot ;
    /** 排序码 */
    private String sortCode ;
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

    /** 主键 */
    public String getId(){
        return this.id;
    }
    /** 主键 */
    public void setId(String id){
        this.id = id;
    }
    /** 常用语 */
    public String getCommonComment(){
        return this.commonComment;
    }
    /** 常用语 */
    public void setCommonComment(String commonComment){
        this.commonComment = commonComment;
    }
    /** 热度 */
    public String getHotSpot(){
        return this.hotSpot;
    }
    /** 热度 */
    public void setHotSpot(String hotSpot){
        this.hotSpot = hotSpot;
    }
    /** 排序码 */
    public String getSortCode(){
        return this.sortCode;
    }
    /** 排序码 */
    public void setSortCode(String sortCode){
        this.sortCode = sortCode;
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
