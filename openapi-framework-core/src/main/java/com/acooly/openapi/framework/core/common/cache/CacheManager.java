/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.common.cache;

/**
 * 缓存接口
 *
 * @author qiubo
 * @author zhangpu
 * @date 2014年6月27日
 */
public interface CacheManager {

  void add(String key, Object value);

  /**
   * 添加缓存
   *
   * @param key
   * @param value
   * @param holdSecond 秒
   */
  void add(String key, Object value, int holdSecond);

  Object get(String key);

  void cleanup(String key);
}
