package com.vekai.appframe.cmon.shortcutmenu.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/** 快捷菜单信息 */
@Table(name="CMON_SHORTCUT_MENU")
public class CmonShortcutMenu implements Serializable,Cloneable{
    /** 流水号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 菜单ID */
    private String meunId ;
    /** 菜单名称 */
    private String menuName ;
    /** 菜单URL */
    private String menuUrl ;
    /** 菜单图标 */
    private String menuIcon ;
    /** 菜单图标样式 */
    private String iconType ;
    /** 关联用户 */
    private String relatedUserId;
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
    public String getId(){
        return this.id;
    }
    /** 流水号 */
    public void setId(String id){
        this.id = id;
    }
    /** 菜单ID */
    public String getMeunId(){
        return this.meunId;
    }
    /** 菜单ID */
    public void setMeunId(String meunId){
        this.meunId = meunId;
    }
    /** 菜单名称 */
    public String getMenuName(){
        return this.menuName;
    }
    /** 菜单名称 */
    public void setMenuName(String menuName){
        this.menuName = menuName;
    }
    /** 菜单URL */
    public String getMenuUrl(){
        return this.menuUrl;
    }
    /** 菜单URL */
    public void setMenuUrl(String menuUrl){
        this.menuUrl = menuUrl;
    }
    /** 菜单图标 */
    public String getMenuIcon(){
        return this.menuIcon;
    }
    /** 菜单图标 */
    public void setMenuIcon(String menuIcon){
        this.menuIcon = menuIcon;
    }
    /** 菜单图标样式 */
    public String getIconType(){
        return this.iconType;
    }
    /** 菜单图标样式 */
    public void setIconType(String iconType){
        this.iconType = iconType;
    }

    public String getRelatedUserId() {
        return relatedUserId;
    }

    public void setRelatedUserId(String relatedUserId) {
        this.relatedUserId = relatedUserId;
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