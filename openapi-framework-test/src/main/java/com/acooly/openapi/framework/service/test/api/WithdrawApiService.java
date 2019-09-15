/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.service.test.api;

import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.dto.OrderDto;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.core.service.base.AbstractAsyncApiService;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.acooly.openapi.framework.service.test.notify.WithdrawApiNotify;
import com.acooly.openapi.framework.service.test.request.WithdrawApiRequest;
import com.acooly.openapi.framework.service.test.response.WithdrawApiResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpu
 * @date 2014年7月29日
 */
@Slf4j
@ApiDocType(code = "test", name = "测试")
@ApiDocNote("测试异步接口。<li>1、client -> 同步请求 -> gateway</li><li>2、gateway -> 异步通知（notifyUrl） -> client</li>")
@OpenApiService(name = "withdraw", desc = "测试：提现", responseType = ResponseType.ASNY, busiType = ApiBusiType.Trade, owner = "zhangpu")
public class WithdrawApiService extends AbstractAsyncApiService<WithdrawApiRequest, WithdrawApiResponse, WithdrawApiNotify> {

    /**
     * 同步处理
     *
     * @param request  已验证和组装好的请求报文，请直接享用
     * @param response 完成业务处理后，回填到response中，框架负责响应给请求客户端
     */
    @Override
    protected void doService(WithdrawApiRequest request, WithdrawApiResponse response) {
        // do any business and back filling to response
        response.setMerchOrderNo(request.getMerchOrderNo());
        response.setResult(ApiServiceResultCode.PROCESSING);
    }


    /**
     * 异步处理定制
     * <p>
     * 针对下层系统的异步通知，在发送给给客户前，做服务层的自定义处理
     * 这里不建议进行逻辑处理，主要用于格式，类型转换处理。
     *
     * @param orderInfo      原始订单信息
     * @param apiNotifyOrder 外部调用过来准备推送的数据
     * @param apiNotify      准备发送的推送内容对象
     */
    @Override
    protected void customizeApiNotify(OrderDto orderInfo, ApiNotifyOrder apiNotifyOrder, ApiNotify apiNotify) {
        super.customizeApiNotify(orderInfo, apiNotifyOrder, apiNotify);
    }
}
