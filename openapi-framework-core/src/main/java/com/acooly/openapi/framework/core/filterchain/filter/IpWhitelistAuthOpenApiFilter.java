/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
package com.acooly.openapi.framework.core.filterchain.filter;

import com.acooly.module.filterchain.FilterChain;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.core.auth.ApiIpAuthentication;
import com.acooly.openapi.framework.core.filterchain.OpenApiFilterEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
@Slf4j
@Component
public class IpWhitelistAuthOpenApiFilter extends AbstractOpenApiFilter {

    @Resource
    protected ApiIpAuthentication apiIpAuthentication;

    @Override
    protected void doInternalFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        apiIpAuthentication.authenticate(context);
    }

    @Override
    protected OpenApiFilterEnum openApiFilter() {
        return OpenApiFilterEnum.IpWhitelistAuth;
    }
}
