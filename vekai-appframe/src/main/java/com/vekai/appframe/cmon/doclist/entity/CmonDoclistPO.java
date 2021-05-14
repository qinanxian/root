package com.vekai.appframe.cmon.doclist.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="CMON_DOCLIST")
public class CmonDoclistPO implements Serializable,Cloneable{
    /** 文档ID */
    @Id
    @GeneratedValue
    private String doclistId ;
    /** 文档清单模板代码 */
    private String doclistCode ;
    /** 文档清单名称 */
    private String doclistName ;
    /** 文档清单类型 */
    private String doclistType ;
    /** 分类 */
    private String classification ;
    /** 文档清单说明 */
    private String doclistSummary ;
    /** 处理HANDLER */
    private String handlerClass ;
    /** 条目数量 */
    private Integer itemCount ;
    /** 文件数量 */
    private Integer fileCount ;
    /** 文档状态 */
    private String docStatus ;
    /** 文档标签 */
    private String docTags ;
    /** 条形码 */
    private String barCode ;
    /** 二维码 */
    private String qrCode ;
    /** 关键字 */
    private String keyWords ;
    /** 对象类型 */
    private String objectType ;
    /** 对象ID */
    private String objectId ;
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
    /** 文档清单名称 */
    public String getDoclistName(){
        return this.doclistName;
    }
    /** 文档清单名称 */
    public void setDoclistName(String doclistName){
        this.doclistName = doclistName;
    }
    /** 文档清单类型 */
    public String getDoclistType(){
        return this.doclistType;
    }
    /** 文档清单类型 */
    public void setDoclistType(String doclistType){
        this.doclistType = doclistType;
    }
    /** 分类 */
    public String getClassification(){
        return this.classification;
    }
    /** 分类 */
    public void setClassification(String classification){
        this.classification = classification;
    }
    /** 文档清单说明 */
    public String getDoclistSummary(){
        return this.doclistSummary;
    }
    /** 文档清单说明 */
    public void setDoclistSummary(String doclistSummary){
        this.doclistSummary = doclistSummary;
    }
    /** 处理HANDLER */
    public String getHandlerClass(){
        return this.handlerClass;
    }
    /** 处理HANDLER */
    public void setHandlerClass(String handlerClass){
        this.handlerClass = handlerClass;
    }
    /** 条目数量 */
    public Integer getItemCount(){
        return this.itemCount;
    }
    /** 条目数量 */
    public void setItemCount(Integer itemCount){
        this.itemCount = itemCount;
    }
    /** 文件数量 */
    public Integer getFileCount(){
        return this.fileCount;
    }
    /** 文件数量 */
    public void setFileCount(Integer fileCount){
        this.fileCount = fileCount;
    }
    /** 文档状态 */
    public String getDocStatus(){
        return this.docStatus;
    }
    /** 文档状态 */
    public void setDocStatus(String docStatus){
        this.docStatus = docStatus;
    }
    /** 文档标签 */
    public String getDocTags(){
        return this.docTags;
    }
    /** 文档标签 */
    public void setDocTags(String docTags){
        this.docTags = docTags;
    }
    /** 条形码 */
    public String getBarCode(){
        return this.barCode;
    }
    /** 条形码 */
    public void setBarCode(String barCode){
        this.barCode = barCode;
    }
    /** 二维码 */
    public String getQrCode(){
        return this.qrCode;
    }
    /** 二维码 */
    public void setQrCode(String qrCode){
        this.qrCode = qrCode;
    }
    /** 关键字 */
    public String getKeyWords(){
        return this.keyWords;
    }
    /** 关键字 */
    public void setKeyWords(String keyWords){
        this.keyWords = keyWords;
    }
    /** 对象类型 */
    public String getObjectType(){
        return this.objectType;
    }
    /** 对象类型 */
    public void setObjectType(String objectType){
        this.objectType = objectType;
    }
    /** 对象ID */
    public String getObjectId(){
        return this.objectId;
    }
    /** 对象ID */
    public void setObjectId(String objectId){
        this.objectId = objectId;
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