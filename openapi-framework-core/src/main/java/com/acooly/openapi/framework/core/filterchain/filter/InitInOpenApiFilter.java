/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-08-31 21:33
 */
package com.acooly.openapi.framework.core.filterchain.filter;

import com.acooly.core.utils.Encodes;
import com.acooly.core.utils.Strings;
import com.acooly.module.filterchain.FilterChain;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.executor.ApiService;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.service.factory.ApiServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 初始化
 *
 * @author zhangpu
 * @date 2019-08-31 21:33
 */
@Slf4j
@Component
public class InitInOpenApiFilter extends AbstractOpenApiFilter {

    @Autowired
    private ApiServiceFactory apiServiceFactory;

    @Override
    public void doInternalFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        try {
            context.init();
            doInitApiService(context);
            context.initResponse();
        } finally {
            logRequestData(context);
        }
    }

    private void logRequestData(ApiContext apiContext) {
        String serviceName = apiContext.getServiceName();
        String labelPostfix = (Strings.isNotBlank(serviceName) ? "[" + serviceName + "]:" : ":");
        openApiLoggerHandler.log("服务请求" + labelPostfix, apiContext.getRequest(), apiContext.getRequestBody());
    }


    protected void doInitApiService(ApiContext context) {
        ApiService apiService = apiServiceFactory.getApiService(context.getServiceName(), context.getServiceVersion());
        context.setApiService(apiService);
        ApiRequest apiRequest = apiService.getRequestBean();
        ApiResponse apiResponse = apiService.getResponseBean();
        context.setRequest(apiRequest);
        context.setResponse(apiResponse);
    }

    protected void initResponse(ApiContext context) {
        ApiResponse apiResponse = context.getResponse();
        if (context.getResponse() == null) {
            return;
        }
        ApiRequest apiRequest = context.getRequest();
        if (apiRequest == null) {
            return;
        }
        apiResponse.setRequestNo(apiRequest.getRequestNo());
        apiResponse.setPartnerId(apiRequest.getPartnerId());
        apiResponse.setService(apiRequest.getService());
        apiResponse.setVersion(apiRequest.getVersion());
        apiResponse.setContext(apiRequest.getContext());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

