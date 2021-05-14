package com.vekai.fiscal.event.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 记账分录配置 */
@Table(name="CONF_FISC_EVENT_ENTRY")
public class ConfFiscEventEntryPO implements Serializable,Cloneable{
    /** 事件代码 */
    private String eventDef ;
    /** 制证规则项ID */
    @Id
    @GeneratedValue
    private String eventEntryDef ;
    /** 事件摘要 */
    private String eventSummary ;
    /** 凭证分组 */
    private String voucherGroup ;
    /** 使用账套 */
    private String bookCode ;
    /** 科目代码 */
    private String entryCode ;
    /** 科目名称;一级科目-二级科目... */
    private String entryName ;
    /** 借贷方向;代码：ChargeupDirection ；Debit 借方，Credit 贷方 */
    private String direction ;
    /** 金额取值 */
    private String valueFetcher ;
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

    /** 事件代码 */
    public String getEventDef(){
        return this.eventDef;
    }
    /** 事件代码 */
    public void setEventDef(String eventDef){
        this.eventDef = eventDef;
    }
    /** 制证规则项ID */
    public String getEventEntryDef(){
        return this.eventEntryDef;
    }
    /** 制证规则项ID */
    public void setEventEntryDef(String eventEntryDef){
        this.eventEntryDef = eventEntryDef;
    }
    /** 事件摘要 */
    public String getEventSummary(){
        return this.eventSummary;
    }
    /** 事件摘要 */
    public void setEventSummary(String eventSummary){
        this.eventSummary = eventSummary;
    }
    /** 凭证分组 */
    public String getVoucherGroup(){
        return this.voucherGroup;
    }
    /** 凭证分组 */
    public void setVoucherGroup(String voucherGroup){
        this.voucherGroup = voucherGroup;
    }
    /** 使用账套 */
    public String getBookCode(){
        return this.bookCode;
    }
    /** 使用账套 */
    public void setBookCode(String bookCode){
        this.bookCode = bookCode;
    }
    /** 科目代码 */
    public String getEntryCode(){
        return this.entryCode;
    }
    /** 科目代码 */
    public void setEntryCode(String entryCode){
        this.entryCode = entryCode;
    }
    /** 科目名称;一级科目-二级科目... */
    public String getEntryName(){
        return this.entryName;
    }
    /** 科目名称;一级科目-二级科目... */
    public void setEntryName(String entryName){
        this.entryName = entryName;
    }
    /** 借贷方向;代码：ChargeupDirection ；Debit 借方，Credit 贷方 */
    public String getDirection(){
        return this.direction;
    }
    /** 借贷方向;代码：ChargeupDirection ；Debit 借方，Credit 贷方 */
    public void setDirection(String direction){
        this.direction = direction;
    }
    /** 金额取值 */
    public String getValueFetcher(){
        return this.valueFetcher;
    }
    /** 金额取值 */
    public void setValueFetcher(String valueFetcher){
        this.valueFetcher = valueFetcher;
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