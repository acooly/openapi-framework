/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-31 14:09 创建
 *
 */
package com.acooly.openapi.framework.core.listener.multicaster;

import com.acooly.openapi.framework.common.event.dto.ServiceEvent;

/**
 * 只处理服务级的事件
 *
 * @author qiubo@qq.com
 */
public class ServiceEventMulticaster<E extends ServiceEvent>
    extends AbstractOpenApiEventMulticaster<E> {}
