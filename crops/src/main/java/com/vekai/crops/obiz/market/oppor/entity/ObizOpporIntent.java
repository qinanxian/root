package com.vekai.crops.obiz.market.oppor.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name="OBIZ_OPPOR_INTENT")
public class ObizOpporIntent implements Serializable,Cloneable{
    /** 商机流水号 */
    @Id
    @GeneratedValue
    private String opporId ;
    /** 客户姓名 */
    private String custName ;
    /** 客户证件类型 */
    private String custCertType ;
    /** 客户证件号 */
    private String custCertId ;
    /** 客户手机号 */
    private String custMobilePhone ;
    /** 客户出生日期 */
    private Date custBirth ;
    /** 客户年龄 */
    private Integer custAge ;
    /** 客户性别 */
    private String custGender ;
    /** 客户位置 */
    private String custLocation ;
    /** 客户联系地址 */
    private String custAddress ;
    /** 意向金额 */
    private Double howMuchMoney ;
    /** 来源渠道 */
    private String fromChannel ;
    /** 商机状态 */
    private String opprStatus ;
    /** 负责人 */
    private String holder ;
    /** 商机描述 */
    private String remarkNote ;
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

    /** 商机流水号 */
    public String getOpporId(){
        return this.opporId;
    }
    /** 商机流水号 */
    public void setOpporId(String opprId){
        this.opporId = opprId;
    }
    /** 客户姓名 */
    public String getCustName(){
        return this.custName;
    }
    /** 客户姓名 */
    public void setCustName(String custName){
        this.custName = custName;
    }
    /** 客户证件类型 */
    public String getCustCertType(){
        return this.custCertType;
    }
    /** 客户证件类型 */
    public void setCustCertType(String custCertType){
        this.custCertType = custCertType;
    }
    /** 客户证件号 */
    public String getCustCertId(){
        return this.custCertId;
    }
    /** 客户证件号 */
    public void setCustCertId(String custCertId){
        this.custCertId = custCertId;
    }
    /** 客户手机号 */
    public String getCustMobilePhone(){
        return this.custMobilePhone;
    }
    /** 客户手机号 */
    public void setCustMobilePhone(String custMobilePhone){
        this.custMobilePhone = custMobilePhone;
    }
    /** 客户出生日期 */
    public Date getCustBirth(){
        return this.custBirth;
    }
    /** 客户出生日期 */
    public void setCustBirth(Date custBirth){
        this.custBirth = custBirth;
    }
    /** 客户年龄 */
    public Integer getCustAge(){
        return this.custAge;
    }
    /** 客户年龄 */
    public void setCustAge(Integer custAge){
        this.custAge = custAge;
    }
    /** 客户性别 */
    public String getCustGender(){
        return this.custGender;
    }
    /** 客户性别 */
    public void setCustGender(String custGender){
        this.custGender = custGender;
    }
    /** 客户位置 */
    public String getCustLocation(){
        return this.custLocation;
    }
    /** 客户位置 */
    public void setCustLocation(String custLocation){
        this.custLocation = custLocation;
    }
    /** 客户联系地址 */
    public String getCustAddress(){
        return this.custAddress;
    }
    /** 客户联系地址 */
    public void setCustAddress(String custAddress){
        this.custAddress = custAddress;
    }
    /** 意向金额 */
    public Double getHowMuchMoney(){
        return this.howMuchMoney;
    }
    /** 意向金额 */
    public void setHowMuchMoney(Double howMuchMoney){
        this.howMuchMoney = howMuchMoney;
    }
    /** 来源渠道 */
    public String getFromChannel(){
        return this.fromChannel;
    }
    /** 来源渠道 */
    public void setFromChannel(String fromChannel){
        this.fromChannel = fromChannel;
    }
    /** 商机状态 */
    public String getOpprStatus(){
        return this.opprStatus;
    }
    /** 商机状态 */
    public void setOpprStatus(String opprStatus){
        this.opprStatus = opprStatus;
    }
    /** 负责人 */
    public String getHolder(){
        return this.holder;
    }
    /** 负责人 */
    public void setHolder(String holder){
        this.holder = holder;
    }
    /** 商机描述 */
    public String getRemarkNote(){
        return this.remarkNote;
    }
    /** 商机描述 */
    public void setRemarkNote(String remarkNote){
        this.remarkNote = remarkNote;
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