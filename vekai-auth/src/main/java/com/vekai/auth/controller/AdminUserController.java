package com.vekai.auth.controller;

import com.vekai.auth.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "用户管理")
@RestController
@RequestMapping("/auth/admin/user/")
public class AdminUserController {

    @Autowired
    protected UserService userService;

    /**
     * 关联角色至当前用户下
     *
     * @param userId
     * @param roleIdList
     * @return
     */
    @PostMapping("/relateRoles/{userId}")
    public Integer relateRole(@PathVariable("userId") String userId, @RequestBody List<String> roleIdList) {
        return userService.relateRoles(userId, roleIdList);
    }

    /**
     * 删除当前用户下的某个角色
     *
     * @param userId
     * @param roleId
     * @return
     */
    @PostMapping("/deleteRole/{userId}/{roleId}")
    public Integer deleteRole(@PathVariable("userId") String userId, @PathVariable("roleId") String roleId) {
        return userService.deleteRole(userId, roleId);
    }
    /**
     * 某角色下添加用户
     */
    @PostMapping("/relateUsers/{roleId}")
    public Integer relateUser(@PathVariable("roleId") String roleId, @RequestBody List<String> userIdList) {
        return userService.relateUsers(roleId, userIdList);
    }

}
