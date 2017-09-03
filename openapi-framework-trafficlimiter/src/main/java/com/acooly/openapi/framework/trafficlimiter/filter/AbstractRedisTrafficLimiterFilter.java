/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-12-18 19:55 创建
 */
package com.acooly.openapi.framework.trafficlimiter.filter;

import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.trafficlimiter.TrafficLimiterFilter;
import com.acooly.openapi.framework.trafficlimiter.exception.TrafficLimiterException;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis流控抽象实现
 *
 * @author acooly
 */
public abstract class AbstractRedisTrafficLimiterFilter implements TrafficLimiterFilter {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRedisTrafficLimiterFilter.class);

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;


    @Override
    public void evaluate(ApiRequest request) {
        String key = getKey(request) + System.currentTimeMillis() / 1000;
        logger.debug("key:{}", key);
        String current = redisTemplate.opsForValue().get(key);
        if (current != null && Long.valueOf(current) >= getMaxCounterPerSecond()) {
            throw new TrafficLimiterException();
        } else {
            final byte[] rawKey = key.getBytes();
            redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.multi();
                    connection.incr(rawKey);
                    connection.expire(rawKey, 1);
                    connection.exec();
                    return null;
                }
            });
        }
    }

    /**
     * 缓存的key
     *
     * @param request
     * @return
     */
    protected abstract String getKey(ApiRequest request);

    /**
     * 每秒最大允许数量
     *
     * @return
     */
    protected abstract int getMaxCounterPerSecond();


    @Override
    public boolean enable() {
        return getMaxCounterPerSecond() > 0;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("type", getTrafficLimiterType())
                .add("enable", enable())
                .add("max",getMaxCounterPerSecond())
                .toString();
    }
}
