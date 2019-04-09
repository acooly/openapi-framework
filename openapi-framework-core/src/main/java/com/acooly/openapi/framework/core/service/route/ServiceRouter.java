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

import com.acooly.openapi.framework.common.executor.ApiService;

import java.util.Collection;

/**
 * 服务路由器
 *
 * @author qiubo@qq.com
 */
public interface ServiceRouter {
  ApiService route(String serviceName, String version, Collection<ApiService> apiServices);
}
