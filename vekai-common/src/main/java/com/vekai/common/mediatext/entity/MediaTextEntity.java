package com.vekai.common.mediatext.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="CMON_MEDIA_TEXT")
public class MediaTextEntity implements Serializable,Cloneable{
    /** 流水号 */
    @Id
    @GeneratedValue
    private String mediaTextId ;
    /** 外部业务码 */
    private String outerId ;
    /** 文字数 */
    private Integer wordCount ;
    /** 内容长度 */
    private Integer contentLength ;
    /** 文本内容 */
    private String contentRaw ;
    /** 数据内容 */
    private String contentData ;
    /** 标签 */
    private String keyWords ;
    /** 条形码 */
    private String barCode ;
    /** 二维码 */
    private String qrCode ;
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

    /** 流水号 */
    public String getMediaTextId(){
        return this.mediaTextId;
    }
    /** 流水号 */
    public void setMediaTextId(String mediaTextId){
        this.mediaTextId = mediaTextId;
    }
    /** 外部业务码 */
    public String getOuterId(){
        return this.outerId;
    }
    /** 外部业务码 */
    public void setOuterId(String outerId){
        this.outerId = outerId;
    }
    /** 文字数 */
    public Integer getWordCount(){
        return this.wordCount;
    }
    /** 文字数 */
    public void setWordCount(Integer wordCount){
        this.wordCount = wordCount;
    }
    /** 内容长度 */
    public Integer getContentLength(){
        return this.contentLength;
    }
    /** 内容长度 */
    public void setContentLength(Integer contentLength){
        this.contentLength = contentLength;
    }
    /** 文本内容 */
    public String getContentRaw(){
        return this.contentRaw;
    }
    /** 文本内容 */
    public void setContentRaw(String contentRaw){
        this.contentRaw = contentRaw;
    }
    /** 数据内容 */
    public String getContentData(){
        return this.contentData;
    }
    /** 数据内容 */
    public void setContentData(String contentData){
        this.contentData = contentData;
    }
    /** 标签 */
    public String getKeyWords(){
        return this.keyWords;
    }
    /** 标签 */
    public void setKeyWords(String keyWords){
        this.keyWords = keyWords;
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