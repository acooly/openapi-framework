/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.log;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.acooly.integration.bean.AbstractSpringProxyBean;

/**
 * Spring 代理实现
 * 
 * @author zhangpu
 */
@Component("openApiLoggerHandler")
public class SpringProxyOpenApiLoggerHandler extends
		AbstractSpringProxyBean<OpenApiLoggerHandler, DefaultOpenApiLoggerHandler> implements OpenApiLoggerHandler {

	@Override
	public void log(String label, Map<String, ?> data) {
		getTarget().log(label, data);
	}

	@Override
	public void log(String label, String msg) {
		getTarget().log(label, msg);
	}

}
