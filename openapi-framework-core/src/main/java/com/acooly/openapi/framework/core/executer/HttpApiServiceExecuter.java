/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.executer;

import com.acooly.core.utils.validate.Validators;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.Servlets;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.core.auth.ApiAuthorization;
import com.acooly.openapi.framework.core.exception.ApiServiceExceptionHander;
import com.acooly.openapi.framework.core.log.OpenApiLoggerHandler;
import com.acooly.openapi.framework.core.marshall.ApiRedirectMarshall;
import com.acooly.openapi.framework.core.marshall.ApiRequestMarshall;
import com.acooly.openapi.framework.core.marshall.ApiResponseMarshall;
import com.acooly.openapi.framework.core.service.base.AbstractApiService;
import com.acooly.openapi.framework.common.executor.ApiService;
import com.acooly.openapi.framework.core.service.factory.ApiServiceFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 服务执行HTTP实现
 *
 * @author zhangpu
 */
public abstract class HttpApiServiceExecuter
    implements ApiServiceExecuter<HttpServletRequest, HttpServletResponse> {

  protected static final Logger logger = LoggerFactory.getLogger(HttpApiServiceExecuter.class);

  @Resource protected OpenApiLoggerHandler openApiLoggerHandler;
  @Resource protected ApiAuthentication apiAuthentication;
  @Resource protected ApiAuthorization apiAuthorization;
  @Resource protected ApiServiceFactory apiServiceFactory;
  @Autowired private ApiServiceExceptionHander apiServiceExceptionHander;
  @Autowired private ApiRequestMarshall apiRequestMarshall;
  @Autowired private ApiResponseMarshall apiResponseMarshall;
  @Autowired private ApiRedirectMarshall apiRedirectMarshall;

  @SuppressWarnings("rawtypes")
  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response) {
    ApiContext apiContext = getApiContext();
    try {
      // 初始化ApiContext
      doInitApiContext(apiContext, request, response);
      doVerify(apiContext);
      // 执行服务
      doExceute(apiContext);
    } catch (Throwable ex) {
      doException(apiContext, ex);
    } finally {
      doFinally(apiContext);
    }
  }

  protected void doFinally(ApiContext apiContext) {
    try {
      doResponse(apiContext);
    } catch (Exception e) {
      logger.warn("响应处理失败:", e);
    } finally {
      MDC.clear();
      destoryApiContext(apiContext);
    }
  }

  protected void doVerify(ApiContext apiContext) {
    // 认证
    doAuthenticate(apiContext);
    // 授权
    doAuthorize(apiContext);
    // 解码
    doUnmarshal(apiContext);

    prepareResponse(apiContext);
    // 参数校验
    doValidateParameter(apiContext);
  }

  protected abstract void doInitApiContext(
      ApiContext apiContext,
      HttpServletRequest orignalRequest,
      HttpServletResponse orignalResponse);

  protected ApiContext getApiContext() {
    if (!ApiContextHolder.isInited()) {
      ApiContextHolder.init();
    }
    return ApiContextHolder.getApiContext();
  }

  /** @param apiContext */
  @SuppressWarnings({"rawtypes", "unchecked"})
  protected abstract void doExceute(ApiContext apiContext);

  protected void prepareResponse(ApiContext apiContext) {
    ApiResponse apiResponse = apiContext.getResponse();
    if (apiContext.getResponse() == null) {
      return;
    }
    ApiRequest apiRequest = apiContext.getRequest();
    if (apiRequest == null) {
      return;
    }
    apiResponse.setRequestNo(apiRequest.getRequestNo());
    apiResponse.setPartnerId(apiRequest.getPartnerId());
    apiResponse.setService(apiRequest.getService());
    apiResponse.setVersion(apiRequest.getVersion());
    apiResponse.setContext(apiRequest.getContext());
  }

  protected void doResponse(ApiContext apiContext) {
    ApiService service = apiContext.getApiService();
    ApiResponse apiResponse = apiContext.getResponse();
    HttpServletResponse response = apiContext.getOrignalResponse();
    boolean redirect = apiContext.isRedirect();
    String redirectUrl = getRedirectUrl(service, apiResponse, apiContext);
    String marshallStr = null;
    if (redirect && StringUtils.isNotBlank(redirectUrl)) {
      marshallStr = doResponseMarshal(apiResponse, true);
      String location = buildRedirctLocation(redirectUrl, marshallStr);
      openApiLoggerHandler.log("服务跳转[url]:", redirectUrl);
      Servlets.redirect(response, location);
    } else {
      marshallStr = doResponseMarshal(apiResponse, false);
      Servlets.writeResponse(response, marshallStr);
    }
  }

  /**
   * 解报
   *
   * @param apiContext
   * @return
   */
  protected void doUnmarshal(ApiContext apiContext) {
    ApiRequest apiRequest = apiRequestMarshall.marshall(apiContext);
    getApiContext().setRequest(apiRequest);
  }

  /**
   * 组报
   *
   * @return
   */
  protected String doResponseMarshal(ApiResponse apiResponse, boolean isRedirect) {
    if (isRedirect) {
      return (String) apiRedirectMarshall.marshall(apiResponse);
    } else {
      return (String) apiResponseMarshall.marshall(apiResponse);
    }
  }

  /**
   * 销毁释放线程绑定ApiContext
   *
   * @param apiContext
   */
  protected void destoryApiContext(ApiContext apiContext) {
    apiContext.destory();
    ApiContextHolder.clear();
  }

  /**
   * 公共Api参数合法性检查
   *
   * @param apiRequest
   */
  protected void doValidateParameter(ApiContext apiContext) {
    try {
      apiContext.getOpenApiService().responseType().accept(apiContext);
      Validators.assertJSR303(apiContext.getRequest());
      apiContext.getRequest().check();
    } catch (IllegalArgumentException iae) {
      throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR, iae.getMessage());
    } catch (ApiServiceException ae) {
      throw ae;
    } catch (Exception e) {
      throw new ApiServiceException(
          ApiServiceResultCode.PARAMETER_ERROR, "参数合法性检查未通过:" + e.getMessage());
    }
  }

  /**
   * 认证
   *
   * @param apiContext
   */
  protected void doAuthenticate(ApiContext apiContext) {
    apiAuthentication.authenticate(apiContext);
    apiContext.setAuthenticated(true);
  }

  protected void doAuthorize(ApiContext apiContext) {
    apiAuthorization.authorize(apiContext);
  }

  /**
   * 错误处理
   *
   * @param apiContext
   * @param e
   */
  protected void doException(ApiContext apiContext, Throwable e) {
    if (apiContext.getResponse() == null) {
      ApiResponse response = new ApiResponse();
      apiContext.setResponse(response);
      prepareResponse(apiContext);
    }
    apiServiceExceptionHander.handleApiServiceException(
        apiContext.getRequest(), apiContext.getResponse(), e);
  }

  private String getRedirectUrl(
      ApiService service, ApiResponse apiResponse, ApiContext apiContext) {
    String redirectUrl = apiContext.getRedirectUrl();
    // 如果没有设置，则使用程序员设置的服务默认跳转URL到下层服务
    if (StringUtils.isBlank(redirectUrl)) {
      if (service instanceof AbstractApiService) {
        redirectUrl = ((AbstractApiService) service).getDefaultRedirectUrl();
      }
    }
    // 如果任然没有没有设置,如果通过认证并设置了returnUrl,则使用用户的returnUrl跳转返回错误。
    if (apiContext.isAuthenticated()
        && !apiResponse.isSuccess()
        && StringUtils.isBlank(redirectUrl)) {
      // 已通过签名认证，确定了身份合法，非恶意攻击
      // fixme
      //      redirectUrl = apiContext.getRequestData().get(ApiConstants.RETURN_URL);
    }

    return redirectUrl;
  }

  private String buildRedirctLocation(String redirectUrl, String marshallStr) {
    if (redirectUrl == null) {
      throw new ApiServiceException(ApiServiceResultCode.REDIRECT_URL_NOT_EXIST);
      // ?是否考虑使用openApi提供一个URL显示这种特别的错误情况？或则这种问题应该在调试阶段解决？ by zhangpu on
      // 20140821
    }
    if (redirectUrl.contains("?")) {
      return redirectUrl + "&" + marshallStr;
    } else {
      return redirectUrl + "?" + marshallStr;
    }
  }
}
