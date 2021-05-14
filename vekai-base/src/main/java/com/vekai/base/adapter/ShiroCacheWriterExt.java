package com.vekai.base.adapter;


import org.springframework.data.redis.cache.RedisCacheWriter;

import java.util.Collection;
import java.util.Set;

public interface ShiroCacheWriterExt extends RedisCacheWriter {
    int size(String name, byte[] pattern);

    Set<byte[]> keys(String name, byte[] pattern);

    Collection<byte[]> values(String name, byte[] pattern);
}
