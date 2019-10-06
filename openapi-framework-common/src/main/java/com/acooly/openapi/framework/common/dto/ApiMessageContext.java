/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-16 09:31
 */
package com.acooly.openapi.framework.common.dto;

import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.common.utils.Encodes;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2019-09-16 09:31
 */
@Getter
@Setter
public class ApiMessageContext {
    /**
     * 头
     */
    private Map<String, String> headers = Maps.newHashMap();
    /**
     * 参数
     */
    private Map<String, String> parameters = Maps.newHashMap();

    /**
     * 报文体
     */
    private String body;

    /**
     * 相关url
     */
    private String url;

    public String getGid() {
        return getValue(ApiConstants.GID);
    }
    
    public String getPartnerId() {
        return getValueDefault(ApiConstants.PARTNER_ID, getAccessKey());
    }

    public String getAccessKey() {
        return getValue(ApiConstants.ACCESS_KEY);
    }

    public String getSign() {
        return getValue(ApiConstants.SIGN);
    }

    public String getSignType() {
        return getValueDefault(ApiConstants.SIGN_TYPE, SignTypeEnum.MD5.getCode());
    }

    public String getProtocol() {
        // 先从新协议头获取，如果没有设置则默认为老协议
        for (String key : this.headers.keySet()) {
            if (Strings.startsWithIgnoreCase(key, ApiConstants.X_HEAD_PREFIX)) {
                return ApiProtocol.JSON.code();
            }
        }
        return ApiProtocol.HTTP_FORM_JSON.code();
    }

    public String buildRedirectUrl(String redirectUrl) {
        StringBuilder sb = new StringBuilder(redirectUrl)
                .append("?").append(ApiConstants.BODY).append("=").append(Encodes.urlEncode(this.getBody()))
                .append("&").append(ApiConstants.ACCESS_KEY).append("=").append(getAccessKey())
                .append("&").append(ApiConstants.SIGN_TYPE).append("=").append(getSignType())
                .append("&").append(ApiConstants.SIGN).append("=").append(getSign())
                .append("&").append(ApiConstants.GID).append("=").append(ApiContextHolder.getApiContext().getGid());
        return sb.toString();
    }

    public ApiMessageContext header(String key, String value) {
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
        return Strings.defaultString(getHeader(key), getParameter(key));
    }

    public String getValueDefault(String key, String defaultValue) {
        String value = getValue(key);
        if (Strings.isBlank(value)) {
            value = defaultValue;
        }
        return value;
    }

    public String getHeader(String key) {
        return Strings.defaultString(this.getHeaders().get(key), this.getHeaders().get(ApiConstants.X_HEAD_PREFIX + key));
    }

    public String getParameter(String key) {
        return Strings.defaultString(this.parameters.get(key), this.parameters.get(ApiConstants.X_HEAD_PREFIX + key));
    }

    public Map<String, String> headersToParameters() {
        Map<String, String> headersToParameters = Maps.newHashMap();

        this.headers.forEach((k, v) -> {
            if (!Strings.startsWithIgnoreCase(k, ApiConstants.X_HEAD_PREFIX)) {
                headersToParameters.put(k, v);
            }
        });

        headersToParameters.put(ApiConstants.ACCESS_KEY, getAccessKey());
        headersToParameters.put(ApiConstants.SIGN_TYPE, getSignType());
        headersToParameters.put(ApiConstants.SIGN, getSign());
        return headersToParameters;
    }

}
