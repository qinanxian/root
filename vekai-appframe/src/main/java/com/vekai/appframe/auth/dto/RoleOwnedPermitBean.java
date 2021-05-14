package com.vekai.appframe.auth.dto;


import java.util.List;

public class RoleOwnedPermitBean {
    private String roleId;
    private String roleCode;
    List<PermitNode> permits;

    public String getRoleId() {
        return roleId;
    }

    private void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    private void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public List<PermitNode> getPermits() {
        return permits;
    }

    private void setPermits(List<PermitNode> permits) {
        this.permits = permits;
    }
}
