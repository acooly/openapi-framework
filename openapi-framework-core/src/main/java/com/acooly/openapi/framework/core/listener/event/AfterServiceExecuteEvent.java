/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-31 17:29 创建
 *
 */
package com.acooly.openapi.framework.core.listener.event;

import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;

/**
 * 服务执行完后的事件.
 *
 * @author qiubo@qq.com
 */
public class AfterServiceExecuteEvent extends ServiceEvent {
  private ApiResponse apiResponse = null;
  private ApiRequest apiRequest = null;

  public AfterServiceExecuteEvent(ApiRequest apiRequest, ApiResponse apiResponse) {
    this.apiResponse = apiResponse;
    this.apiRequest = apiRequest;
  }

  public ApiResponse getApiResponse() {
    return apiResponse;
  }

  public ApiRequest getApiRequest() {
    return apiRequest;
  }
}
