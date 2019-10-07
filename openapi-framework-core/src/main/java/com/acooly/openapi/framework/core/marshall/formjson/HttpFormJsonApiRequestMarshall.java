/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.formjson;

import com.acooly.openapi.framework.common.OpenApis;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.core.marshall.ApiRequestMarshall;
import com.acooly.openapi.framework.common.utils.json.ObjectAccessor;
import com.acooly.openapi.framework.core.marshall.crypt.ApiMarshallCryptService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhangpu： 增加对部分隐私加密信息解密功能
 * @author zhangpu 重构接口抽象框架
 * @author zhangpu 多协议支持改造整合，入参调整为ApiContext 2019-10-06
 */
@Component("httpFormJsonApiRequestMarshall")
public class HttpFormJsonApiRequestMarshall implements ApiRequestMarshall<ApiRequest, ApiContext> {

    @Autowired
    private ApiMarshallCryptService apiMarshallCryptService;

    @Override
    public ApiRequest marshall(ApiContext apiContext) {
        ApiRequest apiRequest = apiContext.getApiService().getRequestBean();
        ObjectAccessor<ApiRequest> objectAccessor = ObjectAccessor.of(apiRequest);
        String partnerId = apiContext.getPartnerId();
        Map<String, String> source = apiContext.getApiRequestContext().getParameters();
        for (Map.Entry<String, String> entry : source.entrySet()) {
            setFieldValue(objectAccessor, partnerId, source, entry.getKey());
        }
        // 特别处理兼容orderNo的老API
        apiRequest.setRequestNo(OpenApis.getRequestNo(source));
        return apiRequest;
    }

    /**
     * 设置请求对象值
     *
     * @param objectAccessor
     * @param partnerId
     * @param key
     */
    protected void setFieldValue(ObjectAccessor<ApiRequest> objectAccessor, String partnerId, Map<String, String> source, String key) {
        String value = source.get(key);
        if (!Strings.isNullOrEmpty(value)) {
            if (objectAccessor.isSecurityField(key)) {
                value = apiMarshallCryptService.decrypt(key, value, partnerId);
            }
            objectAccessor.setPropertyValue(key, value);
        }
    }


    @Override
    public ApiProtocol getProtocol() {
        return ApiProtocol.HTTP_FORM_JSON;
    }

}
