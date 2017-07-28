/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-25 16:25 创建
 *
 */
package com.acooly.openapi.framework.core.service.route;

import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceRouteException;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.core.service.base.ApiService;

/**
 * 默认服务路由实现
 * 
 * 支持服务版本路由: 1.用户请求没有版本,选择最高的版本 2.用户请求有版本,完全匹配
 *
 * @author qiubo@qq.com
 */
@Component("serviceRouter")
public class DefaultServiceRouterImpl implements ServiceRouter {
	private static final Logger logger = LoggerFactory.getLogger(DefaultServiceRouterImpl.class);
	
	@Override
	public ApiService route(String serviceName, String version, Collection<ApiService> apiServices) {
		if (apiServices == null) {
			return serverNotFound(serviceName, version);
		}
		Iterator<ApiService> iterator = apiServices.iterator();
		if (Strings.isNullOrEmpty(version)) {
			if (apiServices.size() == 1) {
				return iterator.next();
			} else {
				return findLargestVersion(iterator);
			}
		} else {
			while (iterator.hasNext()) {
				ApiService current = iterator.next();
				OpenApiService currentOpenApiService = getOpenApiServiceAnnotation(current);
				if (currentOpenApiService.version().equals(version)) {
					return current;
				}
			}
		}
		return serverNotFound(serviceName, version);
	}
	
	private ApiService findLargestVersion(Iterator<ApiService> iterator) {
		ApiService largestVersionApiService = iterator.next();
		while (iterator.hasNext()) {
			OpenApiService largestVersionOpenApiService = getOpenApiServiceAnnotation(largestVersionApiService);
			ApiService current = iterator.next();
			OpenApiService currentOpenApiService = getOpenApiServiceAnnotation(current);
			if (currentOpenApiService.version().compareTo(largestVersionOpenApiService.version()) > 0) {
				largestVersionApiService = current;
			}
		}
		return largestVersionApiService;
	}
	
	private ApiService serverNotFound(String serviceName, String version) {
		logger.warn("请求服务[" + serviceName + ":" + version + "]不存在");
		throw new ApiServiceRouteException(serviceName, version);
	}
	
	private OpenApiService getOpenApiServiceAnnotation(ApiService apiService) {
		return apiService.getClass().getAnnotation(OpenApiService.class);
		
	}
}
