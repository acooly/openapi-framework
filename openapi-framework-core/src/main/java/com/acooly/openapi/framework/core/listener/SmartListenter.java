/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-31 12:17 创建
 *
 */
package com.acooly.openapi.framework.core.listener;

import com.acooly.openapi.framework.core.listener.event.OpenApiEvent;

/** @author qiubo@qq.com */
public interface SmartListenter<E extends OpenApiEvent> extends ApiListener<E> {
  boolean supportsEventType(Class<E> eventType);
}
