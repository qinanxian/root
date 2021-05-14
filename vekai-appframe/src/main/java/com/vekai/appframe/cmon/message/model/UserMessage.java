package com.vekai.appframe.cmon.message.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 发送给用户的信息对象
 */
public class UserMessage implements Serializable,Cloneable{
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
    /** 创建人 */
    private String createdBy ;
    /** 创建时间 */
    private Date createdTime ;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageTye() {
        return messageTye;
    }

    public void setMessageTye(String messageTye) {
        this.messageTye = messageTye;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getHaveRead() {
        return haveRead;
    }

    public void setHaveRead(String haveRead) {
        this.haveRead = haveRead;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
