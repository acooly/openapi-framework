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
import lombok.Data;

/**
 * @author zhangpu
 * @date 2019-02-12 23:13
 */
@Data
public class ApiAuthUpdateEvent {

    private ApiAuth apiAuth;

    public ApiAuthUpdateEvent() {
    }

    public ApiAuthUpdateEvent(ApiAuth apiAuth) {
        this.apiAuth = apiAuth;
    }
}
