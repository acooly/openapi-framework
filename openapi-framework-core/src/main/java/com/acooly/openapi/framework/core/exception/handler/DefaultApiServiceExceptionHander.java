/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.exception.handler;

import com.acooly.core.utils.enums.Messageable;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.exception.ApiServiceExceptionHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 框架统一异常处理 默认实现
 *
 * @author acooly
 */
@Service
@Slf4j
public class DefaultApiServiceExceptionHander implements ApiServiceExceptionHander {

  @Override
  public void handleApiServiceException(
      ApiRequest apiRequest, ApiResponse apiResponse, Throwable ase) {
    if (ApiServiceException.class.isAssignableFrom(ase.getClass())) {
      handleApiServiceException(apiResponse, (ApiServiceException) ase);
    } else if (Messageable.class.isAssignableFrom(ase.getClass())) {
      handleMessageable(apiResponse, (Messageable) ase);
    } else {
      String serviceName = "";
      if (apiRequest != null) {
        serviceName = apiRequest.getService();
      }
      log.error("处理服务[{}]异常", serviceName, ase);
      handleInternalException(apiResponse);
    }
  }

  protected void handleMessageable(ApiResponse apiResponse, Messageable ex) {
    apiResponse.setCode(ex.code());
    apiResponse.setMessage(ex.message());
    apiResponse.setDetail(ex.message());
  }
  /**
   * 服务异常处理
   *
   * @param apiResponse
   * @param ase
   */
  protected void handleApiServiceException(ApiResponse apiResponse, ApiServiceException ase) {
    apiResponse.setCode(ase.getResultCode());
    apiResponse.setMessage(ase.getResultMessage());
    apiResponse.setDetail(ase.getDetail());
  }

  /**
   * 系统异常处理
   *
   * @param apiResponse
   */
  protected void handleInternalException(ApiResponse apiResponse) {
    apiResponse.setCode(ApiServiceResultCode.INTERNAL_ERROR.code());
    apiResponse.setMessage(ApiServiceResultCode.INTERNAL_ERROR.message());
  }
}
