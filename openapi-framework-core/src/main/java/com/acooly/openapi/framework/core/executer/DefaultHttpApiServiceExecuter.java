package com.acooly.openapi.framework.core.executer;

import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.listener.event.AfterServiceExecuteEvent;
import com.acooly.openapi.framework.core.listener.event.BeforeServiceExecuteEvent;
import com.acooly.openapi.framework.core.listener.event.ServiceExceptionEvent;
import com.acooly.openapi.framework.core.listener.multicaster.EventPublisher;
import com.acooly.openapi.framework.core.service.base.ApiService;
import com.acooly.openapi.framework.service.OrderInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** @author qiubo@yiji.com */
@Component
public class DefaultHttpApiServiceExecuter extends HttpApiServiceExecuter {
  @Resource private EventPublisher eventPublisher;
  @Resource private OrderInfoService orderInfoService;

  protected void doInitApiContext(
      ApiContext apiContext,
      HttpServletRequest orignalRequest,
      HttpServletResponse orignalResponse) {
    apiContext.setOrignalRequest(orignalRequest);
    apiContext.setOrignalResponse(orignalResponse);
    apiContext.init();
    try {
      ApiService apiService =
          apiServiceFactory.getApiService(
              apiContext.getServiceName(), apiContext.getServiceVersion());
      apiContext.setApiService(apiService);
      ApiRequest apiRequest = apiService.getRequestBean();
      ApiResponse apiResponse = apiService.getResponseBean();
      apiContext.setRequest(apiRequest);
      apiContext.setResponse(apiResponse);
      prepareResponse(apiContext);
    } finally {
      logRequestData(apiContext);
    }
  }

  @Override
  protected void doVerify(ApiContext apiContext) {
    super.doVerify(apiContext);
  }

  private void logRequestData(ApiContext apiContext) {
    String serviceName = apiContext.getServiceName();
    String labelPostfix = (StringUtils.isNotBlank(serviceName) ? "[" + serviceName + "]:" : ":");
    openApiLoggerHandler.log("服务请求" + labelPostfix, apiContext.getRequestBody());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
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
}
