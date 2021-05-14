/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.fisok.raw.cache;

import cn.fisok.raw.cache.dialect.EhCacheCacheManager;
import cn.fisok.raw.cache.dialect.MemoryCacheManager;
import cn.fisok.raw.cache.dialect.RedisCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/7/31
 * @desc : 扩展spring-cache的配置项
 */
public class CacheExtendConfiguration extends CachingConfigurerSupport {

    @Bean
    @ConditionalOnProperty(prefix = "cn.fisok.raw.cache", name = "dialect", havingValue = "Memory")
    public CacheManager getMemoryCacheManager(){
        MemoryCacheManager cacheManager = new MemoryCacheManager();
        return cacheManager;
    }
    @Bean
    @ConditionalOnProperty(prefix = "cn.fisok.raw.cache", name = "dialect", havingValue = "Redis")
    public CacheManager getRedisCacheManager(){
        RedisCacheManager cacheManager = new RedisCacheManager();
        return cacheManager;
    }
    @Bean
    @ConditionalOnProperty(prefix = "cn.fisok.raw.cache", name = "dialect", havingValue = "EHCache")
    public CacheManager getEhCacheManager(){
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
        return cacheManager;
    }
}
