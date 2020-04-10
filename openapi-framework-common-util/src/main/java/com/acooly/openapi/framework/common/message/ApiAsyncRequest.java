/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-16 09:31
 */
package com.acooly.openapi.framework.common.message;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiField;

/**
 * 异步请求报文基类
 *
 * @author zhangpu@acooly.cn
 * @date 2019-7-12
 */
public class ApiAsyncRequest extends ApiRequest {
    @OpenApiField(desc = "通知地址", constraint = "使用异步服务时必填",
            demo = "http://xxx.xxx.com/notifyUrl.html", ordinal = ApiConstants.ORDINAL_MAX + 1)
    private String notifyUrl;

    @OpenApiField(desc = "回调地址", constraint = "需要回跳到商户时必填",
            demo = "http://xxx.xxx.com/returnUrl.html", ordinal = ApiConstants.ORDINAL_MAX + 2)
    private String returnUrl;

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
