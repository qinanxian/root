package com.vekai.crops.customer.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Table(name = "CUST_BASE")
public class CustomerPO implements Serializable,Cloneable{
    /** 客户ID */
    @Id
    @GeneratedValue(generator = "com.vekai.crops.customer.entity.CustomerPO.custId")
    private String custId ;
    /** 客户名 */
    private String custName ;
    /** 客户类型 */
    private String custType ;
    /** 证件国别 */
    private String certCountry ;
    /** 证件类型 */
    private String certType ;
    /** 证件号 */
    private String certId ;
    /** 头像 */
    private String avatar ;
    /** 外部识别码 */
    private String externalCode ;
    /** 参与交易角色 */
    private String tradedAct ;
    /** 风险标记 */
    private String riskTags ;
    /** 来源渠道 */
    private String channel ;
    /** 评分值 */
    private Integer ratingScore ;
    /** 评分等级 */
    private String rating ;
    /** 转换商机ID */
    private String opporId ;
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

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getCertCountry() {
        return certCountry;
    }

    public void setCertCountry(String certCountry) {
        this.certCountry = certCountry;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public String getTradedAct() {
        return tradedAct;
    }

    public void setTradedAct(String tradedAct) {
        this.tradedAct = tradedAct;
    }

    public String getRiskTags() {
        return riskTags;
    }

    public void setRiskTags(String riskTags) {
        this.riskTags = riskTags;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(Integer ratingScore) {
        this.ratingScore = ratingScore;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOpporId() {
        return opporId;
    }

    public void setOpporId(String opporId) {
        this.opporId = opporId;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
