/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
package com.acooly.openapi.framework.core.filterchain.filter;

import com.acooly.core.utils.StringUtils;
import com.acooly.module.filterchain.FilterChain;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.Servlets;
import com.acooly.openapi.framework.core.marshall.ApiRedirectMarshall;
import com.acooly.openapi.framework.core.marshall.ApiResponseMarshall;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
@Slf4j
@Component
public class ResponseOutOpenApiFilter extends AbstractOpenApiFilter {

    @Autowired
    private ApiResponseMarshall apiResponseMarshall;
    @Autowired
    private ApiRedirectMarshall apiRedirectMarshall;

    @Override
    public void doFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        doResponse(context);
    }

    @Override
    public void doInternalFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
    }

    protected void doResponse(ApiContext context) {
        ApiResponse apiResponse = context.getResponse();
        HttpServletResponse response = context.getHttpResponse();
        String marshallStr = null;
        String redirectUrl = context.getRedirectUrl();
        if (context.isRedirect() && !StringUtils.isEmpty(redirectUrl)) {
            marshallStr = doResponseMarshal(apiResponse, true);
            String signType = context.getHttpResponse().getHeader(ApiConstants.SIGN_TYPE);
            String sign = context.getHttpResponse().getHeader(ApiConstants.SIGN);
            String location = buildRedirctLocation(redirectUrl, marshallStr, signType, sign);
            openApiLoggerHandler.log("服务跳转[url]:", redirectUrl);
            Servlets.redirect(response, location);
        } else {
            marshallStr = doResponseMarshal(apiResponse, false);
        }
        Servlets.writeResponse(response, marshallStr);
    }

    /**
     * 组报
     *
     * @return
     */
    protected String doResponseMarshal(ApiResponse apiResponse, boolean isRedirect) {
        if (isRedirect) {
            return (String) apiRedirectMarshall.marshall(apiResponse);
        } else {
            return (String) apiResponseMarshall.marshall(apiResponse);
        }
    }

    private String buildRedirctLocation(
            String returnUrl, String marshallStr, String signType, String sign) {
        StringBuilder sb = null;
        try {
            sb = new StringBuilder(returnUrl)
                    .append("?").append(ApiConstants.SIGN_TYPE).append("=").append(signType)
                    .append("&").append(ApiConstants.SIGN).append("=").append(sign)
                    .append("&").append(ApiConstants.BODY).append("=").append(URLEncoder.encode(marshallStr, Charsets.UTF_8.name()))
                    .append("&").append(ApiConstants.GID).append("=").append(getContext().getGid());
        } catch (UnsupportedEncodingException e) {
            throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, e);
        }
        return sb.toString();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
