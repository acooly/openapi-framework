/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.service.base;

import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.listener.OpenApiEventPublisher;
import com.acooly.openapi.framework.core.listener.event.ServiceEvent;
import com.acooly.openapi.framework.domain.OrderInfo;

/**
 * ApiService 服务处理框架接口
 *
 * @param <O>
 * @param <R>
 * @author zhangpu
 * @author Bohr.Qiu <qiubo@qq.com>
 * @date 2014年8月3日
 */
public interface ApiService<O extends ApiRequest, R extends ApiResponse>
    extends OpenApiEventPublisher<ServiceEvent> {

  void service(O request, R response);

  ApiNotify handleNotify(OrderInfo orderInfo, Object data);

  O getRequestBean();

  R getResponseBean();

  ApiNotify getApiNotifyBean();
}
