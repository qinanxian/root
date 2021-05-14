package com.vekai.batch.entity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 定时器 */
@Table(name="FOWK_TIMER")
public class TimerEntity implements Serializable,Cloneable{
    /** 标识号 */
    @Id
    @GeneratedValue
    private String timerKey ;
    /** 定时器类型;代码:TimerType BatchJob:SpringBatch批量 ShellScript-Shell脚本 */
    private String timerType ;
    /** 名称 */
    private String timerName ;
    /** 描述说明 */
    private String timerIntro ;
    /** 表达式 */
    private String cronExpr ;
    /** 执行内容 */
    private String execScript ;
    /** 最近执行开始时间 */
    private Date lastExecStartTime ;
    /** 最近执行耗时;秒为单位 */
    private Long lastExecTimeCost ;
    /** 最近执行状态;成功或失败 */
    private String lastExecStatus ;
    /** 是否正在执行 */
    private String isRunning ;
    /** 状态 */
    private String timerStatus ;
    /** 乐观锁;TimmerStatus: 1-正常,0-停用 */
    private Integer revision ;
    /** 创建人 */
    private String createdBy ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新人 */
    private String updatedBy ;
    /** 更新时间 */
    private Date updatedTime ;

    /** 标识号 */
    public String getTimerKey(){
        return this.timerKey;
    }
    /** 标识号 */
    public void setTimerKey(String timerKey){
        this.timerKey = timerKey;
    }
    /** 定时器类型;代码:TimerType BatchJob:SpringBatch批量 ShellScript-Shell脚本 */
    public String getTimerType(){
        return this.timerType;
    }
    /** 定时器类型;代码:TimerType BatchJob:SpringBatch批量 ShellScript-Shell脚本 */
    public void setTimerType(String timerType){
        this.timerType = timerType;
    }
    /** 名称 */
    public String getTimerName(){
        return this.timerName;
    }
    /** 名称 */
    public void setTimerName(String timerName){
        this.timerName = timerName;
    }
    /** 描述说明 */
    public String getTimerIntro(){
        return this.timerIntro;
    }
    /** 描述说明 */
    public void setTimerIntro(String timerIntro){
        this.timerIntro = timerIntro;
    }
    /** 表达式 */
    public String getCronExpr(){
        return this.cronExpr;
    }
    /** 表达式 */
    public void setCronExpr(String cronExpr){
        this.cronExpr = cronExpr;
    }
    /** 执行内容 */
    public String getExecScript(){
        return this.execScript;
    }
    /** 执行内容 */
    public void setExecScript(String execScript){
        this.execScript = execScript;
    }
    /** 最近执行开始时间 */
    public Date getLastExecStartTime(){
        return this.lastExecStartTime;
    }
    /** 最近执行开始时间 */
    public void setLastExecStartTime(Date lastExecStartTime){
        this.lastExecStartTime = lastExecStartTime;
    }
    /** 最近执行耗时;秒为单位 */
    public Long getLastExecTimeCost(){
        return this.lastExecTimeCost;
    }
    /** 最近执行耗时;秒为单位 */
    public void setLastExecTimeCost(Long lastExecTimeCost){
        this.lastExecTimeCost = lastExecTimeCost;
    }
    /** 最近执行状态;成功或失败 */
    public String getLastExecStatus(){
        return this.lastExecStatus;
    }
    /** 最近执行状态;成功或失败 */
    public void setLastExecStatus(String lastExecStatus){
        this.lastExecStatus = lastExecStatus;
    }
    /** 是否正在执行 */
    public String getIsRunning(){
        return this.isRunning;
    }
    /** 是否正在执行 */
    public void setIsRunning(String isRunning){
        this.isRunning = isRunning;
    }
    /** 状态 */
    public String getTimerStatus(){
        return this.timerStatus;
    }
    /** 状态 */
    public void setTimerStatus(String timerStatus){
        this.timerStatus = timerStatus;
    }
    /** 乐观锁;TimmerStatus: 1-正常,0-停用 */
    public Integer getRevision(){
        return this.revision;
    }
    /** 乐观锁;TimmerStatus: 1-正常,0-停用 */
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