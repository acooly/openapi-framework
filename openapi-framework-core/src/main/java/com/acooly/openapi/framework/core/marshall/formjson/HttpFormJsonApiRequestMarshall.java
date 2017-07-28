/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.formjson;

import com.google.common.base.Strings;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.core.executer.ApiContext;
import com.acooly.openapi.framework.core.executer.ApiContextHolder;
import com.acooly.openapi.framework.core.marshall.ApiRequestMarshall;
import com.acooly.openapi.framework.core.marshall.ObjectAccessor;
import com.acooly.openapi.framework.core.marshall.crypt.ApiMarshallCryptService;
import com.acooly.openapi.framework.common.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Bohr.Qiu <qiubo@qq.com>
 * @author zhangpu： 增加对部分隐私加密信息解密功能
 * @author zhangpu 重构接口抽象框架
 */
@Component
public class HttpFormJsonApiRequestMarshall implements ApiRequestMarshall<ApiRequest, Map<String, String>> {

    @Autowired
    private ApiMarshallCryptService apiMarshallCryptService;

    @Override
    public ApiRequest marshall(Map<String, String> source) {
        ApiContext apiContext = ApiContextHolder.getApiContext();
        ApiRequest apiRequest = apiContext.getApiService().getRequestBean();
        ObjectAccessor<ApiRequest> objectAccessor = ObjectAccessor.of(apiRequest);
        String partnerId = source.get(ApiConstants.PARTNER_ID);
        for (Map.Entry<String, String> entry : source.entrySet()) {
            setFieldValue(objectAccessor, partnerId, source, entry.getKey());
        }
        // 特别处理兼容orderNo的老API
        apiRequest.setRequestNo(ApiUtils.getRequestNo(source));
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
