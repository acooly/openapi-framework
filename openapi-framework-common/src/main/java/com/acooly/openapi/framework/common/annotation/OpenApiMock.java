/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-09-20 12:57
 */
package com.acooly.openapi.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 标记OpenApi服务为mock实现
 * <p>
 * 用于：连通性测试，提供一下两个能力
 * 1、对请求报文完整性和合法性验证
 * 2、以Response报文的@OpenApiFiled.demo的内容作为数据填充响应对象返回
 *
 * @author zhangpu
 * @date 2022-09-20 12:57
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenApiMock {
}
