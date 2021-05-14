package com.vekai.base.adapter;


import org.springframework.core.convert.ConversionService;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class SpringRedisCache extends RedisCache implements ShiroCacheParitalInterface {

    private final RedisCacheWriterExt redisCacheWriterExt;
    public SpringRedisCache(String name, RedisCacheWriterExt cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
        this.redisCacheWriterExt = cacheWriter;
    }

    @Override
    public RedisCacheWriterExt getNativeCache() {
        return this.redisCacheWriterExt;
    }


    @Override
    public int size() {
        byte[] pattern = getConversionService().convert(createCacheKey("*"), byte[].class);
        return this.redisCacheWriterExt.size(getName(), pattern);
    }

    @Override
    public Set keys() {
        byte[] pattern = getConversionService().convert(createCacheKey("*"), byte[].class);
        return this.redisCacheWriterExt.keys(getName(), pattern)
                .stream()
                .map(bytes -> deserializeCacheKey(bytes))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection values() {
        byte[] pattern = getConversionService().convert(createCacheKey("*"), byte[].class);
        return this.redisCacheWriterExt.values(getName(), pattern)
                .stream()
                .map(bytes -> fromStoreValue(deserializeCacheValue(bytes)))
                .collect(Collectors.toList());
    }

    private String deserializeCacheKey(byte[] bytes) {
        return getCacheConfiguration().getKeySerializationPair().read(ByteBuffer.wrap(bytes));
    }

    private ConversionService getConversionService() {
        return getCacheConfiguration().getConversionService();
    }
}
