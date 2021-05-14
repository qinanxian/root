package com.vekai.crops.customer.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="WECHAT_INFO")
public class CustomerWechatInfoPO implements Serializable,Cloneable{
    /** 编号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 客户编号 */
    private String customerId ;
    /** 微信UnionID */
    private String unionId ;
    /** 公众号OpenID */
    private String openId ;
    /** 昵称 */
    private String nickName ;
    /** 性别 */
    private String sex ;
    /** 国家 */
    private String country ;
    /** 省份 */
    private String province ;
    /** 城市 */
    private String city ;
    /** 用户头像 */
    private String headImgUrl ;
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
    /** 微信UnionID */
    public String getUnionId(){
        return this.unionId;
    }
    /** 微信UnionID */
    public void setUnionId(String unionId){
        this.unionId = unionId;
    }
    /** 公众号OpenID */
    public String getOpenId(){
        return this.openId;
    }
    /** 公众号OpenID */
    public void setOpenId(String openId){
        this.openId = openId;
    }
    /** 昵称 */
    public String getNickName(){
        return this.nickName;
    }
    /** 昵称 */
    public void setNickName(String nickName){
        this.nickName = nickName;
    }
    /** 性别 */
    public String getSex(){
        return this.sex;
    }
    /** 性别 */
    public void setSex(String sex){
        this.sex = sex;
    }
    /** 国家 */
    public String getCountry(){
        return this.country;
    }
    /** 国家 */
    public void setCountry(String country){
        this.country = country;
    }
    /** 省份 */
    public String getProvince(){
        return this.province;
    }
    /** 省份 */
    public void setProvince(String province){
        this.province = province;
    }
    /** 城市 */
    public String getCity(){
        return this.city;
    }
    /** 城市 */
    public void setCity(String city){
        this.city = city;
    }
    /** 用户头像 */
    public String getHeadImgUrl(){
        return this.headImgUrl;
    }
    /** 用户头像 */
    public void setHeadImgUrl(String headImgUrl){
        this.headImgUrl = headImgUrl;
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