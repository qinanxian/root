package com.vekai.auth.service;

import com.vekai.auth.autoconfigure.AuthProperties;
import com.vekai.auth.entity.UserRole;
import com.vekai.auth.event.AuthzCfgModifiedEvent;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    AuthService authService;

    @Autowired
    protected BeanCruder beanCruder;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    AuthProperties authProperties;

    /**
     * 关联角色至当前用户下
     *
     * @param userId
     * @param roleIdList
     * @return
     */
    @Transactional
    public Integer relateRoles(String userId, List<String> roleIdList) {
        Integer result = 0;
        if (null == roleIdList || roleIdList.isEmpty() || StringKit.isBlank(userId)) {
            return result;
        }

        for (String roleId : roleIdList) {
            UserRole userRole = beanCruder.selectOne(UserRole.class, "SELECT * FROM AUTH_USER_ROLE WHERE USER_ID=:userId and ROLE_ID=:roleId",
                    MapKit.mapOf("userId", userId, "roleId", roleId));
            if (userRole == null) {
                userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                result = result + beanCruder.save(userRole);
            }
        }
        applicationEventPublisher.publishEvent(new AuthzCfgModifiedEvent.UserAttachRoleEvent(userId, roleIdList));
        return result;
    }

    /**
     * 删除当前用户下的某个角色
     *
     * @param userId
     * @param roleId
     * @return
     */
    @Transactional
    public Integer deleteRole(String userId, String roleId) {
        Integer result = 0;
        if (StringKit.isBlank(userId) || StringKit.isBlank(roleId)) {
            return result;
        }

        result = beanCruder.execute("DELETE FROM AUTH_USER_ROLE WHERE USER_ID=:userId and ROLE_ID=:roleId",
                MapKit.mapOf("userId", userId, "roleId", roleId));

        applicationEventPublisher.publishEvent(new AuthzCfgModifiedEvent.UserRelieveRoleEvent(userId, roleId));
        return result;
    }

    /**
     * 关联用户至当前角色下
     */
    @Transactional
    public Integer relateUsers(String roleId, List<String> userIdList) {
        Integer result = 0;
        if (null == userIdList || userIdList.isEmpty() || StringKit.isBlank(roleId)) {
            return result;
        }

        for (String userId : userIdList) {
            UserRole userRole = beanCruder.selectOne(UserRole.class, "SELECT * FROM AUTH_USER_ROLE WHERE USER_ID=:userId and ROLE_ID=:roleId",
                    MapKit.mapOf("userId", userId, "roleId", roleId));
            if (userRole == null) {
                userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                result = result + beanCruder.save(userRole);
            }
        }
        return result;
    }

    private int updateUserStatus(String userId,String status){
        String sql = "UPDATE AUTH_USER SET STATUS=:status WHERE ID=:userId";
        return beanCruder.execute(sql,MapKit.mapOf("userId",userId,"status",status));
    }

    public int enableUser(String userId){
        return updateUserStatus(userId,"1");
    }
    public int disableUser(String userId){
        return updateUserStatus(userId,"0");
    }
    public int initUserPassword(String userId){
//        String userInitPassword = authProperties.getUserInitPassword();
        String userInitPassword = "urcb"+userId;
        authService.updatePassword(userId, null, userInitPassword, false);
        return 1;
    }

}
