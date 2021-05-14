package com.vekai.auth.service.impl;

import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.core.BeanQuery;
import com.vekai.auth.autoconfigure.AuthProperties;
import com.vekai.auth.entity.*;
import com.vekai.auth.event.AuthcCfgModifiedEvent;
import com.vekai.auth.mapper.*;
import com.vekai.auth.service.AuthService;
import com.vekai.auth.shiro.DatabaseRealm;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tisir<yangsong158@qq.com> on 2017-05-13
 */
public class AuthServiceImpl implements AuthService {

    public static final String CACHE_KEY = "UserCache";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Autowired
    private PermitMapper permitMapper;
    @Autowired
    private BeanQuery dataQuery;

    @Autowired
    OrgMapper orgMapper;

    @Autowired
    RolePermitMapper rolePermitMapper;

    @Autowired
    BeanCruder beanCruder;

    @Autowired
    AuthProperties authProperties;

    @Autowired
    AuthService self;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    private static class SecureRandomNumberGeneratorContainer {
        public static final SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();
    }

    private SecureRandomNumberGenerator getSecureRandomNumberGenerator() {
        return SecureRandomNumberGeneratorContainer.secureRandomNumberGenerator;
    }

    public Org getOrg(String orgId) {
        return orgMapper.selectOne(orgId);
    }

    public String getUserOrgName(String userId) {
        User user = getUser(userId);
        Org org = getOrg(user.getOrgId());
        return org.getName();
    }

    public String getUserNameById(String userId) {
        if (StringKit.isBlank(userId)) {
            return null;
        }
        User user = getUser(userId);
        if (null == user) {
            return userId;
        }
        return user.getName();
    }

    public List<User> selectUserList(String where, Map<String, Object> vars) {
        StringBuilder sql = new StringBuilder("select * from AUTH_USER");
        if (StringKit.isNotBlank(where)) {
            sql.append(" where ").append(where);
        }
        sql.append(" order by ID asc");
        return dataQuery.selectList(User.class, sql.toString(), vars);
    }

    @Cacheable(value = CACHE_KEY, key = "#userId")
    public User getUser(String userId) {
        User user = userMapper.selectById(userId);
        return user;
    }

    public User getUserByCode(String userCode) {
        return userMapper.selectByCode(userCode);
    }

    @CacheEvict(value = CACHE_KEY, key = "#{user.id}")
    public int saveUser(User user) {
        boolean insertModel = true;
        //有id,则更新
        if (user.getId() != null) {
            User exists = getUser(user.getId());
            if (exists != null) {//存在,则更新
                insertModel = false;
            }
        }

        if (insertModel) {
            userMapper.insert(user);
        } else {
            userMapper.update(user);

        }
        return 1;
    }

    public int deleteUser(String userId) {
        userMapper.deleteById(userId);
        return 1;
    }

    public Map<String, String> getUserProperties(String userId) {
        return null;
    }

    public int addUserProperty(String userId, String propertyName, String propertyValue) {
        return 0;
    }

    public int removeUserProperty(String userId, String propertyName) {
        return 0;
    }

    public Role getRole(String roleId) {
        return roleMapper.selectById(roleId);
    }

    public Role getRoleByCode(String roleCode) {
        return roleMapper.selectByCode(roleCode);
    }

    public int saveRole(Role role) {
        boolean insertModel = true;
        //有id,则更新
        if (role.getId() != null) {
            Role exists = roleMapper.selectById(role.getId());
            if (exists != null) {//存在,则更新
                insertModel = false;
            }
        }

        if (insertModel) {
            roleMapper.insert(role);
        } else {
            roleMapper.update(role);
        }
        return 1;
    }

    public int deleteRole(String roleId) {
        roleMapper.deleteById(roleId);
        return 1;
    }

    public Set<Role> getUserRoles(String userId) {
        return roleMapper.selectRolesByUser(userId);
    }

