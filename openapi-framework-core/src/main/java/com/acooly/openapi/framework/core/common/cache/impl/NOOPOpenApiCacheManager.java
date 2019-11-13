/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-08-12 16:31 创建
 *
 */
package com.acooly.openapi.framework.core.common.cache.impl;

import com.acooly.openapi.framework.core.common.cache.OpenApiCacheManager;

/** @author qiubo@qq.com */
public class NOOPOpenApiCacheManager implements OpenApiCacheManager {
  @Override
  public void add(String key, Object value) {}

  @Override
  public void add(String key, Object value, int holdSecond) {}

  @Override
  public Object get(String key) {
    return null;
  }

  @Override
  public void cleanup(String key) {}
}
