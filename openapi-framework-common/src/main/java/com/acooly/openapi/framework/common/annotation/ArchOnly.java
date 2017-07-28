/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-08-02 02:51 创建
 *
 */
package com.acooly.openapi.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 仅框架使用api.
 * @author qiubo@qq.com
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ArchOnly {


}
