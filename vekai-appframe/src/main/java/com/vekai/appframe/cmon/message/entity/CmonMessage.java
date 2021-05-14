package com.vekai.appframe.cmon.message.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="CMON_MESSAGE")
public class CmonMessage implements Serializable,Cloneable{
    /** 信息ID */
    @Id
    @GeneratedValue
    private String messageId ;
    /** 消息类型 */
    private String messageTye ;
    /** 消息标题 */
    private String title ;
    /** 消息内容 */
    private String content ;
    /** 发送人 */
    private String sender ;
    /** 信息状态;DRAFT:草稿;SENT:已发送 */
    private String status ;
    /** 阅读次数 */
    private Integer viewCount ;
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

    /** 信息ID */
    public String getMessageId(){
        return this.messageId;
    }
    /** 信息ID */
    public void setMessageId(String messageId){
        this.messageId = messageId;
    }
    /** 消息类型 */
    public String getMessageTye(){
        return this.messageTye;
    }
    /** 消息类型 */
    public void setMessageTye(String messageTye){
        this.messageTye = messageTye;
    }
    /** 消息标题 */
    public String getTitle(){
        return this.title;
    }
    /** 消息标题 */
    public void setTitle(String title){
        this.title = title;
    }
    /** 消息内容 */
    public String getContent(){
        return this.content;
    }
    /** 消息内容 */
    public void setContent(String content){
        this.content = content;
    }
    /** 发送人 */
    public String getSender(){
        return this.sender;
    }
    /** 发送人 */
    public void setSender(String sender){
        this.sender = sender;
    }
    /** 信息状态;DRAFT:草稿;SENT:已发送 */
    public String getStatus(){
        return this.status;
    }
    /** 信息状态;DRAFT:草稿;SENT:已发送 */
    public void setStatus(String status){
        this.status = status;
    }
    /** 阅读次数 */
    public Integer getViewCount(){
        return this.viewCount;
    }
    /** 阅读次数 */
    public void setViewCount(Integer viewCount){
        this.viewCount = viewCount;
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