/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-31 12:14 创建
 *
 */
package com.yiji.framework.openapi.core.listener;

import com.yiji.framework.openapi.core.listener.event.OpenApiEvent;

/**
 * @author qzhanbo@yiji.com
 */
public interface OpenApiEventPublisher<E extends OpenApiEvent> {

    /**
     * 发布此事件
     * @param event
     */
	void publishEvent(E event);

    /**
     * 是否可以发布此事件
     * @return
     */
	boolean canPublish();
}
