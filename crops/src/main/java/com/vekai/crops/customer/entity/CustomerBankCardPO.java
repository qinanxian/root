package com.vekai.crops.customer.entity;


import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="CUSTOMER_BANK_CARD")
public class CustomerBankCardPO implements Serializable,Cloneable{
    /** 编号 */
    private String id ;
    /** 客户编号 */
    private String customerId ;
    /** 银行卡开户名 */
    private String cardName ;
    /** 银行卡卡号 */
    private String cardNo ;
    /** 身份证号 */
    private String certId ;
    /** 手机号 */
    private String tel ;
    /** 开户行编号 */
    private String bankId ;
    /** 开户行名称 */
    private String bankName ;
    /** 乐观锁 */
    private Integer revision ;
    /** 创建人 */
    private String createdBy ;
    /** 创建机构 */
    private String createdOrg ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新人 */
    private String updateBy ;
    /** 更新机构 */
    private String updateOrg ;
    /**  */
    private String untitled10 ;
    /** 更新时间 */
    private Date updateTime ;

    /** 编号 */
    public String getId(){
        return this.id;
    }
    /** 编号 */
    public void setId(String id){
        this.id = id;
    }
    /** 客户编号 */
    public String getCustomerId(){
        return this.customerId;
    }
    /** 客户编号 */
    public void setCustomerId(String customerId){
        this.customerId = customerId;
    }
    /** 银行卡开户名 */
    public String getCardName(){
        return this.cardName;
    }
    /** 银行卡开户名 */
    public void setCardName(String cardName){
        this.cardName = cardName;
    }
    /** 银行卡卡号 */
    public String getCardNo(){
        return this.cardNo;
    }
    /** 银行卡卡号 */
    public void setCardNo(String cardNo){
        this.cardNo = cardNo;
    }
    /** 身份证号 */
    public String getCertId(){
        return this.certId;
    }
    /** 身份证号 */
    public void setCertId(String certId){
        this.certId = certId;
    }
    /** 手机号 */
    public String getTel(){
        return this.tel;
    }
    /** 手机号 */
    public void setTel(String tel){
        this.tel = tel;
    }
    /** 开户行编号 */
    public String getBankId(){
        return this.bankId;
    }
    /** 开户行编号 */
    public void setBankId(String bankId){
        this.bankId = bankId;
    }
    /** 开户行名称 */
    public String getBankName(){
        return this.bankName;
    }
    /** 开户行名称 */
    public void setBankName(String bankName){
        this.bankName = bankName;
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
    /** 创建机构 */
    public String getCreatedOrg(){
        return this.createdOrg;
    }
    /** 创建机构 */
    public void setCreatedOrg(String createdOrg){
        this.createdOrg = createdOrg;
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
    public String getUpdateBy(){
        return this.updateBy;
    }
    /** 更新人 */
    public void setUpdateBy(String updateBy){
        this.updateBy = updateBy;
    }
    /** 更新机构 */
    public String getUpdateOrg(){
        return this.updateOrg;
    }
    /** 更新机构 */
    public void setUpdateOrg(String updateOrg){
        this.updateOrg = updateOrg;
    }
    /**  */
    public String getUntitled10(){
        return this.untitled10;
    }
    /**  */
    public void setUntitled10(String untitled10){
        this.untitled10 = untitled10;
    }
    /** 更新时间 */
    public Date getUpdateTime(){
        return this.updateTime;
    }
    /** 更新时间 */
    public void setUpdateTime(Date updateTime){
        this.updateTime = updateTime;
    }
}