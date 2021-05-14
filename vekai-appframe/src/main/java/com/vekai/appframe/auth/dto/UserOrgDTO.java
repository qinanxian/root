package com.vekai.appframe.auth.dto;

import com.vekai.auth.entity.User;

import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name="AUTH_USER")
public class UserOrgDTO extends User {
    @Transient
    private String orgCode;
    @Transient
    private String orgName;
    @Transient
    private String orgFullName;
    @Transient
    private String orgSortCode;
    @Transient
    private String orgParentId;
    @Transient
    private String orgLevel;
    @Transient
    private String orgType;
    @Transient
    private String orgLeader;
    @Transient
    private String orgRemark;

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Override
    public String getOrgName() {
        return orgName;
    }

    @Override
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgFullName() {
        return orgFullName;
    }

    public void setOrgFullName(String orgFullName) {
        this.orgFullName = orgFullName;
    }

    public String getOrgSortCode() {
        return orgSortCode;
    }

    public void setOrgSortCode(String orgSortCode) {
        this.orgSortCode = orgSortCode;
    }

    public String getOrgParentId() {
        return orgParentId;
    }

    public void setOrgParentId(String orgParentId) {
        this.orgParentId = orgParentId;
    }

    public String getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgLeader() {
        return orgLeader;
    }

    public void setOrgLeader(String orgLeader) {
        this.orgLeader = orgLeader;
    }

    public String getOrgRemark() {
        return orgRemark;
    }

    public void setOrgRemark(String orgRemark) {
        this.orgRemark = orgRemark;
    }
}
