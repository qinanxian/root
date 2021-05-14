package com.vekai.crops.etl.job;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="ETL_SCHEDULE_JOB")
public class ScheduleJob implements Serializable,Cloneable{
    /** 编号 */
    @Id
    @GeneratedValue
    private String jobId ;
    /** 名称 */
    private String jobName ;
    /** 类型 */
    private String jobType ;
    /** 描述 */
    private String jobDesc ;
    /** 分组;etl1,etl2 */
    private String jobGroup ;
    /** 时间表达式 */
    private String cronExp ;
    /** ETL定义文件 */
    private String defineFile ;
    /** 当前状态;0暂停,1运行中,2等待调度 */
    private String jobStatus ;
    /** 上次运行时间 */
    private String latestTime ;
    /** 上次运行结果;succeed,failed */
    private String latestRst ;
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
    public String getJobId(){
        return this.jobId;
    }
    /** 编号 */
    public void setJobId(String jobId){
        this.jobId = jobId;
    }
    /** 名称 */
    public String getJobName(){
        return this.jobName;
    }
    /** 名称 */
    public void setJobName(String jobName){
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    /** 描述 */
    public String getJobDesc(){
        return this.jobDesc;
    }
    /** 描述 */
    public void setJobDesc(String jobDesc){
        this.jobDesc = jobDesc;
    }
    /** 分组;etl1,etl2 */
    public String getJobGroup(){
        return this.jobGroup;
    }
    /** 分组;etl1,etl2 */
    public void setJobGroup(String jobGroup){
        this.jobGroup = jobGroup;
    }
    /** 时间表达式 */
    public String getCronExp(){
        return this.cronExp;
    }
    /** 时间表达式 */
    public void setCronExp(String cronExp){
        this.cronExp = cronExp;
    }
    /** ETL定义文件 */
    public String getDefineFile(){
        return this.defineFile;
    }
    /** ETL定义文件 */
    public void setDefineFile(String defineFile){
        this.defineFile = defineFile;
    }
    /** 当前状态;0暂停,1运行中,2等待调度 */
    public String getJobStatus(){
        return this.jobStatus;
    }
    /** 当前状态;0暂停,1运行中,2等待调度 */
    public void setJobStatus(String jobStatus){
        this.jobStatus = jobStatus;
    }
    /** 上次运行时间 */
    public String getLatestTime(){
        return this.latestTime;
    }
    /** 上次运行时间 */
    public void setLatestTime(String latestTime){
        this.latestTime = latestTime;
    }
    /** 上次运行结果;succeed,failed */
    public String getLatestRst(){
        return this.latestRst;
    }
    /** 上次运行结果;succeed,failed */
    public void setLatestRst(String latestRst){
        this.latestRst = latestRst;
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

    @Override
    public String toString() {
        return "ScheduleJob{" +
                "jobId='" + jobId + '\'' +
                ", jobName='" + jobName + '\'' +
                ", jobDesc='" + jobDesc + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", cronExp='" + cronExp + '\'' +
                ", defineFile='" + defineFile + '\'' +
                ", jobStatus='" + jobStatus + '\'' +
                ", latestTime='" + latestTime + '\'' +
                ", latestRst='" + latestRst + '\'' +
                ", revision=" + revision +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime=" + createdTime +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedTime=" + updatedTime +
                '}';
    }
}