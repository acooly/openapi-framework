/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-31 17:51 创建
 *
 */
package com.yiji.framework.openapi.core.listener.event;


import com.yiji.framework.openapi.common.message.ApiRequest;
import com.yiji.framework.openapi.common.message.ApiResponse;

/**
 * 服务执行异常事件.
 * @author qzhanbo@yiji.com
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
