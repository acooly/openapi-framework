/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-16 09:31
 */
package com.acooly.openapi.framework.common.dto;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.common.utils.ApiUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangpu
 * @date 2019-09-16 09:31
 */
public class ApiMessageContext implements Serializable {

    /**
     * 'application/x-www-form-urlencoded' content type header value
     */
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

    /**
     * 'application/json' content type header value
     */
    public static final String CONTENT_TYPE_JSON = "application/json";

    /**
     * 头
     */
    private Map<String, String> headers = new HashMap();
    /**
     * 参数
     */
    private Map<String, String> parameters = new HashMap();

    /**
     * 报文体
     */
    private String body;

    /**
     * 相关url
     */
    private String url;

    /**
     * contextType
     */
    private String contextType = CONTENT_TYPE_JSON;


    public ApiMessageContext() {
    }

    public ApiMessageContext(ApiMessageContext context) {
        this.headers = context.getHeaders();
        this.parameters = context.getParameters();
        this.body = context.getBody();
        this.url = context.getUrl();
    }

    public String getGid() {
        return getValue(ApiConstants.GID);
    }

    public String getPartnerId() {
        return getValueDefault(ApiConstants.PARTNER_ID, getAccessKey());
    }

    public String getAccessKey() {
        return getValue(ApiConstants.ACCESS_KEY);
    }

    public String getRequestIp() {
        return getValue(ApiConstants.REQUEST_IP);
    }

    public String getSign() {
        return getValue(ApiConstants.SIGN);
    }

    public String getSignType() {
        return getValueDefault(ApiConstants.SIGN_TYPE, SignTypeEnum.MD5.getCode());
    }

    public String getProtocol() {
        // 只有显示传入协议，才已协议为准（v4以下报文体中默认protocol=HTTP_FORM_JSON）
        if (getValue(ApiConstants.PROTOCOL) != null) {
            return getValue(ApiConstants.PROTOCOL);
        }

        // 头里面包含：x-api-sign 或者 参数里面包含: body(临时)，后调整跳转报文的协议也为x-api-开头
        if (ApiUtils.isNoneBlank(getHeader(ApiConstants.X_API_SIGN)) || ApiUtils.isNoneBlank(getParameter(ApiConstants.BODY))) {
            return ApiProtocol.JSON.code();
        }

        return ApiProtocol.HTTP_FORM_JSON.code();
    }

    public String buildRedirectUrl(String redirectUrl) {
        StringBuilder sb = new StringBuilder(redirectUrl);
        int index = 0;
        for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
            sb.append(index == 0 ? "?" : "&");
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            index = index + 1;
        }
        return sb.toString();
    }

    public String buildRedirectUrl() {
        return buildRedirectUrl(this.url);
    }

    public ApiMessageContext header(String key, String value) {
        if (ApiUtils.isBlank(value)) {
            return this;
        }
        return put(this.headers, key, value);
    }

    public ApiMessageContext parameter(String key, String value) {
        return put(this.parameters, key, value);
    }

    protected ApiMessageContext put(Map<String, String> data, String key, String value) {
        data.put(key, value);
        return this;
    }

    public String getValue(String key) {
        return ApiUtils.defaultString(getHeader(key), getParameter(key));
    }

    public String getValueDefault(String key, String defaultValue) {
        String value = getValue(key);
        if (ApiUtils.isBlank(value)) {
            value = defaultValue;
        }
        return value;
    }

    public String getHeader(String key) {
        return ApiUtils.defaultString(this.getHeaders().get(key), this.getHeaders().get(ApiConstants.X_HEAD_PREFIX + key));
    }

    public String getParameter(String key) {
        return ApiUtils.defaultString(this.parameters.get(key), this.parameters.get(ApiConstants.X_HEAD_PREFIX + key));
    }

    public Map<String, String> headersToParameters() {
        Map<String, String> headersToParameters = new HashMap();

        this.headers.forEach((k, v) -> {
            if (!ApiUtils.startsWithIgnoreCase(k, ApiConstants.X_HEAD_PREFIX)) {
                headersToParameters.put(k, v);
            }
        });

        headersToParameters.put(ApiConstants.ACCESS_KEY, getAccessKey());
        headersToParameters.put(ApiConstants.SIGN_TYPE, getSignType());
        headersToParameters.put(ApiConstants.SIGN, getSign());
        return headersToParameters;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }
}
