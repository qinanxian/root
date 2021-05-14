package com.vekai.appframe.cmon.dashboard.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 默认关联角色 */
@Table(name="CONF_DASH_BOARD_ROLE")
public class ConfDashBoardRolePO implements Serializable,Cloneable{
    /** 标识号 */
    @Id
    @GeneratedValue
    private String id ;
    /** WIGET代码 */
    private String boardKey ;
    /** 角色号 */
    private String roleId ;
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

    /** 标识号 */
    public String getId(){
        return this.id;
    }
    /** 标识号 */
    public void setId(String id){
        this.id = id;
    }
    /** WIGET代码 */
    public String getBoardKey(){
        return this.boardKey;
    }
    /** WIGET代码 */
    public void setBoardKey(String boardKey){
        this.boardKey = boardKey;
    }
    /** 角色号 */
    public String getRoleId(){
        return this.roleId;
    }
    /** 角色号 */
    public void setRoleId(String roleId){
        this.roleId = roleId;
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
