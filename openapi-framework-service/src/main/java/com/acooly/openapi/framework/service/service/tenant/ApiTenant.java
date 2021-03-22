/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-03-21 01:17
 */
package com.acooly.openapi.framework.service.service.tenant;

/**
 * Tenant集成接口
 *
 * @author zhangpu
 * @date 2021-03-21 01:17
 */
public interface ApiTenant {

    /**
     * 租户ID
     *
     * @return
     */
    String getTenantId();

    /**
     * 租户名称
     *
     * @return
     */
    String getTenantName();

}
