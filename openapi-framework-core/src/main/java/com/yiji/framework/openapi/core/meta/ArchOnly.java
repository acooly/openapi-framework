/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-08-02 02:51 创建
 *
 */
package com.yiji.framework.openapi.core.meta;

import java.lang.annotation.*;

/**
 * 仅框架使用api.
 * @author qzhanbo@yiji.com
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ArchOnly {


}
