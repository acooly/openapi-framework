/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-09 16:40
 */
package com.acooly.openapi.framework.core.filterchain.filter;

import com.acooly.module.filterchain.Filter;
import com.acooly.module.filterchain.FilterChain;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.core.log.OpenApiLoggerHandler;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @author zhangpu
 * @date 2019-09-09 16:40
 */
@Slf4j
public abstract class AbstractOpenApiFilter implements Filter<ApiContext> {

    @Resource
    protected OpenApiLoggerHandler openApiLoggerHandler;

    protected ApiContext getContext() {
        if (!ApiContextHolder.isInited()) {
            ApiContextHolder.init();
        }
        return ApiContextHolder.getContext();
    }

    @Override
    public void doFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        try {
            if (context.isError()) {
                return;
            }
            doInternalFilter(context, filterChain);
            log.debug("filter execute success: {}", this.getClass().getSimpleName());
        } catch (Exception e) {
            log.warn("filter execute failure: {}, error: {}", this.getClass().getSimpleName(), e.getMessage());
            context.setError(true);
            ApiServiceException apiServiceException;
            if (ApiServiceException.class.isAssignableFrom(e.getClass())) {
                apiServiceException = (ApiServiceException) e;
            } else {
                apiServiceException = new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, e.getMessage());
            }
            context.setException(apiServiceException);
        }
    }

    /**
     * 无需单独考虑异常处理
     *
     * @param context
     * @param filterChain
     */
    protected void doInternalFilter(ApiContext context, FilterChain<ApiContext> filterChain) {

    }


}
