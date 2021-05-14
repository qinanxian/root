package com.vekai.fiscal.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 会计准则 */
@Table(name="CONF_FISC_GAAP")
public class ConfFiscGaapPO implements Serializable,Cloneable{
    /** 会计准则代码 */
    @Id
    @GeneratedValue
    private String gaapDef ;
    /** 会计准则名称 */
    private String gaapName ;
    /** 会计准则说明 */
    private String gaapIntro ;
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

    /** 会计准则代码 */
    public String getGaapDef(){
        return this.gaapDef;
    }
    /** 会计准则代码 */
    public void setGaapDef(String gaapDef){
        this.gaapDef = gaapDef;
    }
    /** 会计准则名称 */
    public String getGaapName(){
        return this.gaapName;
    }
    /** 会计准则名称 */
    public void setGaapName(String gaapName){
        this.gaapName = gaapName;
    }
    /** 会计准则说明 */
    public String getGaapIntro(){
        return this.gaapIntro;
    }
    /** 会计准则说明 */
    public void setGaapIntro(String gaapIntro){
        this.gaapIntro = gaapIntro;
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
