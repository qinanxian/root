package com.vekai.crops.common.entity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="AUTH_ORG")
public class AuthOrg implements Serializable,Cloneable{
    /** 机构代号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 机构代号 */
    private String code ;
    /** 机构名 */
    private String name ;
    /** 机构路径全称 */
    private String fullName ;
    /** 机构简称 */
    private String shortName ;
    /** 排序代码 */
    private String sortCode ;
    /** 上级机构 */
    private String parentId ;
    /** 机构级别 */
    private String orgLevel ;
    /** 机构类型 */
    private String orgType ;
    /** 所选地区 */
    private String districts ;
    /** 详细地址 */
    private String address ;
    /** 邮编 */
    private String postCode ;
    /** 客服电话 */
    private String phone ;
    /** 工作时间 */
    private String workeHour ;
    /** 经度 */
    private Double longitude ;
    /** 纬度 */
    private Double latitude ;
    /** 负责人 */
    private String leader ;
    /** 机构说明 */
    private String remark ;
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

    /** 机构代号 */
    public String getId(){
        return this.id;
    }
    /** 机构代号 */
    public void setId(String id){
        this.id = id;
    }
    /** 机构代号 */
    public String getCode(){
        return this.code;
    }
    /** 机构代号 */
    public void setCode(String code){
        this.code = code;
    }
    /** 机构名 */
    public String getName(){
        return this.name;
    }
    /** 机构名 */
    public void setName(String name){
        this.name = name;
    }
    /** 机构路径全称 */
    public String getFullName(){
        return this.fullName;
    }
    /** 机构路径全称 */
    public void setFullName(String fullName){
        this.fullName = fullName;
    }
    /** 机构简称 */
    public String getShortName(){
        return this.shortName;
    }
    /** 机构简称 */
    public void setShortName(String shortName){
        this.shortName = shortName;
    }
    /** 排序代码 */
    public String getSortCode(){
        return this.sortCode;
    }
    /** 排序代码 */
    public void setSortCode(String sortCode){
        this.sortCode = sortCode;
    }
    /** 上级机构 */
    public String getParentId(){
        return this.parentId;
    }
    /** 上级机构 */
    public void setParentId(String parentId){
        this.parentId = parentId;
    }
    /** 机构级别 */
    public String getOrgLevel(){
        return this.orgLevel;
    }
    /** 机构级别 */
    public void setOrgLevel(String orgLevel){
        this.orgLevel = orgLevel;
    }
    /** 机构类型 */
    public String getOrgType(){
        return this.orgType;
    }
    /** 机构类型 */
    public void setOrgType(String orgType){
        this.orgType = orgType;
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
    public String getPostCode(){
        return this.postCode;
    }
    /** 邮编 */
    public void setPostCode(String postCode){
        this.postCode = postCode;
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
    /** 负责人 */
    public String getLeader(){
        return this.leader;
    }
    /** 负责人 */
    public void setLeader(String leader){
        this.leader = leader;
    }
    /** 机构说明 */
    public String getRemark(){
        return this.remark;
    }
    /** 机构说明 */
    public void setRemark(String remark){
        this.remark = remark;
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