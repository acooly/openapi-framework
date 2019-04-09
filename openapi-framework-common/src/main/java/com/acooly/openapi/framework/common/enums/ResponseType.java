/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-27 01:44 创建
 *
 */
package com.acooly.openapi.framework.common.enums;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.message.ApiAsyncRequest;
import com.acooly.openapi.framework.common.utils.ApiUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

/**
 * 定义服务响应类型
 *
 * @author qiubo@qq.com
 */
public enum ResponseType implements Consumer<ApiContext> {

  /** 请求直接响应 */
  SYN("同步服务") {
    @Override
    public void accept(ApiContext apiContext) {}
  },
  /** 请求需要异步通知确认 */
  ASNY("异步服务") {
    @Override
    public void accept(ApiContext apiContext) {
      ApiAsyncRequest request = (ApiAsyncRequest) apiContext.getRequest();
      ApiUtils.checkOpenAPIUrl(request.getNotifyUrl(), ApiConstants.NOTIFY_URL);
    }
  },
  /** 请求响应为重定向 */
  REDIRECT("重定向服务") {
    @Override
    public void accept(ApiContext apiContext) {
      ApiAsyncRequest request = (ApiAsyncRequest) apiContext.getRequest();
      if (StringUtils.isNotEmpty(request.getNotifyUrl())) {
        ApiUtils.checkOpenAPIUrl(request.getNotifyUrl(), ApiConstants.NOTIFY_URL);
      }
      ApiUtils.checkOpenAPIUrl(request.getReturnUrl(), ApiConstants.RETURN_URL);
    }
  };

  private String msg;

  ResponseType(String msg) {
    this.msg = msg;
  }

  public String getMsg() {
    return msg;
  }
}
