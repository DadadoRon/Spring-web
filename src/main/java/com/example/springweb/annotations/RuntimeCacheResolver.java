package com.example.springweb.annotations;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;

import java.util.Collection;
import java.util.List;

public class RuntimeCacheResolver extends SimpleCacheResolver {
    protected RuntimeCacheResolver(CacheManager cacheManager) {
        super(cacheManager);
    }

    @Override
    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
        CacheConfig cacheConfig = context.getTarget().getClass().getAnnotation(CacheConfig.class);
        String[] cacheNames = cacheConfig.cacheNames();
        return List.of(cacheNames);
    }
}
