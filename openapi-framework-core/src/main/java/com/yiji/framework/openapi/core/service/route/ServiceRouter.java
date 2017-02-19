/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-25 16:22 创建
 *
 */
package com.yiji.framework.openapi.core.service.route;

import java.util.Collection;

import com.yiji.framework.openapi.core.service.base.ApiService;

/**
 * 服务路由器
 * @author qzhanbo@yiji.com
 */
public interface ServiceRouter {
	ApiService route(String serviceName, String version, Collection<ApiService> apiServices);
}
