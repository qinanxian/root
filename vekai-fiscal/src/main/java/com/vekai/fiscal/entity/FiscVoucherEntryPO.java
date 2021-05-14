package com.vekai.fiscal.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 凭证科目 */
@Table(name="FISC_VOUCHER_ENTRY")
public class FiscVoucherEntryPO implements Serializable,Cloneable{
    /** 凭证科目ID */
    @Id
    @GeneratedValue
    private String voucherEntryId ;
    /** 凭证ID */
    private String voucherId ;
    /** 账套科目ID */
    private String bookEntryId ;
    /** 记账事件分录ID */
    private String eventEntryId ;
    /** 科目代码 */
    private String entryCode ;
    /** 科目层级;最小为1 */
    private Integer hierarchy ;
    /** 助记码 */
    private String mnemonicCode ;
    /** 科目名称;一级科目-二级科目... */
    private String entryName ;
    /** 科目全名 */
    private String entryFullName ;
    /** 摘要说明 */
    private String entryNote ;
    /** 借贷方向;代码：ChargeupDirection ；Debit 借方，Credit 贷方 */
    private String direction ;
    /** 原币 */
    private String originalCurrency ;
    /** 原币金额 */
    private Double originalAmt ;
    /** 本位币 */
    private String currency ;
    /** 发生金额 */
    private Double occurAmt ;
    /** 现金科目 */
    private String isCash ;
    /** 银行科目 */
    private String isBank ;
    /** 辅助对象名称;根据这个名称，制作分录 */
    private String assistName ;
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

    /** 凭证科目ID */
    public String getVoucherEntryId(){
        return this.voucherEntryId;
    }
    /** 凭证科目ID */
    public void setVoucherEntryId(String voucherEntryId){
        this.voucherEntryId = voucherEntryId;
    }
    /** 凭证ID */
    public String getVoucherId(){
        return this.voucherId;
    }
    /** 凭证ID */
    public void setVoucherId(String voucherId){
        this.voucherId = voucherId;
    }
    /** 账套科目ID */
    public String getBookEntryId(){
        return this.bookEntryId;
    }
    /** 账套科目ID */
    public void setBookEntryId(String bookEntryId){
        this.bookEntryId = bookEntryId;
    }
    /** 记账事件分录ID */
    public String getEventEntryId(){
        return this.eventEntryId;
    }
    /** 记账事件分录ID */
    public void setEventEntryId(String eventEntryId){
        this.eventEntryId = eventEntryId;
    }
    /** 科目代码 */
    public String getEntryCode(){
        return this.entryCode;
    }
    /** 科目代码 */
    public void setEntryCode(String entryCode){
        this.entryCode = entryCode;
    }
    /** 科目层级;最小为1 */
    public Integer getHierarchy(){
        return this.hierarchy;
    }
    /** 科目层级;最小为1 */
    public void setHierarchy(Integer hierarchy){
        this.hierarchy = hierarchy;
    }
    /** 助记码 */
    public String getMnemonicCode(){
        return this.mnemonicCode;
    }
    /** 助记码 */
    public void setMnemonicCode(String mnemonicCode){
        this.mnemonicCode = mnemonicCode;
    }
    /** 科目名称;一级科目-二级科目... */
    public String getEntryName(){
        return this.entryName;
    }
    /** 科目名称;一级科目-二级科目... */
    public void setEntryName(String entryName){
        this.entryName = entryName;
    }
    /** 科目全名 */
    public String getEntryFullName(){
        return this.entryFullName;
    }
    /** 科目全名 */
    public void setEntryFullName(String entryFullName){
        this.entryFullName = entryFullName;
    }
    /** 摘要说明 */
    public String getEntryNote(){
        return this.entryNote;
    }
    /** 摘要说明 */
    public void setEntryNote(String entryNote){
        this.entryNote = entryNote;
    }
    /** 借贷方向;代码：ChargeupDirection ；Debit 借方，Credit 贷方 */
    public String getDirection(){
        return this.direction;
    }
    /** 借贷方向;代码：ChargeupDirection ；Debit 借方，Credit 贷方 */
    public void setDirection(String direction){
        this.direction = direction;
    }
    /** 发生金额 */
    public Double getOccurAmt(){
        return this.occurAmt;
    }
    /** 发生金额 */
    public void setOccurAmt(Double occurAmt){
        this.occurAmt = occurAmt;
    }
    /** 现金科目 */
    public String getIsCash(){
        return this.isCash;
    }
    /** 现金科目 */
    public void setIsCash(String isCash){
        this.isCash = isCash;
    }
    /** 银行科目 */
    public String getIsBank(){
        return this.isBank;
    }
    /** 银行科目 */
    public void setIsBank(String isBank){
        this.isBank = isBank;
    }
    /** 辅助对象名称;根据这个名称，制作分录 */
    public String getAssistName(){
        return this.assistName;
    }
    /** 辅助对象名称;根据这个名称，制作分录 */
    public void setAssistName(String assistName){
        this.assistName = assistName;
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

    public String getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(String originalCurrency) {
        this.originalCurrency = originalCurrency;
    }

    public Double getOriginalAmt() {
        return originalAmt;
    }

    public void setOriginalAmt(Double originalAmt) {
        this.originalAmt = originalAmt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}