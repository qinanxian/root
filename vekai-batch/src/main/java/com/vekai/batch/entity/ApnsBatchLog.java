package com.vekai.batch.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="APNS_BATCH_LOG")
public class ApnsBatchLog implements Serializable,Cloneable{
    /** 作业实例号 */
    @Id
    @GeneratedValue
    private Long jobInstanceId ;
    /** 批量标识 */
    private String batchJobKey ;
    /** 批量名称 */
    private String batchJobName ;
    /** 行号 */
    @Id
    @GeneratedValue
    private Long lineNumber ;
    /** 日志名称 */
    private String loggerName ;
    /** 日志级别 */
    private String logLevel ;
    /** 线程名 */
    private String threadName ;
    /** 日志内容 */
    private String logMessage ;
    /** 日志时间 */
    private Long logTimestamp ;
    /** 创建人 */
    private String createdBy ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新人 */
    private String updatedBy ;
    /** 更新时间 */
    private Date updatedTime ;

    /** 作业实例号 */
    public Long getJobInstanceId(){
        return this.jobInstanceId;
    }
    /** 作业实例号 */
    public void setJobInstanceId(Long jobInstanceId){
        this.jobInstanceId = jobInstanceId;
    }
    /** 批量标识 */
    public String getBatchJobKey(){
        return this.batchJobKey;
    }
    /** 批量标识 */
    public void setBatchJobKey(String batchJobKey){
        this.batchJobKey = batchJobKey;
    }
    /** 批量名称 */
    public String getBatchJobName(){
        return this.batchJobName;
    }
    /** 批量名称 */
    public void setBatchJobName(String batchJobName){
        this.batchJobName = batchJobName;
    }
    /** 行号 */
    public Long getLineNumber(){
        return this.lineNumber;
    }
    /** 行号 */
    public void setLineNumber(Long lineNumber){
        this.lineNumber = lineNumber;
    }
    /** 日志名称 */
    public String getLoggerName(){
        return this.loggerName;
    }
    /** 日志名称 */
    public void setLoggerName(String loggerName){
        this.loggerName = loggerName;
    }
    /** 日志级别 */
    public String getLogLevel(){
        return this.logLevel;
    }
    /** 日志级别 */
    public void setLogLevel(String logLevel){
        this.logLevel = logLevel;
    }
    /** 线程名 */
    public String getThreadName(){
        return this.threadName;
    }
    /** 线程名 */
    public void setThreadName(String threadName){
        this.threadName = threadName;
    }
    /** 日志内容 */
    public String getLogMessage(){
        return this.logMessage;
    }
    /** 日志内容 */
    public void setLogMessage(String logMessage){
        this.logMessage = logMessage;
    }
    /** 日志时间 */
    public Long getLogTimestamp(){
        return this.logTimestamp;
    }
    /** 日志时间 */
    public void setLogTimestamp(Long logTimestamp){
        this.logTimestamp = logTimestamp;
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