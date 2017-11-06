/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.formjson;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.message.ApiReturn;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.core.log.OpenApiLoggerHandler;
import com.acooly.openapi.framework.core.marshall.ApiMarshall;
import com.acooly.openapi.framework.core.marshall.ObjectAccessor;
import com.acooly.openapi.framework.core.marshall.crypt.ApiMarshallCryptService;
import com.acooly.openapi.framework.core.security.sign.SignerFactory;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * API返回报文抽象实现
 *
 * <p>Created by zhangpu on 2015/11/19.
 */
public abstract class AbstractHttpFormJsonResponseMarshall<T, S extends ApiResponse>
    implements ApiMarshall<T, S> {

  @Resource protected OpenApiLoggerHandler openApiLoggerHandler;
  @Resource protected ApiAuthentication apiAuthentication;
  @Resource protected SignerFactory<Map<String, String>> signerFactory;
  @Resource private ApiMarshallCryptService apiMarshallCryptService;

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

  protected Map<String, String> perSignData(
      ApiResponse apiResponse, ObjectAccessor<ApiResponse> objectAccessor) {
    Map<String, String> signData = objectAccessor.getAllDataExcludeTransient();
    doEncrypt(apiResponse, signData);
    doBeforeMarshall(apiResponse, signData);
    doSign(apiResponse, signData);
    return signData;
  }

  protected Map<String, Object> perMarshallData(
      ApiResponse apiResponse, ObjectAccessor objectAccessor) {
    Map<String, Object> responseData = objectAccessor.getAllDataExcludeTransientForJsonProcess();
    doBeforeMarshall(apiResponse, responseData);
    return responseData;
  }

  protected abstract T doMarshall(Map<String, Object> responseData);

  /**
   * 日志
   *
   * @param apiResponse
   * @param marshallData
   */
  protected void doLogger(ApiResponse apiResponse, T marshallData) {
    if (marshallData.getClass().isAssignableFrom(String.class)) {
      openApiLoggerHandler.log(getLogLabel(apiResponse), (String) marshallData);
    } else if (marshallData.getClass().isAssignableFrom(Map.class)) {
      openApiLoggerHandler.log(getLogLabel(apiResponse), (Map) marshallData);
    } else {
      openApiLoggerHandler.log(getLogLabel(apiResponse), marshallData.toString());
    }
  }

  protected String getLogLabel(ApiResponse apiResponse) {
    String labelPostfix =
        (StringUtils.isNotBlank(apiResponse.getService())
            ? "[" + apiResponse.getService() + "]:"
            : ":");
    if (ApiNotify.class.isAssignableFrom(apiResponse.getClass())) {
      return "异步通知" + labelPostfix;
    } else if (ApiReturn.class.isAssignableFrom(apiResponse.getClass())) {
      return "服务跳转" + labelPostfix;
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
    String partnerId = apiResponse.getPartnerId();
    if (Strings.isNullOrEmpty(partnerId)) {
      return;
    }
    String key = null;
    Object value = null;
    for (Map.Entry<String, String> entry : responseData.entrySet()) {
      key = entry.getKey();
      value = entry.getValue();
      if (value instanceof String
          && !Strings.isNullOrEmpty((String) value)
          && isSecurityField(apiResponse, key)) {
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
    String signType = apiResponse.getSignType();
    String partnerId = apiResponse.getPartnerId();
    String resultCode = apiResponse.getResultCode();
    doBeforeMarshall(apiResponse, signData);
    if (Strings.isNullOrEmpty(signType) || Strings.isNullOrEmpty(partnerId)) {
      return;
    }
    // 服务认证失败
    if (resultCode != null
        && resultCode.equals(ApiServiceResultCode.UN_AUTHENTICATED_ERROR.getCode())) {
      return;
    }
    // 授权失败
    if (resultCode != null
        && resultCode.equals(ApiServiceResultCode.UN_AUTHORIZED_ERROR.getCode())) {
      return;
    }
    apiAuthentication.signature(signData, partnerId, signType);
  }

  @Override
  public ApiProtocol getProtocol() {
    return ApiProtocol.JSON;
  }

  protected void doBeforeMarshall(ApiResponse apiResponse, Map<String, ?> data) {}
}
