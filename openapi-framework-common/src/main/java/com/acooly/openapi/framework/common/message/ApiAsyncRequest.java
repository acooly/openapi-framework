package com.acooly.openapi.framework.common.message;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qiubo@yiji.com
 */
@Getter
@Setter
public class ApiAsyncRequest extends ApiRequest {
    @OpenApiField(desc = "通知地址", constraint = "使用异步服务时必填",
            demo = "http://xxx.xxx.com/notifyUrl.html", ordinal = ApiConstants.ORDINAL_MAX + 1)
    private String notifyUrl;

    @OpenApiField(desc = "回调地址", constraint = "需要回跳到商户时必填",
            demo = "http://xxx.xxx.com/returnUrl.html", ordinal = ApiConstants.ORDINAL_MAX + 2)
    private String returnUrl;
}
