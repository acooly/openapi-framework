/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.formjson;

import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.json.ObjectAccessor;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.core.log.OpenApiLoggerHandler;
import com.acooly.openapi.framework.core.marshall.ApiMarshall;
import com.acooly.openapi.framework.core.marshall.crypt.ApiMarshallCryptService;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * API返回报文抽象实现
 * <p/>
 *
 * @author zhangpu on 2015/11/19.
 * @author zhangpu on 2019/10/06 整合调整，支持多协议
 */
public abstract class AbstractHttpFormJsonResponseMarshall<T, S extends ApiResponse> implements ApiMarshall<T, S> {

    @Resource
    protected OpenApiLoggerHandler openApiLoggerHandler;
    @Resource
    private ApiMarshallCryptService apiMarshallCryptService;
    @Resource
    protected ApiAuthentication apiAuthentication;

    @Override
    public T marshall(ApiResponse response) {
        ObjectAccessor<ApiResponse> objectAccessor = ObjectAccessor.of(response);
        // 签名数据
        Map<String, String> signData = perSignData(response, objectAccessor);
        // 输出数据
        Map<String, Object> responseData = perMarshallData(response, objectAccessor);
        responseData.put(ApiConstants.SIGN, signData.get(ApiConstants.SIGN));
        T result = doMarshall(responseData);
        doLogger(response, result);
        return result;
    }

    protected Map<String, String> perSignData(ApiResponse apiResponse, ObjectAccessor<ApiResponse> objectAccessor) {
        Map<String, String> signData = objectAccessor.getAllDataExcludeTransient();
        doEncrypt(apiResponse, signData);
        doBeforeMarshall(apiResponse, signData);
        doSign(apiResponse, signData);
        return signData;
    }

    protected Map<String, Object> perMarshallData(ApiResponse apiResponse, ObjectAccessor objectAccessor) {
        Map<String, Object> responseData = objectAccessor.getAllDataExcludeTransientForJsonProcess();
        doBeforeMarshall(apiResponse, responseData);
        return responseData;
    }

    /**
     * marshall
     *
     * @param responseData
     * @return
     */
    protected abstract T doMarshall(Map<String, Object> responseData);

    /**
     * 日志
     *
     * @param apiResponse
     * @param result
     */
    protected void doLogger(ApiResponse apiResponse, T result) {
        if (result.getClass().isAssignableFrom(String.class)) {
            openApiLoggerHandler.log(getLogLabel(apiResponse), apiResponse, (String) result);
        } else if (result.getClass().isAssignableFrom(Map.class)) {
            openApiLoggerHandler.log(getLogLabel(apiResponse), (Map) result);
        } else if (result.getClass().isAssignableFrom(ApiMessageContext.class)) {
            openApiLoggerHandler.log(getLogLabel(apiResponse), ((ApiMessageContext) result).getBody());
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
    protected String getLogLabel(ApiResponse apiResponse) {
        String labelPostfix =
                (Strings.isNotBlank(apiResponse.getService())
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

    /**
     * 加密
     *
     * @param apiResponse
     * @param responseData
     */
    protected void doEncrypt(ApiResponse apiResponse, Map<String, String> responseData) {
        ApiContext context = getApiContext();
        if (!context.isAuthenticated()) {
            return;
        }
        String partnerId = apiResponse.getPartnerId();
        if (Strings.isBlank(partnerId)) {
            return;
        }
        String key = null;
        Object value = null;
        for (Map.Entry<String, String> entry : responseData.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (value instanceof String && !Strings.isBlank((String) value) && isSecurityField(apiResponse, key)) {
                responseData.put(key, apiMarshallCryptService.encrypt(key, (String) value, partnerId));
            }
        }
    }


    private boolean isSecurityField(ApiResponse apiResponse, String propertyName) {
        try {
            Field field = apiResponse.getClass().getDeclaredField(propertyName);
            if (field == null) {
                return false;
            }
            OpenApiField openApiField = field.getAnnotation(OpenApiField.class);
            return openApiField != null && openApiField.security();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 签名
     *
     * @param signData
     */
    protected void doSign(ApiResponse apiResponse, Map<String, String> signData) {
        ApiContext context = getApiContext();
        String signType = context.getSignType().getCode();
        String accessKey = context.getAccessKey();
        String resultCode = apiResponse.getCode();
        if (Strings.isBlank(signType) || Strings.isBlank(accessKey)) {
            return;
        }
        // 服务认证失败
        if (resultCode != null && resultCode.equals(ApiServiceResultCode.UNAUTHENTICATED_ERROR.getCode())) {
            return;
        }
        // 授权失败
        if (resultCode != null && resultCode.equals(ApiServiceResultCode.UNAUTHORIZED_ERROR.getCode())) {
            return;
        }
        if (context.isAuthenticated()) {
            signData.put(ApiConstants.SIGN, apiAuthentication.signature(signData, accessKey, signType));
        }
    }

    @Override
    public ApiProtocol getProtocol() {
        return ApiProtocol.HTTP_FORM_JSON;
    }

    /**
     * 兼容老协议
     *
     * @param apiResponse
     * @param data
     */
    protected void doBeforeMarshall(ApiResponse apiResponse, Map data) {
        data.put(ApiConstants.SIGN_TYPE, getApiContext().getSignType().code());
        data.put(ApiConstants.RESULT_CODE, data.remove(ApiConstants.CODE));
        String resultCode = (String) data.get(ApiConstants.RESULT_CODE);
        if (Strings.equals(resultCode, ApiServiceResultCode.SUCCESS.code())
                || Strings.equals(resultCode, ApiServiceResultCode.PROCESSING.code())
        ) {
            data.put(ApiConstants.RESULT_CODE, "EXECUTE_" + resultCode);
        }
        data.put(ApiConstants.RESULT_MESSAGE, data.remove(ApiConstants.MESSAGE));
        String removedDetail = (String) data.remove(ApiConstants.DETAIL);
        if (Strings.isNoneBlank(removedDetail)) {
            data.put(ApiConstants.RESULT_DETAIL, removedDetail);
        }

    }

    protected ApiContext getApiContext() {
        return ApiContextHolder.getContext();
    }

}
