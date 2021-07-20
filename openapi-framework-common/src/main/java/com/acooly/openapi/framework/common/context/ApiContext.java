/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-26 15:54 创建
 *
 */
package com.acooly.openapi.framework.common.context;

import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import com.acooly.module.filterchain.Context;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.executor.ApiService;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.ApiServerUtils;
import com.acooly.openapi.framework.common.utils.ApiUtils;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import lombok.Getter;
import lombok.Setter;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * OpenApi全局回话对象
 *
 * @author zhangpu
 */
@Getter
@Setter
public class ApiContext extends Context {
    private static final Logger perlogger = LoggerFactory.getLogger(ApiConstants.PERFORMANCE_LOGGER);
    public static final String ACCESSKEY_SUB_SPLIT_CHAR = "#";

    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;
    /**
     * 请求会话内容
     */
    private ApiMessageContext apiRequestContext;

    /**
     * 响应会话内容
     */
    private ApiMessageContext apiResponseContext;

    private ApiProtocol apiProtocol;

    /**
     * 是否已认证通过
     */
    private boolean authenticated = false;


    private boolean error;

    private Exception exception;

    private String gid;

    private OpenApiService openApiService;

    @SuppressWarnings("rawtypes")
    private ApiService apiService;

    private ApiRequest request;

    private ApiResponse response;

    private String redirectUrl;

    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务版本
     */
    private String serviceVersion;

    private String userAgent;

    private boolean appClient;

    private String sign;

    private SignTypeEnum signType = SignTypeEnum.MD5;

    private String accessKey;

    private String tenantId;

    private String requestBody;

    private String requestIp;

    /**
     * 标记返回报文是否需签名
     * 直接使用：authenticated属性判断，true：则可以加密和签名，false：则无需加密和签名
     */
    @Deprecated
    private boolean signResponse = true;

    /**
     * 响应头参数
     */
    private Map<String, String> responseHeaders = Maps.newHashMap();

    private String requestNo;

    private String context;

    private String partnerId;

    private StopWatch stopWatch;

    private Map<String, Object> ext = Maps.newHashMap();

    /**
     * 标记：同步(false)/异步(true)通知
     */
    private boolean asyncNotify = true;

    public ApiContext() {
    }

    public ApiContext(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
    }

    /**
     * 初始化
     */
    public void init() {
        if (this.httpRequest == null) {
            this.httpRequest = Servlets.getRequest();
        }
        if (this.httpResponse == null) {
            this.httpResponse = Servlets.getResponse();
        }
        // 设置会话全局ID
        this.gid = Ids.gid();
        MDC.put(ApiConstants.GID, gid);
        // 预解析报文
        this.apiRequestContext = ApiUtils.getApiRequestContext(this.httpRequest);
    }

    /**
     * 预解析报文
     */
    public void doPrevHandleRequest() {
        this.putAll(apiRequestContext.headersToParameters());
        this.putAll(apiRequestContext.getParameters());

        this.apiProtocol = ApiProtocol.find(apiRequestContext.getProtocol());
        // && ApiUtils.isJson(apiRequestContext.getBody())
        if (Strings.isNoneBlank(apiRequestContext.getBody())) {
            this.requestBody = apiRequestContext.getBody();
            this.putAll(ApiServerUtils.parseJsonBody(this.requestBody));
        } else {
            // 老协议，则组装参数为requestBody，用于后续签名认证
            this.requestBody = ApiUtils.getWaitForSignString(apiRequestContext.getParameters());
        }

        this.accessKey = getParameterNoBlank(ApiConstants.ACCESS_KEY, ApiConstants.PARTNER_ID);
        this.sign = getParameterNoBlank(ApiConstants.SIGN);
        String signType = getParameter(ApiConstants.SIGN_TYPE, ApiConstants.SIGN_TYPE);
        try {
            this.signType = Strings.isNoneBlank(signType) ? SignTypeEnum.valueOf(signType.toUpperCase()) : SignTypeEnum.MD5;
        } catch (IllegalArgumentException e) {
            throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR, "不支持的签名类型:" + signType);
        }
        this.partnerId = getParameterNoBlank(ApiConstants.PARTNER_ID, ApiConstants.ACCESS_KEY);
        this.serviceName = getParameterNoBlank(ApiConstants.SERVICE);
        this.serviceVersion = getParameterDefault(ApiConstants.VERSION, ApiConstants.VERSION_DEFAULT);
        this.requestNo = getParameterNoBlank(ApiConstants.REQUEST_NO);
        this.context = getParameter(ApiConstants.CONTEXT);
        // 扩展
        this.userAgent = getParameter(HttpHeaders.USER_AGENT);
        this.requestIp = getParameter(ApiConstants.REQUEST_IP);
        MDC.put(ApiConstants.REQUEST_NO, this.requestNo);
        this.stopWatch = new Slf4JStopWatch(serviceName, perlogger);
        stopWatch.start();
    }


    /**
     * 初始化响应信息
     */
    public void doRrevHandleResponse() {
        // 预处理响应
        this.apiResponseContext = ApiUtils.getApiResponseContext(this.apiRequestContext);
        if (this.response == null) {
            this.response = new ApiResponse();
        }
        this.response.setRequestNo(this.requestNo);
        this.response.setPartnerId(this.partnerId);
        this.response.setService(this.serviceName);
        this.response.setVersion(this.serviceVersion);
        this.response.setContext(this.context);
        this.response.setProtocol(this.apiProtocol);

    }


    public void setOpenApiService(OpenApiService openApiService) {
        this.openApiService = openApiService;
        setServiceName(openApiService.name());
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

    /**
     * 获取规范的AccessKey
     * 兼容动态AccessKey和subAccessKey模式，形如：accessKey#username模式
     *
     * @return
     */
    public String getCanonicalAccessKey() {
        if (Strings.contains(getAccessKey(), ACCESSKEY_SUB_SPLIT_CHAR)) {
            return Strings.substringBefore(getAccessKey(), ACCESSKEY_SUB_SPLIT_CHAR);
        }
        return getAccessKey();
    }


    public void setApiService(ApiService apiService) {
        this.setOpenApiService(apiService.getClass().getAnnotation(OpenApiService.class));
        this.apiService = apiService;
    }

    public void exception(Exception exception) {
        this.error = true;
        this.exception = exception;
    }

    private String throwIfEmpty(String key, String value) {
        if (Strings.isBlank(value)) {
            throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR, key + "是必填项");
        }
        return value;
    }


    protected String getParameter(String key) {
        return (String) get(key);
    }

    protected String getParameter(String key1, String key2) {
        return Strings.defaultString((String) get(key1), (String) get(key2));
    }

    protected String getParameterDefault(String key, String defaultValue) {
        return Strings.defaultString((String) get(key), defaultValue);
    }

    private String getParameterNoBlank(String key) {
        String value = getParameter(key);
        return throwIfEmpty(key, value);
    }

    protected String getParameterNoBlank(String key1, String key2) {
        String value = Strings.defaultString((String) get(key1), (String) get(key2));
        return throwIfEmpty(key1, value);
    }

    public void destory() {
        if (stopWatch != null) {
            stopWatch.stop();
        }
    }
}
