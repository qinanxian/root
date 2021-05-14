package com.vekai.auth;

import com.vekai.auth.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ShiroTest extends BaseTest {
    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void helloword(){
        Subject subject = SecurityUtils.getSubject();
        Assert.assertNotNull(subject);
        User user = new User();
        user.setCode("tisir");
        user.setPassword("000000");
        UsernamePasswordToken token = new UsernamePasswordToken(user.getCode(),user.getPassword());

        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            //5、身份验证失败
        }

        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录

        //6、退出
        subject.logout();
    }

    @Test
    public void helloword0(){
        Subject subject = SecurityUtils.getSubject();
        Assert.assertNotNull(subject);
        UsernamePasswordToken token = new UsernamePasswordToken("cs11","0000001");

        thrown.expect(UnknownAccountException.class);//密码错误
        subject.login(token);

    }

    @Test
    public void helloword1(){
        Subject subject = SecurityUtils.getSubject();
        Assert.assertNotNull(subject);
        UsernamePasswordToken token = new UsernamePasswordToken("syang","0000001");

        thrown.expect(IncorrectCredentialsException.class);//密码错误
        subject.login(token);

    }

    @Test
    public void helloword2(){
        Subject subject = SecurityUtils.getSubject();
        Assert.assertNotNull(subject);
        UsernamePasswordToken token = new UsernamePasswordToken("syang","Ro000000");

        subject.login(token);

    }

    @Test
    public void testRandom() {
        SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();
        ByteSource byteSource = secureRandomNumberGenerator.nextBytes();
    }

    @After
    public void tearDown() throws Exception {
        ThreadContext.unbindSubject();//退出时请解除绑定Subject到线程 否则对下次测试造成影响
    }
}
