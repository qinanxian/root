package com.vekai.crops.codetodo.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="MSB_PARK_INFO")
public class MsbParkInfo implements Serializable,Cloneable{
    /** 编号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 网点信息ID */
    private String networkId ;
    /** 停车场名称 */
    private String parkName ;
    /** 区 */
    private String districts ;
    /** 街道 */
    private String street ;
    /** 地址 */
    private String address ;
    /** 经度 */
    private Double longitude ;
    /** 纬度 */
    private Double latitude ;
    /** 收费规则 */
    private String chargeRule ;
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
    /** 网点信息ID */
    public String getNetworkId(){
        return this.networkId;
    }
    /** 网点信息ID */
    public void setNetworkId(String networkId){
        this.networkId = networkId;
    }
    /** 停车场名称 */
    public String getParkName(){
        return this.parkName;
    }
    /** 停车场名称 */
    public void setParkName(String parkName){
        this.parkName = parkName;
    }
    /** 区 */
    public String getDistricts(){
        return this.districts;
    }
    /** 区 */
    public void setDistricts(String districts){
        this.districts = districts;
    }
    /** 街道 */
    public String getStreet(){
        return this.street;
    }
    /** 街道 */
    public void setStreet(String street){
        this.street = street;
    }
    /** 地址 */
    public String getAddress(){
        return this.address;
    }
    /** 地址 */
    public void setAddress(String address){
        this.address = address;
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
    /** 收费规则 */
    public String getChargeRule(){
        return this.chargeRule;
    }
    /** 收费规则 */
    public void setChargeRule(String chargeRule){
        this.chargeRule = chargeRule;
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