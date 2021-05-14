package com.vekai.auth.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by tisir<yangsong158@qq.com> on 2017-05-08
 */

@Table(name="AUTH_ORG")
public class Org implements Serializable,Cloneable{
    private static final long serialVersionUID = 4773996798219764966L;
    @Id
    @GeneratedValue
    private String id;
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
    /** 区 */
    private String districts ;
    /** 地址 */
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
    @Version
    private Integer revision ;
    /** 创建人 */
    private String createdBy ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新人 */
    private String updatedBy ;
    /** 更新时间 */
    private Date updatedTime ;


    public String getId() {
        return id;
    }

    public void setId(String id) {
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
    /** 区 */
    public String getDistricts(){
        return this.districts;
    }
    /** 区 */
    public void setDistricts(String districts){
        this.districts = districts;
    }
    /** 地址 */
    public String getAddress(){
        return this.address;
    }
    /** 地址 */
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
