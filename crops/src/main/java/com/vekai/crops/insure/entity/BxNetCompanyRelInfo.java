package com.vekai.crops.insure.entity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name="BX_NET_COMPANY_REL")
public class BxNetCompanyRelInfo implements Serializable,Cloneable{
    /** 保险公司编号 */
    @Id
    @GeneratedValue
    private String comId ;
    /** 网点编号 */
    private String netId ;
    /** 网点名称/二级支行 */
    private String netName ;
    /** 删除标识 */
    private String flag ;
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

    /** 保险公司编号 */
    public String getComId(){
        return this.comId;
    }
    /** 保险公司编号 */
    public void setComId(String comId){
        this.comId = comId;
    }
    /** 网点编号 */
    public String getNetId(){
        return this.netId;
    }
    /** 网点编号 */
    public void setNetId(String netId){
        this.netId = netId;
    }
    /** 网点名称/二级支行 */
    public String getNetName(){
        return this.netName;
    }
    /** 网点名称/二级支行 */
    public void setNetName(String netName){
        this.netName = netName;
    }
    /** 删除标识 */
    public String getFlag() {
        return flag;
    }
    /** 删除标识 */
    public void setFlag(String flag) {
        this.flag = flag;
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