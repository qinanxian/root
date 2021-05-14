package com.vekai.base.adapter;


import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;


public class CacheManagerAdapter implements CacheManager{
    private final org.springframework.cache.CacheManager cacheManager;

    public CacheManagerAdapter(org.springframework.cache.CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        org.springframework.cache.Cache cache = cacheManager.getCache(name);
        return new CacheAdapter<>(cache);
    }
}
