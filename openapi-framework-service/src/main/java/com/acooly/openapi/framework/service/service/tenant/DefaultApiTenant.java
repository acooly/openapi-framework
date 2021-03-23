/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-03-23 16:47
 */
package com.acooly.openapi.framework.service.service.tenant;

import com.acooly.core.common.facade.InfoBase;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangpu
 * @date 2021-03-23 16:47
 */
@Getter
@Setter
public class DefaultApiTenant extends InfoBase implements ApiTenant {

    private String tenantId;
    private String tenantName;

    public DefaultApiTenant() {
    }

    public DefaultApiTenant(String tenantId, String tenantName) {
        this.tenantId = tenantId;
        this.tenantName = tenantName;
    }
}
