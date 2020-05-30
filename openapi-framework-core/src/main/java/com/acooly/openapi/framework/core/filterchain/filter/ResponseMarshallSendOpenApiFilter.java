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
import com.acooly.core.utils.Strings;
import com.acooly.module.filterchain.FilterChain;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.filterchain.OpenApiFilterEnum;
import com.acooly.openapi.framework.core.marshall.ApiMarshallFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 同步/跳转发送处理filter
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
@Slf4j
@Component
public class ResponseMarshallSendOpenApiFilter extends AbstractOpenApiFilter {

    @Resource
    protected ApiMarshallFactory apiMarshallFactory;

    @Override
    public void doFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        if (context.isRedirect() && Strings.isNotBlank(context.getRedirectUrl())) {
            doRedirect(context);
        } else {
            doResponse(context);
        }
    }

    protected void doMarshall(ApiContext context) {
        ApiResponse apiResponse = context.getResponse();
        ApiMessageContext result = null;
        if (context.isRedirect() && Strings.isNotBlank(context.getRedirectUrl())) {
            result = (ApiMessageContext) apiMarshallFactory.getRedirectMarshall(context.getApiProtocol()).marshall(apiResponse);
        } else {
            result = (ApiMessageContext) apiMarshallFactory.getResponseMarshall(context.getApiProtocol()).marshall(apiResponse);
        }
        context.setApiResponseContext(result);
    }


    protected void doResponse(ApiContext context) {
        ApiMessageContext result = (ApiMessageContext) apiMarshallFactory.
                getResponseMarshall(context.getApiProtocol()).marshall(context.getResponse());
        HttpServletResponse response = context.getHttpResponse();
        Servlets.setHeaders(response, result.getHeaders());
        Servlets.writeResponse(response, result.getBody());
    }

    protected void doRedirect(ApiContext context) {
        ApiMessageContext result = (ApiMessageContext) apiMarshallFactory.
                getRedirectMarshall(context.getApiProtocol()).marshall(context.getResponse());
        HttpServletResponse response = context.getHttpResponse();
        Servlets.setHeaders(response, result.getHeaders());
        String redirectUrl = context.getRedirectUrl();
        Servlets.redirect(response, result.buildRedirectUrl(redirectUrl));
    }

    @Override
    protected OpenApiFilterEnum openApiFilter() {
        return OpenApiFilterEnum.ResponseMarshall;
    }
}
