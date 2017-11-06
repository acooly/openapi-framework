/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-26 15:54 创建
 *
 */
package com.acooly.openapi.framework.core.executer;

import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.security.sign.SignTypeEnum;
import com.acooly.openapi.framework.core.service.base.ApiService;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.CharStreams;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;

/** @author qiubo@qq.com */
@Data
public class ApiContext {

  Map<String, String> requestData;
  private HttpServletRequest orignalRequest;
  private HttpServletResponse orignalResponse;
  /** 是否已认证通过 */
  private boolean authenticated = false;
  /** 交易级内部ID */
  private String gid;
  /** 请求及内部ID */
  private String oid;

  private OpenApiService openApiService;

  @SuppressWarnings("rawtypes")
  private ApiService apiService;

  private ApiRequest request;
  private ApiResponse response;

  private String redirectUrl;

  /** 服务名称 */
  private String serviceName;
  /** 服务版本 */
  private String serviceVersion;
  /** 访问协议 */
  private ApiProtocol protocol = ApiProtocol.JSON;

  private String userAgent;

  private boolean appClient;

  private String sign;

  private SignTypeEnum signType;

  private String requestBody;

  private String responseBody;

  private String requestNo;

  private String partnerId;

  public void initGid() {
    gid = Ids.gid();
  }

  public OpenApiService getOpenApiService() {
    return openApiService;
  }

  public void setOpenApiService(OpenApiService openApiService) {
    this.openApiService = openApiService;
    setServiceName(openApiService.name());
  }

  /**
   * 响应类型是否是重定向
   *
   * @return
   */
  public boolean isRedirect() {
    if (openApiService == null) {
      return false;
    }
    return openApiService.responseType() == ResponseType.REDIRECT;
  }

  public void init(ApiService apiService) {
    this.setOpenApiService(apiService.getClass().getAnnotation(OpenApiService.class));
    this.apiService = apiService;
    this.appClient = Boolean.valueOf(requestData.get("appClient"));
  }

  public void initRequestParam() {
    // sign
    Map<String, String> queryStringMap = getQueryStringMap();
    this.sign = (notBlankParam(queryStringMap, ApiConstants.SIGN));
    // signType
    String signType = notBlankParam(queryStringMap, ApiConstants.SIGN_TYPE);
    try {
      this.signType = SignTypeEnum.valueOf(signType);
    } catch (IllegalArgumentException e) {
      throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR, "不支持的签名类型:" + signType);
    }

    // protocol
    String protocol = queryStringMap.get(ApiConstants.PROTOCOL);
    if (Strings.isBlank(protocol)) {
      protocol = orignalRequest.getHeader(ApiConstants.PROTOCOL);
      if (!Strings.isBlank(protocol)) {
        this.setProtocol(ApiProtocol.find(protocol));
      }
    }

    // body
    try {
      this.setRequestBody(
          CharStreams.toString(
              new InputStreamReader(orignalRequest.getInputStream(), Charsets.UTF_8)));
    } catch (IOException e) {
      throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, e);
    }

    // service
    this.serviceName = notBlankParam(queryStringMap, ApiConstants.SERVICE);
    // version
    this.serviceVersion = notBlankParam(queryStringMap, ApiConstants.VERSION);
    // partnerId
    this.partnerId = notBlankParam(queryStringMap, ApiConstants.PARTNER_ID);

    this.userAgent = orignalRequest.getHeader("User-Agent");
  }

  private String notBlankParam(Map<String, String> queryStringMap, String param) {
    String value = queryStringMap.get(param);
    if (Strings.isBlank(value)) {
      value = orignalRequest.getHeader(param);
      if (Strings.isBlank(value)) {
        throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR, param + " 是必填项");
      }
    }
    return value;
  }

  private Map<String, String> getQueryStringMap() {
    String queryString = orignalRequest.getQueryString();
    if (com.acooly.core.utils.Strings.isBlank(queryString)) {
      return Collections.emptyMap();
    }
    Map<String, String> queryStringMap =
        Splitter.on("&").withKeyValueSeparator("=").split(queryString);
    return queryStringMap;
  }
}
