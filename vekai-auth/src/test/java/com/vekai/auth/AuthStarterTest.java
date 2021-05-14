package com.vekai.auth;//package com.vekai.auth;
//
//import com.vekai.auth.service.AuthService;
//import com.vekai.common.CommonConsts;
//import com.vekai.common.fileman.service.FileManageService;
//import org.apache.shiro.session.mgt.SessionManager;
//import org.apache.shiro.spring.LifecycleBeanPostProcessor;
//import org.apache.shiro.web.servlet.AbstractShiroFilter;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.cache.CacheManager;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//public class AuthStarterTest extends BaseTest {
//    @Autowired
//    protected JdbcTemplate jdbcTemplate;
//    @Autowired
//    protected CacheManager cacheManager;
//
//    @Autowired
//    @Qualifier(CommonConsts.IMG_FILE_SERVICE_NAME)
//    protected FileManageService imgFileManageService;
//
//    @Autowired
//    protected AuthService authService;
//
//    @Autowired
//    protected SessionManager sessionManager;
//
//    @Autowired
//    protected LifecycleBeanPostProcessor lifecycleBeanPostProcessor;
//    @Autowired
//    protected AbstractShiroFilter shiroFilter;
//
//    @Test
//    public void runtimeTest(){
//        Assert.assertNotNull(jdbcTemplate);
//    }
//
//
//    @Test
//    public void testBase(){
//        Assert.assertNotNull(cacheManager);
//    }
//
//    @Test
//    public void testCommon(){
//        Assert.assertNotNull(imgFileManageService);
//    }
//
//    @Test
//    public void testAuth(){
//        Assert.assertNotNull(authService);
//    }
//
//    @Test
//    public void testShiro(){
//        Assert.assertNotNull(sessionManager);
//        Assert.assertNotNull(lifecycleBeanPostProcessor);
//        Assert.assertNotNull(shiroFilter);
//    }
//}
