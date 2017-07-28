/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-26 15:54 创建
 *
 */
package com.acooly.openapi.framework.core.executer;

import com.acooly.core.utils.Ids;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.core.service.base.ApiService;
import com.acooly.openapi.framework.common.enums.ResponseType;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author qiubo@qq.com
 */
public class ApiContext {

    private HttpServletRequest orignalRequest;
    private HttpServletResponse orignalResponse;

    /**
     * 是否已认证通过
     */
    private boolean authenticated = false;

    /**
     * 交易级内部ID
     */
    private String gid;
    /**
     * 请求及内部ID
     */
    private String oid;

    private OpenApiService openApiService;
    @SuppressWarnings("rawtypes")
    private ApiService apiService;

    private ApiRequest request;
    private ApiResponse response;

    Map<String, String> requestData;
    private String redirectUrl;

    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务版本
     */
    private String serviceVersion;
    /**
     * 访问协议
     */
    private ApiProtocol protocol = ApiProtocol.HTTP_FORM_JSON;

    private String userAgent;

    private boolean appClient;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void initGid() {
        gid = Ids.gid();
    }

    public OpenApiService getOpenApiService() {
        return openApiService;
    }

    public void setOpenApiService(OpenApiService openApiService) {
        this.openApiService = openApiService;
        setServiceName(openApiService.name());
    }

    public Map<String, String> getRequestData() {
        return requestData;
    }

    public void setRequestData(Map<String, String> requestData) {
        this.requestData = requestData;
    }

    /**
     * 响应类型是否是重定向
     *
     * @return
     */
    public boolean isRedirect() {
        if (openApiService == null) {
            return false;
        }
        return openApiService.responseType() == ResponseType.REDIRECT;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ApiService getApiService() {
        return apiService;
    }

    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    public HttpServletRequest getOrignalRequest() {
        return orignalRequest;
    }

    public void setOrignalRequest(HttpServletRequest orignalRequest) {
        this.orignalRequest = orignalRequest;
    }

    public HttpServletResponse getOrignalResponse() {
        return orignalResponse;
    }

    public void setOrignalResponse(HttpServletResponse orignalResponse) {
        this.orignalResponse = orignalResponse;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public ApiRequest getRequest() {
        return request;
    }

    public void setRequest(ApiRequest request) {
        this.request = request;
    }

    public ApiResponse getResponse() {
        return response;
    }

    public void setResponse(ApiResponse response) {
        this.response = response;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public ApiProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(ApiProtocol protocol) {
        this.protocol = protocol;
    }

    public boolean isAppClient() {
        return appClient;
    }

    public void setAppClient(boolean appClient) {
        this.appClient = appClient;
    }

    public void init(Map<String, String> requestData, ApiService apiService) {
        this.setOpenApiService(apiService.getClass().getAnnotation(OpenApiService.class));
        String theVersion = this.getOpenApiService().version();
        this.serviceVersion = StringUtils.isBlank(theVersion) ? ApiConstants.VERSION_DEFAULT : theVersion;
        this.setRequestData(requestData);
        this.setApiService(apiService);
        String theProtocol = requestData.get(ApiConstants.PROTOCOL);
        this.protocol = StringUtils.isNotBlank(theProtocol) ? ApiProtocol.valueOf(theProtocol)
                : ApiProtocol.HTTP_FORM_JSON;
        this.appClient=Boolean.valueOf(requestData.get("appClient"));
    }
}
