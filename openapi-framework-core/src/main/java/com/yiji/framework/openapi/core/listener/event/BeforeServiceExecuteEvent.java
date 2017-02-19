/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-31 17:25 创建
 *
 */
package com.yiji.framework.openapi.core.listener.event;


import com.yiji.framework.openapi.common.message.ApiRequest;
import com.yiji.framework.openapi.common.message.ApiResponse;

/**
 * 服务执行之前的事件.
 * @author qzhanbo@yiji.com
 */
public class BeforeServiceExecuteEvent extends ServiceEvent {
	private ApiResponse apiResponse = null;
	private ApiRequest apiRequest = null;
	
	public BeforeServiceExecuteEvent(ApiRequest apiRequest, ApiResponse apiResponse) {
		this.apiResponse = apiResponse;
		this.apiRequest = apiRequest;
	}
	
	public ApiResponse getApiResponse() {
		return apiResponse;
	}
	
	public ApiRequest getApiRequest() {
		return apiRequest;
	}
}
