/*
 * acooly.cn Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.common.event.dto;

import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;

/**
 * 服务执行事件基类.
 *
 * @author zhangpu 增加ApiContext的事件参数和构造
 */
public abstract class ServiceExecuteEvent extends ServiceEvent {
    protected ApiContext apiContext;
    protected ApiResponse apiResponse = null;
    protected ApiRequest apiRequest = null;

    public ServiceExecuteEvent(ApiRequest apiRequest, ApiResponse apiResponse) {
        this.apiResponse = apiResponse;
        this.apiRequest = apiRequest;
    }

    public ServiceExecuteEvent(ApiContext apiContext) {
        this.apiContext = apiContext;
        this.apiRequest = apiContext.getRequest();
        this.apiResponse = apiContext.getResponse();
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

    public ApiRequest getApiRequest() {
        return apiRequest;
    }

    public ApiContext getApiContext() {
        return apiContext;
    }

    @Override
    public String toString() {
        return apiRequest.getService() + " : " + super.toString();
    }
}
