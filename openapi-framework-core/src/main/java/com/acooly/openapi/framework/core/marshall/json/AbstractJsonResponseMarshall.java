/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.json;

import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.json.ObjectAccessor;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.core.log.OpenApiLoggerHandler;
import com.acooly.openapi.framework.core.marshall.ApiMarshall;
import com.acooly.openapi.framework.core.marshall.crypt.ApiMarshallCryptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * API返回报文抽象实现
 *
 * @author zhangpu
 * <p>Created by zhangpu on 2015/11/19.
 */
@Slf4j
public abstract class AbstractJsonResponseMarshall<T, S extends ApiResponse> implements ApiMarshall<T, S> {

    @Resource
    protected OpenApiLoggerHandler openApiLoggerHandler;
    @Resource
    private ApiMarshallCryptService apiMarshallCryptService;
    @Resource
    protected ApiAuthentication apiAuthentication;

    @Override
    public T marshall(S response) {
        doEncrypt(response);
        T result = doMarshall(response);
        doSign(response, result);
        doLogger(response, result);
        return result;
    }

    /**
     * 按需加密
     */
    protected void doEncrypt(S response) {
        ObjectAccessor<ApiResponse> objectAccessor = ObjectAccessor.of(response);
        ApiContext context = ApiContextHolder.getContext();
        for (Map.Entry<String, Field> entry :
                objectAccessor.getClassMeta().getSecurityfieldMap().entrySet()) {
            String value = objectAccessor.getPropertyValue(entry.getKey());
            String encrypt = apiMarshallCryptService.encrypt(entry.getKey(), value, context.getAccessKey());
            objectAccessor.setPropertyValue(entry.getKey(), encrypt);
        }
    }

    abstract T doMarshall(S response);

    protected T doSign(S apiResponse, T result) {
        ApiContext context = ApiContextHolder.getContext();
        if (result.getClass().isAssignableFrom(ApiMessageContext.class)) {
            ApiMessageContext amc = (ApiMessageContext) result;
            if (Strings.isNotBlank(context.getAccessKey()) && context.isAuthenticated()) {
                String sign = apiAuthentication.signature(amc.getBody(), context.getAccessKey(), context.getSignType().name());
                amc.header(ApiConstants.X_API_SIGN, sign);
                amc.header(ApiConstants.X_API_SIGN_TYPE, context.getSignType().code());
                amc.header(ApiConstants.X_API_ACCESS_KEY, context.getAccessKey());
                if (context.getApiProtocol() != null) {
                    amc.header(ApiConstants.X_API_PROTOCOL, context.getApiProtocol().code());
                }
            }
        }
        return result;
    }

    /**
     * 日志
     *
     * @param apiResponse
     * @param result
     */
    protected void doLogger(S apiResponse, T result) {
        if (result.getClass().isAssignableFrom(String.class)) {
            openApiLoggerHandler.log(getLogLabel(apiResponse), apiResponse, (String) result);
        } else if (result.getClass().isAssignableFrom(Map.class)) {
            openApiLoggerHandler.log(getLogLabel(apiResponse), (Map) result);
        } else if (result.getClass().isAssignableFrom(ApiMessageContext.class)) {
            ApiMessageContext amc = (ApiMessageContext) result;
            openApiLoggerHandler.log(getLogLabel(apiResponse), apiResponse, amc.getBody(), amc.getHeaders(), null);
        } else {
            openApiLoggerHandler.log(getLogLabel(apiResponse), result.toString());
        }
    }

    /**
     * 日志头
     *
     * @param apiResponse
     * @return
     */
    protected String getLogLabel(S apiResponse) {
        String labelPostfix =
                (StringUtils.isNotBlank(apiResponse.getService())
                        ? "[" + apiResponse.getService() + "]:"
                        : ":");
        if (ApiContextHolder.getApiContext().isRedirect()) {
            return "服务跳回" + labelPostfix;
        }
        if (ApiNotify.class.isAssignableFrom(apiResponse.getClass())) {
            return "服务通知" + labelPostfix;
        } else {
            return "服务响应" + labelPostfix;
        }
    }

    @Override
    public ApiProtocol getProtocol() {
        return ApiProtocol.JSON;
    }
}
