package com.vekai.crops.applicaition.entiry;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="APP_AUTH")
public class AppAuthPO implements Serializable,Cloneable{
    /** 编号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 应用编号 */
    private String appId ;
    /** 排序号 */
    private String sortNo ;
    /** 流程编号（匹配前端组件） */
    private String flowNo ;
    /** 流程名称 */
    private String flowName ;
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
    /** 应用编号 */
    public String getAppId(){
        return this.appId;
    }
    /** 应用编号 */
    public void setAppId(String appId){
        this.appId = appId;
    }
    /** 排序号 */
    public String getSortNo(){
        return this.sortNo;
    }
    /** 排序号 */
    public void setSortNo(String sortNo){
        this.sortNo = sortNo;
    }
    /** 流程编号（匹配前端组件） */
    public String getFlowNo(){
        return this.flowNo;
    }
    /** 流程编号（匹配前端组件） */
    public void setFlowNo(String flowNo){
        this.flowNo = flowNo;
    }
    /** 流程名称 */
    public String getFlowName(){
        return this.flowName;
    }
    /** 流程名称 */
    public void setFlowName(String flowName){
        this.flowName = flowName;
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