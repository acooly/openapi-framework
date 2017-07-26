/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall;

import java.util.Map;

import com.acooly.openapi.framework.common.enums.ApiProtocol;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

/**
 * @author zhangpu
 */
@Component
public class ApiMarshallFactoryImpl implements ApiMarshallFactory, InitializingBean {

	@Autowired
	private ApplicationContext applicationContext;

	private Map<ApiProtocol, ApiRequestMarshall> ApiRequestMarshallMap = Maps.newHashMap();
	private Map<ApiProtocol, ApiResponseMarshall> ApiResponseMarshallMap = Maps.newHashMap();
	private Map<ApiProtocol, ApiRedirectMarshall> ApiRedirectMarshallMap = Maps.newHashMap();
	private Map<ApiProtocol, ApiNotifyMarshall> ApiNotifyMarshallMap = Maps.newHashMap();

	@Override
	public ApiRequestMarshall getRequestMarshall(ApiProtocol apiProtocol) {
		return this.ApiRequestMarshallMap.get(apiProtocol);
	}

	@Override
	public ApiResponseMarshall getResponseMarshall(ApiProtocol apiProtocol) {
		return this.ApiResponseMarshallMap.get(apiProtocol);
	}

	@Override
	public ApiRedirectMarshall getRedirectMarshall(ApiProtocol apiProtocol) {
		return this.ApiRedirectMarshallMap.get(apiProtocol);
	}

	@Override
	public ApiNotifyMarshall getNotifyMarshall(ApiProtocol apiProtocol) {
		return this.ApiNotifyMarshallMap.get(apiProtocol);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		Map<String, ApiRequestMarshall> apiRequestMarshalls = applicationContext
				.getBeansOfType(ApiRequestMarshall.class);
		if (apiRequestMarshalls != null && apiRequestMarshalls.size() > 0) {
			for (ApiRequestMarshall t : apiRequestMarshalls.values()) {
				this.ApiRequestMarshallMap.put(t.getProtocol(), t);
			}
		}

		Map<String, ApiResponseMarshall> apiResponseMarshalls = applicationContext
				.getBeansOfType(ApiResponseMarshall.class);
		if (apiResponseMarshalls != null && apiResponseMarshalls.size() > 0) {
			for (ApiResponseMarshall t : apiResponseMarshalls.values()) {
				this.ApiResponseMarshallMap.put(t.getProtocol(), t);
			}
		}

		Map<String, ApiRedirectMarshall> apiRedirectMarshalls = applicationContext
				.getBeansOfType(ApiRedirectMarshall.class);
		if (apiRedirectMarshalls != null && apiRedirectMarshalls.size() > 0) {
			for (ApiRedirectMarshall t : apiRedirectMarshalls.values()) {
				this.ApiRedirectMarshallMap.put(t.getProtocol(), t);
			}
		}

		Map<String, ApiNotifyMarshall> apiNotifyMarshalls = applicationContext.getBeansOfType(ApiNotifyMarshall.class);
		if (apiNotifyMarshalls != null && apiNotifyMarshalls.size() > 0) {
			for (ApiNotifyMarshall t : apiNotifyMarshalls.values()) {
				this.ApiNotifyMarshallMap.put(t.getProtocol(), t);
			}
		}

	}

}
