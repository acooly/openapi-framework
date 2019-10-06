/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
package com.acooly.openapi.framework.core.filterchain.filter;

import com.acooly.core.utils.Servlets;
import com.acooly.module.filterchain.FilterChain;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.marshall.ApiResponseMarshall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * 同步响应处理filter
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
@Slf4j
@Component
public class ResponseOpenApiFilter extends AbstractOpenApiFilter {

    @Autowired
    private ApiResponseMarshall apiResponseMarshall;

    @Override
    public void doFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        if (!context.isRedirect()) {
            doResponse(context);
        }
    }

    protected void doResponse(ApiContext context) {
        ApiResponse apiResponse = context.getResponse();
        ApiMessageContext result = (ApiMessageContext) apiResponseMarshall.marshall(apiResponse);
        HttpServletResponse response = context.getHttpResponse();
        Servlets.setHeaders(response, result.getHeaders());
        Servlets.writeResponse(response, result.getBody());
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }
}
