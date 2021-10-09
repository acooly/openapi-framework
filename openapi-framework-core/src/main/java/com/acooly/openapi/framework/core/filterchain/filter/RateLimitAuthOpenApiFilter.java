/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
package com.acooly.openapi.framework.core.filterchain.filter;

import com.acooly.module.cache.limit.RateChecker;
import com.acooly.module.filterchain.FilterChain;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.core.OpenAPIProperties;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.core.auth.ApiAuthorization;
import com.acooly.openapi.framework.core.filterchain.OpenApiFilterEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 流控认证 filter
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
@Slf4j
@Component
public class RateLimitAuthOpenApiFilter extends AbstractOpenApiFilter {
    public static final String PREFIX = "openapi-rate-";
    @Autowired
    private RateChecker rateChecker;
    @Resource
    protected OpenAPIProperties openAPIProperties;

    @Override
    protected void doInternalFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        if (openAPIProperties.getRates().isEmpty()) {
            return;
        }
        String partnerId = context.getPartnerId();
        String method = context.getServiceName();
        openAPIProperties.getRates().forEach(rate -> {
            if (rate.acceptPartnerId(partnerId) && rate.acceptMethod(method)) {
                String partnerIdStr = partnerId;
                String methodStr = method;
                if (rate.allPartnerId()) {
                    partnerIdStr = ApiConstants.WILDCARD_TOKEN;
                }
                if (rate.allMethod()) {
                    methodStr = ApiConstants.WILDCARD_TOKEN;
                }
                if (!rateChecker.check(PREFIX + partnerIdStr + "." + methodStr, rate.getInterval(), rate.getMaxRequests())) {
                    throw new ApiServiceException(ApiServiceResultCode.TOO_MANY_REQUEST);
                }
            }
        });
    }

    @Override
    protected OpenApiFilterEnum openApiFilter() {
        return OpenApiFilterEnum.RateLimitAuth;
    }
}
