package com.vekai.crops.etl.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Table(name="MSB_SEQ_NO")
public class MsbSeqNo implements Serializable,Cloneable{
    /** 请求流水号 */
    @Id
    @GeneratedValue(generator = "JDBC")
    private String reqSeqNo ;
    /** 业务跟踪号 */
    private String traceSeqNo ;
    /** 服务方法名 */
    private String serviceName ;
    /** 乐观锁 */
    private Integer revision ;
    /** 创建人 */
    private String createdBy ;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime ;
    /** 更新人 */
    private String updatedBy ;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime ;

    /** 请求流水号 */
    public String getReqSeqNo(){
        return this.reqSeqNo;
    }
    /** 请求流水号 */
    public void setReqSeqNo(String reqSeqNo){
        this.reqSeqNo = reqSeqNo;
    }
    /** 业务跟踪号 */
    public String getTraceSeqNo(){
        return this.traceSeqNo;
    }
    /** 业务跟踪号 */
    public void setTraceSeqNo(String traceSeqNo){
        this.traceSeqNo = traceSeqNo;
    }
    /** 服务方法名 */
    public String getServiceName(){
        return this.serviceName;
    }
    /** 服务方法名 */
    public void setServiceName(String serviceName){
        this.serviceName = serviceName;
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
