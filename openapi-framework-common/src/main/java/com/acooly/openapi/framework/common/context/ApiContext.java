/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-26 15:54 创建
 *
 */
package com.acooly.openapi.framework.common.context;

import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.executor.ApiService;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import lombok.Data;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;

import static com.acooly.openapi.framework.common.ApiConstants.BODY;

/** @author qiubo@qq.com */
@Data
public class ApiContext {
  private static final Logger perlogger = LoggerFactory.getLogger(ApiConstants.PERFORMANCE_LOGGER);
  private HttpServletRequest orignalRequest;
  private HttpServletResponse orignalResponse;
  /** 是否已认证通过 */
  private boolean authenticated = false;
  /** 交易级内部ID */
  private String gid;

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

  private String userAgent;

  private boolean appClient;

  private String sign;

  private SignTypeEnum signType;

  private String accessKey;

  private String requestBody;

  private String responseBody;

  private String responseSign;

  private String requestNo;

  private String partnerId;

  private StopWatch stopWatch;

  private Map<String, Object> ext = Maps.newHashMap();

  public void ext(String key, Object value) {
    this.ext.put(key, value);
  }

  public Object ext(String key) {
    return this.ext.get(key);
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

  public void setApiService(ApiService apiService) {
    this.setOpenApiService(apiService.getClass().getAnnotation(OpenApiService.class));
    this.apiService = apiService;
  }

  public void init() {
    this.gid = Ids.gid();
    MDC.put(ApiConstants.GID, gid);

    Map<String, String> queryStringMap = getQueryStringMap();
    // sign
    parseSign(queryStringMap);
    // signType
    parseSignType(queryStringMap);
    // accesskey
    parseAccessKey(queryStringMap);

    parseBody();
    MDC.put("oid", requestNo);
    this.stopWatch = new Slf4JStopWatch(serviceName, perlogger);
    this.userAgent = orignalRequest.getHeader("User-Agent");
  }

  private void parseAccessKey(Map<String, String> queryStringMap) {
    this.accessKey = notBlankParam(queryStringMap, ApiConstants.ACCESS_KEY);
  }

  private void parseBody() {
    String mediaType = null;
    String contentType = orignalRequest.getContentType();
    if (Strings.isNotBlank(contentType)) {
      if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
        mediaType = MediaType.APPLICATION_JSON_VALUE;
      } else if (contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
        mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE;
      }
    } else {
      mediaType = MediaType.APPLICATION_JSON_VALUE;
    }

    if (mediaType == null) {
      throw new ApiServiceException(
          ApiServiceResultCode.PARAMETER_ERROR,
          "http请求报文头Content-Type支持三种：1.空(默认为json)，2.application/json，3.application/x-www-form-urlencoded");
    }
    String body;
    // body
    if (mediaType.equals(MediaType.APPLICATION_JSON_VALUE)) {
      try {
        body =
            CharStreams.toString(
                new InputStreamReader(orignalRequest.getInputStream(), Charsets.UTF_8));
      } catch (IOException e) {
        throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, e);
      }
    } else {
      body = orignalRequest.getParameter(BODY);
    }
    throwIfBlank(body, "报文内容为空");
    this.requestBody = body.trim();
    parseBaseRequestParams(body);
  }

  private void parseSignType(Map<String, String> queryStringMap) {
    String signType = notBlankParam(queryStringMap, ApiConstants.SIGN_TYPE);
    try {
      this.signType = SignTypeEnum.valueOf(signType.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR, "不支持的签名类型:" + signType);
    }
  }

  private void parseSign(Map<String, String> queryStringMap) {
    this.sign = notBlankParam(queryStringMap, ApiConstants.SIGN);
  }

  private void parseBaseRequestParams(String body) {
    parseJson(body);
    throwIfBlank(serviceName, ApiConstants.SERVICE + "不能为空");
    throwIfBlank(serviceVersion, ApiConstants.VERSION + "不能为空");
    throwIfBlank(requestNo, ApiConstants.REQUEST_NO + "不能为空");
  }

  private void parseJson(String body) {
    JSONObject jsonObject = (JSONObject) JSON.parse(body);
    serviceName = (String) jsonObject.get(ApiConstants.SERVICE);
    serviceVersion = (String) jsonObject.get(ApiConstants.VERSION);
    requestNo = (String) jsonObject.get(ApiConstants.REQUEST_NO);
    partnerId = (String) jsonObject.get(ApiConstants.PARTNER_ID);
  }

  private String notBlankParam(Map<String, String> queryStringMap, String param) {
    String value = queryStringMap.get(param);
    if (Strings.isBlank(value)) {
      value = orignalRequest.getHeader(param);
      throwIfBlank(value, param + " 是必填项");
    }
    return value;
  }

  private void throwIfBlank(String value, String detail) {
    if (Strings.isBlank(value)) {
      throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR, detail);
    }
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

  public void destory() {
    if (stopWatch != null) {
      stopWatch.stop();
    }
  }
}
