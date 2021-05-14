package com.vekai.appframe.conf.dossier.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="CONF_DOSSIER_ITEM")
public class ConfDossierItemPO implements Serializable,Cloneable{
    /** 明细项ID */
    @Id
    @GeneratedValue
    private String itemDefId ;
    /** 文档清单模板代码 */
    private String dossierDefKey ;
    /** 明细项代码 */
    private String itemDefKey ;
    /** 分组;ID:名称的形式 */
    private String itemGroup ;
    /** 明细项名称 */
    private String itemName ;
    /** 排序代码 */
    private String sortCode ;
    /** 统一识别码 */
    private String uniqueCode ;
    /** 明细项说明 */
    private String itemIntro ;
    /** 重要性 */
    private String importance ;
    /** 媒体类型 */
    private String mediaType ;
    /** 媒体文本号 */
    private String mediaTextId ;
    /** 模版文件Id */
    private String tplFileId ;
    /** 状态 */
    private String itemStatus ;
    /** 乐观锁版本 */
    private Integer revision ;
    /** 创建人 */
    private String createdBy ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新人 */
    private String updatedBy ;
    /** 更新时间 */
    private Date updatedTime ;

    /** 明细项ID */
    public String getItemDefId(){
        return this.itemDefId;
    }
    /** 明细项ID */
    public void setItemDefId(String itemDefId){
        this.itemDefId = itemDefId;
    }
    /** 文档清单模板代码 */
    public String getDossierDefKey(){
        return this.dossierDefKey;
    }
    /** 文档清单模板代码 */
    public void setDossierDefKey(String dossierDefKey){
        this.dossierDefKey = dossierDefKey;
    }
    /** 明细项代码 */
    public String getItemDefKey(){
        return this.itemDefKey;
    }
    /** 明细项代码 */
    public void setItemDefKey(String itemDefKey){
        this.itemDefKey = itemDefKey;
    }
    /** 分组;ID:名称的形式 */
    public String getItemGroup(){
        return this.itemGroup;
    }
    /** 分组;ID:名称的形式 */
    public void setItemGroup(String itemGroup){
        this.itemGroup = itemGroup;
    }
    /** 明细项名称 */
    public String getItemName(){
        return this.itemName;
    }
    /** 明细项名称 */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    /** 排序代码 */
    public String getSortCode(){
        return this.sortCode;
    }
    /** 排序代码 */
    public void setSortCode(String sortCode){
        this.sortCode = sortCode;
    }
    /** 统一识别码 */
    public String getUniqueCode(){
        return this.uniqueCode;
    }
    /** 统一识别码 */
    public void setUniqueCode(String uniqueCode){
        this.uniqueCode = uniqueCode;
    }
    /** 明细项说明 */
    public String getItemIntro(){
        return this.itemIntro;
    }
    /** 明细项说明 */
    public void setItemIntro(String itemIntro){
        this.itemIntro = itemIntro;
    }
    /** 重要性 */
    public String getImportance(){
        return this.importance;
    }
    /** 重要性 */
    public void setImportance(String importance){
        this.importance = importance;
    }
    /** 媒体类型 */
    public String getMediaType(){
        return this.mediaType;
    }
    /** 媒体类型 */
    public void setMediaType(String mediaType){
        this.mediaType = mediaType;
    }
    /** 媒体文本号 */
    public String getMediaTextId(){
        return this.mediaTextId;
    }
    /** 媒体文本号 */
    public void setMediaTextId(String mediaTextId){
        this.mediaTextId = mediaTextId;
    }
    /** 模版文件Id */
    public String getTplFileId(){
        return this.tplFileId;
    }
    /** 模版文件Id */
    public void setTplFileId(String tplFileId){
        this.tplFileId = tplFileId;
    }
    /** 状态 */
    public String getItemStatus(){
        return this.itemStatus;
    }
    /** 状态 */
    public void setItemStatus(String itemStatus){
        this.itemStatus = itemStatus;
    }
    /** 乐观锁版本 */
    public Integer getRevision(){
        return this.revision;
    }
    /** 乐观锁版本 */
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