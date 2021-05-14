package com.vekai.auth;

import com.vekai.auth.entity.Role;
import com.vekai.auth.entity.User;
import com.vekai.auth.service.AuthService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tisir<yangsong158@qq.com> on 2017-05-14
 */
public class AuthServiceTest extends BaseTest {
    @Autowired
    private AuthService authService;

    @Test
    public void userCruder(){

        //用户部分测试
        authService.deleteUser("2889");

        User user = new User();
        user.setId("2889");
        user.setName("田舍先生");
        user.setCode("tisir");

        authService.saveUser(user);
        user.setEmail("syang@amarsoft.com");
        authService.saveUser(user);

        Assert.assertNotNull(authService.getUserByCode("tisir"));

        //角色部分测试
        authService.deleteRole("9999");
        authService.deleteRole("8888");
        Role role = new Role();
        role.setId("9999");
        role.setCode("root");
        role.setName("系统管理员");

        authService.saveRole(role);
        role.setSummary("角色更新测试");
        authService.saveRole(role);

        Assert.assertNotNull(authService.getRoleByCode("root"));

        Role role1 = new Role();
        role1.setId("8888");
        role1.setCode("configer");
        role1.setName("业务配置员");
        authService.saveRole(role1);

        //授权测试
        authService.grantRoleToUser("9999","2889");
        authService.grantRoleToUser("8888","2889");

        Assert.assertEquals(2,authService.getUserRoles("2889").size());

        Assert.assertTrue(authService.userHasRole("2889","9999"));
        Assert.assertTrue(authService.userHasRole("2889","8888"));
        Assert.assertFalse(authService.userHasRole("2889","0"));
        Assert.assertTrue(authService.userHasRoleByCode("tisir","root"));
        Assert.assertTrue(authService.userHasRoleByCode("tisir","configer"));
        Assert.assertFalse(authService.userHasRoleByCode("tisir","configer1"));


    }
}
