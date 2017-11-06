/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-28 23:01 创建
 *
 */
package com.acooly.openapi.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 标识此字段为openapi的字段别名
 *
 * <p>主要用于兼容老接口的字段定义
 *
 * @author zhangpu@aliyun.com
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenApiAlias {

  /**
   * 别名
   *
   * @return
   */
  String value() default "";
}
