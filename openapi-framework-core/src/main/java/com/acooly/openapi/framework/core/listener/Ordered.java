/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-31 03:31 创建
 *
 */
package com.acooly.openapi.framework.core.listener;

/**
 * 执行顺序
 *
 * @author qiubo@qq.com
 */
public interface Ordered {

  int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

  int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

  /**
   * 数字越小越先被执行
   *
   * @return
   */
  int getOrder();
}
