package com.vekai.fiscal.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 记账科目 */
@Table(name="FISC_BOOK_ENTRY")
public class FiscBookEntryPO implements Serializable,Cloneable{
    /** 账套代码 */
    private String bookCode ;
    /** 账套科目ID */
    @Id
    @GeneratedValue
    private String bookEntryId ;
    /** 模板科目定义 */
    private String gaapEntryDef ;
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
    /** 状态 */
    private String status ;
    /** 科目类别;代码：GAAPEntryCategory ；资产，负债，权益，成本，损益，共同，表外 */
    private String categoryCode ;
    /** 外币核算 */
    private String foreignCurrency ;
    /** 期末调汇 */
    private String finalTransfer ;
    /** 往来业务核算 */
    private String tradeAccounting ;
    /** 现金科目 */
    private String isCash ;
    /** 银行科目 */
    private String isBank ;
    /** 辅助科目名称模板 */
    private String assistNameTpl ;
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

    /** 账套代码 */
    public String getBookCode(){
        return this.bookCode;
    }
    /** 账套代码 */
    public void setBookCode(String bookCode){
        this.bookCode = bookCode;
    }
    /** 账套科目ID */
    public String getBookEntryId(){
        return this.bookEntryId;
    }
    /** 账套科目ID */
    public void setBookEntryId(String bookEntryId){
        this.bookEntryId = bookEntryId;
    }
    /** 模板科目定义 */
    public String getGaapEntryDef(){
        return this.gaapEntryDef;
    }
    /** 模板科目定义 */
    public void setGaapEntryDef(String gaapEntryDef){
        this.gaapEntryDef = gaapEntryDef;
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
    /** 状态 */
    public String getStatus(){
        return this.status;
    }
    /** 状态 */
    public void setStatus(String status){
        this.status = status;
    }
    /** 科目类别;代码：GAAPEntryCategory ；资产，负债，权益，成本，损益，共同，表外 */
    public String getCategoryCode(){
        return this.categoryCode;
    }
    /** 科目类别;代码：GAAPEntryCategory ；资产，负债，权益，成本，损益，共同，表外 */
    public void setCategoryCode(String categoryCode){
        this.categoryCode = categoryCode;
    }
    /** 外币核算 */
    public String getForeignCurrency(){
        return this.foreignCurrency;
    }
    /** 外币核算 */
    public void setForeignCurrency(String foreignCurrency){
        this.foreignCurrency = foreignCurrency;
    }
    /** 期末调汇 */
    public String getFinalTransfer(){
        return this.finalTransfer;
    }
    /** 期末调汇 */
    public void setFinalTransfer(String finalTransfer){
        this.finalTransfer = finalTransfer;
    }
    /** 往来业务核算 */
    public String getTradeAccounting(){
        return this.tradeAccounting;
    }
    /** 往来业务核算 */
    public void setTradeAccounting(String tradeAccounting){
        this.tradeAccounting = tradeAccounting;
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
    /** 辅助科目名称模板 */
    public String getAssistNameTpl(){
        return this.assistNameTpl;
    }
    /** 辅助科目名称模板 */
    public void setAssistNameTpl(String assistNameTpl){
        this.assistNameTpl = assistNameTpl;
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