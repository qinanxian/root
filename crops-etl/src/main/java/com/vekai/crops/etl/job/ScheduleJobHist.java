package com.vekai.crops.etl.job;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="ETL_SCHEDULE_JOB_HIST")
public class ScheduleJobHist implements Serializable,Cloneable{
    /** 历史编号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 任务编号 */
    private String jobId ;
    /** 任务名称 */
    private String jobName ;
    /** 类型 */
    private String jobType ;
    /** 任务描述 */
    private String jobDesc ;
    /** 分组;etl1,etl2 */
    private String jobGroup ;
    /** 时间表达式 */
    private String cronExp ;
    /** ETL定义文件 */
    private String defineFile ;
    /** 任务结果;运行结果succeed,failed */
    private String result ;
    /** 任务启动 */
    private String startTime ;
    /** 任务结束 */
    private String endTime ;
    /** 任务总用时 */
    private String totalTime ;
    /** 步骤度量;kettle步骤度量 */
    private String stepStatus ;
    /** 错误信息 */
    private String zzErrorMsg;
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

    /** 历史编号 */
    public String getId(){
        return this.id;
    }
    /** 历史编号 */
    public void setId(String id){
        this.id = id;
    }
    /** 任务编号 */
    public String getJobId(){
        return this.jobId;
    }
    /** 任务编号 */
    public void setJobId(String jobId){
        this.jobId = jobId;
    }
    /** 任务名称 */
    public String getJobName(){
        return this.jobName;
    }
    /** 任务名称 */
    public void setJobName(String jobName){
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    /** 任务描述 */
    public String getJobDesc(){
        return this.jobDesc;
    }
    /** 任务描述 */
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
    /** 任务结果;运行结果succeed,failed */
    public String getResult(){
        return this.result;
    }
    /** 任务结果;运行结果succeed,failed */
    public void setResult(String result){
        this.result = result;
    }
    /** 任务启动 */
    public String getStartTime(){
        return this.startTime;
    }
    /** 任务启动 */
    public void setStartTime(String startTime){
        this.startTime = startTime;
    }
    /** 任务结束 */
    public String getEndTime(){
        return this.endTime;
    }
    /** 任务结束 */
    public void setEndTime(String endTime){
        this.endTime = endTime;
    }
    /** 任务总用时 */
    public String getTotalTime(){
        return this.totalTime;
    }
    /** 任务总用时 */
    public void setTotalTime(String totalTime){
        this.totalTime = totalTime;
    }
    /** 步骤度量;kettle步骤度量 */
    public String getStepStatus(){
        return this.stepStatus;
    }
    /** 步骤度量;kettle步骤度量 */
    public void setStepStatus(String stepStatus){
        this.stepStatus = stepStatus;
    }
    /** 错误信息 */
    public String getZzErrorMsg(){
        return this.zzErrorMsg;
    }
    /** 错误信息 */
    public void setZzErrorMsg(String zzErrorMsg){
        this.zzErrorMsg = zzErrorMsg;
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