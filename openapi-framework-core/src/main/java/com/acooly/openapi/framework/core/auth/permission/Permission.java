/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-27 18:11 创建
 *
 */
package com.acooly.openapi.framework.core.auth.permission;

/** @author qiubo@qq.com */
public interface Permission {
  /**
   * 判断资源是否有权限.
   *
   * @param resource
   * @return
   */
  boolean implies(String resource);
}
