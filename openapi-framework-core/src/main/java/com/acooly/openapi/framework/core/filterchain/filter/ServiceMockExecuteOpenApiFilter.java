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
import com.acooly.openapi.framework.common.event.dto.AfterServiceExecuteEvent;
import com.acooly.openapi.framework.common.event.dto.BeforeServiceExecuteEvent;
import com.acooly.openapi.framework.common.event.dto.ServiceExceptionEvent;
import com.acooly.openapi.framework.common.executor.ApiService;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.OpenAPIProperties;
import com.acooly.openapi.framework.core.filterchain.OpenApiFilterEnum;
import com.acooly.openapi.framework.core.listener.multicaster.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 服务MOCK调用和实现
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
@Slf4j
@Component
public class ServiceMockExecuteOpenApiFilter extends AbstractOpenApiFilter {

    @Resource
    protected OpenAPIProperties openAPIProperties;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public void doInternalFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        // 如果没有打开MOCK全局开关，则跳过
        if (!openAPIProperties.getMock().isEnable()) {
            return;
        }
        doMock(context);
    }


    protected void doMock(ApiContext context) {

        ApiService apiService = context.getApiService();
        ApiRequest apiRequest = context.getRequest();
        ApiResponse apiResponse = context.getResponse();
        try {




            publishBeforeServiceExecuteEvent(context);
            apiService.service(context);
        } catch (Throwable ex) {
            publishServiceExceptionEvent(apiResponse, apiRequest, apiService, ex);
            throw ex;
        } finally {
            publishAfterServiceExecuteEvent(context);
        }
    }


    private void publishBeforeServiceExecuteEvent(ApiContext context) {
        if (eventPublisher.canPublishEvent(context.getApiService())) {
            eventPublisher.publishEvent(new BeforeServiceExecuteEvent(context), context.getApiService());
        }
    }

    private void publishServiceExceptionEvent(
            ApiResponse apiResponse, ApiRequest apiRequest, ApiService apiService, Throwable throwable) {
        if (eventPublisher.canPublishEvent(apiService)) {
            eventPublisher.publishEvent(new ServiceExceptionEvent(apiRequest, apiResponse, throwable), apiService);
        }
    }

    private void publishAfterServiceExecuteEvent(ApiContext context) {
        if (eventPublisher.canPublishEvent(context.getApiService())) {
            eventPublisher.publishEvent(new AfterServiceExecuteEvent(context), context.getApiService());
        }
    }

    @Override
    protected OpenApiFilterEnum openApiFilter() {
        return OpenApiFilterEnum.ServiceMock;
    }
}
