/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.common.executor;

import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.dto.OrderDto;
import com.acooly.openapi.framework.common.event.OpenApiEventPublisher;
import com.acooly.openapi.framework.common.event.dto.ServiceEvent;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;

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

    void service(ApiContext apiContext);

    ApiNotify handleNotify(OrderDto orderInfo, Object data);

    O getRequestBean();

    R getResponseBean();

    ApiNotify getApiNotifyBean();

    /**
     * 跳转接口时，跳转到下层服务的地址
     *
     * @return
     */
    String getRedirectUrl();

    void setRedirectUrl(String redirectUrl);
}
