/*
 * 修订记录:
 * zhike@yiji.com 2017-03-31 13:40 创建
 *
 */
package com.acooly.openapi.framework.common.annotation;

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
     *
     * @return
     */
    String code();

    /**
     * 解决方案类型名称
     *
     * @return
     */
    String name();

    /**
     * 父级解决方案类型编码
     *
     * @return
     */
    String parentCode() default "";

    /**
     * 父级解决方案类型名称
     *
     * @return
     */
    String parentName() default "";

}
