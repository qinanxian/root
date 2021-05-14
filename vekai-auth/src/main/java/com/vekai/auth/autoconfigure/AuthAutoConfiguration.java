package com.vekai.auth.autoconfigure;


import cn.fisok.raw.autoconfigure.RawProperties;
import com.jagregory.shiro.freemarker.ShiroTags;
import com.vekai.auth.authorization.DefaultAuthorizationSupport;
import com.vekai.auth.audit.AutoFieldProperties;
import com.vekai.auth.authorization.AuthorizationSupport;
import com.vekai.auth.authorization.GeneralAuthorizationFilter;
import com.vekai.auth.authorization.MenuAuthorizationSupport;
import com.vekai.auth.event.listener.AuthcCfgEventListener;
import com.vekai.auth.event.listener.AuthzCfgEventListener;
import com.vekai.auth.interceptor.AuthHolderInterceptor;
import com.vekai.auth.jwt.shiro.GeneralSessionIdGenerator;
import com.vekai.auth.model.LoginResponse;
import com.vekai.auth.service.AuthService;
import com.vekai.auth.service.impl.AuthServiceImpl;
import com.vekai.auth.shiro.*;
import com.vekai.auth.tags.AuthTags;
import com.vekai.base.autoconfigure.BaseAutoConfiguration;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AbstractAuthenticator;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.servlet.Cookie;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.util.*;

@Configuration
@EnableConfigurationProperties({AuthProperties.class, AutoFieldProperties.class, SessionProperties.class})
@MapperScan(basePackages = "com.vekai.auth.mapper")   //加上这句，指定mybatis包的扫描路径
@AutoConfigureBefore(name = {"org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration",
        "org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration",
        "org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration"})
@ComponentScan(basePackages = {
        "com.vekai.auth",
//        "com.vekai.auth.service",
//        "com.vekai.auth.controller",
//        "com.vekai.auth.audit",
//        "com.vekai.auth.interceptor",
//        "com.vekai.auth.jwt",
//        "com.vekai.auth.authorization.aop"
})
@AutoConfigureAfter(BaseAutoConfiguration.class)
public class AuthAutoConfiguration extends WebMvcConfigurerAdapter implements InitializingBean {

    protected AuthProperties authProperties;
    protected RawProperties rawProperties;

    @Autowired
    private freemarker.template.Configuration configuration;

    @Autowired
    ApplicationContext applicationContext;

    public AuthAutoConfiguration(AuthProperties properties, RawProperties rawProperties) {
        this.authProperties = properties;
        this.rawProperties = rawProperties;
    }

