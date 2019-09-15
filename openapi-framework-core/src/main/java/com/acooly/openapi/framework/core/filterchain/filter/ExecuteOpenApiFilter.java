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
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.event.dto.AfterServiceExecuteEvent;
import com.acooly.openapi.framework.common.event.dto.BeforeServiceExecuteEvent;
import com.acooly.openapi.framework.common.event.dto.ServiceExceptionEvent;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.executor.ApiService;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.listener.multicaster.EventPublisher;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 组织请求报文为报文对象
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
@Slf4j
@Component
public class ExecuteOpenApiFilter extends AbstractOpenApiFilter {

    @Resource
    private EventPublisher eventPublisher;

    @Override
    public void doInternalFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        doExceute(context);
    }


    protected void doExceute(ApiContext context) {
        ApiService apiService = context.getApiService();
        ApiRequest apiRequest = context.getRequest();
        ApiResponse apiResponse = context.getResponse();
        try {
            publishBeforeServiceExecuteEvent(context);
            apiService.service(context);
            // 如果是跳转接口，必须设置redirect的检查
            if (context.isRedirect() && Strings.isNullOrEmpty(apiService.getRedirectUrl())) {
                throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, "跳转接口必须设置下层RedirectUrl");
            }
        } catch (Throwable ex) {
            publishServiceExceptionEvent(apiResponse, apiRequest, apiService, ex);
            throw ex;
        } finally {
            publishAfterServiceExecuteEvent(apiResponse, apiRequest, apiService);
        }
    }


    private void publishBeforeServiceExecuteEvent(ApiContext apiContext) {
        if (eventPublisher.canPublishEvent(apiContext.getApiService())) {
            eventPublisher.publishEvent(
                    new BeforeServiceExecuteEvent(apiContext.getRequest(), apiContext.getResponse()),
                    apiContext.getApiService());
        }
    }

    private void publishServiceExceptionEvent(
            ApiResponse apiResponse, ApiRequest apiRequest, ApiService apiService, Throwable throwable) {
        if (eventPublisher.canPublishEvent(apiService)) {
            eventPublisher.publishEvent(
                    new ServiceExceptionEvent(apiRequest, apiResponse, throwable), apiService);
        }
    }

    private void publishAfterServiceExecuteEvent(
            ApiResponse apiResponse, ApiRequest apiRequest, ApiService apiService) {
        if (eventPublisher.canPublishEvent(apiService)) {
            eventPublisher.publishEvent(new AfterServiceExecuteEvent(apiRequest, apiResponse), apiService);
        }
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
