/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-25 15:00 创建
 *
 */
package com.acooly.openapi.framework.common.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * openapi 介绍标志
 *
 * @author qiubo@qq.com
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
