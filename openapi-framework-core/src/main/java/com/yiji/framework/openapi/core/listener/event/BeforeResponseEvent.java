/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-31 18:00 创建
 *
 */
package com.yiji.framework.openapi.core.listener.event;

import java.util.Map;

/**
 * 服务响应之前的事件.
 * @author qzhanbo@yiji.com
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
