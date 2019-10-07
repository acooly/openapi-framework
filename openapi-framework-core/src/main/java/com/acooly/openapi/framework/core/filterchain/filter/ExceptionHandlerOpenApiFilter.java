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
import com.acooly.openapi.framework.core.exception.ApiServiceExceptionHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
@Slf4j
@Component
public class ExceptionHandlerOpenApiFilter extends AbstractOpenApiFilter {

    @Resource
    protected ApiServiceExceptionHander apiServiceExceptionHander;

    @Override
    public void doFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        if (!context.isError()) {
            return;
        }
        if (context.getResponse() == null) {
            context.prevHandleResponse();
        }
        apiServiceExceptionHander.handleApiServiceException(context.getRequest(),
                context.getResponse(), context.getException());
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }
}