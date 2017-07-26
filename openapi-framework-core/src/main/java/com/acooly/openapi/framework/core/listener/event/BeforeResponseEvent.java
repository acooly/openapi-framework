/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-31 18:00 创建
 *
 */
package com.acooly.openapi.framework.core.listener.event;

import java.util.Map;

/**
 * 服务响应之前的事件.
 * @author qiubo@qq.com
 */
public class BeforeResponseEvent extends ServiceEvent {
	private Map<String, String> requestData;
	
	public BeforeResponseEvent(Map<String, String> requestData) {
		this.requestData = requestData;
	}
	
	public Map<String, String> getRequestData() {
		return requestData;
	}
}
