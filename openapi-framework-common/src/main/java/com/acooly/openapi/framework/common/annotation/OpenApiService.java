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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ApiScheme;
import com.acooly.openapi.framework.common.enums.ResponseType;
import org.springframework.stereotype.Service;


/**
 *
 * openapi 服务类定义
 * 
 * @author qiubo@qq.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface OpenApiService {
	/**
	 * 服务名称
	 * 
	 * @return
	 */
	String name();

	/**
	 * 服务版本
	 * 
	 * @return
	 */
	String version() default ApiConstants.VERSION_DEFAULT;

	/**
	 * 服务描述
	 * 
	 * @return
	 */
	String desc();

	/**
	 * 服务响应类型,默认为同步响应
	 * 
	 * @return
	 */
	ResponseType responseType() default ResponseType.SYN;

	/**
	 * 访问通讯协议
	 *
	 * @return
	 */
	ApiScheme scheme() default ApiScheme.ALL;

	/**
	 * 归属
	 *
	 * @return
	 */
	String owner() default "unknown";
	
	/**
	 * 业务类型
	 *
	 * @return
	 */
	ApiBusiType busiType() default ApiBusiType.Trade;
}
