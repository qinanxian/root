package com.vekai.appframe.cmon.dashboard.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/** 主页小组件 */
@Table(name="CMON_DASH_BOARD")
public class CmonDashBoardPO implements Serializable,Cloneable{
  /** 标识号 */
  @Id
  @GeneratedValue
  private String id ;
  /** 用户号 */
  private String userId ;
  /** 代码 */
  private String boardKey ;
  /** 名称 */
  private String name ;
  /** X轴 */
  private Integer axisX ;
  /** Y轴 */
  private Integer axisY ;
  /** 宽度 */
  private Integer sizeW ;
  /** 高度 */
  private Integer sizeH ;
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

  /** 标识号 */
  public String getId(){
    return this.id;
  }
  /** 标识号 */
  public void setId(String id){
    this.id = id;
  }
  /** 用户号 */
  public String getUserId(){
    return this.userId;
  }
  /** 用户号 */
  public void setUserId(String userId){
    this.userId = userId;
  }
  /** 代码 */
  public String getBoardKey(){
    return this.boardKey;
  }
  /** 代码 */
  public void setBoardKey(String boardKey){
    this.boardKey = boardKey;
  }
  /** 名称 */
  public String getName(){
    return this.name;
  }
  /** 名称 */
  public void setName(String name){
    this.name = name;
  }
  /** X轴 */
  public Integer getAxisX(){
    return this.axisX;
  }
  /** X轴 */
  public void setAxisX(Integer axisX){
    this.axisX = axisX;
  }
  /** Y轴 */
  public Integer getAxisY(){
    return this.axisY;
  }
  /** Y轴 */
  public void setAxisY(Integer axisY){
    this.axisY = axisY;
  }
  /** 宽度 */
  public Integer getSizeW(){
    return this.sizeW;
  }
  /** 宽度 */
  public void setSizeW(Integer sizeW){
    this.sizeW = sizeW;
  }
  /** 高度 */
  public Integer getSizeH(){
    return this.sizeH;
  }
  /** 高度 */
  public void setSizeH(Integer sizeH){
    this.sizeH = sizeH;
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
