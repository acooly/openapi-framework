/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-31 16:59 创建
 *
 */
package com.acooly.openapi.framework.common.event.dto;

import java.util.Map;

/**
 * 收到用户请求时的事件.
 *
 * @author qiubo@qq.com
 */
public class RequestReceivedEvent extends SystemEvent {
  private Map<String, String> requestData;

  public RequestReceivedEvent(Map<String, String> requestData) {
    this.requestData = requestData;
  }

  public Map<String, String> getRequestData() {
    return requestData;
  }
}
