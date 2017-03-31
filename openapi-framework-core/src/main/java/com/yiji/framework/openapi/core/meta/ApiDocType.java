/*
 * 修订记录:
 * zhike@yiji.com 2017-03-31 13:40 创建
 *
 */
package com.yiji.framework.openapi.core.meta;

import java.lang.annotation.*;

/**
 * 修订记录：
 *
 * @author zhike@yiji.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiDocType {

    /**
     * 解决方案类型编码
     * @return
     */
    String code() default "Common";

    /**
     * 解决方案类型名称
     * @return
     */
    String name() default "通用类型";
}
