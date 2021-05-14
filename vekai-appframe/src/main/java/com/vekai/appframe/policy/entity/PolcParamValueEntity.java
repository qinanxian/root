package com.vekai.appframe.policy.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="POLC_PARAM_VALUE")
public class PolcParamValueEntity implements Serializable,Cloneable{
    /** 参数值ID */
    @Id
    @GeneratedValue
    private String paramValueId ;
    /** 参数ID */
    private String paramId ;
    /** 参数值代码 */
    private String valueCode ;
    /** 参数值名称 */
    private String valueName ;
    /** 参数值顺序 */
    private String valueSort ;
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

    /** 参数值ID */
    public String getParamValueId(){
        return this.paramValueId;
    }
    /** 参数值ID */
    public void setParamValueId(String paramValueId){
        this.paramValueId = paramValueId;
    }
    /** 参数ID */
    public String getParamId(){
        return this.paramId;
    }
    /** 参数ID */
    public void setParamId(String paramId){
        this.paramId = paramId;
    }
    /** 参数值代码 */
    public String getValueCode(){
        return this.valueCode;
    }
    /** 参数值代码 */
    public void setValueCode(String valueCode){
        this.valueCode = valueCode;
    }
    /** 参数值名称 */
    public String getValueName(){
        return this.valueName;
    }
    /** 参数值名称 */
    public void setValueName(String valueName){
        this.valueName = valueName;
    }
    /** 参数值顺序 */
    public String getValueSort(){
        return this.valueSort;
    }
    /** 参数值顺序 */
    public void setValueSort(String valueSort){
        this.valueSort = valueSort;
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
