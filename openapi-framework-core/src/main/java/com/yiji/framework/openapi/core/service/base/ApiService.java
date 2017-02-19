/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.yiji.framework.openapi.core.service.base;

import com.yiji.framework.openapi.common.message.ApiNotify;
import com.yiji.framework.openapi.common.message.ApiRequest;
import com.yiji.framework.openapi.common.message.ApiResponse;
import com.yiji.framework.openapi.core.listener.OpenApiEventPublisher;
import com.yiji.framework.openapi.core.listener.event.ServiceEvent;
import com.yiji.framework.openapi.domain.OrderInfo;


/**
 * ApiService 服务处理框架接口
 *
 * @param <O>
 * @param <R>
 * @author zhangpu
 * @author Bohr.Qiu <qzhanbo@yiji.com>
 * @date 2014年8月3日
 */
public interface ApiService<O extends ApiRequest, R extends ApiResponse> extends OpenApiEventPublisher<ServiceEvent> {

    void service(O request, R response);

    ApiNotify handleNotify(OrderInfo orderInfo, Object data);

    O getRequestBean();

    R getResponseBean();

    ApiNotify getApiNotifyBean();

}
