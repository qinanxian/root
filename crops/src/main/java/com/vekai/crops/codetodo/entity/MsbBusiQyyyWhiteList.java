package com.vekai.crops.codetodo.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="MSB_BUSI_QYYY_WHITE_LIST")
public class MsbBusiQyyyWhiteList implements Serializable,Cloneable{
    /** 白名单ID */
    @Id
    @GeneratedValue(generator = "JDBC")
    private String whiteId ;
    /** 机构号 */
    private String orgNo ;
    /** 机构名称 */
    private String orgName ;
    /** 员工OA号 */
    private String oaNo ;
    /** 员工姓名 */
    private String oaName ;
    /** 员工手机号 */
    private String mobileNo ;
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

    /** 白名单ID */
    public String getWhiteId(){
        return this.whiteId;
    }
    /** 白名单ID */
    public void setWhiteId(String whiteId){
        this.whiteId = whiteId;
    }
    /** 机构号 */
    public String getOrgNo(){
        return this.orgNo;
    }
    /** 机构号 */
    public void setOrgNo(String orgNo){
        this.orgNo = orgNo;
    }
    /** 机构名称 */
    public String getOrgName(){
        return this.orgName;
    }
    /** 机构名称 */
    public void setOrgName(String orgName){
        this.orgName = orgName;
    }
    /** 员工OA号 */
    public String getOaNo(){
        return this.oaNo;
    }
    /** 员工OA号 */
    public void setOaNo(String oaNo){
        this.oaNo = oaNo;
    }
    /** 员工姓名 */
    public String getOaName(){
        return this.oaName;
    }
    /** 员工姓名 */
    public void setOaName(String oaName){
        this.oaName = oaName;
    }
    /** 员工手机号 */
    public String getMobileNo(){
        return this.mobileNo;
    }
    /** 员工手机号 */
    public void setMobileNo(String mobileNo){
        this.mobileNo = mobileNo;
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
