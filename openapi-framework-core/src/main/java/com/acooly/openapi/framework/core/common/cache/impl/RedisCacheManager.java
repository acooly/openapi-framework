package com.acooly.openapi.framework.core.common.cache.impl;

import com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm;
import com.acooly.openapi.framework.core.common.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author qiuboboy@qq.com
 * @date 2018-01-25 14:45
 */
public class RedisCacheManager implements CacheManager {
  private int timeout;

  @Autowired private RedisTemplate<String, Object> redisTemplate;

  public RedisCacheManager(int timeout) {
    this.timeout = timeout;
  }

  @Override
  public void add(String key, Object value) {
    add(key, value, timeout);
  }

  @Override
  public void add(String key, Object value, int holdSecond) {
    redisTemplate.opsForValue().set(key, value, holdSecond, TimeUnit.SECONDS);
  }

  @Override
  public Object get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  @Override
  public void cleanup() {
    redisTemplate.delete(redisTemplate.keys(AuthInfoRealm.AUTHC_CACHE_KEY_PREFIX + "*"));
    redisTemplate.delete(redisTemplate.keys(AuthInfoRealm.AUTHZ_CACHE_KEY_PREFIX + "*"));
  }

  @Override
  public void cleanup(String key) {
    redisTemplate.delete(key);
  }
}
