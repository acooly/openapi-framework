/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-08-02 00:21 创建
 *
 */
package com.acooly.openapi.framework.common.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 服务监听器注解
 *
 * @author qiubo@qq.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface OpenApiListener {

  /**
   * 是否是全局监听器,默认false
   *
   * @return
   */
  boolean global() default false;

  /**
   * 是否异步,默认false
   *
   * @return
   */
  boolean asyn() default false;
}
