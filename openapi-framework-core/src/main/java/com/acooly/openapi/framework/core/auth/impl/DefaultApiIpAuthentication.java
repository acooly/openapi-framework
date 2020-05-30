/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-05-29 14:22
 */
package com.acooly.openapi.framework.core.auth.impl;

import com.acooly.core.utils.Collections3;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.core.auth.ApiIpAuthentication;
import com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author zhangpu
 * @date 2020-05-29 14:22
 */
@Slf4j
@Component
public class DefaultApiIpAuthentication implements ApiIpAuthentication {

    @Resource
    protected AuthInfoRealm authInfoRealm;

    @Override
    public void authenticate(ApiContext apiContext) {
        Set<String> ipWhitelist = authInfoRealm.getIpWhitelist(apiContext.getAccessKey());
        if (Collections3.isNotEmpty(ipWhitelist) && !ipWhitelist.contains(apiContext.getRequestIp())) {
            throw new ApiServiceException(ApiServiceResultCode.UNAUTH_IP_ERROR, apiContext.getRequestIp());
        }
    }
}
