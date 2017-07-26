/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月31日
 *
 */
package com.acooly.openapi.framework.common.annotation;


import com.acooly.openapi.framework.common.enums.ApiMessageType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义服务报文
 *
 * @author zhangpu
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenApiMessage {
    /**
     * 报文类型
     *
     * @return
     */
    public ApiMessageType type() default ApiMessageType.Request;

    /**
     * 服务名称
     *
     * @return
     */
    public String service() default "";

    /**
     * 服务版本
     * @return
     */
    public String version() default "1.0";

}
