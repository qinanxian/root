package com.vekai.base.adapter;


import net.sf.ehcache.Ehcache;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.ClassUtils;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class CacheAdapter<K, V> implements Cache<K, V> {

    protected final org.springframework.cache.Cache cache;

    private static final boolean EHCACHE_EXIST;
    private static final boolean REDIS_CACHE_EXIST;

    static {
        EHCACHE_EXIST = ClassUtils.isAvailable("net.sf.ehcache.Ehcache");
        REDIS_CACHE_EXIST = ClassUtils.isAvailable("org.springframework.data.redis.cache.RedisCache");
    }

    CacheAdapter(org.springframework.cache.Cache cache) {
        this.cache = cache;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public V get(K k) throws CacheException {
        return (V)getRealValue(cache.get(k));
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public V put(K k, V v) throws CacheException {
        Object preValue = cache.get(k);
        cache.put(k, v);
        return (V)getRealValue(preValue);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public V remove(K k) throws CacheException {
        Object preValue = cache.get(k);
        cache.evict(k);
        return (V)getRealValue(preValue);
    }

    private Object getRealValue(Object o) {
        if (null == o) return null;
        if (o instanceof org.springframework.cache.Cache.ValueWrapper)
            return ((org.springframework.cache.Cache.ValueWrapper)o).get();
        return o;
    }

    @Override
    public void clear() throws CacheException {
        cache.clear();
    }

    @Override
    public int size() {
        Object nativeCache = cache.getNativeCache();
        if (nativeCache instanceof ConcurrentMap) {
            ConcurrentMap concurrentMap = (ConcurrentMap)nativeCache;
            return concurrentMap.size();
        } else if (EHCACHE_EXIST && nativeCache instanceof Ehcache) {
            Ehcache ehcache = (Ehcache)nativeCache;
            return ehcache.getSize();
        } else if (REDIS_CACHE_EXIST && nativeCache instanceof SpringRedisCache){
            SpringRedisCache redisCache = (SpringRedisCache)nativeCache;
            return redisCache.size();
        } else {
            throw new CacheException(this.getClass().getCanonicalName() + " does not support " + nativeCache.getClass().getCanonicalName());
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Set<K> keys() {
        Object nativeCache = cache.getNativeCache();
        if (nativeCache instanceof ConcurrentMap) {
            ConcurrentMap concurrentMap = (ConcurrentMap)nativeCache;
            return concurrentMap.keySet();
        } else if (EHCACHE_EXIST && nativeCache instanceof Ehcache) {
            Ehcache ehcache = (Ehcache)nativeCache;
            return new HashSet<>(ehcache.getKeys());
        } else if (REDIS_CACHE_EXIST && nativeCache instanceof SpringRedisCache) {
            SpringRedisCache redisCache = (SpringRedisCache)nativeCache;
            return redisCache.keys();
        }else {
            throw new CacheException(this.getClass().getCanonicalName() + " does not support " + nativeCache.getClass().getCanonicalName());
        }
    }

    @Override
    public Collection<V> values() {
        Object nativeCache = cache.getNativeCache();
        if (nativeCache instanceof ConcurrentMap) {
            ConcurrentMap concurrentMap = (ConcurrentMap)nativeCache;
            return concurrentMap.values();
        } else if (EHCACHE_EXIST && nativeCache instanceof Ehcache) {
            Ehcache ehcache = (Ehcache)nativeCache;
            try {
                @SuppressWarnings({"unchecked"})
                List<K> keys = ehcache.getKeys();
                if (!keys.isEmpty()) {
                    List<V> values = new ArrayList<V>(keys.size());
                    for (K key : keys) {
                        V value = get(key);
                        if (value != null) {
                            values.add(value);
                        }
                    }
                    return Collections.unmodifiableList(values);
                } else {
                    return Collections.emptyList();
                }
            } catch (Throwable t) {
                throw new CacheException(t);
            }
        }else if (REDIS_CACHE_EXIST && cache instanceof SpringRedisCache) {
            SpringRedisCache redisCache = (SpringRedisCache)cache;
            return redisCache.values();
        } else {
            throw new CacheException(this.getClass().getCanonicalName() + " does not support " + nativeCache.getClass().getCanonicalName());
        }
    }
}
