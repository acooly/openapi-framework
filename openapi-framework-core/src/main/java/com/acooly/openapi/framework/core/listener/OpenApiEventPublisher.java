/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-31 12:14 创建
 *
 */
package com.acooly.openapi.framework.core.listener;

import com.acooly.openapi.framework.core.listener.event.OpenApiEvent;

/**
 * @author qiubo@qq.com
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
