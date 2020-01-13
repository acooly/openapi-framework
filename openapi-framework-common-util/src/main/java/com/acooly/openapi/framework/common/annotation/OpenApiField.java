/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-28 23:01 创建
 *
 */
package com.acooly.openapi.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 标识此字段为openapi的字段. openapi通过次annotation生成文档. 不标注此annotation的字段不参与Marshall/Unmarshall
 *
 * @author qiubo@qq.com
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenApiField {

    /**
     * 字段标题信息
     *
     * @return
     */
    String desc() default "";

    /**
     * 字段描述信息
     *
     * @return
     */
    String constraint() default "";

    /**
     * 字段样子
     *
     * @return
     */
    String demo() default "";

    /**
     * 字段需要加密(AES)
     *
     * @return
     */
    boolean security() default false;

    /**
     * 排序，按数字升序
     *
     * @return
     */
    int ordinal() default 0;
}
