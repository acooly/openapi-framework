/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-28 23:01 创建
 *
 */
package com.yiji.framework.openapi.common.annotation;

import java.lang.annotation.*;

/**
 * 标识此字段为openapi的字段别名
 *
 * 主要用于兼容老接口的字段定义
 *
 * @author zhangpu@yiji.com
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
