package com.vekai.appframe.conf.doclist.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Table(name="CONF_DOSSIER")
public class ConfDocListPO implements Serializable,Cloneable{
    /** 文档清单模板代码 */
    @Id
    @GeneratedValue
    private String doclistCode ;
    /** 文档清单名称 */
    private String doclistName ;
    /** 文档清单类型 */
    private String doclistType ;
    /** 分类 */
    private String classification ;
    /** 文档清单说明 */
    private String doclistSummary ;
    /** 处理HANDLER */
    private String handlerClass ;
    /** 乐观锁版本 */
    private Integer revision ;
    /** 创建人 */
    private String createdBy ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新人 */
    private String updatedBy ;
    /** 更新时间 */
    private Date updatedTime ;

    /** 文档清单模板代码 */
    public String getDoclistCode(){
        return this.doclistCode;
    }
    /** 文档清单模板代码 */
    public void setDoclistCode(String doclistCode){
        this.doclistCode = doclistCode;
    }
    /** 文档清单名称 */
    public String getDoclistName(){
        return this.doclistName;
    }
    /** 文档清单名称 */
    public void setDoclistName(String doclistName){
        this.doclistName = doclistName;
    }
    /** 文档清单类型 */
    public String getDoclistType(){
        return this.doclistType;
    }
    /** 文档清单类型 */
    public void setDoclistType(String doclistType){
        this.doclistType = doclistType;
    }
    /** 分类 */
    public String getClassification(){
        return this.classification;
    }
    /** 分类 */
    public void setClassification(String classification){
        this.classification = classification;
    }
    /** 文档清单说明 */
    public String getDoclistSummary(){
        return this.doclistSummary;
    }
    /** 文档清单说明 */
    public void setDoclistSummary(String doclistSummary){
        this.doclistSummary = doclistSummary;
    }
    /** 处理HANDLER */
    public String getHandlerClass(){
        return this.handlerClass;
    }
    /** 处理HANDLER */
    public void setHandlerClass(String handlerClass){
        this.handlerClass = handlerClass;
    }
    /** 乐观锁版本 */
    public Integer getRevision(){
        return this.revision;
    }
    /** 乐观锁版本 */
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
