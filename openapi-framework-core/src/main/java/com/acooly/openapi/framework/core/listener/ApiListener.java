/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-31 12:10 创建
 *
 */
package com.acooly.openapi.framework.core.listener;

import com.acooly.openapi.framework.core.listener.event.OpenApiEvent;

import java.util.EventListener;

/** @author qiubo@qq.com */
public interface ApiListener<E extends OpenApiEvent> extends EventListener, Ordered {

  void onOpenApiEvent(E event);
}
