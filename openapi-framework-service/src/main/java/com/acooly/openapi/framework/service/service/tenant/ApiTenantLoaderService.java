/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-03-22 11:40
 */
package com.acooly.openapi.framework.service.service.tenant;

import java.util.List;

/**
 * Api Tenant 管理服务接口
 * <p>
 * 1、OpenApi层默认不建议提供租户的管理，该接口实现可以由集成系统实现接口返回租户相关数据，用于Api层配置。
 * 2、OpenApi框架提供默认实现，集成系统实现该接口后，通过@Primary标记注入
 *
 * @author zhangpu
 * @date 2021-03-22 11:40
 */
public interface ApiTenantLoaderService {

    /**
     * 加载所有的有效租户
     *
     * @return
     */
    List<ApiTenant> load();

}
