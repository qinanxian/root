package com.vekai.appframe.auth.handler.controller;

import com.vekai.appframe.auth.dto.PermitNode;
import com.vekai.appframe.auth.service.RoleOwnedPermitTreeManager;
import com.vekai.appframe.auth.dto.RoleOwnedPermitBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/PermissionTree")
public class PermissionTreeController {

    @Autowired
    RoleOwnedPermitTreeManager roleOwnedPermitTreeManager;


    @GetMapping("/viewRoleOwnedPermissions")
    public PermitNode viewRoleOwnedPermissionTree(@RequestParam String roleCode){

        return roleOwnedPermitTreeManager.getRoleOwnedPermitTree(roleCode);
    }

    @PostMapping("/relateRoleWithPermissions")
    public void relateRoleOwnedPermissions(@RequestBody RoleOwnedPermitBean roleOwnedPermitBean) {
        roleOwnedPermitTreeManager.updateRoleOwnedPermit(roleOwnedPermitBean);
    }



}
