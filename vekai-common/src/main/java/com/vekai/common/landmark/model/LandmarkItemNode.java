/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vekai.common.landmark.model;

import cn.fisok.raw.lang.TreeNode;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/7/22
 * @desc :
 */
public class LandmarkItemNode extends TreeNode<LandmarkItemNode> {
    /** 流水号 */
    @Id
    @GeneratedValue
    private Long markItemId ;
    /** 实例号 */
    private Long landMarkId ;
    /** 子项代码 */
    private String itemKey ;
    /** 子项名称 */
    private String itemName ;
    /** 子项层级 */
    private Integer hierarchy ;
    /** 排序代码 */
    private String sortCode ;
    /** 子项样式 */
    private String itemStyle ;
    /** 脚本动作模板 */
    private String actionTpl ;
    /** 乐观锁 */
    private Integer revision ;
    /** 租户 */
    private Long tenantId ;
    /** 状态 */
    private String status ;
    /** 暂存草稿 */
    private String isDraft ;
    /** 是否删除 */
    private String isDelete ;
    /** 创建人 */
    private Long createdUser ;
    /** 创建机构 */
    private Long createdOrg ;
    /** 创建日期 */
    private String createdDate ;
    /** 创建时间 */
    private String createdTime ;
    /** 更新人 */
    private Long updatedUser ;
    /** 更新机构 */
    private Long updatedOrg ;
    /** 更新日期 */
    private String updatedDate ;
    /** 更新时间 */
    private String updatedTime ;
    /** 备注 */
    private String remark ;

    /** 流水号 */
    public Long getMarkItemId(){
        return this.markItemId;
    }
    /** 流水号 */
    public void setMarkItemId(Long markItemId){
        this.markItemId = markItemId;
    }
    /** 实例号 */
    public Long getLandMarkId(){
        return this.landMarkId;
    }
    /** 实例号 */
    public void setLandMarkId(Long landMarkId){
        this.landMarkId = landMarkId;
    }
    /** 子项代码 */
    public String getItemKey(){
        return this.itemKey;
    }
    /** 子项代码 */
    public void setItemKey(String itemKey){
        this.itemKey = itemKey;
    }
    /** 子项名称 */
    public String getItemName(){
        return this.itemName;
    }
    /** 子项名称 */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    /** 子项层级 */
    public Integer getHierarchy(){
        return this.hierarchy;
    }
    /** 子项层级 */
    public void setHierarchy(Integer hierarchy){
        this.hierarchy = hierarchy;
    }
    /** 排序代码 */
    public String getSortCode(){
        return this.sortCode;
    }
    /** 排序代码 */
    public void setSortCode(String sortCode){
        this.sortCode = sortCode;
    }
    /** 子项样式 */
    public String getItemStyle(){
        return this.itemStyle;
    }
    /** 子项样式 */
    public void setItemStyle(String itemStyle){
        this.itemStyle = itemStyle;
    }
    /** 脚本动作模板 */
    public String getActionTpl(){
        return this.actionTpl;
    }
    /** 脚本动作模板 */
    public void setActionTpl(String actionTpl){
        this.actionTpl = actionTpl;
    }
    /** 乐观锁 */
    public Integer getRevision(){
        return this.revision;
    }
    /** 乐观锁 */
    public void setRevision(Integer revision){
        this.revision = revision;
    }
    /** 租户 */
    public Long getTenantId(){
        return this.tenantId;
    }
    /** 租户 */
    public void setTenantId(Long tenantId){
        this.tenantId = tenantId;
    }
    /** 状态 */
    public String getStatus(){
        return this.status;
    }
    /** 状态 */
    public void setStatus(String status){
        this.status = status;
    }
    /** 暂存草稿 */
    public String getIsDraft(){
        return this.isDraft;
    }
    /** 暂存草稿 */
    public void setIsDraft(String isDraft){
        this.isDraft = isDraft;
    }
    /** 是否删除 */
    public String getIsDelete(){
        return this.isDelete;
    }
    /** 是否删除 */
    public void setIsDelete(String isDelete){
        this.isDelete = isDelete;
    }
    /** 创建人 */
    public Long getCreatedUser(){
        return this.createdUser;
    }
    /** 创建人 */
    public void setCreatedUser(Long createdUser){
        this.createdUser = createdUser;
    }
    /** 创建机构 */
    public Long getCreatedOrg(){
        return this.createdOrg;
    }
    /** 创建机构 */
    public void setCreatedOrg(Long createdOrg){
        this.createdOrg = createdOrg;
    }
    /** 创建日期 */
    public String getCreatedDate(){
        return this.createdDate;
    }
    /** 创建日期 */
    public void setCreatedDate(String createdDate){
        this.createdDate = createdDate;
    }
    /** 创建时间 */
    public String getCreatedTime(){
        return this.createdTime;
    }
    /** 创建时间 */
    public void setCreatedTime(String createdTime){
        this.createdTime = createdTime;
    }
    /** 更新人 */
    public Long getUpdatedUser(){
        return this.updatedUser;
    }
    /** 更新人 */
    public void setUpdatedUser(Long updatedUser){
        this.updatedUser = updatedUser;
    }
    /** 更新机构 */
    public Long getUpdatedOrg(){
        return this.updatedOrg;
    }
    /** 更新机构 */
    public void setUpdatedOrg(Long updatedOrg){
        this.updatedOrg = updatedOrg;
    }
    /** 更新日期 */
    public String getUpdatedDate(){
        return this.updatedDate;
    }
    /** 更新日期 */
    public void setUpdatedDate(String updatedDate){
        this.updatedDate = updatedDate;
    }
    /** 更新时间 */
    public String getUpdatedTime(){
        return this.updatedTime;
    }
    /** 更新时间 */
    public void setUpdatedTime(String updatedTime){
        this.updatedTime = updatedTime;
    }
    /** 备注 */
    public String getRemark(){
        return this.remark;
    }
    /** 备注 */
    public void setRemark(String remark){
        this.remark = remark;
    }
}
