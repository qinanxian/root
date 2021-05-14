package com.vekai.appframe.cmon.version.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luyu on 2018/9/30.
 */
@Table(name="CMON_VERSION")
public class CmonVersion implements Serializable,Cloneable{
    /** 版本ID */
    @Id
    @GeneratedValue
    private String versionId ;
    /** 版本编号 */
    private String versionCode ;
    /** 发布时间 */
    private Date releaseTime ;
    /** 下个版本发布时间 */
    private Date nextReleaseTime ;
    /** 发布备注事项 */
    private String releaseComment ;
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

    /** 版本变更集合 */
    @Transient
    private List<CmonVersionChange> versionChanges = new ArrayList<>();

    public List<CmonVersionChange> getVersionChanges() {
        return versionChanges;
    }

    public void setVersionChanges(List<CmonVersionChange> versionChanges) {
        this.versionChanges = versionChanges;
    }

    /** 版本ID */
    public String getVersionId(){
        return this.versionId;
    }
    /** 版本ID */
    public void setVersionId(String versionId){
        this.versionId = versionId;
    }
    /** 版本编号 */
    public String getVersionCode(){
        return this.versionCode;
    }
    /** 版本编号 */
    public void setVersionCode(String versionCode){
        this.versionCode = versionCode;
    }
    /** 发布时间 */
    public Date getReleaseTime(){
        return this.releaseTime;
    }
    /** 发布时间 */
    public void setReleaseTime(Date releaseTime){
        this.releaseTime = releaseTime;
    }
    /** 下个版本发布时间 */
    public Date getNextReleaseTime(){
        return this.nextReleaseTime;
    }
    /** 下个版本发布时间 */
    public void setNextReleaseTime(Date nextReleaseTime){
        this.nextReleaseTime = nextReleaseTime;
    }
    /** 发布备注事项 */
    public String getReleaseComment(){
        return this.releaseComment;
    }
    /** 发布备注事项 */
    public void setReleaseComment(String releaseComment){
        this.releaseComment = releaseComment;
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
