/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-02-11 17:37
 */
package com.acooly.openapi.framework.core.common.cache.impl;

import com.acooly.openapi.framework.common.cache.OpenApiCacheManager;
import com.acooly.openapi.framework.core.OpenAPIProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 多级缓存实现
 * <p>
 * 本地缓存 + redis
 *
 * @author zhangpu
 * @date 2020-02-11 17:37
 */
@Slf4j
public class MultiLevelOpenApiCacheManager implements OpenApiCacheManager, InitializingBean {

    private static final String NAME_SPACE = "openapi.cache.";

    @Autowired
    private OpenAPIProperties openAPIProperties;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    private Cache<String, Object> localCache = null;

    @Override
    public void add(String key, Object value, int holdSecond) {
        try {
            String namedKey = getKey(key);
            // 设置一级缓存：本地缓存
            putLocalCache(namedKey, value);
            // 设置二级缓存：Redis缓存
            if (redisTemplate == null) {
                return;
            }
            redisTemplate.opsForValue().set(namedKey, value, getMillisecondTimeout(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            //ig
            log.warn("OpenApi缓存 写入 失败。key: {}", key, e);
        }
    }

    @Override
    public void add(String key, Object value) {
        add(key, value, getMillisecondTimeout());
    }

    @Override
    public Object get(String key) {
        try {
            String namedKey = getKey(key);
            // 获取一级缓存
            Object value = getLocalCache(namedKey);
            if (value != null) {
                return value;
            }
            // 获取二级缓存
            if (redisTemplate == null) {
                return value;
            }
            value = redisTemplate.opsForValue().get(namedKey);
            if (value != null) {
                return value;
            }
            log.debug("OpenApi缓存 获取 不存在 key: {}", key);
        } catch (Exception e) {
            log.warn("OpenApi缓存 获取 失败。key: {}", key, e);
            //ig
        }
        return null;
    }

    @Override
    public void cleanup(String key) {
        String namedKey = getKey(key);
        invalidateLocalCache(namedKey);
        redisTemplate.delete(namedKey);
    }


    protected void putLocalCache(String key, Object val) {
        if (!openAPIProperties.getAuthInfoCache().isLevelOneEnable()) {
            return;
        }
        localCache.put(key, val);
    }

    protected Object getLocalCache(String key) {
        if (!openAPIProperties.getAuthInfoCache().isLevelOneEnable()) {
            return null;
        }
        return localCache.getIfPresent(key);
    }

    protected void invalidateLocalCache(String key) {
        if (!openAPIProperties.getAuthInfoCache().isLevelOneEnable()) {
            return;
        }
        localCache.invalidate(key);
    }


    private int getMillisecondTimeout() {
        return openAPIProperties.getAuthInfoCache().getDefaultTimeout();
    }

    private String getKey(String key) {
        return NAME_SPACE + key;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!openAPIProperties.getAuthInfoCache().isLevelOneEnable()) {
            return;
        }
        localCache = Caffeine.newBuilder().expireAfter(new Expiry<String, Object>() {
            @Override
            public long expireAfterCreate(
                    String key, Object value, long currentTime) {
                return TimeUnit.MILLISECONDS.toNanos(openAPIProperties.getAuthInfoCache().getLevelOneTimeout());
            }

            @Override
            public long expireAfterUpdate(
                    String key, Object value, long currentTime, long currentDuration) {
                return currentDuration;
            }

            @Override
            public long expireAfterRead(
                    String key, Object value, long currentTime, long currentDuration) {
                return currentDuration;
            }
        }).build();
    }
}
