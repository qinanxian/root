package com.vekai.crops.codetodo.image.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="MSB_IMAGE_RECORD")
public class MsbImageRecord implements Serializable,Cloneable{
    /** 编号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 关联ID */
    private String realId ;
    /** 关联类别 */
    private String realType ;
    /** 大图FileID */
    private String bigImageId ;
    /** 文件名 */
    private String imageName ;
    /** 缩略图base64 */
    private String imageBase ;
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

    /** 编号 */
    public String getId(){
        return this.id;
    }
    /** 编号 */
    public void setId(String id){
        this.id = id;
    }
    /** 关联ID */
    public String getRealId(){
        return this.realId;
    }
    /** 关联ID */
    public void setRealId(String realId){
        this.realId = realId;
    }
    /** 关联类别 */
    public String getRealType(){
        return this.realType;
    }
    /** 关联类别 */
    public void setRealType(String realType){
        this.realType = realType;
    }
    /** 大图FileID */
    public String getBigImageId(){
        return this.bigImageId;
    }
    /** 大图FileID */
    public void setBigImageId(String bigImageId){
        this.bigImageId = bigImageId;
    }
    /** 文件名 */
    public String getImageName(){
        return this.imageName;
    }
    /** 文件名 */
    public void setImageName(String imageName){
        this.imageName = imageName;
    }
    /** 缩略图base64 */
    public String getImageBase(){
        return this.imageBase;
    }
    /** 缩略图base64 */
    public void setImageBase(String imageBase){
        this.imageBase = imageBase;
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