package com.vekai.auth.controller;

import com.vekai.auth.entity.RolePermit;
import com.vekai.auth.service.RoleService;
import cn.fisok.raw.lang.MapObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/role")
public class RoleController {
    @Autowired
    RoleService roleService;

    @PostMapping("/savePermit/{roleId}")
    public int saveUserPilot(@PathVariable("roleId") String roleId, @RequestBody MapObject[] nodes){
        return roleService.saveUserPilot(roleId,nodes);
    }

    @GetMapping("/getPermits/{roleId}")
    public List<RolePermit> getRolePermits(@PathVariable("roleId") String roleId){
        return roleService.getRolePermits(roleId);
    }


}