    public int grantRoleToUser(String roleId, String userId) {
        userRoleMapper.delete(userId, roleId);   //先删除可能存在的关联

        Map<String, Object> userRole = MapKit.newHashMap();
        userRole.put("id", userRoleMapper.selectMaxId() + 1);
        userRole.put("userId", userId);
        userRole.put("roleId", roleId);
        userRoleMapper.insert(userRole);

        return 1;
    }

    public int revokeRoleFormUser(String roleId, String userId) {
        userRoleMapper.delete(userId, roleId);
        return 1;
    }

    public boolean userHasRole(String userId, String roleId) {
        return userRoleMapper.selectCountByUserIdAndRoleId(userId, roleId) > 0;
    }

    public boolean userHasRoleByCode(String userCode, String roleCode) {
        return userRoleMapper.selectCountByUserCodeAndRoleCode(userCode, roleCode) > 0;
    }

    @Override
    public Set<Permit> getUserAllPermission(String userId) {
        return permitMapper.selectAllPermissionsByUserId(userId);
    }

    @Override
    public Set<Permit> getRolePermission(String roleId) {
        return permitMapper.selectPermissionsByRoleId(roleId);
    }

    @Override
    public Set<Permit> getRolePermissionByRoleCode(String roleCode) {
        return permitMapper.selectPermissionsByRoleCode(roleCode);
    }

    @Override
    @Transactional
    public void deleteRoleOwnedPermit(String roleId) {
        rolePermitMapper.deleteRoleOwnedPermit(roleId);
    }

    @Override
    public void addRoleOwnedPermit(List<RolePermit> rolePermits) {
        if (null == rolePermits || rolePermits.isEmpty())
            return;
        if (rolePermits.size() == 1)
            beanCruder.save(rolePermits.iterator().next());
        else
            beanCruder.save(rolePermits);
    }

    @Override
    public List<Permit> getAllUserDefinedPermits() {
        return permitMapper.selectAllUserDefinedPermits();
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_KEY, key = "#userId")
    public void updatePassword(String userId, String oldPwd, String newPwd, boolean forcePwdConfirm) {
        User user = getUser(userId);
        updatePassword(user, oldPwd, newPwd, forcePwdConfirm);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_KEY, key = "#user.id")
    public void updatePassword(User user, String oldPwd, String newPwd, boolean forcePwdConfirm) {
        if (StringKit.isBlank(oldPwd) && forcePwdConfirm)
            throw new IllegalArgumentException("oldPwd argument must not be empty if force password confirmation");
        if (forcePwdConfirm) {
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(),
                    ApplicationContextHolder.getBean(DatabaseRealm.class).getName());
            if (authProperties.isHashSalted()) {
                authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(user.getHashSalt()));
            }
            boolean pwdConfirm = ApplicationContextHolder.getBean(CredentialsMatcher.class).doCredentialsMatch(
                    new UsernamePasswordToken(user.getCode(), oldPwd), authenticationInfo);
            if (!pwdConfirm)
                throw new BizException("原始密码不正确");
        }

        if (!authProperties.isEnableHashPwd()) {
            userMapper.updatePassword(user.getId(), newPwd);
        } else {
            if (!authProperties.isHashSalted()) {
                SimpleHash simpleHash = new SimpleHash(authProperties.getPwdHashAlgorithmName(), newPwd,
                        null, authProperties.getHashIterations());
                userMapper.updatePassword(user.getId(), simpleHash.toBase64());
            } else {
                SecureRandomNumberGenerator secureRandomNumberGenerator = getSecureRandomNumberGenerator();
                ByteSource byteSource = secureRandomNumberGenerator.nextBytes();
                SimpleHash simpleHash = new SimpleHash(authProperties.getPwdHashAlgorithmName(), newPwd, byteSource,
                        authProperties.getHashIterations());
                userMapper.updateSaltedPwd(user.getId(), simpleHash.toBase64(), byteSource.toBase64());
            }
        }
        applicationEventPublisher.publishEvent(new AuthcCfgModifiedEvent.UserPwdModifiedEvent(user.getCode(), user.getId()));
    }
}
