/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-31 12:24 创建
 *
 */
package com.yiji.framework.openapi.core.listener;

import com.yiji.framework.openapi.core.listener.event.OpenApiEvent;

/**
 * @author qzhanbo@yiji.com
 */
public interface OpenApiEventMulticaster<E extends OpenApiEvent> extends OpenApiEventPublisher<E>{

	void addListener(ApiListener<E> listener);
	
	void removeListener(ApiListener<E> listener);
	
	void removeAllListeners();
	
}