    @PostConstruct
    public void init() {
//        if (StringKit.isEmpty(rawProperties.getStaticResourceProxyUrl()))
        LoginResponse.logonRealUrl = authProperties.getLoginStaticPath();
//        else
//            LoginResponse.logonRealUrl = rawProperties.getStaticResourceProxyUrl() + authProperties.getLoginStaticPath();
        LoginResponse.setSUCCESS(new LoginResponse(LoginResponse.LoginResCode.Success, authProperties.getSuccessUrl(), "登陆成功"));
        LoginResponse.setFAIL(new LoginResponse(LoginResponse.LoginResCode.Fail, authProperties.getLoginStaticPath(), "登陆失败"));
        if(authProperties.getPwdResetPath() != null && authProperties.getPwdResetPath().length>0){
            LoginResponse.setPWDRESET(new LoginResponse(LoginResponse.LoginResCode.PasswordNeedReset, authProperties.getPwdResetPath()[0], "请重置密码"));
        }
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthHolderInterceptor()).addPathPatterns("/**");
    }

    public void afterPropertiesSet() throws Exception {
        // 加上这句后，可以在页面上使用shiro标签
        configuration.setSharedVariable(authProperties.getShiroTagName(), new ShiroTags());
        configuration.setSharedVariable(authProperties.getAuthTagName(), new AuthTags());
        configuration.setNumberFormat("#");
    }


    @Configuration
    //@Import({SpringRedisCacheConfiguration.class, CacheConfiguration.class})
    public static class ShiroConfiguration {

        @Autowired(required = false)
        Collection<AuthenticationListener> authenticationListeners;

        @Autowired
        private AuthProperties authProperties;

        @Autowired
        private SessionProperties sessionProperties;

        @Autowired
        private Environment environment;

        @Bean
        @ConditionalOnMissingBean
        public GeneralAuthorizationFilter authz() {
            return new GeneralAuthorizationFilter();
        }

        @Bean
        public FilterRegistrationBean authzFilterRegistrationBean(GeneralAuthorizationFilter filter) {
            FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
            filterRegistrationBean.setFilter(filter);
            filterRegistrationBean.addUrlPatterns("/Nonexistent");
            return filterRegistrationBean;
        }

        @Bean
        @ConditionalOnMissingBean
        public CredentialsMatcher credentialsMatcher() {
            if (authProperties.isEnableHashPwd()) {
                HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher(authProperties.getPwdHashAlgorithmName());
                credentialsMatcher.setHashIterations(authProperties.getHashIterations());
                credentialsMatcher.setStoredCredentialsHexEncoded(authProperties.isStoredCredentialsHexEncoded());
                return credentialsMatcher;
            } else
                return new SimpleCredentialsMatcher();
        }

        @Bean
        @ConditionalOnMissingBean
        public PermissionResolver permissionResolver() {
            return new CustomWildcardPermissionResolver();
        }

        @Bean
        @ConditionalOnMissingBean
        public RolePermissionResolver rolePermissionResolver() {
            return new CustomRolePermissionResolver();
        }

        @Bean
        @ConditionalOnMissingBean(value = DatabaseRealm.class)
        public Realm realm(CredentialsMatcher credentialsMatcher) {
            DatabaseRealm realm = new DatabaseRealm(); // permission resolver will be changed by ModularRealmAuthorizer
            realm.setAuthorizationCachingEnabled(authProperties.isAuthorizationCachingEnabled());
            realm.setAuthenticationCachingEnabled(authProperties.isAuthenticationCachingEnabled());
            realm.setCredentialsMatcher(credentialsMatcher);
            return realm;
        }

        @Bean
        @Primary
        public AuthorizationSupport authorizationSupport() {
            return new DefaultAuthorizationSupport();
        }

        @Bean
        public MenuAuthorizationSupport menuAuthorizationSupport() {
            return new MenuAuthorizationSupport();
        }


        @Bean
        public ShiroFilterChainDefinition shiroFilterChainDefinition() {
            CustomShiroFilterChainDefinition chainDefinition = new CustomShiroFilterChainDefinition();
            chainDefinition.addPathDefinition("/logout", "authc,logout");
            chainDefinition.addPathDefinition(environment.getProperty("shiro.loginUrl"), "authc");
            Map<String, List<String>> accessControl = authProperties.getAccessControlStrategy();
            if (null != accessControl && !accessControl.isEmpty()) {
                accessControl.forEach(
                        (definition, paths) ->
                                paths.forEach(path -> chainDefinition.addPathDefinition(path, definition)));
            }
            chainDefinition.addPathDefinition("/**", "authc,authz");

            return chainDefinition;
        }

        @Bean
        @ConditionalOnMissingBean
        public SessionIdGenerator sessionIdGenerator() {
            return new GeneralSessionIdGenerator();
        }


        @Bean
        @ConditionalOnMissingBean
        public SessionDAO sessionDAO(SessionIdGenerator sessionIdGenerator) {
            EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
            sessionDAO.setActiveSessionsCacheName(sessionProperties.getActiveSessionsCacheName());
            sessionDAO.setSessionIdGenerator(sessionIdGenerator);
            return sessionDAO;
        }

        @Bean
        @ConditionalOnMissingBean
        public Cookie sessionIdCookie() {
            // max-age defaults -1
            ExtendedCookie cookie = new ExtendedCookie(sessionProperties.getSessionIdCookieName());
            cookie.setCookieEnable(sessionProperties.isSessionIdCookieEnabled());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setDomain(sessionProperties.getSessionIdCookieDomain());
            return cookie;
        }


        @Bean
        @ConditionalOnMissingBean
        public SessionManager sessionManager(SessionDAO sessionDAO, Cookie sessionIdCookie,
                                             Collection<SessionListener> sessionListeners) {
            CustomWebSessionManager webSessionManager = new CustomWebSessionManager();
            webSessionManager.setSessionIdCookieEnabled(sessionProperties.isSessionIdCookieEnabled());
            webSessionManager.setGlobalSessionTimeout(sessionProperties.getGlobalSessionTimeout());
            webSessionManager.setDeleteInvalidSessions(sessionProperties.isDeleteInvalidSessions());
            webSessionManager.setSessionValidationSchedulerEnabled(
                    sessionProperties.isSessionValidationSchedulerEnabled() && !authProperties.isEnableJwtReplaceSession());
            webSessionManager.setSessionValidationInterval(sessionProperties.getSessionValidationInterval());
            webSessionManager.setSessionIdCookie(sessionIdCookie);
            webSessionManager.setSessionDAO(sessionDAO);
            webSessionManager.setSessionListeners(sessionListeners);

            return webSessionManager;
        }

        @Bean
        @ConditionalOnMissingBean
        public SessionsSecurityManager securityManager(SessionDAO sessionDAO,  Cookie sessionIdCookie,
                CacheManager cacheManager, Collection<AuthenticationListener> authenticationListeners,
                CredentialsMatcher credentialsMatcher, Collection<Realm> realms,
                 Optional<Collection<SessionListener>> sessionListeners) {
            CustomWebSecurityManager securityManager = new CustomWebSecurityManager(realms);
            SessionManager sessionManager = sessionManager(sessionDAO, sessionIdCookie, sessionListeners.orElse(Collections.emptyList()));
            securityManager.setSessionManager(sessionManager);
            securityManager.setCacheManager(cacheManager);
            securityManager.setSessionCreationEnabled(!authProperties.isEnableJwtReplaceSession());
            if (securityManager.getSubjectDAO() instanceof DefaultSubjectDAO) {
                DefaultSubjectDAO subjectDAO = (DefaultSubjectDAO) securityManager.getSubjectDAO();
                DefaultSessionStorageEvaluator sessionStorageEvaluator = (DefaultSessionStorageEvaluator)
                        subjectDAO.getSessionStorageEvaluator();
                sessionStorageEvaluator.setSessionStorageEnabled(!authProperties.isEnableJwtReplaceSession());
            }
            if (authProperties.isEnableJwtReplaceSession()) {
                CustomSubjectFactory customSubjectFactory = new CustomSubjectFactory(
                        sessionIdCookie, realms.iterator().next().getName(), jwtParser());
                securityManager.setSubjectFactory(customSubjectFactory);
                if (sessionManager instanceof CacheManagerAware) {
                    ((CacheManagerAware) sessionManager).setCacheManager(null);
                    ((CacheManagerAware) ((DefaultSessionManager) sessionManager).getSessionDAO()).setCacheManager(null);
                }
            }
            SecurityUtils.setSecurityManager(securityManager);
            Authenticator authenticator = securityManager.getAuthenticator();
            if (authenticator instanceof AbstractAuthenticator) {
                AbstractAuthenticator abstractAuthenticator = (AbstractAuthenticator) authenticator;
                abstractAuthenticator.setAuthenticationListeners(authenticationListeners);
            }
            ModularRealmAuthorizer authorizer = (ModularRealmAuthorizer) securityManager.getAuthorizer();
            authorizer.setPermissionResolver(permissionResolver());
            authorizer.setRolePermissionResolver(rolePermissionResolver());
            return securityManager;
        }

        @Bean
        public AuthService authService() {
            return new AuthServiceImpl();
        }

        @Bean
        @ConditionalOnMissingBean
        public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
            AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
            advisor.setSecurityManager(securityManager);
            return advisor;
        }

        @Bean
        @ConditionalOnProperty(prefix = "com.vekai.auth", name = "enableJwtReplaceSession", havingValue = "true")
        public JwtParser jwtParser() {
            DefaultJwtParser jwtParser = new DefaultJwtParser();
            jwtParser.requireIssuer(authProperties.getJwtIssuer())
                    .setSigningKey(DatatypeConverter.parseBase64Binary(authProperties.getJwtSecretKey()));
            return jwtParser;
        }

        @Bean
        @ConditionalOnProperty(prefix = "com.vekai.auth", name = "authorizationCachingEnabled", havingValue = "true")
        public AuthzCfgEventListener authzCfgEventListener(DatabaseRealm realm, CacheManager cacheManager, AuthService authService) {
            return new AuthzCfgEventListener(realm.getAuthorizationCacheName(), realm.getName(), cacheManager, authService);
        }

        @Bean
        @ConditionalOnProperty(prefix = "com.vekai.auth", name = "authenticationCachingEnabled", havingValue = "true")
        public AuthcCfgEventListener authcCfgEventListener(DatabaseRealm realm, CacheManager cacheManager) {
            return new AuthcCfgEventListener(realm.getAuthenticationCacheName(), cacheManager);
        }



        @Bean
        public AuthenticationListener loginReasonAttributeSettingAuthenticationListener() {
            return new LoginFailDefaultMsgSettingAuthenticationListener();
        }
    }
}
