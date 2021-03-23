/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-02-12 23:13
 */
package com.acooly.openapi.framework.service.event;

import com.acooly.openapi.framework.service.domain.ApiAuth;
import com.acooly.openapi.framework.service.domain.ApiAuthAcl;
import com.acooly.openapi.framework.service.domain.ApiPartner;
import lombok.Data;

/**
 * @author zhangpu
 * @date 2019-02-12 23:13
 */
@Data
public class ApiUpdateEvent {

    private ApiAuth apiAuth;

    private ApiAuthAcl apiAuthAcl;

    private ApiPartner apiPartner;

    public ApiUpdateEvent() {
    }

    public ApiUpdateEvent(ApiAuth apiAuth) {
        this.apiAuth = apiAuth;
    }

    public ApiUpdateEvent(ApiAuthAcl apiAuthAcl) {
        this.apiAuthAcl = apiAuthAcl;
    }

    public ApiUpdateEvent(ApiPartner apiPartner) {
        this.apiPartner = apiPartner;
    }
}
