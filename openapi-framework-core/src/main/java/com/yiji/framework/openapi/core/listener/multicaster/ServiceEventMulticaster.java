/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-31 14:09 创建
 *
 */
package com.yiji.framework.openapi.core.listener.multicaster;

import com.yiji.framework.openapi.core.listener.event.ServiceEvent;

/**
 * 只处理服务级的事件
 * @author qzhanbo@yiji.com
 */
public class ServiceEventMulticaster<E extends ServiceEvent> extends AbstractOpenApiEventMulticaster<E> {

}
