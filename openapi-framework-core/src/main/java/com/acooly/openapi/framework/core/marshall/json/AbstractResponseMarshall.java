/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.json;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.message.ApiReturn;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.core.executer.ApiContext;
import com.acooly.openapi.framework.core.executer.ApiContextHolder;
import com.acooly.openapi.framework.core.log.OpenApiLoggerHandler;
import com.acooly.openapi.framework.core.marshall.ApiMarshall;
import com.acooly.openapi.framework.core.marshall.ObjectAccessor;
import com.acooly.openapi.framework.core.marshall.crypt.ApiMarshallCryptService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * API返回报文抽象实现
 *
 * <p>Created by zhangpu on 2015/11/19.
 */
public abstract class AbstractResponseMarshall<T, S extends ApiResponse>
    implements ApiMarshall<T, S> {

  @Resource protected OpenApiLoggerHandler openApiLoggerHandler;
  @Resource private ApiMarshallCryptService apiMarshallCryptService;
  @Resource protected ApiAuthentication apiAuthentication;

  @Override
  public T marshall(ApiResponse response) {
    ApiContext apiContext = ApiContextHolder.getApiContext();
    ObjectAccessor<ApiResponse> objectAccessor = ObjectAccessor.of(response);
    for (Map.Entry<String, Field> entry :
        objectAccessor.getClassMeta().getSecurityfieldMap().entrySet()) {
      String value = objectAccessor.getPropertyValue(entry.getKey());
      String encrypt =
          apiMarshallCryptService.encrypt(entry.getKey(), value, apiContext.getPartnerId());
      objectAccessor.setPropertyValue(entry.getKey(), encrypt);
    }
    T result = doMarshall(response);
    doLogger(response, result);
    apiContext.setResponseBody((String) result);
    String sign =
        apiAuthentication.signature(
            (String) result, apiContext.getPartnerId(), apiContext.getSignType().name());
    apiContext
        .getOrignalResponse()
        .setHeader(ApiConstants.SIGN_TYPE, apiContext.getSignType().name());
    apiContext.getOrignalResponse().setHeader(ApiConstants.SIGN, sign);
    return result;
  }

  abstract T doMarshall(ApiResponse response);
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

  @Override
  public ApiProtocol getProtocol() {
    return ApiProtocol.JSON;
  }
}
