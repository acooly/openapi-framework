/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-31 17:29 创建
 *
 */
package com.acooly.openapi.framework.common.event.dto;

import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;

/**
 * 服务执行完后的事件.
 *
 * @author qiubo@qq.com
 */
public class AfterServiceExecuteEvent extends ServiceExecuteEvent {

  public AfterServiceExecuteEvent(ApiRequest apiRequest, ApiResponse apiResponse) {
    super(apiRequest, apiResponse);
  }

  public AfterServiceExecuteEvent(ApiContext apiContext) {
    super(apiContext);
  }
}
