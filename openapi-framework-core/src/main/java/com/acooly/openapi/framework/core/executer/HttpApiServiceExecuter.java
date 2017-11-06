/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.executer;

import com.acooly.core.utils.Ids;
import com.acooly.core.utils.validate.Validators;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.ApiUtils;
import com.acooly.openapi.framework.common.utils.Servlets;
import com.acooly.openapi.framework.core.OpenApiConstants;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.core.auth.ApiAuthorization;
import com.acooly.openapi.framework.core.exception.ApiServiceExceptionHander;
import com.acooly.openapi.framework.core.listener.event.AfterServiceExecuteEvent;
import com.acooly.openapi.framework.core.listener.event.BeforeServiceExecuteEvent;
import com.acooly.openapi.framework.core.listener.event.RequestReceivedEvent;
import com.acooly.openapi.framework.core.listener.event.ServiceExceptionEvent;
import com.acooly.openapi.framework.core.listener.multicaster.EventPublisher;
import com.acooly.openapi.framework.core.log.OpenApiLoggerHandler;
import com.acooly.openapi.framework.core.marshall.ApiMarshallFactory;
import com.acooly.openapi.framework.core.service.base.AbstractApiService;
import com.acooly.openapi.framework.core.service.base.ApiService;
import com.acooly.openapi.framework.core.service.factory.ApiServiceFactory;
import com.acooly.openapi.framework.service.OrderInfoService;
import org.apache.commons.lang3.StringUtils;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 服务执行HTTP实现
 *
 * @author zhangpu
 */
