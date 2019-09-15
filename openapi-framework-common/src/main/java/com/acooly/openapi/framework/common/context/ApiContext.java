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
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.executor.ApiService;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import lombok.Getter;
import lombok.Setter;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import static com.acooly.openapi.framework.common.ApiConstants.BODY;

/**
 * OpenApi全局回话对象
 *
 * @author zhangpu
 */
@Getter
@Setter
public class ApiContext extends Context {
    private static final Logger perlogger = LoggerFactory.getLogger(ApiConstants.PERFORMANCE_LOGGER);
    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;

    /**
     * 请求头参数
     */
    private Map<String, String> headers = Maps.newHashMap();

    private ApiProtocol apiProtocol;

    /**
     * 是否已认证通过
     */
    private boolean authenticated = false;


    private boolean error;

    private Exception exception;

    /**
     * 交易级内部ID
     */
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

    private SignTypeEnum signType;

    private String accessKey;

    private String requestBody;

    private String responseBody;

    private String responseSign;

    private String requestNo;

    private String context;

    private String partnerId;

    private StopWatch stopWatch;

    private Map<String, Object> ext = Maps.newHashMap();


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
        // 设置会话全局ID
        this.gid = Ids.gid();
        MDC.put(ApiConstants.GID, gid);
        doLoad();
        doParse();
        MDC.put(ApiConstants.REQUEST_NO, this.requestNo);
        this.stopWatch = new Slf4JStopWatch(serviceName, perlogger);
    }

    public void initResponse() {
        if (getResponse() == null) {
            this.response = new ApiResponse();
        }
        this.response.setRequestNo(this.requestNo);
        this.response.setPartnerId(this.partnerId);
        this.response.setService(this.serviceName);
        this.response.setVersion(this.serviceName);
        this.response.setContext(this.context);
        this.response.setProtocol(this.apiProtocol);
    }


    protected void doLoad() {
        // 获取Http请求数据
        this.putAll(Servlets.getHeaders(httpRequest, "x-api"));
        this.putAll(Servlets.getParameters(httpRequest));
        this.put(HttpHeaders.USER_AGENT, httpRequest.getHeader("User-Agent"));
        doParseProtocol();
        if (this.apiProtocol == ApiProtocol.JSON) {
            loadJsonBody();
        }
    }

    protected void doParse() {
        this.accessKey = getParameterNoBlank(ApiConstants.X_API_ACCESS_KEY, ApiConstants.PARTNER_ID);
        this.sign = getParameterNoBlank(ApiConstants.X_API_SIGN, ApiConstants.SIGN);
        String signType = getParameter(ApiConstants.X_API_SIGN_TYPE, ApiConstants.SIGN_TYPE);
        try {
            this.signType = Strings.isNoneBlank(signType) ? SignTypeEnum.valueOf(signType.toUpperCase()) : SignTypeEnum.MD5;
        } catch (IllegalArgumentException e) {
            throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR, "不支持的签名类型:" + signType);
        }

        this.partnerId = getParameterNoBlank(ApiConstants.PARTNER_ID, ApiConstants.X_API_ACCESS_KEY);
        this.serviceName = getParameterNoBlank(ApiConstants.SERVICE);
        this.serviceVersion = getParameterDefault(ApiConstants.VERSION, ApiConstants.VERSION_DEFAULT);
        this.requestNo = getParameterNoBlank(ApiConstants.REQUEST_NO);
        this.userAgent = getParameter(HttpHeaders.USER_AGENT);
        this.context = getParameter(ApiConstants.CONTEXT);

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

    public void setApiService(ApiService apiService) {
        this.setOpenApiService(apiService.getClass().getAnnotation(OpenApiService.class));
        this.apiService = apiService;
    }

    /**
     * 解析Protocol
     */
    protected void doParseProtocol() {
        // 先从新协议头获取，如果没有设置则默认为老协议
        String requestProtocol = Strings.defaultString(getParameter(ApiConstants.X_API_PROTCOL),
                getParameter(ApiProtocol.HTTP_FORM_JSON.code()));
        if (Strings.isNoneBlank(requestProtocol)) {
            this.apiProtocol = ApiProtocol.valueOf(requestProtocol);
        } else {
            this.apiProtocol = ApiProtocol.JSON;
        }

    }

    /**
     * 解析安全相关参数
     */
    private void loadJsonBody() {
        String mediaType = null;
        String contentType = httpRequest.getContentType();
        if (Strings.isNotBlank(contentType)) {
            if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                mediaType = MediaType.APPLICATION_JSON_VALUE;
            } else if (contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE;
            }
        } else {
            mediaType = MediaType.APPLICATION_JSON_VALUE;
        }
        if (mediaType == null) {
            throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR,
                    "http请求报文头Content-Type支持三种：1.空(默认为json)，2.application/json，3.application/x-www-form-urlencoded");
        }
        String body = null;
        // body
        if (mediaType.equals(MediaType.APPLICATION_JSON_VALUE)) {
            try {
                body = CharStreams.toString(new InputStreamReader(httpRequest.getInputStream(), Charsets.UTF_8));
            } catch (IOException e) {
                throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, e);
            }
        }

        if (Strings.isBlank(body)) {
            body = httpRequest.getParameter(BODY);
        }
        throwIfBlank(body, "报文内容为空");
        this.requestBody = body.trim();
        JSONObject jsonObject = (JSONObject) JSON.parse(requestBody);
        putAll(jsonObject);
    }


    private void throwIfBlank(String value, String detail) {
        if (Strings.isBlank(value)) {
            throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR, detail);
        }
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
