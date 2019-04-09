/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-07-28 16:43 创建
 */
package com.acooly.openapi.apidoc.generator.parser.support;

import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;

/**
 * @author acooly
 */
public class ApiServiceMeta {

    private Class<?> apiServiceClass;

    private Class<? extends ApiRequest> requestClass;

    private Class<? extends ApiResponse> responseClass;

    private Class<? extends ApiNotify> notifyClass;


    public ApiServiceMeta() {
    }

    public ApiServiceMeta(Class<?> apiServiceClass) {
        this.apiServiceClass = apiServiceClass;
    }


    public Class<? extends ApiRequest> getRequestClass() {
        return requestClass;
    }

    public void setRequestClass(Class<? extends ApiRequest> requestClass) {
        this.requestClass = requestClass;
    }

    public Class<? extends ApiResponse> getResponseClass() {
        return responseClass;
    }

    public void setResponseClass(Class<? extends ApiResponse> responseClass) {
        this.responseClass = responseClass;
    }

    public Class<? extends ApiNotify> getNotifyClass() {
        return notifyClass;
    }

    public void setNotifyClass(Class<? extends ApiNotify> notifyClass) {
        this.notifyClass = notifyClass;
    }

    public boolean check() {
        return (apiServiceClass != null && requestClass != null) && (responseClass != null);
    }

    public Class<?> getApiServiceClass() {
        return apiServiceClass;
    }

    public void setApiServiceClass(Class<?> apiServiceClass) {
        this.apiServiceClass = apiServiceClass;
    }

    @Override
    public String toString() {
        return com.acooly.core.utils.ToString.toString(this);
    }
}
