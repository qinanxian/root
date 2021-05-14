package com.vekai.appframe.cmon.version.entity;

/**
 * Created by luyu on 2018/9/30.
 */
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="CMON_VERSION_CHANGE")
public class CmonVersionChange implements Serializable,Cloneable{
    /** 版本变更ID */
    @Id
    @GeneratedValue
    private String versionChangeId ;
    /** 版本ID */
    private String versionId ;
    /** 变更分类 */
    private String changeCategory ;
    /** 变更内容 */
    private String changeContent ;
    /** 备注 */
    private String changeComment ;
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

    /** 版本变更ID */
    public String getVersionChangeId(){
        return this.versionChangeId;
    }
    /** 版本变更ID */
    public void setVersionChangeId(String versionChangeId){
        this.versionChangeId = versionChangeId;
    }
    /** 版本ID */
    public String getVersionId(){
        return this.versionId;
    }
    /** 版本ID */
    public void setVersionId(String versionId){
        this.versionId = versionId;
    }
    /** 变更分类 */
    public String getChangeCategory(){
        return this.changeCategory;
    }
    /** 变更分类 */
    public void setChangeCategory(String changeCategory){
        this.changeCategory = changeCategory;
    }
    /** 变更内容 */
    public String getChangeContent(){
        return this.changeContent;
    }
    /** 变更内容 */
    public void setChangeContent(String changeContent){
        this.changeContent = changeContent;
    }
    /** 备注 */
    public String getChangeComment(){
        return this.changeComment;
    }
    /** 备注 */
    public void setChangeComment(String changeComment){
        this.changeComment = changeComment;
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
