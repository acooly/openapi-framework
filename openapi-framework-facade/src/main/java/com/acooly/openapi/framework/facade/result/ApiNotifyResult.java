/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.facade.result;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.Encodes;
import com.acooly.openapi.framework.common.ApiConstants;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author zhangpu
 */
public class ApiNotifyResult extends ResultBase {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -348730141776307996L;
    /**
     * 签名 x-api-sign
     */
    private String sign;
    /**
     * x-api-signType
     */
    private String signType;
    private String accessKey;
    private String body;


    /**
     * 跳转通知URL
     */
    private String returnUrl;
    /**
     * 异步通知URL
     */
    private String notifyUrl;

    public String getQueryString() {
        Map<String, String> data = Maps.newHashMap();
        data.put(ApiConstants.SIGN, this.sign);
        data.put(ApiConstants.SIGN_TYPE, this.signType);
        data.put(ApiConstants.X_API_ACCESS_KEY, this.accessKey);
        data.put(ApiConstants.BODY, this.body);
        StringBuilder sb = new StringBuilder();

        sb.append(ApiConstants.BODY).append("=").append(Encodes.urlEncode(this.body)).append("&")
                .append(ApiConstants.X_API_ACCESS_KEY).append("=").append(this.accessKey).append("&")
                .append(ApiConstants.SIGN_TYPE).append("=").append(this.accessKey).append("&")
                .append(ApiConstants.SIGN).append("=").append(this.sign);
        return sb.toString();
    }

    public String getCompleteReturnUrl() {

        if (StringUtils.isEmpty(getReturnUrl())) {
            return null;
        }
        String queryString = getQueryString();
        if (StringUtils.contains(getReturnUrl(), "?")) {
            return getReturnUrl() + "&" + queryString;
        } else {
            return getReturnUrl() + "?" + queryString;
        }
    }

    public String getCompleteNotifyUrl() {
        if (StringUtils.isEmpty(getNotifyUrl())) {
            return null;
        }
        String queryString = getQueryString();
        if (StringUtils.contains(getNotifyUrl(), "?")) {
            return getNotifyUrl() + "&" + queryString;
        } else {
            return getNotifyUrl() + "?" + queryString;
        }
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
