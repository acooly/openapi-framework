/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.demo.service.api;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.dto.OrderDto;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.core.service.base.AbstractAsyncApiService;
import com.acooly.openapi.framework.demo.message.notify.WithdrawApiNotify;
import com.acooly.openapi.framework.demo.message.request.WithdrawApiRequest;
import com.acooly.openapi.framework.demo.message.response.WithdrawApiResponse;
import com.acooly.openapi.framework.demo.service.DemoApiUtils;
import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangpu
 * @date 2014年7月29日
 */
@Slf4j
@ApiDocType(code = DemoApiUtils.API_DEMO_DOC_TYPE_CODE, name = DemoApiUtils.API_DEMO_DOC_TYPE_NAME)
@ApiDocNote("测试异步接口。<li>1、client -> 同步请求 -> gateway</li><li>2、gateway -> 异步通知（notifyUrl） -> client</li>")
@OpenApiService(name = "withdraw", desc = "测试：提现异步接口", responseType = ResponseType.ASNY, busiType = ApiBusiType.Trade, owner = "zhangpu")
public class WithdrawApiService extends AbstractAsyncApiService<WithdrawApiRequest, WithdrawApiResponse, WithdrawApiNotify> {

    @Autowired
    private OpenApiRemoteService openApiRemoteService;

    /**
     * 同步处理
     *
     * @param request  已验证和组装好的请求报文，请直接享用
     * @param response 完成业务处理后，回填到response中，框架负责响应给请求客户端
     */
    @Override
    protected void doService(WithdrawApiRequest request, WithdrawApiResponse response) {
        // mock 调用下层服务
        mockInnerSystemHandleWithFacade(request);

        // do any business and back filling to response
        response.setMerchOrderNo(request.getMerchOrderNo());
        response.setResult(ApiServiceResultCode.PROCESSING);
    }

    /**
     * 模拟下层内部系统处理提现并发送异步通知
     *
     * @param request
     */
    protected void mockInnerSystemHandleWithFacade(WithdrawApiRequest request) {
        ExecutorService executor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        // 1、根据gid获获取和partnerId（用户分库分表的标志），这里一般是下次业务系统持久化，这里通过传入参数方式mock
        final String gid = getGid();
        final String partnerId = request.getPartnerId();
        final String merchOrderNo = request.getMerchOrderNo();
        executor.execute(() -> {
            // MOCK 下层业务处理2秒
            try {
                Thread.sleep(2 * 1000);
            } catch (Exception e) {
                // ignore
            }
            // 2、组装
            WithdrawApiNotify notify = new WithdrawApiNotify();
            notify.setMerchOrderNo(merchOrderNo);
            notify.setAmountIn(Money.amout("199.60"));
            notify.setFee(Money.amout("0.40"));
            notify.setStatus(ResultStatus.success);
            // 3、组装通知facade-order，并调用OpenAPI专用通知facade
            ApiNotifyOrder order = new ApiNotifyOrder();
            order.setGid(gid);
            order.setPartnerId(partnerId);
            order.setNotifyMessage(notify);
            ResultBase apiNotifyResult = openApiRemoteService.asyncNotify(order);
            log.info("提现 服务端 MOCK服务处理，发送异步通知 order: {}", order);
            log.info("提现 服务端 MOCK服务处理，发送异步通知 result: {},", apiNotifyResult);
        });
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
