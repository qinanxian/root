package com.vekai.auth.service;


import com.vekai.auth.entity.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tisir<yangsong158@qq.com> on 2017-05-13
 */
public interface AuthService {

    Org getOrg(String orgId);
    String getUserOrgName(String userId);
    String getUserNameById(String userId);

    public List<User> selectUserList(String where, Map<String, Object> vars);
    public User getUser(String id);
    public User getUserByCode(String userCode);
    public int saveUser(User user);
    public int deleteUser(String userId);
    public Map<String,String> getUserProperties(String userId);
    public int addUserProperty(String userId, String propertyName, String propertyValue);
    public int removeUserProperty(String userId, String propertyName);

    void updatePassword(String userId, String oldPwd, String newPwd, boolean forcePwdConfirm);
    void updatePassword(User user, String oldPwd, String newPwd, boolean forcePwdConfirm);


    public Role getRole(String roleId);
    public Role getRoleByCode(String roleCode);
    public int saveRole(Role role);
    public int deleteRole(String roleId);
    public Set<Role> getUserRoles(String userId);
    public int grantRoleToUser(String roleId, String userId);
    public int revokeRoleFormUser(String roleId, String userId);
    public boolean userHasRoleByCode(String userCode, String roleCode);
    public boolean userHasRole(String userId, String roleId);

    /**
     * get user all permissions not only owned directly by user but also owned roles'
     * @param userId
     * @return
     */
    Set<Permit> getUserAllPermission(String userId);
    Set<Permit> getRolePermission(String roleId);
    Set<Permit> getRolePermissionByRoleCode(String roleCode);

    void deleteRoleOwnedPermit(String roleId);
    void addRoleOwnedPermit(List<RolePermit> rolePermits);

    List<Permit> getAllUserDefinedPermits();

}
