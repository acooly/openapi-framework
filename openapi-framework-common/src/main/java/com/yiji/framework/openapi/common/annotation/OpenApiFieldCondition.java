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
 * 标识此字段为条件可选。
 *
 * @author zhangpu
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenApiFieldCondition {

    /**
     * 添加可选的说明
     */
    String value();


}
