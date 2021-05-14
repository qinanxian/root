package com.vekai.batch.service.lock.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class BatchLauncherLockStoreByCache {
    public static final String CACHE_KEY = "BatchLockCache";

    @Cacheable(value=CACHE_KEY,key="#lockId")
    public String getLockValue(String lockId,String value){
        return value;
    }

    @CachePut(value=CACHE_KEY,key="#lockId")
    public String setLockValue(String lockId,String value){
        return value;
    }

    /**
     * 清空缓存
     */
    @CacheEvict(value=CACHE_KEY,key="#lockId",beforeInvocation=true)
    public void clearLockValue(String lockId){

    }
}
