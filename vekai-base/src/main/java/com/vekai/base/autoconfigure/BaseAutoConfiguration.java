package com.vekai.base.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;


@Configuration
//@EnableAspectJAutoProxy(proxyTargetClass=true, exposeProxy=true)
@ComponentScan(basePackages = "com.vekai.base", excludeFilters = {@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {CacheConfiguration.class, SpringRedisCacheConfiguration.class})})
@EnableConfigurationProperties(BaseProperties.class)
@Import({SpringRedisCacheConfiguration.class, CacheConfiguration.class})
public class BaseAutoConfiguration {

    @Autowired
    protected BaseProperties properties;

//    @Bean(BaseConsts.DICT_CACHE)
//    public ConcurrentMapCacheFactoryBean getDictCache(){
//        ConcurrentMapCacheFactoryBean cache = new ConcurrentMapCacheFactoryBean();
//        cache.setName(BaseConsts.DICT_CACHE);
//        return cache;
//    }


}