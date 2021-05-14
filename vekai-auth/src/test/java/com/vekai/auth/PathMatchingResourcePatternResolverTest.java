package com.vekai.auth;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

public class PathMatchingResourcePatternResolverTest {

    @Test
    public void test01() throws IOException {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource[] mappers = resourceResolver.getResources("classpath*:com/vekai/**/mapper/*Mapper.xml");
        System.out.println(mappers);
        System.out.println(mappers.length);
    }
}
