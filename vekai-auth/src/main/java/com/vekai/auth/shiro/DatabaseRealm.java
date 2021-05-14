package com.vekai.auth.shiro;


import com.vekai.auth.AuthConsts;
import com.vekai.auth.autoconfigure.AuthProperties;
import com.vekai.auth.entity.Permit;
import com.vekai.auth.entity.User;
import com.vekai.auth.service.AuthService;
import com.vekai.auth.entity.Role;
import cn.fisok.raw.kit.StringKit;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.SimpleByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

/**
 * 继承 AuthorizingRealm 实现认证和授权2个方法
 * Created by tisir<yangsong158@qq.com> on 2017-05-14
 */
public class DatabaseRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseRealm.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthProperties authProperties;

    @Autowired(required = false)
    private List<UserAccountVerifier> userAccountVerifiers = new ArrayList<>();

    public AuthService getAuthService() {
        return authService;
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = null;
        Subject subject = SecurityUtils.getSubject();
        if (null != subject.getPrincipals())
            user = subject.getPrincipals().oneByType(User.class);
        if (null == user) return null;
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<Role> roles = authService.getUserRoles(user.getId());
        Set<Permit> permits = authService.getUserAllPermission(user.getId());
        info.setRoles(roles.stream().map(role -> role.getCode()).collect(Collectors.toSet()));
        info.setStringPermissions(permits.stream().map(permit -> permit.getCode()).collect(Collectors.toSet()));
        return info;
    }

    @Override
    public AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {
        return super.getAuthorizationInfo(principals);
    }


    @Override
    public Collection<Permission> getPermissions(AuthorizationInfo info) {
        Set<Permission> permissions = new HashSet<Permission>();

        if (info != null) {
            Collection<Permission> perms = info.getObjectPermissions();
            if (null != perms && isAuthorizationCachingEnabled()) {
                permissions.addAll(perms);
                return permissions; //as an object collection cache
            }
            perms = resolvePermissions(info.getStringPermissions());
            if (!CollectionUtils.isEmpty(perms)) {
                permissions.addAll(perms);
            }
        }

        if (info instanceof SimpleAuthorizationInfo && isAuthorizationCachingEnabled())
            ((SimpleAuthorizationInfo) info).setObjectPermissions(permissions);
        if (permissions.isEmpty()) {
            return Collections.emptySet();
        } else {
            return Collections.unmodifiableSet(permissions);
        }
    }

    // just super class method, but as it is private, so copy here
    protected Collection<Permission> resolvePermissions(Collection<String> stringPerms) {
        Collection<Permission> perms = Collections.emptySet();
        PermissionResolver resolver = getPermissionResolver();
        if (resolver != null && !CollectionUtils.isEmpty(stringPerms)) {
            perms = new LinkedHashSet<>(stringPerms.size());
            for (String strPermission : stringPerms) {
                Permission permission = resolver.resolvePermission(strPermission);
                perms.add(permission);
            }
        }
        return perms;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken) throws AuthenticationException {
        //UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        //查出是否有此用户
        User user = authService.getUserByCode(token.getUsername());   //userName是shiro中的概念,等同于我们的userCode
        if (user == null) {
            setLoginFailMsg(AuthConsts.ACOUNT_PWD_INCORRECT_MSG);
            throw new UnknownAccountException("no account code: " + token.getUsername());//没找到帐号
        }
        if(!"1".equals(user.getStatus())){
            setLoginFailMsg("该账户无效");
            throw new UnknownAccountException("disable name: " + token.getUsername());//没找到帐号
        }
        if (!userAccountVerifiers.isEmpty()) {
            for (UserAccountVerifier userAccountVerifier : userAccountVerifiers) {
                try {
                    userAccountVerifier.verifyAccount(user);
                } catch (AuthenticationException ex) {
                    setLoginFailMsg(ex.getMessage());
                    throw ex;
                }
            }
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), getName());

        if (authProperties.isHashSalted() && StringKit.isNoneBlank(user.getHashSalt())) {
            authenticationInfo.setCredentialsSalt(new SimpleByteSource(Base64.getDecoder().decode(user.getHashSalt())));
        }
        return authenticationInfo;
    }

    @Override
    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException {
        CredentialsMatcher cm = getCredentialsMatcher();
        if (cm != null) {
            if (!cm.doCredentialsMatch(token, info)) {
                setLoginFailMsg(AuthConsts.ACOUNT_PWD_INCORRECT_MSG);
                throw new IncorrectCredentialsWithPrincipalException(info.getPrincipals(), AuthConsts.ACOUNT_PWD_INCORRECT_MSG);
            }
        }
        super.assertCredentialsMatch(token, info);
    }

    private void setLoginFailMsg(String msg) {
        RequestContextHolder.getRequestAttributes().setAttribute(AuthConsts.LOGIN_FAIL_MSG_ATTRIBUTE_NAME, msg, SCOPE_REQUEST);
    }

}
