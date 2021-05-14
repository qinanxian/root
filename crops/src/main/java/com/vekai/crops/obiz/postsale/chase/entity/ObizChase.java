package com.vekai.crops.obiz.postsale.chase.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="OBIZ_CHASE")
public class ObizChase implements Serializable,Cloneable{
    /** 催收ID */
    @Id
    @GeneratedValue
    private String chaseId ;
    /** 催收对象类型 */
    private String objectType ;
    /** 催收对象编号 */
    private String objectId ;
    /** 客户号 */
    private String custId ;
    /** 客户名 */
    private String custName ;
    /** 证件类型 */
    private String certType ;
    /** 证件号 */
    private String certId ;
    /** 手机号 */
    private String mobilePhone ;
    /** 电子邮件 */
    private String email ;
    /** 地址 */
    private String address ;
    /** 第二联系人姓名 */
    private String contactor2 ;
    /** 第二联系人手机号 */
    private String contactor2Phone ;
    /** 第二联系人邮件 */
    private String contactor2Mail ;
    /** 催收金额 */
    private Double loanAmt ;
    /** 催收说明 */
    private String intro ;
    /** 催收状态 */
    private String chaseStatus ;
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

    /** 催收ID */
    public String getChaseId(){
        return this.chaseId;
    }
    /** 催收ID */
    public void setChaseId(String chaseId){
        this.chaseId = chaseId;
    }
    /** 催收对象类型 */
    public String getObjectType(){
        return this.objectType;
    }
    /** 催收对象类型 */
    public void setObjectType(String objectType){
        this.objectType = objectType;
    }
    /** 催收对象编号 */
    public String getObjectId(){
        return this.objectId;
    }
    /** 催收对象编号 */
    public void setObjectId(String objectId){
        this.objectId = objectId;
    }
    /** 客户号 */
    public String getCustId(){
        return this.custId;
    }
    /** 客户号 */
    public void setCustId(String custId){
        this.custId = custId;
    }
    /** 客户名 */
    public String getCustName(){
        return this.custName;
    }
    /** 客户名 */
    public void setCustName(String custName){
        this.custName = custName;
    }
    /** 证件类型 */
    public String getCertType(){
        return this.certType;
    }
    /** 证件类型 */
    public void setCertType(String certType){
        this.certType = certType;
    }
    /** 证件号 */
    public String getCertId(){
        return this.certId;
    }
    /** 证件号 */
    public void setCertId(String certId){
        this.certId = certId;
    }
    /** 手机号 */
    public String getMobilePhone(){
        return this.mobilePhone;
    }
    /** 手机号 */
    public void setMobilePhone(String mobilePhone){
        this.mobilePhone = mobilePhone;
    }
    /** 电子邮件 */
    public String getEmail(){
        return this.email;
    }
    /** 电子邮件 */
    public void setEmail(String email){
        this.email = email;
    }
    /** 地址 */
    public String getAddress(){
        return this.address;
    }
    /** 地址 */
    public void setAddress(String address){
        this.address = address;
    }
    /** 第二联系人姓名 */
    public String getContactor2(){
        return this.contactor2;
    }
    /** 第二联系人姓名 */
    public void setContactor2(String contactor2){
        this.contactor2 = contactor2;
    }
    /** 第二联系人手机号 */
    public String getContactor2Phone(){
        return this.contactor2Phone;
    }
    /** 第二联系人手机号 */
    public void setContactor2Phone(String contactor2Phone){
        this.contactor2Phone = contactor2Phone;
    }
    /** 第二联系人邮件 */
    public String getContactor2Mail(){
        return this.contactor2Mail;
    }
    /** 第二联系人邮件 */
    public void setContactor2Mail(String contactor2Mail){
        this.contactor2Mail = contactor2Mail;
    }
    /** 催收金额 */
    public Double getLoanAmt(){
        return this.loanAmt;
    }
    /** 催收金额 */
    public void setLoanAmt(Double loanAmt){
        this.loanAmt = loanAmt;
    }
    /** 催收说明 */
    public String getIntro(){
        return this.intro;
    }
    /** 催收说明 */
    public void setIntro(String intro){
        this.intro = intro;
    }
    /** 催收状态 */
    public String getChaseStatus(){
        return this.chaseStatus;
    }
    /** 催收状态 */
    public void setChaseStatus(String chaseStatus){
        this.chaseStatus = chaseStatus;
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