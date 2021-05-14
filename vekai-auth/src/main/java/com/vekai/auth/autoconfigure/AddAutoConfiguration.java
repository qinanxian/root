package com.vekai.auth.autoconfigure;

import com.vekai.auth.model.LoginResponse;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;

@Configuration
@AutoConfigureAfter(name = "org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration")
public class AddAutoConfiguration {

    @Autowired
    ApplicationContext applicationContext;

    public AddAutoConfiguration() {

    }

    @PostConstruct
    public void init() {
        AbstractShiroFilter shiroFilter = applicationContext.getBean(AbstractShiroFilter.class);
        if (null != shiroFilter) changeLogoutFilterRedirectUrl(shiroFilter);
    }

    private void changeLogoutFilterRedirectUrl(AbstractShiroFilter filter) {
        FilterChainResolver chainResolver = filter.getFilterChainResolver();
        if (chainResolver instanceof PathMatchingFilterChainResolver) {
            PathMatchingFilterChainResolver resolver = (PathMatchingFilterChainResolver)chainResolver;
            Filter logoutFilter = resolver.getFilterChainManager().getFilters().get(DefaultFilter.logout.name());
            if (logoutFilter instanceof LogoutFilter) {
                ((LogoutFilter) logoutFilter).setRedirectUrl(LoginResponse.logonRealUrl);
            }
        }
    }


}
