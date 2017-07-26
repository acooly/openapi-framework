/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-25 16:22 创建
 *
 */
package com.acooly.openapi.framework.core.service.route;

import java.util.Collection;

import com.acooly.openapi.framework.core.service.base.ApiService;

/**
 * 服务路由器
 * @author qiubo@qq.com
 */
public interface ServiceRouter {
	ApiService route(String serviceName, String version, Collection<ApiService> apiServices);
}
