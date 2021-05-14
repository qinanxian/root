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
package com.vekai.auth.model;

import cn.fisok.raw.lang.TreeNode;


/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/7/22
 * @desc :
 */
public class OrgNode extends TreeNode<OrgNode> {
    /** 机构ID */
    private Long id ;
    /** 机构代号 */
    private String code ;
    /** 机构名称 */
    private String name ;
    /** 部门路径全称 */
    private String fullName ;
    /** 排序代码 */
    private String sortCode ;
    /** 上级机构code */
    private String parentCode ;
    /** 级别 */
    private String level ;
    /** 机构类型 */
    private String orgType ;
    /** 负责人 */
    private String leader ;
    /** 说明 */
    private String remark ;
    /** 是否管理机构1是 0否 */
    private String manageFlag ;
    /** 状态 */
    private String status ;
    /** 暂存草稿;YesNo */
    private String isDraft ;
    /** 是否删除;YesNo */
    private String isDelete ;
    /** 租户 */
    private Long tenantId ;
    /** 乐观锁 */
    private Integer revision ;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getManageFlag() {
        return manageFlag;
    }

    public void setManageFlag(String manageFlag) {
        this.manageFlag = manageFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(String isDraft) {
        this.isDraft = isDraft;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public Long getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(Long createdUser) {
        this.createdUser = createdUser;
    }

    public Long getCreatedOrg() {
        return createdOrg;
    }

    public void setCreatedOrg(Long createdOrg) {
        this.createdOrg = createdOrg;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(Long updatedUser) {
        this.updatedUser = updatedUser;
    }

    public Long getUpdatedOrg() {
        return updatedOrg;
    }

    public void setUpdatedOrg(Long updatedOrg) {
        this.updatedOrg = updatedOrg;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

}
