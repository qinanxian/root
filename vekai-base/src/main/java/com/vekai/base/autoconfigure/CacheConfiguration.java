package com.vekai.base.autoconfigure;


import com.vekai.base.adapter.CacheManagerAdapter;
import org.apache.shiro.cache.CacheManager;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;



@Configuration
@AutoConfigureAfter(SpringRedisCacheConfiguration.class)
public class CacheConfiguration {

    //    @Bean
//    @Profile("dev")
//    @ConditionalOnMissingBean
//    public CacheManager devCacheManager(org.springframework.cache.CacheManager cacheManager) {
//        //return new MemoryConstrainedCacheManager();
//        return new CacheManagerAdapter(cacheManager);
//    }
//
//    @Bean
//    @Profile("prod")
//    @ConditionalOnClass(name = "net.sf.ehcache.Ehcache")
//    @ConditionalOnMissingBean
//    @Order(Ordered.LOWEST_PRECEDENCE)
//    public CacheManager prodCacheManager() {
//        return new EhCacheManager();
//    }
//

    @Bean("cacheManager")
    @Profile("prod")
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "net.sf.ehcache.CacheManager")
    @ConditionalOnProperty(prefix = "com.vekai.base", name = "cache", havingValue = "EHCache")
    public org.springframework.cache.CacheManager prodEhcheManager() {
        return new EhCacheCacheManager();
    }


    @Bean("cacheManager")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "com.vekai.base", name = "cache", havingValue = "Memory", matchIfMissing = true)
    public org.springframework.cache.CacheManager devCacheManager() {
        return new ConcurrentMapCacheManager();
    }


    @Bean
    public CacheManager shiroCacheManager(org.springframework.cache.CacheManager cacheManager) {
        return new CacheManagerAdapter(cacheManager);
    }

}
