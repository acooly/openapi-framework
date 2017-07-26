/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-31 17:51 创建
 *
 */
package com.acooly.openapi.framework.core.listener.event;


import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;

/**
 * 服务执行异常事件.
 * @author qiubo@qq.com
 */
public class ServiceExceptionEvent extends AfterServiceExecuteEvent {
	private Throwable throwable;
	
	public ServiceExceptionEvent(ApiRequest apiRequest, ApiResponse apiResponse, Throwable ex) {
		super(apiRequest, apiResponse);
		this.throwable = ex;
	}
	
	public Throwable getThrowable() {
		return throwable;
	}
}
