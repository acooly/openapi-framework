/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
package com.acooly.openapi.framework.core.filterchain.filter;

import com.acooly.core.utils.Asserts;
import com.acooly.core.utils.Servlets;
import com.acooly.module.filterchain.FilterChain;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.marshall.ApiRedirectMarshall;
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
public class RedirectOpenApiFilter extends AbstractOpenApiFilter {

    @Autowired
    private ApiRedirectMarshall apiRedirectMarshall;

    @Override
    public void doFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        if (context.isRedirect()) {
            doRedirect(context);
        }
    }

    protected void doRedirect(ApiContext context) {
        ApiResponse apiResponse = context.getResponse();
        ApiMessageContext result = (ApiMessageContext) apiRedirectMarshall.marshall(apiResponse);
        HttpServletResponse response = context.getHttpResponse();
        Servlets.setHeaders(response, result.getHeaders());
        String redirectUrl = context.getRedirectUrl();
        Asserts.notEmpty(redirectUrl, "跳转下层的redirectUrl不能为空，请在该服务内手动设置跳转地址");
        Servlets.redirect(response, result.buildRedirectUrl(redirectUrl));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }
}
