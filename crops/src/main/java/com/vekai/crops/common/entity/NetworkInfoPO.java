package com.vekai.crops.common.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="NETWORK_INFO")
public class NetworkInfoPO implements Serializable,Cloneable{
    /** 编号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 名称 */
    private String name ;
    /** 所选地区 */
    private String districts ;
    /** 详细地址 */
    private String address ;
    /** 邮编 */
    private String code ;
    /** 客服电话 */
    private String phone ;
    /** 工作时间 */
    private String workeHour ;
    /** 经度 */
    private Double longitude ;
    /** 纬度 */
    private Double latitude ;
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
    /** 名称 */
    public String getName(){
        return this.name;
    }
    /** 名称 */
    public void setName(String name){
        this.name = name;
    }
    /** 所选地区 */
    public String getDistricts(){
        return this.districts;
    }
    /** 所选地区 */
    public void setDistricts(String districts){
        this.districts = districts;
    }
    /** 详细地址 */
    public String getAddress(){
        return this.address;
    }
    /** 详细地址 */
    public void setAddress(String address){
        this.address = address;
    }
    /** 邮编 */
    public String getCode(){
        return this.code;
    }
    /** 邮编 */
    public void setCode(String code){
        this.code = code;
    }
    /** 客服电话 */
    public String getPhone(){
        return this.phone;
    }
    /** 客服电话 */
    public void setPhone(String phone){
        this.phone = phone;
    }
    /** 工作时间 */
    public String getWorkeHour(){
        return this.workeHour;
    }
    /** 工作时间 */
    public void setWorkeHour(String workeHour){
        this.workeHour = workeHour;
    }
    /** 经度 */
    public Double getLongitude(){
        return this.longitude;
    }
    /** 经度 */
    public void setLongitude(Double longitude){
        this.longitude = longitude;
    }
    /** 纬度 */
    public Double getLatitude(){
        return this.latitude;
    }
    /** 纬度 */
    public void setLatitude(Double latitude){
        this.latitude = latitude;
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