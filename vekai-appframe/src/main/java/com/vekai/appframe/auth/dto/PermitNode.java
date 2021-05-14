package com.vekai.appframe.auth.dto;


import cn.fisok.raw.lang.TreeNode;
import com.vekai.auth.authorization.PermissionResourceType;
import com.vekai.base.menu.model.RWSortEntity;

public class PermitNode extends TreeNode<PermitNode> implements RWSortEntity {
    private String sortCode;
    private String code;
    private String name;
    private String rw;
    private PermissionResourceType type;

    public PermitNode() {}

    public PermitNode(PermitNode that) {
        this.sortCode = that.sortCode;
        this.code = that.code;
        this.name = that.name;
        this.rw = that.rw;
        this.type = that.type;
    }

    public PermitNode(PermitNode parent, String sortCode, String code, String name, PermissionResourceType type) {
        this(sortCode,code,name,type);
        this.parent = parent;
    }
    public PermitNode(String sortCode, String code, String name, PermissionResourceType type) {
        this.sortCode = sortCode;
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public PermitNode(String sortCode, String code, String name, PermissionResourceType type, String rw) {
        this(sortCode, code, name, type);
        this.rw = rw;
    }

    @Override
    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
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

    public String getRw() {
        return rw;
    }

    @Override
    public void setRw(String rw) {
        this.rw = rw;
    }

    public PermissionResourceType getType() {
        return type;
    }

    public void setType(PermissionResourceType type) {
        this.type = type;
    }
}
