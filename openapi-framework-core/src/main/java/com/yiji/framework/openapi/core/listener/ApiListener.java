/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-31 12:10 创建
 *
 */
package com.yiji.framework.openapi.core.listener;

import java.util.EventListener;

import com.yiji.framework.openapi.core.listener.event.OpenApiEvent;

/**
 * @author qzhanbo@yiji.com
 */
public interface ApiListener<E extends OpenApiEvent> extends EventListener, Ordered {
	
	void onOpenApiEvent(E event);
}
