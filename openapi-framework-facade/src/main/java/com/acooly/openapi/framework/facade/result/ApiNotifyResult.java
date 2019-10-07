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
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.ApiConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhangpu
 */
public class ApiNotifyResult extends ResultBase {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -348730141776307996L;
    private String protocol;
    private String sign;
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
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConstants.BODY).append("=").append(Encodes.urlEncode(this.body)).append("&")
                .append(ApiConstants.ACCESS_KEY).append("=").append(this.accessKey).append("&")
                .append(ApiConstants.SIGN_TYPE).append("=").append(this.signType).append("&")
                .append(ApiConstants.SIGN).append("=").append(this.sign);

        if (Strings.isNotBlank(this.protocol)) {
            sb.append("&").append(ApiConstants.PROTOCOL).append("=").append(this.protocol);
        }
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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