@Component
public class HttpApiServiceExecuter
    implements ApiServiceExecuter<HttpServletRequest, HttpServletResponse> {

  protected static final Logger logger = LoggerFactory.getLogger(HttpApiServiceExecuter.class);
  private static final Logger perlogger =
      LoggerFactory.getLogger(OpenApiConstants.PERFORMANCE_LOGGER);
  @Resource protected OpenApiLoggerHandler openApiLoggerHandler;
  @Resource protected ApiAuthentication apiAuthentication;
  @Resource protected ApiAuthorization apiAuthorization;
  @Resource protected ApiServiceFactory apiServiceFactory;
  @Resource protected ApiMarshallFactory apiMarshallFactory;
  @Resource private OrderInfoService orderInfoService;
  @Resource private EventPublisher eventPublisher;
  @Autowired private ApiServiceExceptionHander apiServiceExceptionHander;

  @SuppressWarnings("rawtypes")
  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response) {
    ApiContext apiContext = getApiContext();
    StopWatch stopWatch = null;
    try {
      // 初始化ApiContext
      doInitApiContext(request, response);
      // 性能日志埋点
      stopWatch = initPerfLog(apiContext);
      // 序列化及前后验证
      doUnmarshallBeforeVerify(apiContext, request);
      doUnmarshal(apiContext);
      doUnmarshallAfterVerify(apiContext, request);
      // 执行服务
      doExceute(apiContext);
    } catch (Throwable ex) {
      handleException(apiContext, ex);
    } finally {
      try {
        doResponse(apiContext, response);
      } catch (Exception e) {
        logger.warn("响应处理失败:", e);
      } finally {
        MDC.clear();
        destoryApiContext();
        if (stopWatch != null) {
          stopWatch.stop();
        }
      }
    }
  }

  protected ApiContext doInitApiContext(
      HttpServletRequest orignalRequest, HttpServletResponse orignalResponse) {
    ApiContext apiContext = getApiContext();
    apiContext.setOrignalRequest(orignalRequest);
    apiContext.setOrignalResponse(orignalResponse);
    apiContext.initRequestParam();
    try {
      ApiService apiService =
          apiServiceFactory.getApiService(
              apiContext.getServiceName(), apiContext.getServiceVersion());
      apiContext.init(apiService);

      ApiRequest apiRequest = apiService.getRequestBean();
      ApiResponse apiResponse = apiService.getResponseBean();
      prepareResponse(apiResponse, apiContext.getRequestData());
      apiContext.setRequest(apiRequest);
      apiContext.setResponse(apiResponse);
      initGid();
      apiContext.getOpenApiService().responseType().validate(apiContext.getRequestData());

    } finally {
      // todo
      //      logRequestData(requestData);
    }
    return apiContext;
  }

  protected ApiContext getApiContext() {
    if (!ApiContextHolder.isInited()) {
      ApiContextHolder.init();
    }
    return ApiContextHolder.getApiContext();
  }

  protected void initGid() {
    ApiContext apiContext = getApiContext();
    String service = apiContext.getServiceName();
    String version = apiContext.getServiceVersion();
    String partnerId = apiContext.getPartnerId();
    //    String requestNo = ApiUtils.getRequestNo(apiContext.getRequestData());
    //    String orderNo = apiContext.getRequestData().get(ApiConstants.MERCH_ORDER_NO);
    //    if (StringUtils.isBlank(orderNo)) {
    //      orderNo = requestNo;
    //    }
    //    String tradeGid = orderInfoService.findGidByTrade(partnerId, service, version, orderNo);
    //    if (StringUtils.isBlank(tradeGid)) {
    //      // 查找依赖关系的服务
    //      OpenApiDependence openApiDependence =
    //          apiContext.getApiService().getClass().getAnnotation(OpenApiDependence.class);
    //      if (openApiDependence != null && StringUtils.isNotBlank(openApiDependence.value())) {
    //        tradeGid =
    //            orderInfoService.findGidByTrade(partnerId, openApiDependence.value(), version,
    // orderNo);
    //      }
    //    }
    //    if (StringUtils.isBlank(tradeGid)) {
    //      apiContext.initGid();
    //    } else {
    //      apiContext.setGid(tradeGid);
    //    }
    // 生成性的OID
    apiContext.initGid();
    apiContext.setOid(Ids.oid());
    MDC.put(ApiConstants.GID, apiContext.getGid());
  }

  /** @param apiContext */
  @SuppressWarnings({"rawtypes", "unchecked"})
  protected void doExceute(ApiContext apiContext) {
    ApiService apiService = apiContext.getApiService();
    ApiRequest apiRequest = apiContext.getRequest();
    ApiResponse apiResponse = apiContext.getResponse();
    try {
      publishBeforeServiceExecuteEvent(apiContext);
      apiService.service(apiRequest, apiResponse);
    } catch (Throwable ex) {
      publishServiceExceptionEvent(apiResponse, apiRequest, apiService, ex);
      throw ex;
    } finally {
      publishAfterServiceExecuteEvent(apiResponse, apiRequest, apiService);
    }
  }

  protected void prepareResponse(ApiResponse apiResponse, Map<String, String> requestData) {
    if (apiResponse == null || requestData == null) {
      return;
    }
    apiResponse.setRequestNo(ApiUtils.getRequestNo(requestData));
    apiResponse.setMerchOrderNo(requestData.get(ApiConstants.MERCH_ORDER_NO));
    apiResponse.setPartnerId(requestData.get(ApiConstants.PARTNER_ID));
    apiResponse.setService(requestData.get(ApiConstants.SERVICE));
    apiResponse.setContext(requestData.get(ApiConstants.CONTEXT));
  }

  protected void doResponse(ApiContext apiContext, HttpServletResponse response) {
    ApiService service = apiContext.getApiService();
    ApiResponse apiResponse = apiContext.getResponse();
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
    Map<String, String> requestData = apiContext.getRequestData();
    ApiRequest apiRequest =
        apiMarshallFactory.getRequestMarshall(getApiContext().getProtocol()).marshall(requestData);
    getApiContext().setRequest(apiRequest);
  }

  /**
   * 组报
   *
   * @return
   */
  protected String doResponseMarshal(ApiResponse apiResponse, boolean isRedirect) {
    if (isRedirect) {
      return (String)
          apiMarshallFactory
              .getRedirectMarshall(getApiContext().getProtocol())
              .marshall(apiResponse);
    } else {
      return (String)
          apiMarshallFactory
              .getResponseMarshall(getApiContext().getProtocol())
              .marshall(apiResponse);
    }
  }

  /** 销毁释放线程绑定ApiContext */
  protected void destoryApiContext() {
    ApiContextHolder.clear();
  }

  /**
   * marshall 前check
   *
   * @param apiContext
   * @param request
   */
  protected void doUnmarshallBeforeVerify(ApiContext apiContext, HttpServletRequest request) {
    // 认证
    doAuthenticate(apiContext);
  }

  /**
   * marshall 后 服务验证
   *
   * @param request
   * @param apiContext
   */
  protected void doUnmarshallAfterVerify(ApiContext apiContext, HttpServletRequest request) {
    // 校验业务参数
    doValidateParameter(apiContext.getRequest());
    // 校验请求唯一
    doVerifyIdempotence(apiContext.getRequest());
    // 授权
    doAuthorize(apiContext.getRequest());
  }

  /**
   * 幂等性校验
   *
   * @param apiRequest
   */
  protected void doVerifyIdempotence(ApiRequest apiRequest) {
    orderInfoService.checkUnique(apiRequest.getPartnerId(), apiRequest.getRequestNo());
  }

  /** 公共Api参数合法性检查 */
  protected void doValidateParameter(ApiRequest apiRequest) {
    try {
      Validators.assertJSR303(apiRequest);
      apiRequest.check();
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

  protected void doAuthorize(ApiRequest apiRequest) {
    apiAuthorization.authorize(apiRequest);
  }

  /**
   * 错误处理
   *
   * @param apiContext
   * @param e
   */
  protected void handleException(ApiContext apiContext, Throwable e) {
    if (apiContext.getResponse() == null) {
      apiContext.setResponse(createResponse(apiContext.getRequestData()));
    }
    apiServiceExceptionHander.handleApiServiceException(
        apiContext.getRequest(), apiContext.getResponse(), e);
  }

  private void publishBeforeServiceExecuteEvent(ApiContext apiContext) {
    if (eventPublisher.canPublishEvent(apiContext.getApiService())) {
      eventPublisher.publishEvent(
          new BeforeServiceExecuteEvent(apiContext.getRequest(), apiContext.getResponse()),
          apiContext.getApiService());
    }
  }

  private void publishServiceExceptionEvent(
      ApiResponse apiResponse, ApiRequest apiRequest, ApiService apiService, Throwable throwable) {
    if (eventPublisher.canPublishEvent(apiService)) {
      eventPublisher.publishEvent(
          new ServiceExceptionEvent(apiRequest, apiResponse, throwable), apiService);
    }
  }

  private void publishAfterServiceExecuteEvent(
      ApiResponse apiResponse, ApiRequest apiRequest, ApiService apiService) {
    if (eventPublisher.canPublishEvent(apiService)) {
      eventPublisher.publishEvent(
          new AfterServiceExecuteEvent(apiRequest, apiResponse), apiService);
    }
  }

  private void publishRequestReceivedEvent(Map<String, String> requestData) {
    if (eventPublisher.canPublishGlobalEvent()) {
      eventPublisher.publishEvent(new RequestReceivedEvent(requestData));
    }
  }

  private StopWatch initPerfLog(ApiContext apiContext) {
    return new Slf4JStopWatch(apiContext.getServiceName(), perlogger);
  }

  private void logRequestData(Map<String, String> requestData) {
    // changed by zhangpu for 'request log security filtering(mask or
    // ignore)' on 2015-10-16
    String serviceName = requestData.get(ApiConstants.SERVICE);
    String labelPostfix = (StringUtils.isNotBlank(serviceName) ? "[" + serviceName + "]:" : ":");
    openApiLoggerHandler.log("服务请求" + labelPostfix, requestData);
  }

  private ApiResponse createResponse(Map<String, String> requestData) {
    ApiResponse response = new ApiResponse();
    prepareResponse(response, requestData);
    return response;
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
      redirectUrl = apiContext.getRequestData().get(ApiConstants.RETURN_URL);
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
