package com.vekai.fiscal.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 科目辅助项 */
@Table(name="CONF_FISC_GAAP_ASSIST")
public class ConfFiscGaapAssistPO implements Serializable,Cloneable{
    /** 科目定义 */
    @Id
    @GeneratedValue
    private String gaapEntryDef ;
    /** 辅助项代码 */
    @Id
    @GeneratedValue
    private String itemCode ;
    /** 辅助项名称 */
    private String itemName ;
    /** 数据类型 */
    private String dataType ;
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

    /** 科目定义 */
    public String getGaapEntryDef(){
        return this.gaapEntryDef;
    }
    /** 科目定义 */
    public void setGaapEntryDef(String gaapEntryDef){
        this.gaapEntryDef = gaapEntryDef;
    }
    /** 辅助项代码 */
    public String getItemCode(){
        return this.itemCode;
    }
    /** 辅助项代码 */
    public void setItemCode(String itemCode){
        this.itemCode = itemCode;
    }
    /** 辅助项名称 */
    public String getItemName(){
        return this.itemName;
    }
    /** 辅助项名称 */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    /** 数据类型 */
    public String getDataType(){
        return this.dataType;
    }
    /** 数据类型 */
    public void setDataType(String dataType){
        this.dataType = dataType;
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
