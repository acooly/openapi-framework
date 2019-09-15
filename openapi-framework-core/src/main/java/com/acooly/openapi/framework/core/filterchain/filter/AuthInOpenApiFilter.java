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
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.core.auth.ApiAuthorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
@Slf4j
@Component
public class AuthInOpenApiFilter extends AbstractOpenApiFilter {

    @Resource
    protected ApiAuthentication apiAuthentication;
    @Resource
    protected ApiAuthorization apiAuthorization;

    @Override
    protected void doInternalFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        apiAuthentication.authenticate(context);
        apiAuthorization.authorize(context);
        context.setAuthenticated(true);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
