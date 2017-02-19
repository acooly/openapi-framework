/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-31 16:59 创建
 *
 */
package com.yiji.framework.openapi.core.listener.event;

import java.util.Map;

/**
 * 收到用户请求时的事件.
 * @author qzhanbo@yiji.com
 */
public class RequestReceivedEvent extends SystemEvent {
	private Map<String, String> requestData;
	
	public RequestReceivedEvent(Map<String, String> requestData) {
		this.requestData = requestData;
	}
	
	public Map<String, String> getRequestData() {
		return requestData;
	}
	
}
