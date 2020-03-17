/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-03-17 17:11
 */
package com.acooly.openapi.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 专用于标记OpenApi报文字段需加密和解密
 *
 * @author zhangpu
 * @date 2020-03-17 17:11
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenApiSecure {
}
