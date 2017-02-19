/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-25 15:00 创建
 *
 */
package com.yiji.framework.openapi.core.meta;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * openapi 介绍标志
 *
 * @author qzhanbo@yiji.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenApiNote {
    /**
     * 服务业务介绍
     *
     * @return
     */
    String value() default "";


}
