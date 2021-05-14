package com.vekai.appframe.cmon.message.entity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="CMON_MESSAGE_RECEIVER")
public class CmonMessageReceiver implements Serializable,Cloneable{
    /** 唯一编号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 信息ID */
    private String messageId ;
    /** 接收人类型;TO:发送；CC:抄送 */
    private String receiveType ;
    /** 接收人ID */
    private String receiveUserId ;
    /** 是否已读 */
    private String haveRead ;
    /** 打开URL */
    private String linkUrl ;
    /** URL参数 */
    private String linkUrlParam ;
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

    /** 唯一编号 */
    public String getId(){
        return this.id;
    }
    /** 唯一编号 */
    public void setId(String id){
        this.id = id;
    }
    /** 信息ID */
    public String getMessageId(){
        return this.messageId;
    }
    /** 信息ID */
    public void setMessageId(String messageId){
        this.messageId = messageId;
    }
    /** 接收人类型;TO:发送；CC:抄送 */
    public String getReceiveType(){
        return this.receiveType;
    }
    /** 接收人类型;TO:发送；CC:抄送 */
    public void setReceiveType(String receiveType){
        this.receiveType = receiveType;
    }
    /** 接收人ID */
    public String getReceiveUserId(){
        return this.receiveUserId;
    }
    /** 接收人ID */
    public void setReceiveUserId(String receiveUserId){
        this.receiveUserId = receiveUserId;
    }
    /** 是否已读 */
    public String getHaveRead(){
        return this.haveRead;
    }
    /** 是否已读 */
    public void setHaveRead(String haveRead){
        this.haveRead = haveRead;
    }
    /** 打开URL */
    public String getLinkUrl(){
        return this.linkUrl;
    }
    /** 打开URL */
    public void setLinkUrl(String linkUrl){
        this.linkUrl = linkUrl;
    }
    /** URL参数 */
    public String getLinkUrlParam(){
        return this.linkUrlParam;
    }
    /** URL参数 */
    public void setLinkUrlParam(String linkUrlParam){
        this.linkUrlParam = linkUrlParam;
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