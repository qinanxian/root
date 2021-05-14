package com.vekai.auth.autoconfigure;


import com.vekai.auth.shiro.CustomFormAuthenticationFilter;
import com.vekai.auth.shiro.CustomLogoutFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CustomAppContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        try {
            DefaultFilter authcFilter = DefaultFilter.authc;
            DefaultFilter logoutFilter = DefaultFilter.logout;
            Field field = DefaultFilter.class.getDeclaredField("filterClass");
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(authcFilter, getAuthcClass());
            field.set(logoutFilter, CustomLogoutFilter.class);
        } catch (Exception ex) {
           throw new RuntimeException("AppContextInitializer init fails", ex);
        }
    }

    protected Class<? extends FormAuthenticationFilter> getAuthcClass() {
        return CustomFormAuthenticationFilter.class;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
