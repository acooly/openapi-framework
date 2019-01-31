/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.service.test.api;

import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.utils.Ids;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.openapi.framework.service.test.request.WithdrawRequest;
import com.acooly.openapi.framework.service.test.response.WithdrawNotify;
import com.acooly.openapi.framework.service.test.response.WithdrawResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpu
 * @date 2014年7月29日
 */
@Slf4j
@ApiDocType(code = "test", name = "测试")
@ApiDocNote("测试异步接口。<li>1、client -> 同步请求 -> gateway</li><li>2、gateway -> 异步通知（notifyUrl） -> client</li>")
@OpenApiService(name = "withdraw", desc = "测试：提现", responseType = ResponseType.ASNY, busiType = ApiBusiType.Trade, owner = "zhangpu")
public class WithdrawApiService extends BaseApiService<WithdrawRequest, WithdrawResponse> {

    /**
     * 同步处理
     *
     * @param request  已验证和组装好的请求报文，请直接享用
     * @param response 完成业务处理后，回填到response中，框架负责响应给请求客户端
     */
    @Override
    protected void doService(WithdrawRequest request, WithdrawResponse response) {
        // do any business and back filling to response
        response.setTradeNo(Ids.oid());
        response.setResult(ApiServiceResultCode.PROCESSING);
    }

    /**
     * 返回异步通知的报文bean
     * <p>
     * 下层业务处理完成后，通过GID通知OpenApi异步处理框架结果数据，
     * 框架根据这里指定的报文类型对数据进行组装和验证后，签名发送通知给客户端
     *
     * @return 异步通知的报文Bean
     */
    @Override
    public ApiNotify getApiNotifyBean() {
        return new WithdrawNotify();
    }
}
