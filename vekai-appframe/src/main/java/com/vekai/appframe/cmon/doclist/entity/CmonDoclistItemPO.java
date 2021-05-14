package com.vekai.appframe.cmon.doclist.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="CMON_DOCLIST_ITEM")
public class CmonDoclistItemPO implements Serializable,Cloneable{
    /** 明细项ID */
    @Id
    @GeneratedValue
    private String listitemId ;
    /** 文档ID */
    private String doclistId ;
    /** 文档清单模板代码 */
    private String doclistCode ;
    /** 分组;ID:名称的形式 */
    private String itemGroup ;
    /** 明细项代码 */
    private String itemCode ;
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
    /** 状态 */
    private String itemStatus ;
    /** 文件个数 */
    private Integer fileCount ;
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

    /** 明细项ID */
    public String getListitemId(){
        return this.listitemId;
    }
    /** 明细项ID */
    public void setListitemId(String listitemId){
        this.listitemId = listitemId;
    }
    /** 文档ID */
    public String getDoclistId(){
        return this.doclistId;
    }
    /** 文档ID */
    public void setDoclistId(String doclistId){
        this.doclistId = doclistId;
    }
    /** 文档清单模板代码 */
    public String getDoclistCode(){
        return this.doclistCode;
    }
    /** 文档清单模板代码 */
    public void setDoclistCode(String doclistCode){
        this.doclistCode = doclistCode;
    }
    /** 分组;ID:名称的形式 */
    public String getItemGroup(){
        return this.itemGroup;
    }
    /** 分组;ID:名称的形式 */
    public void setItemGroup(String itemGroup){
        this.itemGroup = itemGroup;
    }
    /** 明细项代码 */
    public String getItemCode(){
        return this.itemCode;
    }
    /** 明细项代码 */
    public void setItemCode(String itemCode){
        this.itemCode = itemCode;
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
    /** 状态 */
    public String getItemStatus(){
        return this.itemStatus;
    }
    /** 状态 */
    public void setItemStatus(String itemStatus){
        this.itemStatus = itemStatus;
    }
    /** 文件个数 */
    public Integer getFileCount(){
        return this.fileCount;
    }
    /** 文件个数 */
    public void setFileCount(Integer fileCount){
        this.fileCount = fileCount;
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